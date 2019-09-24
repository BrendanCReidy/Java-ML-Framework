# Java Machine Learning "jFlow"
Java Machine Learning Project
By Brendan C Reidy (Copyright @ Brendan C Reidy)

##### Description:
This program trains neural networks using stochastic gradient descent. This program can handle a wide variety of network structures from RNN's and LTSM's to autoencoders.
The code online is specifically using the **MNIST** dataset, but can be used with any dataset. MNIST is too large to upload to GitHub but can be found [here](http://yann.lecun.com/exdb/mnist/)

Instructions on downloading and formatting the MNIST dataset can be found [here](http://rasbt.github.io/mlxtend/user_guide/data/loadlocal_mnist/)

---------------------------------------------------------------
# Getting Started:
Familiarize yourself with how training data is formatted for these networks (See "Creating Training Data" below). If you understand how the training data is formatted you can start training your network using four lines of code:
```
Matrix2D networkMatrix = NeuralNetwork.GenerateNetwork(Matrix2D trainingData, int numHiddenLayers, int sizeHiddenLayer)
NeuralNetwork network = new NeuralNetwork(networkMatrix);
network.setTrainingData(trainingData);
network.train();
```
---------------------------------------------------------------
# Sample Main:
This main is using the MNIST dataset to train.
###### Code:
```
int numEpoch = 5
Matrix2D trainingData = Matrix2D.loadFromFile("imageTrainingData.csv");
Matrix2D trainingDataSubset = Matrix2D.loadFromFile("imageTrainingDataSubset.csv");
Matrix2D networkMatrix = NeuralNetwork.GenerateNetwork(trainingData, 2, 16);
networkMatrix.saveToFile("networkMatrix.csv");
NeuralNetwork network = new NeuralNetwork(networkMatrix;
network.setConsoleOutput(true);
network.setTrainingData(trainingData);
network.setTrainingSubset(trainingDataSubset);
network.setLearningRate(0.2);
network.printSpecs();
System.out.println("Training...");
for(int i=0; i<numEpoch; i++) {
	network.train();
}
network.setBest();
network.weights.saveToFile("imageWeights.csv");
network.bias.saveToFile("imageBias.csv");
System.out.println("Accuracy: " + (1-network.getSubsetCost()));
```
###### Output>
```
Architecture:
	Input size: 784
	Output size: 10
	Hidden layers: 2
	Hidden neurons: 32
	Weight count: 12960
	Bias count: 42
	Learning rate: 0.2
Training...
Cost: 0.054594295231731656
Cost: 0.05146965578893518
Cost: 0.048291948030104795
Cost: 0.04641472056640749
Cost: 0.04446579799420346
Accuracy: 0.9495821554390572
```
---------------------------------------------------------------
# Matrix2D:
The Matrix2D object is simply a resizable array containing float arrays.
## Example:
```
matrix.getArrayAt(0) --> {0.1, 0.4, 0.4}
matrix.getArrayAt(1) --> {0.2, 0.6, 0.8, 0.3, 0.5}
matrix.getArrayAt(2) --> {0.7}
```

## Methods:
- add(float[] floatArray); // Works with integers and doubles as well. Size of matrix is increased and the input is added to the end of the matrix
- getArrayAt(int index); // Returns float[] at given index
- print(); // Outputs the contents of the array to console
- printInt(); // Rounds each number, then outputs the array to console
- reverse(); // Returns a reversed Matrix2D object: a[0] = a[n-1]; a[1] = a[n-2]; a[2] = a[n-3] ... etc
- saveToFile(String aFileName); // Saves the matrix to a file. Can be .csv or .txt

## Static Methods:
- Matrix2D.loadFromFile(string aFileName); Returns a Matrix2D object given a file location.

---------------------------------------------------------------
# NeuralNetwork:
The NeuralNetwork object is responsible for implementing the RNN.

## Usage:

- NeuralNetwork(Matrix2D aNetwork) // Creates a neural network based on a Matrix2D object that contains the structure. Generates new weights and bias (Note: use NeuralNetwork.GenerateNetwork() to generate - Matrix2D object for structure)
- NeuralNetwork(Matrix2D aNetwork, Matrix2D aWeights, Matrix2D aBias) // Creates a neural network object where aWeights and aBias are Matrix2D objects of trained weights and bias'

## Example:
```
Matrix2D networkMatrix = NeuralNetwork.GenerateNetwork(Matrix2D trainingData, int numHiddenLayers, int sizeHiddenLayer)
NeuralNetwork network = new NeuralNetwork(networkMatrix);
network.setTrainingData(trainingData);
network.setTrainingSubset(trainingDataSubset); // Optional but reccomended to avoid overfitting
network.train();
```

## Methods:

- setActivationFunction(ActivationFunction aFunction) // (Default: Sigmoid) Sets the activation function for the network (Note: every layer uses the same activation)
- setConsoleOutput(boolean aValue) // (Default: false) if true will output the cost after each epoch
- setLearningRate(double aRate) // (Default: 0.1) Sets the learning rate of the network
- setTrainingData(Matrix2D aData) // Sets the training data for the network
- setTrainingSubset(Matrix2D aData) // Sets the training subset data for the network
- setInput(float[] input) // Sets the input for the network

- float[] getOutput() // Returns the output layer of the network
- clear() // Sets all values in the network to 0
- Matrix2D getBestWeights() // Returns highest performing weights 
- Matrix2D getBestBias() // Returns highest performing bias
- setBest() // Sets the network to the highest performing weights and bias
- train() // Will train the network for one epoch
- train(int iterations) // Will train the network for specified number of iterations
- double getSubsetCost() // calculates and returns the subset cost for the network
- propogateBack(float[] correctOutput) // Performs back propogation given the correct output for the network
- feedForward() // Feeds the input values through the network
- printResults() // Prints the input and corresponding output
- printIntResults() // Prints the input and corresponding output as an integer
- printSpecs() // Prints the structure of the network as well as the total number of weights and bias.

## Static Methods:

- Matrix2D NeuralNetwork.GenerateNetwork(int sizeInput, int sizeOutput, int numHiddenLayers, int sizeHiddenLayer) // Returns a network structure Matrix2D object given the size of the input, the size of the output, the number of hidden layers, and the size of the hidden layers. (Note: you can save this to a .csv file and edit the structure manually and the network will run fine.)
- Matrix2D NeuralNetwork.GenerateNetwork(Matrix2D trainingData, int numHiddenLayers, int sizeHiddenLayer) // Returns a network structere Matrix2D object given training data, the number of hidden layers, and the size of the hidden layer.
- Matrix2D NeuralNetwork.GenerateWeights(Matrix2D aMatrix) // Returns a Matrix2D object of weights given a network structure
- Matrix2D NeuralNetwork.GenerateBias(Matrix2D aMatrix) // Returns a Matrix2D object of bias given a network structure


---------------------------------------------------------------
# Creating Training Data:
The training data is stored in a .csv or .txt file. The training data is formatted such that
every odd line is the input, and the corresponding even line is the correct output (Where inputs and outputs are floating point arrays)

## Example:
```
Input1
Output1
Input2
Output2
```

Use Matrix2D.loadFromFile("FILE_NAME") if you have data in this format ready to be trained

For images, this program provides built in support. First, you need to create two separate .csv files: one for input, and one for labels.
Example: images.csv, imageLabels.csv
In this case, the image file needs to be a .csv file where every line is a new image.
The image is represented as a list of integers from 0 to 255 (0, 255, 223, 0, 1, etc).
The label file should have the corresponding classification in integer format on the same line.

## Example:

###### image.csv:
```
0, 255, 223, 0, 34, 42, 255, 255 // Lets say this is a 1
4, 233, 111, 0, 255, 255, 255, 255 // Lets say this is a 4
4, 47, 47, 0, 0, 255, 0, 255 // Lets say this is an 8
...
```
###### imabeLabels.csv:
```
1
4
8
...
```

Note: The classifications are not limited to numbers. You will need to decide ahead of time which numbers represent which classifications. Ex (1 = dog. 2 = cat. 3 = bird)
Once you have your input and label files properly formatted, you can run the following code:
```
Matrix2D input = Matrix2D.loadFromFile("INPUT_FILE_NAME.csv");
Matrix2D labels = Matrix2D.loadFromFile("LABEL_FILE_NAME.csv");
Matrix2D trainingData = TrainingData.GenerateImageTrainingDataFromCSV(input, labels);
trainingData.saveToFile("DESIRED_TRAINING_DATA_NAME.csv);
```

