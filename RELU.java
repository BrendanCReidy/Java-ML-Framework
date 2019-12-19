/*
    By Brendan C. Reidy
    Created 12/10/2019
    Last Modified 12/10/2019
    Sigmoid Activation Function
        Activates neurons with the sigmoid function 'deactivates' them with inverse derivative
 */

public class RELU implements ActivationFunction {
    String name = "RELU";
    public float relu(float x)
    {
        if(x>0)
            return x;
        return 0;
    }
    public float inverseReluDerivative(float y)
    {
        if (y > 0)
            return 1;
        if (y==0)
            return (float) 1;
        else
            return 0;
    }
    public float[] activate(float[] aLayer)
    {
        for(int i=0; i<aLayer.length; i++)
            aLayer[i] = relu(aLayer[i]);
        return aLayer;
    }
    public float[] activationError(float[] aLayer)
    {
        for(int i=0; i<aLayer.length; i++)
            aLayer[i] = inverseReluDerivative(aLayer[i]);
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
