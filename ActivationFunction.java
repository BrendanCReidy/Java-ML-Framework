/*
    By Brendan C. Reidy
    Created 12/10/2019
    Last Modified 12/10/2019
    Activation function interface
 */

public interface ActivationFunction {
    public float[] activate(float[] aLayer); // activate the layer
    public float[] activationError(float[] aLayer); // get the inverse activation error of the layer
    public String getName();
    public String toString();
}
