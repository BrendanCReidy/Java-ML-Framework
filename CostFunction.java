/*
    By Brendan C. Reidy
    Created 12/10/2019
    Last Modified 12/17/2019
        Interface for cost functions
 */

public interface CostFunction {
    public float[] cost(float[] output, float[] correctOutput); // The cost function
    public float[] error(float[] output, float[] correctOutput); // The derivative of the cost function
    public String toString();
}
