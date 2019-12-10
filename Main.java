public class Main {
    public static void main(String[] args) {
        float[][] input_data = {
                {0, 0, 0},
                {0, 0, 1},
                {0, 1, 0},
                {0, 1, 1},
                {1, 0, 0},
                {1, 0, 1},
                {1, 1, 0},
                {1, 1, 1}
        };
        float[][] output_data = {
                {1, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 1}
        };
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
        NeuralNetwork network = new NeuralNetwork();
        network.setTrainingData(inputData, outputData);
        network.setTestingData(testInput, testOutput);
        network.addLayer(new InputLayer(inputData[0].length));
        network.addLayer(new FullyConnected(1024, "sigmoid"));
        network.addLayer(new FullyConnected(512, "sigmoid"));
        network.addLayer(new FullyConnected(256, "sigmoid"));
        network.addLayer(new FullyConnected(128, "sigmoid"));
        network.addLayer(new FullyConnected(64, "sigmoid"));
        network.addLayer(new FullyConnected(32, "sigmoid"));
        network.addLayer(new FullyConnected(16, "sigmoid"));
        network.addLayer(new FullyConnected(outputData[0].length, "sigmoid"));
        network.setInput(trainingData[0]);
        network.feedForward();
        network.propagateBack(trainingData[1]);
        System.out.println("Training...");
        for(int i=0; i<10; i++) {
            network.train();
        }
        //*/
    }
}
