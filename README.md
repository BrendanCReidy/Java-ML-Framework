# Java Machine Learning "jFlow"
Java Machine Learning Project
By Brendan C Reidy

##### Description:
This program trains neural networks using stochastic gradient descent. This program can handle a wide variety of network structures from RNN's and LTSM's to autoencoders.
The code online is specifically using the **MNIST** dataset, but can be used with any dataset. MNIST is too large to upload to GitHub but can be found [here](http://yann.lecun.com/exdb/mnist/)

Instructions on downloading and formatting the MNIST dataset can be found [here](http://rasbt.github.io/mlxtend/user_guide/data/loadlocal_mnist/)

---------------------------------------------------------------
# Getting Started:
Familiarize yourself with how training data is formatted for these networks (See "Creating Training Data" below). If you understand how the training data is formatted you can start training using the following code:
```
int numTrain = 60000; // Number of training inputs
int numTest = 10000;  // Number of validation inputs

int inputSizeX = 28;
int inputSizeY = 28;
int outputSize = 10;

float[][] trainingData = MatrixIO.readTrainingData("trainX.txt", numTrain);
float[][] testingData = MatrixIO.readTrainingData("testX.txt", numTest);

float[][] trainLabels = MatrixIO.readTrainingData("trainY.txt", numTrain);
float[][] testLabels = MatrixIO.readTrainingData("testY.txt", numTest);


NeuralNetwork network = new NeuralNetwork();
network.setTrainingData(trainingData, trainLabels);
network.setTestingData(testingData, testLabels);
network.addLayer(new PlaceHolder2D(inputSizeX, inputSizeY)); // Input layer (28x28 pixel imge in this case)
network.addLayer(new FullyConnectedTernary(16, "sigmoid"));  // First hidden layer (16 neurons, sigmoid activation)
network.addLayer(new FullyConnectedTernary(16, "sigmoid"));  // Second hidden layer (16 neurons, sigmoid activation)
network.addLayer(new FullyConnectedTernary(outputSize, "sigmoid")); // Output layer (10 neurons, sigmoid activation)

network.train(); // Trains for one epoch
```

---------------------------------------------------------------
# Creating Training Data (DEPRECIATED, NEW INFO COMING SOON):
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

