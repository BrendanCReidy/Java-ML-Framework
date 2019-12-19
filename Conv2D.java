/*
    By Brendan C. Reidy
    Created 12/10/2019
    Last Modified 12/10/2019
        2D Convolution layer. Under heavy construction
 */

public class Conv2D implements Layer {
    public String name = "Convolution2D";

    private ActivationFunction activationFunction;
    private Layer previousLayer;
    private float[] neurons;
    private float[] bias;
    private float[][] filter;
    private int length;
    private int sizeX;
    private int sizeY;
    private int numFilters;
    private int stride;
    private int padX;
    private int padY;
    private float learningRate;

    public Conv2D(int filterNum, int filterSize, int aStride, int aPadX, int aPadY, String activationFunctionName)
    {
        init(filterNum, filterSize, aStride, aPadX, aPadY, activationFunctionName);
    }
    public Conv2D(int filterNum, int filterSize, int aStride, String activationFunctionName)
    {
        init(filterNum, filterSize, aStride, 0, 0, activationFunctionName);

    }
    private void init(int filterNum, int filterSize, int aStride, int aPadX, int aPadY, String activationFunctionName)
    {
        if(aStride>filterSize)
            System.out.println("[WARNING] It is not recommended to have a stride larger than the filter size.");
        this.numFilters = filterNum;
        this.activationFunction = ActivationFunctions.getByName(activationFunctionName);
        this.filter = NeuralNetwork.GenerateWeights2D(filterSize, filterSize);
        this.stride = aStride;
        this.padX = aPadX;
        this.padY = aPadY;
        this.learningRate = 0.1f;
    }

    public float[] getNeurons()
    {
        return this.neurons;
    }
    private float valueAt(int x, int y)
    {
        if(x<padX/2)
            return 0;
        if(y<padY/2)
            return 0;
        if(x>previousLayer.sizeX() + ((padX/2))-1)
            return 0;
        if(y>previousLayer.sizeY() + ((padY/2))-1)
            return 0;
        int indexX = x - padX/2;
        int indexY = y - padY/2;
        int index = indexY*previousLayer.sizeY() + indexX;
        return previousLayer.getNeurons()[index];
    }
    public float[] feedForward(float[] aLayer){
        int k=0;
        ///*
        for(int i=0; i<numFilters; i++) {
            for (int y = 0; y < (previousLayer.sizeY() + padY) - filter.length; y += stride) {
                for (int x = 0; x < (previousLayer.sizeX() + padX) - filter.length; x += stride) {
                    float sum = 0;
                    for (int yOffset = 0; yOffset < filter.length; yOffset++) {
                        for (int xOffset = 0; xOffset < filter.length; xOffset++) {
                            float value = valueAt(x + xOffset, y + yOffset);
                            float weight = filter[xOffset][yOffset];
                            sum += value * weight;
                        }
                    }
                    k++;
                    neurons[k] = sum + bias[k];
                }
            }
        }
        activationFunction.activate(neurons);
        //*/
        /*
        for(int y=0; y<(previousLayer.sizeX() + padY) - filter.length; y+=stride)
        {
            for(int x=0; x<(previousLayer.sizeX() + padX) - filter.length; x+=stride)
            {
                System.out.print(((int) (valueAt(x, y)+0.5)) + " ");
            }
            System.out.println();
        }
        */
        return this.neurons;
    }
    public float[] propagateBack(float[] currentError)
    {
        float[] previousError = new float[previousLayer.size()]; // Create a new array for previous error (cumulative error for all layers)
        float[] activationError = activationFunction.activationError(this.neurons); // get error with respect to activation function for current layer
        
        return previousError;
    }
    public void setPreviousLayer(Layer aLayer)
    {
        this.previousLayer = aLayer;
        if(((previousLayer.sizeX() + padX) - filter.length) % stride!=0)
            System.out.println("[FATAL] Incompatible filter and strides for input dimensionX");
        if(((previousLayer.sizeY() + padY) - filter.length) % stride!=0)
            System.out.println("[FATAL] Incompatible filter and strides for input dimensionY");
        this.sizeX = (((previousLayer.sizeX() + padX) - filter.length) / stride) + 1;
        this.sizeY = (((previousLayer.sizeY() + padY) - filter.length) / stride) + 1;
        int validation = 0;
        for(int x=0; x<(previousLayer.sizeX() + padX) - (filter.length-1); x+=stride)
        {
            validation++;
        }
        System.out.println(validation);
        System.out.println(this.sizeX);
        this.length = sizeX*sizeY*numFilters;
        this.neurons = new float[this.length];
        this.bias = NeuralNetwork.GenerateBias2D(this.length);
    }
    public void setNeurons(float[] aNeurons)
    {
        this.neurons = aNeurons;
    }
    public int size()
    {
        return (sizeX()+padX)*(sizeY()+padY)*numFilters;
    }
    public int sizeX()
    {
        return this.sizeX;
    }
    public int sizeY() { return this.sizeY; }
    public void setLearningRate(float aLearningRate)
    {
        this.learningRate = aLearningRate;
    }
    public String toString()
    {
        return this.name + ":" +
                "\n\t   Activation Function: " + this.activationFunction.toString() +
                "\n\t   Size: " + this.length +
                "\n\t   DimX: " + this.sizeX +
                "\n\t   DimY: " + this.sizeY +
                "\n\t   Number of filters: " + this.numFilters +
                "\n\t   Stride: " + this.stride +
                "\n\t   Filter Size: " + this.filter.length +
                "\n\t   Output size: " + this.length +
                "\n\t   Learning Rate: " + this.learningRate;
    }
    public void saveWeightsToFile(String aFileName)
    {
        //TODO: Save conv weights to file
    }
    public void saveBiasToFile(String aFileName)
    {
        //TODO: Save conv bias to file
    }
}
