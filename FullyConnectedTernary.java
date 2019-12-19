/*
    By Brendan C. Reidy
        Created 12/17/2019
        Last Modified 12/18/2019
        Fully Connected Ternary
        A fully connected layer that uses ternary weights and biases
        All weights and biases are either -1, 0, or 1
        Uses deterministic threshold to determine weights and biases
*/
public class FullyConnectedTernary implements Layer {
    public String name = "Fully Connected Ternary";

    private ActivationFunction activationFunction;
    private Layer previousLayer;
    private int length;
    private float learningRate;

    private float[][] weights;
    private float[] bias;
    private float[][] ternaryWeights;
    private float[] ternaryBias;
    private float[] neurons;

    public FullyConnectedTernary(int aLength, String activationFunctionName)
    {
        this.activationFunction = ActivationFunctions.getByName(activationFunctionName);
        this.length = aLength;
        this.neurons = new float[aLength];
        this.weights = null;
        this.bias = NeuralNetwork.GenerateBias2D(aLength);
        this.learningRate = 0.1f;
    }
    public FullyConnectedTernary(float[][] aWeights, float[] aBias, String activationFunctionName)
    {
        this.activationFunction = ActivationFunctions.getByName(activationFunctionName);
        this.length = aBias.length;
        this.neurons = new float[aBias.length];
        this.weights = aWeights;
        this.bias = aBias;
        this.learningRate = 0.1f;
        init();
    }

    private void init()
    {
        this.ternaryWeights = new float[this.length][previousLayer.sizeX()];
        this.ternaryBias = new float[this.weights.length];
        for(int i=0; i<weights.length; i++)
        {
            for(int j=0; j<weights[i].length; j++)
            {
                ternaryWeights[i][j] = ternarize(weights[i][j]);
            }
            this.ternaryBias[i] = ternarize(bias[i]);
        }
    }

    private int ternarize(float aInput)
    {
        if(aInput>0.33)
            return 1;
        else if(aInput<-0.33)
            return -1;
        return 0;
    }

    private float clip(float aInput)
    {
        if(aInput<-1)
            aInput = -1;
        else if(aInput>1)
            aInput = 1;
        return aInput;
    }

    public void setPreviousLayer(Layer aLayer)
    {
        if(aLayer==null)
        {
            System.out.println("[FATAL] Error in setting previous layer: previous is null");
            return;
        }
        this.previousLayer = aLayer;
        if(this.weights==null)
            this.weights = NeuralNetwork.GenerateWeights2D(previousLayer.size(), this.length);
        init();
    }
    public void setNeurons(float[] aNeurons)
    {
        this.neurons = aNeurons;
    }
    public float[] getNeurons()
    {
        return this.neurons;
    }
    public float[] feedForward(float[] aLayer){
        this.neurons = MatrixMath.dotProduct(aLayer, this.ternaryWeights);
        this.neurons = MatrixMath.sum(this.neurons, this.ternaryBias);
        this.neurons = activationFunction.activate(this.neurons);
        return this.neurons;
    }
    public float[] propagateBack(float[] currentError)
    {
        float[] previousError = new float[previousLayer.size()];
        float[] activationError = activationFunction.activationError(this.neurons);
        for(int i=0; i<this.length; i++)
        {
            float neuronError = activationError[i] * currentError[i];
            for(int j=0; j<previousLayer.size(); j++)
            {
                previousError[j] += ternaryWeights[i][j] * neuronError;
                weights[i][j] += neuronError * previousLayer.getNeurons()[j] * learningRate;
                weights[i][j] = clip(weights[i][j]);
                ternaryWeights[i][j] = ternarize(weights[i][j]);
            }
            bias[i] += neuronError*learningRate;
            bias[i] = clip(bias[i]);
            ternaryBias[i] = ternarize(bias[i]);
        }
        return previousError;
    }
    public void setLearningRate(float aLearningRate)
    {
        this.learningRate = aLearningRate;
    }
    public int size()
    {
        return sizeX()*sizeY();
    }
    public int sizeX() // Returns the sizeX of the network
    {
        return this.length;
    }
    public int sizeY() // Returns the sizeY of the network
    {
        return 1;
    }
    public String toString()
    {
        return "Layer Type: " + this.name +
                "\t| Activation Function: " + this.activationFunction.toString() +
                "\t| Learning Rate: " + this.learningRate;
    }
    public void saveWeightsToFile(String aFileName)
    {
        if(this.weights == null)
        {
            System.out.println("[FATAL] Attempted to save weights for layer: no weights present");
            return;
        }
        MatrixIO.saveToFile(MatrixIO.flattenArray(this.weights), aFileName + "fl.txt");
        MatrixIO.saveToFile(MatrixIO.flattenArray(this.ternaryWeights), aFileName + ".txt");
    }
    public void saveBiasToFile(String aFileName)
    {
        if(this.bias == null)
        {
            System.out.println("[FATAL] Attempted to save bias for layer: no bias present");
            return;
        }
        MatrixIO.saveToFile(this.bias, aFileName + "fl.txt");
        MatrixIO.saveToFile(this.ternaryBias, aFileName + ".txt");
    }
}
