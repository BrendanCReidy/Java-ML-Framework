public class Sigmoid implements ActivationFunction {
    String name = "Sigmoid";
    public float sigmoid(float x)
    {
        return 1 / (1 + (float) Math.exp(-x));
    }
    public float inverseSigmoidDerivative(float y)
    {
        return y*(1-y);
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
