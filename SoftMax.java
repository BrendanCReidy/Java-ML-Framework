public class SoftMax implements ActivationFunction{
    String name = "SoftMax";
    float currentHighest;
    int highestIndex;

    public float inverseSigmoidDerivative(float y)
    {
        return y*(1-y);
    }
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
