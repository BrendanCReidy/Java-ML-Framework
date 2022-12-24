# Java Machine Learning "jFlow"
Java Machine Learning Project
By Brendan C Reidy

##### Description:
This program trains neural networks using stochastic gradient descent. This program can handle a wide variety of network structures from RNN's and LTSM's to autoencoders.
The code online is specifically using the **MNIST** dataset, but can be used with any dataset. MNIST is too large to upload to GitHub but can be found [here](http://yann.lecun.com/exdb/mnist/)

Instructions on downloading and formatting the MNIST dataset can be found [here](http://rasbt.github.io/mlxtend/user_guide/data/loadlocal_mnist/)

---------------------------------------------------------------
## Loading Files
Files in CSV format are loaded into two dimensional floating point arrays using the MatrixIO object
Example:
```
float[][] trainingData = MatrixIO.readTrainingData("FILE_NAME", NUM_LINES); // Loads csv file to trainingData variable
```


## Creating a Neural Network
In order to create a Neural Network, start by creating a Neural Network object:

```
NeuralNetwork network = new NeuralNetwork(); // Initializes object
```
Next you want to set the training and validation (test) data for the network:
```
network.setTrainingData(trainingData, trainLabels);
network.setTestingData(testingData, testLabels);
```
Next you can begin definining the topology of the Network. Start with the input layer:
```
network.addLayer(new Input2D(28, 28)); // Creates input layer; MNIST's dimensions are 28x28
```
Next you can begin defining the intermediate layers (you can define as many or as few as you like):
```
network.addLayer(new FullyConnectedBinary(16, "reverse sigmoid")); // Creates a hidden layer with 16 hidden neurons and uses 'reverse sigmoid' as the activation function (see 'Activation functions')
```
Next define the output layer:
```
network.addLayer(new FullyConnectedBinary(10, "reverse sigmoid")); // 10 neurons, reverse sigmoid activation
```

## Running a Neural Network
Now that the neural network hsa been created, you can run the network for one epoch with:
```
network.train();
```
Run for multiple epochs with:
```
for(int i=0; i<numEpochs; i++) {
    network.train();
}
```

## Getting accuracy
You can find the validation accuracy by adding the following line before running the train method:
```
network.addDatasetAnaylsisModel(new MaxOutputAccuracy());
```

## Saving results
You can save the neural network to a file using the following:
```
network.saveToFile("OUTPUT_DIRECTORY_NAME/");
```
This will auto generate a README with info about the network, a file with the training and validation cost over time, and all of the floating point and ternary/binary weights and biases throughout the network

## Activation functions
The in circuit Neural Network uses 'reverse sigmoid' as the activation function, which is why it is used in training as well.

## Creating Activation Functions
Activtion functions can be easily implemented by modifying the following template (we'll use Sigmoid as an example)
```
public class Sigmoid implements ActivationFunction {
    String name = "Sigmoid";
    public float sigmoid(float x)
    {
        return 1 / (1 + (float) Math.exp(-x));
    }
    public float inverseSigmoidDerivative(float y)
    {
        return y*(1-y);
    }
    public float[] activate(float[] aLayer)
    {
        for(int i=0; i<aLayer.length; i++)
            aLayer[i] = sigmoid(aLayer[i]);
        return aLayer;
    }
    public float[] activationError(float[] aLayer)
    {
        for(int i=0; i<aLayer.length; i++)
            aLayer[i] = inverseSigmoidDerivative(aLayer[i]);
        return aLayer;
    }
    public String getName()
    {
        return this.name;
    }
    public String toString()
    {
        return this.name;
    }
}
```
Change the "activate" method to your activation function and the "activationError" method to you error created by that layer (propogated through the model)
