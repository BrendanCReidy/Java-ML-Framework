/*
    By Brendan C. Reidy
    Created 12/10/2019
    Last Modified 12/10/2019
        SoftMax activation function (under construction).
 */

public class SoftMax implements ActivationFunction{
    String name = "SoftMax";
    float currentHighest;
    int highestIndex;

    public float inverseSigmoidDerivative(float y)
    {
        return y*(1-y);
    } // Ignore lol
    public float[] activate(float[] aLayer)
    {
        //TODO: Implement softmax
        return aLayer;
    }
    public float[] activationError(float[] aLayer)
    {
        // TODO Implement softmax derivative
        return aLayer;
    }
    public String getName()
    {
        return this.name;
    }
    public String toString()
    {
        return this.name;
    }
}
