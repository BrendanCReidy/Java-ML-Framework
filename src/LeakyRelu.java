public class LeakyRelu implements ActivationFunction {
    public static final double a = 100;
    public float activate(float x){
        if (x >= 0)
            return x;
        else
            return x / (float) a;
    }
    public float derivative(float x){
        if (x >= 0)
            return 1;
        else
            return (float) (1.0 / a);
    }
}
