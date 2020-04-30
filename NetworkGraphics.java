import java.awt.*;
import java.awt.image.BufferedImage;

// Created by Brendan Reidy
// Created on 4/22/2020
// Last modified 4/29/2020

// Network graphics for one dimensional input depictions of Neural Networks

public class NetworkGraphics {

    static int neuronRadius = 40*2; // Radius of neuron (pixels)
    static int synapseThickness = 1; // Minimum thickness for synapse (pixels)
    static int maxSynapseThickness = 20*2; // Maximum thickness for synapse (pixels)
    static int synapseDistance = 1000*2; // Distance between neurons in one layer and the next layer (pixels)
    static int neuronDistance = 60*2;

    Layer[] layers; // Layers in neural network
    int maxY = 0;
    int maxX = 0;

    int[] layerSizes;

    public NetworkGraphics(NeuralNetwork network)
    {
        this.layers = network.getLayers(); // Get the layers from the network object
        this.maxY = getMaxY(); // Get the maximum Y value for the network (pixels)
        this.maxX = getMaxX(); // Get the maximum X value for the network (pixels)
    }

    private int getMaxX()
    {
        // SizeX =(networkLength - 1)*neuronRadius*2 + (networkLength - 1)*synapseDistance + neuronRadius*2
        int sizeX = neuronRadius*2;
        for(int i=1; i<layers.length; i++) {
            sizeX += neuronRadius*2 + synapseDistance;
        }
        return sizeX;
    }
    private int getMaxY()
    {
        // Max size Y is equal to the the size of the largest layer in the network times the size of each layer
        layerSizes = new int[layers.length];
        int maxY = 0;
        for(int i=0; i<layers.length; i++)
        {
            int sizeY = neuronRadius*2*layers[i].size()+ neuronDistance*layers[i].size() - neuronRadius;
            if(sizeY > maxY)
                maxY = sizeY;
            layerSizes[i] = sizeY;
        }
        return maxY;
    }

    public BufferedImage drawNetwork()
    {
        int sizeX = maxX;
        int sizeY = maxY;

        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB); // Start with fresh image
        Layer previous = layers[0]; //
        for(int indexX=1; indexX < layers.length; indexX++) // Draw synapses first
        {
            float[][] weights = layers[indexX].getWeights(); // Get value of synapse (aka weight)
            for(int x=0; x<previous.size(); x++) // Iterate over each synapse
            {
                for(int y=0; y<layers[indexX].size(); y++)
                {
                    float synapse = weights[y][x];
                    drawSynapse(image, indexX-1, x, indexX, y, synapse); // Draw synapse between neurons
                }
            }
            previous = layers[indexX];
        }
        // Draw neurons
        for(int indexX=0; indexX < layers.length; indexX++)
        {
            float[] neurons = layers[indexX].getNeurons();
            for(int indexY = 0; indexY < neurons.length; indexY++)
            {
                image = drawNeuron(image, indexX, indexY, neurons[indexY]);
            }
        }
        return image;
    }
    public int getNeuronPosX(int indexX)
    {
        // Returns the neurons X position at the "indexX"th position of the layer array
        int posX = indexX*synapseDistance + indexX*neuronRadius*2 + neuronRadius;
        return posX;

    }
    public int getNeuronPosY(int indexX, int indexY)
    {
        // Returns the positionY of the neuron centered along the canvas
        int centerY = maxY / 2;
        int offsetY = layerSizes[indexX] / 2;
        int offset = centerY - offsetY;

        int posY = indexY*neuronDistance + indexY*neuronRadius*2 + neuronRadius;
        posY+=offset;
        return posY;
    }
    public BufferedImage drawNeuron(BufferedImage image, int indexX, int indexY, float neuron)
    {
        // Draws the neuron at specified indexes
        int activation = (int) (neuron*255);
        int posX = getNeuronPosX(indexX);
        int posY = getNeuronPosY(indexX, indexY);
        Color color = new Color(activation, activation, activation);
        ImageUtils.drawCircle(image, color, posX, posY, neuronRadius); // Represent neuron as circle with the neurons activation as the brightness of neuron
        return image;
    }
    public BufferedImage drawSynapse(BufferedImage image, int indexXA, int indexYA, int indexXB, int indexYB, float synapse)
    {
        // Draw synapse between specified neurons
        float magnitude = Math.abs(synapse / 10f);
        int size = (int) (((magnitude)*maxSynapseThickness));
        if(size<1)
            size=synapseThickness;
        float sig = (1 / (1 + (float) Math.exp(-(magnitude*2))));
        int pixelValue = (int) (sig*255);
        int posX1 = getNeuronPosX(indexXA);
        int posY1 = getNeuronPosY(indexXA, indexYA);

        int posX2 = getNeuronPosX(indexXB);
        int posY2 = getNeuronPosY(indexXB, indexYB);
        Color color = new Color(pixelValue, 0, 0);
        if(synapse>0)
            color = new Color(0, pixelValue, 0);
        image = ImageUtils.drawLine(image, color, size, posX1, posY1, posX2, posY2);
        return image;
    }

    public int getWindowSizeX(int maxX)
    {
        // Returns the appropriate window sizeX based on the network size
        int y = this.maxY;
        int x = this.maxX;
        float ratio = (float) x/(float) y;
        if(x>maxX && x>y)
        {
            return maxX;
        }else if(x>maxX){
            return (int) (maxX*ratio);
        }
        return x;
    }
    public int getWindowSizeY(int maxY)
    {
        // Returns the appropriate window sizeY based on the network size
        int y = this.maxY;
        int x = this.maxX;
        float ratio = (float) y/(float) x;
        if(y>maxY && y>x)
        {
            return maxY;
        }else if(y>maxY){
            return (int) (maxY*ratio);
        }
        return y;
    }
}
