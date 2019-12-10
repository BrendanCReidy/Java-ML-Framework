public class Conv2D implements Layer {
    public String name = "Input Layer";

    private Layer previousLayer;
    private float[] neurons;
    private float[][] neurons2D;
    private float[][] kernal;
    private int length;
    private float learningRate;

    public Conv2D(int kernalSize, int padSize)
    {
        this.length = (padSize*padSize)/(kernalSize*kernalSize);
        this.neurons = new float[this.length];
        this.neurons2D = new float[padSize][padSize];
        this.kernal = new float[kernalSize][kernalSize];
        this.learningRate = 0.1f;
    }

    public float[] getNeurons()
    {
        return this.neurons;
    }
    public float[] feedForward(float[] aLayer){
        //TODO: Implement feed forward for convolution layer
        return aLayer;
    }
    public float[] propagateBack(float[] aLayer)
    {
        //TODO: Implement back propagation for convolution layer
        return aLayer;
    }
    public void setPreviousLayer(Layer aLayer)
    {
        this.previousLayer = aLayer;
    }
    public void setNeurons(float[] aNeurons)
    {
        this.neurons = aNeurons;
    }
    public int size()
    {
        return this.length;
    }
    public void setLearningRate(float aLearningRate)
    {
        this.learningRate = aLearningRate;
    }
    public String toString()
    {
        return this.name;
    }
}
