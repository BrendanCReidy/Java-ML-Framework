/*
    By Brendan C. Reidy
    Created 12/10/2019
    Last Modified 12/18/2019
    Input layer
        A place holder for input neurons
 */

import java.awt.geom.Dimension2D;

public class PlaceHolder2D implements Layer {
    public String name = "PlaceHolder2D";
    private float[] neurons;
    private int length;
    private int width;

    public PlaceHolder2D(int aSizeX, int aSizeY)
    {
        this.neurons = new float[aSizeX*aSizeY];
        this.length = aSizeX;
        this.width = aSizeY;
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
        System.out.println("[WARNING] Attempted to set previous layer for placeholder layer... Ignoring");
    }
    public void setNeurons(float[] aNeurons)
    {
        this.neurons = aNeurons;
    }
    public int size()
    {
        return sizeX()*sizeY();
    }
    public int sizeX()
    {
        return this.length;
    }
    public int sizeY()
    {
        return this.width;
    }
    public void setLearningRate(float aLearningRate)
    {
        System.out.println("[WARNING] Attempted to set learning rate for input layer... Ignoring");
    }
    public String toString()
    {
        return this.name + ":" +
                "\n\t   Size: " + this.size() +
                "\n\t   DimX: " + this.sizeX() +
                "\n\t   DimY: " + this.sizeY();
    }
    public void saveWeightsToFile(String aFileName)
    {
        System.out.println("[WARNING] Attempted to save weights for input layer... Ignoring");
    }
    public void saveBiasToFile(String aFileName)
    {
        System.out.println("[WARNING] Attempted to save bias for input layer... Ignoring");
    }
}
