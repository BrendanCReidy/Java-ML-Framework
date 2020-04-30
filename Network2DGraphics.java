import java.awt.*;
import java.awt.image.BufferedImage;

// Created by Brendan Reidy
// Created on 4/22/2020
// Last modified 4/29/2020

// Network graphics for two dimensional input depictions of Neural Networks
// ***Almost identical to 1D except some math for the 2D input layer and calculation for overall size***

public class Network2DGraphics {
    static float scale = 1f; // Used to quickly increase or decrease image quality while maintaining selected ratios

    static int neuronRadius = (int) (60*scale);
    static int synapseThickness = 1;
    static int maxSynapseThickness = (int) (20*scale);
    static int synapseDistance = (int) (1000*scale);
    static int neuronDistance = (int) (60*scale);
    static int inputNeuronDistance = (int) (40*scale);

    Layer[] layers;
    int maxY = 0;
    int maxX = 0;
    int startOffset = 0;

    int[] layerSizes;

    public Network2DGraphics(NeuralNetwork network)
    {
        this.layers = network.getLayers();
        this.maxY = getMaxY();
        this.maxX = getMaxX();
    }

    private int getMaxX()
    {
        int sizeX = neuronRadius*2;
        for(int i=1; i<layers[0].sizeX(); i++)
        {
            sizeX+=neuronRadius*2 + inputNeuronDistance;
        }
        startOffset = sizeX;
        for(int i=1; i<layers.length; i++)
        {
            sizeX+=neuronRadius*2 + synapseDistance;
        }
        return sizeX;
    }
    private int getMaxY()
    {
        layerSizes = new int[layers.length];
        int sizeY = neuronRadius*2;
        for(int i=1; i<layers[0].sizeY(); i++)
        {
            sizeY+=neuronRadius*2 + inputNeuronDistance;
        }
        maxY = sizeY;
        layerSizes[0] = maxY;
        for(int i=1; i<layers.length; i++)
        {
            sizeY = (layers[i].size()) * neuronRadius * 2 + (layers[i].size()-1) * neuronDistance;
            if(sizeY>maxY)
                maxY = sizeY;
            layerSizes[i] = sizeY;
        }
        return maxY;
    }

    public BufferedImage drawNetwork()
    {
        int sizeX = maxX;
        int sizeY = maxY;

        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        Layer inputLayer = layers[0];
        //int k=0;
        /*
        for(int x=0; x<inputLayer.sizeX(); x++)
        {
                int posX = getNeuronPosX(0, x);
                for(int y=0; y<inputLayer.sizeY(); y++) {
                    int posY = getNeuronPosY(0, y);
                    image = drawNeuron(image, posX, posY, neurons[k]);
                    k++;
                }
        }
        */
        float[] neurons = inputLayer.getNeurons();
        Layer previousLayer = inputLayer;
        for(int i=1; i<layers.length; i++)
        {
            int k = 0;
            int l = i-1;
            float[][] weights = layers[i].getWeights();
            for(int x=0; x<previousLayer.size(); x++) {
                for(int y=0; y<layers[i].size(); y++) {
                    float synapse = weights[y][x];
                    int posX1 = getNeuronPosX(i-1, l);
                    int posY1 = getNeuronPosY(i-1, k);
                    int posX2 = getNeuronPosX(i, 0);
                    int posY2 = getNeuronPosY(i, y);
                    try {
                        drawSynapse(image, posX1, posY1, posX2, posY2, synapse);
                    }catch (Exception e)
                    {

                    }
                }
                k++;
                if(k>=previousLayer.sizeX())
                {
                    k=0;
                    l++;
                }
            }
            previousLayer = layers[i];
        }
        for(int i=0; i<layers.length; i++)
        {
            neurons = layers[i].getNeurons();
            int k = 0;
            int l = i;
            for(int y=0; y<layers[i].size(); y++) {
                int posX = getNeuronPosX(i, l);
                int posY = getNeuronPosY(i, k);
                image = drawNeuron(image, posX, posY, neurons[y]);
                k++;
                if(k>=layers[i].sizeX())
                {
                    k=0;
                    l++;
                }
            }
        }
        return image;
    }
    public int getNeuronPosX(int layerIndex, int x)
    {
        if(layerIndex==0)
        {
            return x*neuronRadius*2 + x*inputNeuronDistance + neuronRadius;
        }
        return (layerIndex-1)*neuronRadius*2 + (layerIndex)*synapseDistance + neuronRadius + startOffset;
    }
    public int getNeuronPosY(int layerIndex, int y)
    {
        Layer layer = layers[layerIndex];
        if(layerIndex==0)
        {
            return (maxY/2 - layerSizes[layerIndex]/2) + neuronRadius*y*2 + inputNeuronDistance*y + neuronRadius;
        }
        return (maxY/2 - layerSizes[layerIndex]/2) +  neuronRadius*y*2 + neuronDistance*y + neuronRadius;
    }
    public BufferedImage drawNeuron(BufferedImage image, int posX, int posY, float neuron)
    {
        int activation = (int) (neuron*255);
        Color color = new Color(activation, activation, activation);
        ImageUtils.drawCircle(image, color, posX, posY, neuronRadius);
        return image;
    }
    public BufferedImage drawSynapse(BufferedImage image, int posX1, int posY1, int posX2, int posY2, float synapse)
    {
        float magnitude = Math.abs(synapse / 10f);
        int size = (int) (((magnitude)*maxSynapseThickness));
        if(size<1)
            size=synapseThickness;
        float sig = (1 / (1 + (float) Math.exp(-(magnitude*2))));
        int pixelValue = (int) (sig*255);
        Color color = new Color(pixelValue, 0, 0);
        if(synapse>0)
            color = new Color(0, pixelValue, 0);
        image = ImageUtils.drawLine(image, color, size, posX1, posY1, posX2, posY2);
        return image;
    }



    public int getWindowSizeX(int maxX)
    {
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
