import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class NumberTrainingMain {
    public static final int SAVE_RATE = 10;

    public static void main(String[] args){
        System.out.println("Reading Matrices...");
        Matrix2D theNumber8 = Matrix2D.loadFromFile("TheNumber8.csv");
        Matrix2D matrix = Matrix2D.loadFromFile("imageNetwork.txt");
        Matrix2D weight = Matrix2D.loadFromFile("imageWeights.csv");
        Matrix2D bias = Matrix2D.loadFromFile("imageBias.csv");
        Matrix2D trainingData = Matrix2D.loadFromFile("imageTrainingData.csv");
        Matrix2D trainingDataSubset = Matrix2D.loadFromFile("imageTrainingDataSubset.csv");
        NeuralNetwork network = new NeuralNetwork(matrix, weight, bias);
        network.setConsoleOutput(true);
        network.setTrainingData(trainingData);
        network.setTrainingSubset(trainingDataSubset);
        network.setLearningRate(0.1);
        network.printSpecs();
        /*
        for(int i=0; i<trainingData.length/2; i++){
            network.setInput(trainingData.getArrayAt(2*i));
            network.feedForward();
            //network.printIntResults();
            //System.out.println("-----------------------------");
        }
        //*/
        /*
        float[] number8 = new float[784];
        int count=0;
        for(int i=0; i<theNumber8.length; i++){
            float[] row = theNumber8.getArrayAt(i);
            for(int j=0; j<row.length; j++){
                number8[count] = row[j];
                count++;
            }
        }
        network.setInput(number8);
        network.feedForward();
        float[] output = network.getOutput();
        printImageFloat(number8, 28);
        printFloatArray(output);

        double networkCost = network.getSubsetCost();
        network.bestError = networkCost;
        System.out.println("-------------------------------------------");
        System.out.println("Subset accuracy: " + (1-networkCost));
        System.out.println("-------------------------------------------");
        //*/
        System.out.println("Training...");
        for(int i=0; i<5; i++) {
            network.train();
            if((i+1) % SAVE_RATE == 0){
                System.out.println("Saving...");
                //network.getBestWeights().saveToFile("imageWeights.csv");
                //network.getBestBias().saveToFile("imageBias.csv");
            }
        }
        //*/
        ///*
        network.setBest();
        //network.weights.saveToFile("imageWeights.csv");
        //network.bias.saveToFile("imageBias.csv");
        System.out.println("-------------------------------------------");
        System.out.println("Accuracy: " + (1-network.getSubsetCost()));
        System.out.println("-------------------------------------------");

        /*
        for(int i=0; i<trainingDataSubset.length/1000; i++){
            float[] input = trainingDataSubset.getArrayAt(2*i);
            float[] correctOutput = trainingDataSubset.getArrayAt((2*i)+1);
            network.setInput(input);
            network.feedForward();
            float[] output = network.getOutput();
            printImageFloat(input, 28);
            printFloatArray(output);
            printFloatArray(correctOutput);
            System.out.println("-----------------------------");
        }
        //*/
        /*
        network.setInput(number8);
        network.feedForward();
        output = network.getOutput();
        printImageFloat(number8, 28);
        printFloatArray(output);
        //*/
    }
    public static void printFloatArray(float[] array){
        String str="";
        for(int i=0; i<array.length; i++){
            str+=((int) (array[i]+0.5)) + ",";
        }
        System.out.println(str);
    }
    public static void printImageFloat(float[] array, int size){
        String str="";
        for(int i=0; i<array.length; i++){
            str+=((int) (array[i]+0.5)) + ",";
            if(((i+1) % size)==0)
                str+="\n";
        }
        System.out.println(str);
    }
}