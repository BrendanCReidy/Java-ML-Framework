public class ActivationFunctions {
    static ActivationFunction[] allFunctions = {new Sigmoid(), new SoftMax()};
    static ActivationFunction getByName(String aName)
    {
        for(int i = 0; i<allFunctions.length; i++)
        {
            ActivationFunction current = allFunctions[i];
            if(current.getName().equalsIgnoreCase(aName))
                return allFunctions[i];
        }
        System.out.println("[FATAL] Unable to find activation function: " + aName);
        return null;
    }
}
