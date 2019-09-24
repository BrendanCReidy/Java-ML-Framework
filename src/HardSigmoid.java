public class HardSigmoid implements ActivationFunction {
    public float activate(float x){
        if(x>2.5)
            return 1;
        else if(x<2.5)
            return 0;
        return (float) (x*0.2 + ((float) 0.5));
    }
    public float derivative(float x){
        if(x>2.5)
            return 0;
        else if(x<2.5)
            return 0;
        return ((float) 0.2);
    }
}
