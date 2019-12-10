public interface ActivationFunction {
    public float[] activate(float[] aLayer);
    public float[] activationError(float[] aLayer);
    public String getName();
    public String toString();
}
