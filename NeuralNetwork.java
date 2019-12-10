import java.util.Random;

public class NeuralNetwork {
    int length;

    private Layer[] layers;
    private Layer inputLayer;
    private Layer outputLayer;
    private CostFunction costFunction;
    private float learningRate;

    private float[][] train_inputs;
    private float[][] train_outputs;

    private float[][] test_inputs;
    private float[][] test_outputs;

    public NeuralNetwork()
    {
        this.inputLayer = null;
        this.layers = new Layer[0];
        this.length = 0;
        this.costFunction = new MeanSquaredError();
        this.learningRate = 0.1f;
    }

    public void train()
    {
        if(train_inputs == null || train_outputs==null)
        {
            System.out.println("[FATAL] Unable to train network: data not set.");
            return;
        }
        float avgError = 0;
        for(int i=0; i<train_inputs.length; i++)
        {
            float[] input = train_inputs[i];
            float[] correctOutput = train_outputs[i];
            setInput(input);
            feedForward();
            float[] output = getOutput();
            float[] cost = costFunction.cost(output, correctOutput);
            for(int j=0; j<cost.length; j++)
            {
                avgError+=cost[j];
            }
            propagateBack(correctOutput);
        }
        avgError/=train_inputs.length;
        System.out.print("Cost: " + avgError);
        if(test_inputs!=null)
        {
            avgError = 0;
            for(int i=0; i<test_inputs.length; i++)
            {
                float[] input = test_inputs[i];
                float[] correctOutput = test_outputs[i];
                setInput(input);
                feedForward();
                float[] output = getOutput();
                float[] cost = costFunction.cost(output, correctOutput);
                for(int j=0; j<cost.length; j++)
                {
                    avgError+=cost[j];
                }
            }
            avgError/=test_inputs.length;
            System.out.print("\tTest Cost: " + avgError);
        }
        System.out.println();
    }

    public void setTrainingData(float[][] inputs, float[][] outputs)
    {
        this.train_inputs = inputs;
        this.train_outputs = outputs;
    }
    public void setTestingData(float[][] inputs, float[][] outputs)
    {
        this.test_inputs = inputs;
        this.test_outputs = outputs;
    }

    public void setLearningRate(float aRate)
    {
        this.learningRate = aRate;
        for(int i=1; i<layers.length; i++)
            layers[i].setLearningRate(aRate);
    }
    public void addLayer(Layer aLayer)
    {
        if(length!=0)
            aLayer.setPreviousLayer(layers[length-1]);
        else
            this.inputLayer = aLayer;
        Layer[] temp = new Layer[length+1];
        temp[length] = aLayer;
        for(int i=0; i<length; i++)
        {
            temp[i] = layers[i];
        }
        this.outputLayer = aLayer;
        layers = temp;
        length++;
    }

    public float[] getOutput()
    {
        if(outputLayer==null){
            System.out.println("[FATAL] Attempted to get output but there is no output layer in network.");
            return null;
        }
        return outputLayer.getNeurons();
    }

    public void setInput(float[] aInput)
    {
        if(inputLayer==null){
            System.out.println("[FATAL] Attempted 'setInput' but there is no input layer in network.");
            return;
        }
        inputLayer.setNeurons(aInput);
    }

    public void feedForward()
    {
        float[] previousNeurons = inputLayer.getNeurons();
        for(int i=1; i<layers.length; i++)
        {
            previousNeurons = layers[i].feedForward(previousNeurons);
        }
    }

    public void propagateBack(float[] correctOutput)
    {
        float[] currentError = costFunction.error(getOutput(), correctOutput);
        for(int i=length-1; i>0; i--)
        {
            currentError = layers[i].propagateBack(currentError);
        }
    }

    public static float[][] GenerateWeights2D(int dimX, int dimY)
    {
        float[][] weights = new float[dimY][dimX];
        for(int x=0; x<dimX; x++)
        {
            for(int y=0; y<dimY; y++)
                weights[y][x] = (float) (Math.random()*2-1);
        }
        return weights;
    }

    public static float[] GenerateBias2D(int size)
    {
        float[] bias = new float[size];
        for(int i=0; i<size; i++)
            bias[i] = (float) (Math.random()*2-1);
        return bias;
    }
}
