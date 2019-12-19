/*
    By Brendan C. Reidy
    Created 12/17/2019
    Last Modified 12/17/2019
    Reverse Sigmoid Activation Function
        Activates neurons with the reverse sigmoid function 'deactivates' them with inverse derivative
 */

public class ReverseSigmoid implements ActivationFunction {
    String name = "Reverse Sigmoid";
    public float sigmoid(float x)
    {
        return 1 / (1 + (float) Math.exp(x));
    }
    public float inverseSigmoidDerivative(float y)
    {
        return y*(y-1);
    }
    public float[] activate(float[] aLayer)
    {
        for(int i=0; i<aLayer.length; i++)
            aLayer[i] = sigmoid(aLayer[i]);
        return aLayer;
    }
    public float[] activationError(float[] aLayer)
    {
        for(int i=0; i<aLayer.length; i++)
            aLayer[i] = inverseSigmoidDerivative(aLayer[i]);
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
