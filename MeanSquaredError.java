public class MeanSquaredError implements CostFunction{

    public float individualCost(float a, float b)
    {
        return ((b - a) * (b - a)) / 2;
    }

    public float individualError(float a, float b)
    {
        return b-a;
    }

    public float[] cost(float[] output, float[] correctOutput)
    {
        float[] returnCost = new float[output.length];
        for(int i=0; i<output.length; i++)
        {
            returnCost[i] = individualCost(output[i], correctOutput[i]);
        }
        return returnCost;
    }

    public float[] error(float[] output, float[] correctOutput)
    {
        float[] returnError = new float[output.length];
        for(int i=0; i<output.length; i++)
        {
            returnError[i] = individualError(output[i], correctOutput[i]);
        }
        return returnError;
    }
}
