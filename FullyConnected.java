/*
    By Brendan C. Reidy
    Created 12/10/2019
    Last Modified 12/18/2019
    Fully Connected Layer Object:
        Responsible for handling mathematical operations involved with a single fully connected layer
 */

public class FullyConnected implements Layer {
    public String name = "Fully Connected"; // Name of the layer

    private ActivationFunction activationFunction; // Activation function of the array
    private Layer previousLayer; // Pointer to the previous layer with respect to this
    private int length; // Length of the layer
    private float learningRate; // Learning rate of the layer

    private float[][] weights; // Weights in the layer
    private float[] bias; // Bias in the layer
    private float[] neurons; // Neurons in the layer

    public FullyConnected(int aLength, String activationFunctionName) // Given a length and the name of an activation function, creates layer
    {
        this.activationFunction = ActivationFunctions.getByName(activationFunctionName); // Find activation function object from string
        this.length = aLength; // Sets length to specified length
        this.neurons = new float[aLength]; // Creates an array for the neurons
        this.weights = null; // Weights are null until previous layer is set (handled in back end by NeuralNetwork.java)
        this.bias = NeuralNetwork.GenerateBias2D(aLength); // Sets the bias
        this.learningRate = 0.1f; // Sets learning rate
    }
    public FullyConnected(float[][] aWeights, float[] aBias, String activationFunctionName) // Generate layer with pre-trained weights and bias
    {
        this.activationFunction = ActivationFunctions.getByName(activationFunctionName);
        this.length = aBias.length;
        this.neurons = new float[aBias.length];
        this.weights = aWeights;
        this.bias = aBias;
        this.learningRate = 0.1f;
    }
    public void setPreviousLayer(Layer aLayer) // Set 'pointer' to previous layer in network
    {
        if(aLayer==null) // Make sure layer is valid
        {
            System.out.println("[FATAL] Error in setting previous layer: previous is null");
            return;
        }
        this.previousLayer = aLayer; // Set previous layer to the given layer
        if(this.weights==null)
            this.weights = NeuralNetwork.GenerateWeights2D(previousLayer.size(), this.length); // Generate weights
    }
    public void setNeurons(float[] aNeurons)
    {
        this.neurons = aNeurons;
    } // Set neurons (generally used for input layer)
    public float[] getNeurons()
    {
        return this.neurons;
    } // Get neurons (generally used for output layer)
    public float[] feedForward(float[] aLayer){ // Feed forward given input neurons 'aLayer'
        this.neurons = MatrixMath.dotProduct(aLayer, this.weights); // Perform a dot product
        this.neurons = MatrixMath.sum(this.neurons, this.bias); // Add bias
        this.neurons = activationFunction.activate(this.neurons); // Activate
        return this.neurons;
    }
    public float[] propagateBack(float[] currentError) // Propagate back layer given the current error for the network
    {
        float[] previousError = new float[previousLayer.size()]; // Create a new array for previous error (cumulative error for all layers)
        float[] activationError = activationFunction.activationError(this.neurons); // get error with respect to activation function for current layer
        for(int i=0; i<this.length; i++)
        {
            float neuronError = activationError[i] * currentError[i]; // Error with respect to neuron
            for(int j=0; j<previousLayer.size(); j++) // Iterate through the previous neurons
            {
                previousError[j] += weights[i][j] * neuronError; // Calculate the error for the previous layer (n-1)
                weights[i][j] += neuronError * previousLayer.getNeurons()[j] * learningRate; // Adjust weights
            }
            bias[i]+=neuronError*learningRate; // Adjust bias
        }
        return previousError;
    }
    public void setLearningRate(float aLearningRate)
    {
        this.learningRate = aLearningRate;
    } // Sets learning rate for network
    public int size() // Returns total size of the network
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
    public String toString() // Returns the important properties of the layer
    {
        return this.name + ":" +
                "\n\t   Activation Function: " + this.activationFunction.toString() +
                "\n\t   Size: " + this.length +
                "\n\t   Learning Rate: " + this.learningRate;
    }
    public void saveWeightsToFile(String aFileName) // Saves weights
    {
        MatrixIO.saveToFile(MatrixIO.flattenArray(this.weights), aFileName);
    }
    public void saveBiasToFile(String aFileName) // Save bias
    {
        MatrixIO.saveToFile(this.bias, aFileName);
    }
}
