/*
    By Brendan C. Reidy
    Created 12/10/2019
    Last Modified 12/10/2019
    Mean squared error cost function
 */

public class MeanSquaredError implements CostFunction{

    private String name = "Mean Squared Error";

    public float individualCost(float a, float b)
    {
        return ((b - a) * (b - a)) / 2;
    } // MSE

    public float individualError(float a, float b)
    {
        return b-a;
    } // MSE derivative

    public float[] cost(float[] output, float[] correctOutput) // MSE for array
    {
        float[] returnCost = new float[output.length];
        for(int i=0; i<output.length; i++)
        {
            returnCost[i] = individualCost(output[i], correctOutput[i]);
        }
        return returnCost;
    }

    public float[] error(float[] output, float[] correctOutput) // MSE derivative for array
    {
        float[] returnError = new float[output.length];
        for(int i=0; i<output.length; i++)
        {
            returnError[i] = individualError(output[i], correctOutput[i]);
        }
        return returnError;
    }

    public String toString()
    {
        return name;
    }
}
