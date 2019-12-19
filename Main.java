/*
    By Brendan C. Reidy
    Created 12/10/2019
    Last Modified 12/18/2019
    Main class. "Driver"
 */

public class Main {
    public static void main(String[] args) {
        ///*
        float[][] trainingData = MatrixIO.readTrainingData("imageTrainingData.csv", 60000);
        float[][] testingData = MatrixIO.readTrainingData("imageTrainingDataSubset.csv", 10000);

        float[][] inputData = new float[trainingData.length / 2][];
        float[][] outputData = new float[trainingData.length / 2][];
        float[][] testInput = new float[testingData.length / 2][];
        float[][] testOutput = new float[testingData.length / 2][];

        System.out.println("Formatting data...");
        for (int i = 0; i < inputData.length; i++) {
            inputData[i] = trainingData[i * 2];
            outputData[i] = trainingData[(i * 2) + 1];
            if(i<testInput.length)
            {
                testInput[i] = testingData[i*2];
                testOutput[i] = testingData[(i*2)+1];
            }
        }
        //*/
        NeuralNetwork network = new NeuralNetwork();
        network.setTrainingData(inputData, outputData);
        network.setTestingData(testInput, testOutput);
        network.addLayer(new PlaceHolder2D(28, 28));
        network.addLayer(new Conv2D(1,2, 2, "sigmoid"));
        network.addLayer(new FullyConnected(32, "sigmoid"));
        network.addLayer(new FullyConnected(10, "sigmoid"));
        for(int i=0; i<20; i++) {
            network.train();
            network.saveToFile("Test/");
        }
    }
    public static int fromOneHotToNumber(float[] oneHot)
    {
        for(int i=0; i<oneHot.length; i++)
        {
            if(oneHot[i]==1)
                return i;
        }
        return -1;
    }
}
