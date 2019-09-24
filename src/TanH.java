public class TanH implements ActivationFunction{
    public float activate(float x){
        return (float) ((Math.exp(x) - Math.exp(-x))/(Math.exp(x) + Math.exp(-x)));
    }
    public float derivative(float x){
        return (float) (((Math.exp(x) - Math.exp(-x))*(Math.exp(x) - Math.exp(-x)))/((Math.exp(x) + Math.exp(-x))*(Math.exp(x) + Math.exp(-x))));
    }
}
