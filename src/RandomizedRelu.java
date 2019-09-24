public class RandomizedRelu implements ActivationFunction {
    public double a;
    public RandomizedRelu(){
        this.a = 100;
    }
    public RandomizedRelu(double anA){
        this.a = anA;
    }
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
