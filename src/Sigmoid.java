public class Sigmoid implements ActivationFunction {
    public float activate(float x){
        return 1 / (1 + (float) Math.exp(-x));
    }
    public float derivative(float x){
        return x*(1-x);
    }
}
