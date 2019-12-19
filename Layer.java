/*
    By Brendan C. Reidy
    Created 12/10/2019
    Last Modified 12/10/2019
    Interface for layers
 */

public interface Layer {
    public float[] feedForward(float[] aLayer); // Feed forward the layer
    public float[] propagateBack(float[] aLayer); // Propagate back the layer
    public float[] getNeurons(); // Get neurons in layer
    public void setNeurons(float[] aNeurons); // Set neurons in layer
    public void setPreviousLayer(Layer aLayer); // Set the 'pointer' to the previous layer
    public void setLearningRate(float aLearningRate); // Set the learning rate of the layer
    public void saveWeightsToFile(String aFileName); // Save the layer weights to a file
    public void saveBiasToFile(String aFileName); // Save the bias of the layer to a file
    public int size(); // Get total size of the layer
    public int sizeX(); // Get the sizeX of the layer
    public int sizeY(); // Get the sizeY of the layer
    public String toString(); // Returns a string of important variables in the layer
}
