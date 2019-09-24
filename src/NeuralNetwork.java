// Neural Network
// Created by Brendan C. Reidy
// Created on April 26, 2019

// Last Modified 4/30/19 @ 4:30 PM

public class NeuralNetwork {
    public static final double DEFAULT_LEARNING_RATE = 0.1;

    public static final int RANGE = 1;
    public static final int INPUT_INDEX = 0;

    ActivationFunction activationFunction;
    ActivationFunction outputActivation;
    Matrix2D network;
    Matrix2D weights;
    Matrix2D trainingData;
    Matrix2D trainingSubset;
    Matrix2D bias;
    Matrix2D bestWeights;
    Matrix2D bestBias;
    double bestError;
    double learningRate;
    boolean doOutput;

    public NeuralNetwork(Matrix2D aNetwork){
        this.network = aNetwork;
        this.weights = GenerateWeights(aNetwork);
        this.bias = GenerateBias(aNetwork);
        init();
    }
    public NeuralNetwork(Matrix2D aNetwork, Matrix2D aWeights, Matrix2D aBias){
        this.network = aNetwork;
        this.weights = aWeights;
        this.bias = aBias;
        init();
    }
    private void init(){
        this.activationFunction = new Sigmoid();
        this.outputActivation = new Sigmoid();
        this.learningRate = DEFAULT_LEARNING_RATE;
        this.bestBias = this.bias;
        this.bestWeights = this.weights;
        this.bestError = 100;
        this.doOutput = false;
    }
    public void setActivationFunction(ActivationFunction aFunction){
        this.activationFunction = aFunction;
    }
    public void setOutputActivation(ActivationFunction aFunction){
        this.outputActivation = aFunction;
    }
    public void setConsoleOutput(boolean aValue){
        this.doOutput = aValue;
    }
    public void setLearningRate(double aRate){
        this.learningRate = aRate;
    }
    public void setTrainingData(Matrix2D aData){
        this.trainingData = aData;
    }
    public void setTrainingSubset(Matrix2D aData){
        this.trainingSubset = aData;
    }

    public void setInput(float[] input){
        for(int i=0; i<this.network.getArrayAt(INPUT_INDEX).length; i++){
            float value = input[i];
            network.setValue(value, INPUT_INDEX, i);
        }
    }
    public float[] getOutput(){
        return network.getArrayAt(network.length-1);
    }
    public void clear(){
        for(int index=0; index<network.length; index++){
            float[] values = network.getArrayAt(index);
            for(int y=0; y<values.length; y++)
                network.setValue(0, index, y);
        }
    }
    public Matrix2D getBestWeights(){
        return this.bestWeights;
    }
    public Matrix2D getBestBias(){
        return this.bestBias;
    }
    public void setBest(){
        this.weights = getBestWeights();
        this.bias = getBestBias();
    }
    public void train(){
        beginTraining(trainingData.length/2);
    }
    public void train(int iterations){
        beginTraining(iterations);
    }
    private void beginTraining(int numIterations){
        if(this.trainingData==null){
            System.out.println("[CRITICAL] Training data for network not set! Cancelling operation...");
            System.out.println("Use network.setTrainingData(Matrix2D trainingData) to set!");
            return;
        }
        int count=0;
        double avgError = 0;
        for(int i=0; i<numIterations; i++){
            if(count>=trainingData.length/2)
                count=0;
            clear();
            float[] input = trainingData.getArrayAt(count*2);
            float[] output = trainingData.getArrayAt(count*2+1);
            setInput(input);
            feedForward();
            propogateBack(output);
            count++;
            for(int j=0; j<output.length; j++){
                avgError+=error(output[j], getOutput()[j]);
            }
        }
        double currentError = avgError/numIterations;
        if(trainingSubset!=null)
            testSubset(false);
        else if(currentError < bestError){
            this.bestError = currentError;
            this.bestWeights = this.weights;
            this.bestBias = this.bias;
        }
        if(doOutput)
            System.out.println("Cost: " + currentError);
    }
    public void testSubset(){
        testSubset(true);
    }
    public double getSubsetCost(){
        double avgError = 0;
        int numIterations = trainingSubset.length/2;
        for(int i=0; i<numIterations; i++){
            clear();
            float[] input = trainingSubset.getArrayAt(i*2);
            float[] output = trainingSubset.getArrayAt(i*2+1);
            setInput(input);
            feedForward();
            for(int j=0; j<output.length; j++){
                avgError+=error(output[j], getOutput()[j]);
            }
        }
        double currentError = avgError/numIterations;
        return currentError;
    }
    private void testSubset(boolean doOutput2){
        double currentError = getSubsetCost();
        if(currentError < bestError){
            this.bestError = currentError;
            this.bestWeights = this.weights;
            this.bestBias = this.bias;
        }
        if(doOutput2)
            System.out.println(currentError);
    }
    public float error(float a, float b){
        return ((a - b) * (a - b)) / 2;
    }
    public float errorDerivative(float a, float b){
        return (a - b);
    }
    public void propogateBack(float[] correctOutput){
        int layerIndex = this.network.length-2;
        int count = 0;

        Matrix2D invertedNewWeights = new Matrix2D();
        Matrix2D invertedNewBias = new Matrix2D();

        float[] currentLayer = getOutput();
        float[] previousLayer = network.getArrayAt(layerIndex);
        float[] currentBiasValues = bias.getArrayAt(layerIndex);
        float[] currentWeightValues = weights.getArrayAt(layerIndex);

        float[] deltaValues = new float[currentLayer.length];
        float[] newWeightValues = new float[previousLayer.length * currentLayer.length];
        float[] newBiasValues = new float[currentLayer.length];
        for(int i=0; i<correctOutput.length; i++){
            float currentOutput = currentLayer[i];
            float currentCorrectOutput = correctOutput[i];
            float errorDerivative = errorDerivative(currentCorrectOutput, currentOutput);
            float activationDerivative = activationFunction.derivative(currentOutput);
            float deltaValue = errorDerivative * activationDerivative;
            for(int j=0; j<previousLayer.length; j++){
                float previousNeuronValue = previousLayer[j];
                float currentWeightValue = currentWeightValues[count];
                float deltaWeight = deltaValue * previousNeuronValue;
                newWeightValues[count] = currentWeightValue + (float) learningRate * deltaWeight;
                count++;
            }
            deltaValues[i] = deltaValue;
            newBiasValues[i] = currentBiasValues[i] + deltaValue * (float) learningRate;
        }
        invertedNewBias.add(newBiasValues);
        invertedNewWeights.add(newWeightValues);
        for(layerIndex-=1; layerIndex>=0; layerIndex--) {

            float[] previousDeltaValues = deltaValues;
            float[] previousWeightValues = currentWeightValues;

            currentBiasValues = bias.getArrayAt(layerIndex);
            currentWeightValues = weights.getArrayAt(layerIndex);

            currentLayer = previousLayer;
            previousLayer = network.getArrayAt(layerIndex);
            deltaValues = new float[currentLayer.length];
            newWeightValues = new float[currentLayer.length * previousLayer.length];
            newBiasValues = new float[currentLayer.length];
            int count2 = 0;
            for (int i = 0; i < currentLayer.length; i++) {
                float currentOutput = currentLayer[i];
                count = 0;
                float sum = 0;
                for (int j = 0; j < previousWeightValues.length; j++) {
                    if (j % currentLayer.length == i) {
                        float previousWeightValue = previousWeightValues[j];
                        float previousDelta = previousDeltaValues[count];
                        sum += previousWeightValue * previousDelta;
                        count++;
                    }
                }
                float currentDerivativeValue = activationFunction.derivative(currentOutput);
                float deltaValue = currentDerivativeValue * sum;
                for (int j = 0; j < previousLayer.length; j++) {
                    float weightValue = currentWeightValues[count2];
                    float previousNeuronValue = previousLayer[j];
                    newWeightValues[count2] = weightValue + (float) learningRate * deltaValue * previousNeuronValue;
                    count2++;
                }
                deltaValues[i] = deltaValue;
                newBiasValues[i] = currentBiasValues[i] + deltaValue * (float) learningRate;
            }
            invertedNewBias.add(newBiasValues);
            invertedNewWeights.add(newWeightValues);
        }
        this.weights = Matrix2D.reverse(invertedNewWeights);
        this.bias = Matrix2D.reverse(invertedNewBias);
    }
    public void feedForward(){
        Matrix2D tempMatrix = new Matrix2D();
        float[] memory = network.getArrayAt(INPUT_INDEX);
        tempMatrix.add(memory);
        for(int index=0; index<network.length-1; index++){
            if(index!=0){
                float[] currentBias = bias.getArrayAt(index - 1);
                for(int i=0; i<memory.length; i++){
                    float biasValue = currentBias[i];
                    memory[i]=activationFunction.activate(memory[i] + biasValue);
                }
            }
            int currentSize = memory.length;
            int nextSize = network.getArrayAt(index+1).length;
            float[][] unactivated = new float[nextSize][currentSize];
            float[] currWeights = weights.getArrayAt(index);
            int count=0;
            for(int i=0; i<nextSize; i++){
                for(int j=0; j<currentSize; j++){
                    float value = memory[j];
                    float weight = currWeights[count];
                    unactivated[i][j] = value * weight;
                    count++;
                }
            }
            float[] unactivatedSums = new float[nextSize];
            for(int x=0; x<nextSize; x++){
                float sum=0;
                for(int y=0; y<currentSize; y++)
                    sum+=unactivated[x][y];
                unactivatedSums[x]=sum;
            }
            if(index!=network.length-2) {
                tempMatrix.add(unactivatedSums);
            }else{
                float[] currentBias = bias.getArrayAt(index);
                for(int i=0; i<nextSize; i++){
                    float biasValue = currentBias[i];
                    unactivatedSums[i]=activationFunction.activate(unactivatedSums[i] + biasValue);
                }
                tempMatrix.add(unactivatedSums);
            }
            memory = unactivatedSums;
        }
        this.network=tempMatrix;
    }
    public static Matrix2D GenerateWeights(Matrix2D aMatrix){
        Matrix2D newMatrix = new Matrix2D();
        for(int i=0; i<aMatrix.length-1; i++){
            float[] current = aMatrix.getArrayAt(i);
            float[] next = aMatrix.getArrayAt(i+1);
            float[] newWeights = new float[current.length*next.length];
            for(int j=0; j<newWeights.length; j++)
                newWeights[j] = (float) Math.random() * 2 * RANGE - RANGE;
            newMatrix.add(newWeights);
        }
        return newMatrix;
    }
    public static Matrix2D GenerateBias(Matrix2D aMatrix){
        Matrix2D bias = new Matrix2D();
        for(int i=1; i<aMatrix.length; i++){
            float[] values = aMatrix.getArrayAt(i);
            float[] newValues = new float[values.length];
            for(int j=0; j<values.length; j++)
                newValues[j]=(float) Math.random() * 2 * RANGE - RANGE;
            bias.add(newValues);
        }
        return bias;
    }
    public static Matrix2D GenerateNetwork(int sizeInput, int sizeOutput, int numHiddenLayers, int sizeHiddenLayer){
        Matrix2D newMatrix = new Matrix2D();
        float[] temp = new float[sizeInput];
        for(int i=0; i<sizeInput; i++)
            temp[i]=0;
        newMatrix.add(temp);
        for(int i=0; i<numHiddenLayers; i++){
            temp = new float[sizeHiddenLayer];
            for(int j=0; j<sizeHiddenLayer; j++)
                temp[j]=0;
            newMatrix.add(temp);
        }
        temp = new float[sizeOutput];
        for(int i=0; i<sizeOutput; i++)
            temp[i]=0;
        newMatrix.add(temp);
        return newMatrix;
    }
    public static Matrix2D GenerateNetwork(Matrix2D trainingData, int numHiddenLayers, int sizeHiddenLayer){
        int sizeInput = trainingData.getArrayAt(0).length;
        int sizeOutput = trainingData.getArrayAt(1).length;
        Matrix2D newMatrix = new Matrix2D();
        float[] temp = new float[sizeInput];
        for(int i=0; i<sizeInput; i++)
            temp[i]=0;
        newMatrix.add(temp);
        for(int i=0; i<numHiddenLayers; i++){
            temp = new float[sizeHiddenLayer];
            for(int j=0; j<sizeHiddenLayer; j++)
                temp[j]=0;
            newMatrix.add(temp);
        }
        temp = new float[sizeOutput];
        for(int i=0; i<sizeOutput; i++)
            temp[i]=0;
        newMatrix.add(temp);
        return newMatrix;
    }
    public String toString(){
        String str="Network:\n";
        str+=network.toString();
        str+="\nWeights:\n";
        str+=weights.toString();
        str+="\nBias:\n";
        str+=bias.toString();
        return str;
    }
    public void print(){
        System.out.println(toString());
    }
    public void printNetwork(){
        network.print();
    }
    public void printResults(){
        float[] input = network.getArrayAt(INPUT_INDEX);
        float[] output = getOutput();
        String str = "";
        for(int i=0; i<input.length; i++)
            str+=input[i] + ",";
        System.out.println(str);
        str = "";
        for(int i=0; i<output.length; i++)
            str+=output[i] + ",";
        System.out.println(str);
    }
    public void printIntResults(){
        float[] input = network.getArrayAt(INPUT_INDEX);
        float[] output = getOutput();
        String str = "";
        for(int i=0; i<input.length; i++)
            str+=((int) (input[i] + 0.5)) + ",";
        System.out.println(str);
        str = "";
        for(int i=0; i<output.length; i++)
            str+=((int) (output[i] + 0.5)) + ",";
        System.out.println(str);
    }
    public void printSpecs(){
        String str = "Architecture:";
        int numWeights = 0;
        int numHiddenLayers = 0;
        int numHiddenNeurons = 0;
        int numBias = 0;
        for(int i=0; i<this.network.length; i++){
            float[] layer = this.network.getArrayAt(i);
            if(i>=1) {
                numWeights += layer.length * this.network.getArrayAt(i - 1).length;
                numBias+=layer.length;
                if(i<this.network.length-1){
                    numHiddenLayers++;
                    numHiddenNeurons+=layer.length;
                }
            }
        }
        str+="\n\tInput size: " + this.network.getArrayAt(INPUT_INDEX).length;
        str+="\n\tOutput size: " + getOutput().length;
        str+="\n\tHidden layers: " + numHiddenLayers;
        str+="\n\tHidden neurons: " + numHiddenNeurons;
        str+="\n\tWeight count: " + numWeights;
        str+="\n\tBias count: " + numBias;
        str+="\n\tLearning rate: " + learningRate;
        System.out.println(str);
    }
}
