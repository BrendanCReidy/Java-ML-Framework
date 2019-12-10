public class InputLayer implements Layer {
    public String name = "Input Layer";
    private float[] neurons;
    private int length;
    public InputLayer(int aSize)
    {
        this.neurons = new float[aSize];
        this.length = aSize;
    }
    public float[] getNeurons()
    {
        return this.neurons;
    }
    public float[] feedForward(float[] aLayer){
        System.out.println("[ERROR] Feed forward called on input layer.");
        return aLayer;
    }
    public float[] propagateBack(float[] aLayer)
    {
        System.out.println("[ERROR] Propagate back called on input layer.");
        return aLayer;
    }
    public void setPreviousLayer(Layer aLayer)
    {
        System.out.println("[WARNING] Attempted to set previous layer for input layer... Ignoring");
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
        System.out.println("[WARNING] Attempted to set learning rate for input layer... Ignoring");
    }
    public String toString()
    {
        return this.name;
    }
}
