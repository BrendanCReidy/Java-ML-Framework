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

