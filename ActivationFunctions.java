/*
    By Brendan C. Reidy
    Created 12/10/2019
    Last Modified 12/10/2019
    Activation Functions:
        Catalog with all activation functions for easy use by user
 */

public class ActivationFunctions {
    static ActivationFunction[] allFunctions = {new Sigmoid(), new ReverseSigmoid(), new SoftMax(), new RELU()}; // List of all the functions
    static ActivationFunction getByName(String aName) // Get the activation function by name (makes user interface much smoother)
    {
        for(int i = 0; i<allFunctions.length; i++)
        {
            ActivationFunction current = allFunctions[i];
            if(current.getName().equalsIgnoreCase(aName))
                return allFunctions[i];
        }
        System.out.println("[FATAL] Unable to find activation function: " + aName); // Tell user the specified activation function was not found
        return null;
    }
}
