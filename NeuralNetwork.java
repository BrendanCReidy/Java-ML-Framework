import java.util.LinkedList;
import java.util.Random;
import java.io.File;
/*
    By Brendan C. Reidy
    Created 12/10/2019
    Last Modified 12/18/2019
    Neural Network Object:
        Responsible for handling the Neural Network comprised of various layers and topologies
        Performs operations for neural network at a high level
 */

public class NeuralNetwork {
    int length;

    private Layer[] layers; // All the layers in the network
    private Layer inputLayer; // Input layer of network
    private Layer outputLayer; // Output layer of network
    private CostFunction costFunction; // Cost function for network
    private LinkedList<Float> trainCost; // List of train costs
    private LinkedList<Float> testCost; // List of test costs

    private float learningRate;

    // Training Data
    private float[][] train_inputs;
    private float[][] train_outputs; // AKA Labels

    private float[][] test_inputs;
    private float[][] test_outputs; // AKA Labels

    // For record keeping

    private float bestCost;
    private int epochNum;
    private long lastRecordedTime;
    private long avgTime;

    public NeuralNetwork()
    {
        this.inputLayer = null;
        this.layers = new Layer[0];
        this.length = 0;
        this.costFunction = new MeanSquaredError();
        init();
    }
    private void init()
    {
        this.trainCost = new LinkedList<Float>();
        this.testCost = new LinkedList<Float>();
        this.learningRate = 0.1f;
        this.bestCost = Float.MAX_VALUE;
        this.epochNum = 0;
        this.avgTime = 0;
    }

    private boolean testDataWarning = false;
    private boolean didOutput = false;
    public void train()
    {
        if(epochNum!=0)
            avgTime += System.currentTimeMillis() - lastRecordedTime;
        lastRecordedTime = System.currentTimeMillis();
        epochNum++;
        if(length<=1) // Either there are no layers in the network or there is only an input layer in the network
        {
            System.out.println("[FATAL] Unable to train network: no meaningful layers");
            return;
        }
        if(train_inputs == null || train_outputs==null) // No training data in network
        {
            System.out.println("[FATAL] Unable to train network: training data not set.");
            return;
        }
        if((test_inputs == null || test_inputs==null) && !testDataWarning) // Warn user that there is no test data (Only warns once)
        {
            testDataWarning = true;
            System.out.println("[WARNING] Test data not set for network.");
            return;
        }
        if(!didOutput) // Only need to output once, otherwise floods output
        {
            didOutput = true;
            System.out.println("Training...");
        }
        float avgError = 0; // Variable to store the average error of the network
        for(int i=0; i<train_inputs.length; i++) // Iterate through each training sample
        {
            float[] input = train_inputs[i]; // Get input training data
            float[] correctOutput = train_outputs[i]; // Get correct label/output
            setInput(input); // Set input of the network to the training input
            feedForward(); // Feed forward the network
            float[] output = getOutput(); // Get output of the network
            float[] cost = costFunction.cost(output, correctOutput); // Get cost for each neuron
            for(int j=0; j<cost.length; j++)
                avgError+=cost[j]; // Cost is the sum of the error for all neurons
            propagateBack(correctOutput); // Propagate back with the correct output
        }
        avgError/=train_inputs.length; // Get average error over entire training data
        trainCost.add(avgError); // Keep track of for file IO
        System.out.print("Cost: " + avgError); // Print average error to console
        if(test_inputs!=null) // Check if there is test data
        {
            avgError = 0;
            for(int i=0; i<test_inputs.length; i++) // Get error for each test sample
            {
                float[] input = test_inputs[i]; // Test input
                float[] correctOutput = test_outputs[i]; // Test output/label
                setInput(input); // Set input to test input
                feedForward(); // Feed forward
                float[] output = getOutput(); // Get output neurons
                float[] cost = costFunction.cost(output, correctOutput); // Get cost
                for(int j=0; j<cost.length; j++) // Get sum of error for neurons
                    avgError+=cost[j];
            }
            avgError/=test_inputs.length; // Get average error over test subset length
            testCost.add(avgError); // Store for file IO
            System.out.print("\tTest Cost: " + avgError); // Print test error to console
            if(avgError<bestCost)
                bestCost = avgError;
        }else if(avgError<bestCost)
            bestCost = avgError;

        System.out.println();
    }

    public void setTrainingData(float[][] inputs, float[][] outputs) // Sets training data for the network to specified input/output
    {
        this.train_inputs = inputs;
        this.train_outputs = outputs;
    }
    public void setTestingData(float[][] inputs, float[][] outputs) // Sets test data for the network to specified input/output
    {
        this.test_inputs = inputs;
        this.test_outputs = outputs;
    }

    public void setLearningRate(float aRate) // Set learning rate for all layers
    {
        this.learningRate = aRate; // Keep track of in network
        for(int i=1; i<layers.length; i++) // Set learning rate for each layer in the network
            layers[i].setLearningRate(aRate);
    }
    public void addLayer(Layer aLayer) // Add layer to network
    {
        if(length!=0) // If not first layer in network
            aLayer.setPreviousLayer(layers[length-1]); // Set the previous network for this layer to the previous layer
        else
            this.inputLayer = aLayer; // This is the input layer since it is the first layer
        Layer[] temp = new Layer[length+1]; // Temp array to keep track of all layers
        temp[length] = aLayer; // Set last layer to the new layer
        for(int i=0; i<length; i++) // Copy layers to temp
            temp[i] = layers[i]; // Set layer at given index to the one already designated in the network (copy layers)
        this.outputLayer = aLayer; // Output layer is this layer since it is the last
        layers = temp; // Set the networks layer to the array containing all layers
        length++; // Increase length of network by 1
    }

    public float[] getOutput() // Gets output neurons
    {
        if(outputLayer==null){ // In case user does something very wrong
            System.out.println("[FATAL] Attempted to get output but there is no output layer in network.");
            return null;
        }
        return outputLayer.getNeurons(); // Return neurons in last (output) layer
    }

    public void setInput(float[] aInput) // Sets the input of the network to the given array
    {
        if(inputLayer==null){ // Network is empty but tried to set input
            System.out.println("[FATAL] Attempted 'setInput' but there is no input layer in network.");
            return;
        }
        inputLayer.setNeurons(aInput); // Set the input for the input layer to the array given
    }

    public void feedForward() // Feed forward each layer
    {
        float[] previousNeurons = inputLayer.getNeurons(); // Previous neurons (neurons of the layer indexed n-1)
        for(int i=1; i<layers.length; i++) // Feed forward each layer
            previousNeurons = layers[i].feedForward(previousNeurons); // Call feed forward for each layer object
    }

    public void propagateBack(float[] correctOutput) // Propagate back each layer
    {
        float[] currentError = costFunction.error(getOutput(), correctOutput); // Get the error for the output layer
        for(int i=length-1; i>0; i--)
            currentError = layers[i].propagateBack(currentError); // Propagate back each layer
    }

    public void saveToFile(String outputDirectory) // Save network to output directory
    {
        if(outputDirectory!="") // Check if there is an output directory
            if(!(new File(outputDirectory).exists())) // Check if that directory already exists
                new File(outputDirectory).mkdir(); // Create a new one if not

        String topology = "" + layers[0].sizeX()*layers[0].sizeY(); // Stores the topology of the network in a string
        String structure = "\t" + layers[0].toString() + "\n"; // Stores the structure of the network in a string
        for(int i=1; i<layers.length; i++)
        {
            layers[i].saveBiasToFile(outputDirectory + "B" + i); // Saves the biases
            layers[i].saveWeightsToFile(outputDirectory + "W" + i); // Saves the weights
            topology+= "x" + layers[i].sizeX()*layers[i].sizeY(); // Gets the size at the index and adds to topology
            structure+="\t" + layers[i].toString() + "\n"; // Add current layer structure to network structure
        }
        double averageTime = avgTime*0.001;
        averageTime = averageTime / epochNum;
        String[] outputs={"*** AUTO GENERATED README ***",
                "Topology: ", "\t" + topology,
                "\nCost Function:", "\t" + this.costFunction.toString(),
                "\nStructure:", structure,
                "Statistics: ",
                "\tBest Cost: " + Float.toString(bestCost),
                "\tNumber of epochs: " + Integer.toString(epochNum),
                "\tAverage Epoch Time: " + Double.toString(averageTime) + "s"};

        MatrixIO.saveToFile(outputs, outputDirectory + "README.txt"); // Save README file
        MatrixIO.saveToFile(trainCost, outputDirectory + "trainCosts.txt"); // Save training costs
        MatrixIO.saveToFile(testCost, outputDirectory + "testCosts.txt"); // Save test costs
    }

    public static float[][] GenerateWeights2D(int dimX, int dimY) // Generates a 2D array of weights
    {
        float[][] weights = new float[dimY][dimX]; // Rows = X; Columns = Y
        for(int x=0; x<dimX; x++)
            for(int y=0; y<dimY; y++)
                weights[y][x] = (float) (Math.random()*2-1); // Generate a random float between -1 and 1
        return weights;
    }

    public static float[] GenerateBias2D(int size) // Generate bias for a 2D layer
    {
        float[] bias = new float[size];
        for(int i=0; i<size; i++)
            bias[i] = (float) (Math.random()*2-1); // Generate a random float between -1 and 1
        return bias;
    }
}
