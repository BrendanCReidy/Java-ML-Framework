public interface CostFunction {
    public float[] cost(float[] output, float[] correctOutput);
    public float[] error(float[] output, float[] correctOutput);
}
