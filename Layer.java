public interface Layer {
    public float[] feedForward(float[] aLayer);
    public float[] propagateBack(float[] aLayer);
    public float[] getNeurons();
    public void setNeurons(float[] aNeurons);
    public void setPreviousLayer(Layer aLayer);
    public void setLearningRate(float aLearningRate);
    public int size();
    public String toString();
}
