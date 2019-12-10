public class FullyConnected implements Layer {
    public String name = "Fully Connected Layer";

    private ActivationFunction activationFunction;
    private Layer previousLayer;
    private int length;
    private float learningRate;

    private float[][] weights;
    private float[] bias;
    private float[] neurons;

    public FullyConnected(int aLength, String activationFunctionName)
    {
        this.activationFunction = ActivationFunctions.getByName(activationFunctionName);
        this.length = aLength;
        this.neurons = new float[aLength];
        this.weights = null;
        this.bias = NeuralNetwork.GenerateBias2D(aLength);
        this.learningRate = 0.1f;
    }
    public FullyConnected(float[][] aWeights, float[] aBias, String activationFunctionName)
    {
        this.activationFunction = ActivationFunctions.getByName(activationFunctionName);
        this.length = aBias.length;
        this.neurons = new float[aBias.length];
        this.weights = aWeights;
        this.bias = aBias;
        this.learningRate = 0.1f;
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
        this.neurons = MatrixMath.dotProduct(aLayer, this.weights);
        this.neurons = MatrixMath.sum(this.neurons, this.bias);
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
                previousError[j] += weights[i][j] * neuronError;
                weights[i][j] += neuronError * previousLayer.getNeurons()[j] * learningRate;
            }
            bias[i]+=neuronError*learningRate;
        }
        return previousError;
    }
    public void setLearningRate(float aLearningRate)
    {
        this.learningRate = aLearningRate;
    }
    public int size()
    {
        return this.length;
    }
    public String toString()
    {
        return name;
    }
}
