// Created by Brendan C. Reidy
// Created on April 27, 2019

// Training Data
// Desc: A class to turn a wide range of training data (binary digits, image folders, etc)
// into a Matrix2D object.

// Note:
// Data is stored as follows: TrainingData[i] = input; TrainingData[i+1] = output;

// Last Modified 4/28/19

import java.util.Arrays;

public class TrainingData {
    public static Matrix2D GenerateTrainingData(float[][] inputs, float[][] outputs){
        Matrix2D trainingData = new Matrix2D();
        int inputSize = inputs.length;
        int outputSize = outputs.length;
        if(inputSize!=outputSize)
            return null;
        for(int i=0; i<inputs.length; i++){
            float[] values1 = inputs[i];
            float[] values2 = outputs[i];
            trainingData.add(values1);
            trainingData.add(values2);
        }
        return trainingData;
    }
    public static Matrix2D GenerateTrainingData(double[][] inputs, double[][] outputs){
        Matrix2D trainingData = new Matrix2D();
        int inputSize = inputs[0].length;
        int outputSize = inputs[0].length;
        if(inputSize!=outputSize)
            return null;
        for(int i=0; i<inputs.length; i++){
            double[] values1 = inputs[i];
            double[] values2 = outputs[i];
            float[] values11 = new float[values1.length];
            float[] values12 = new float[values2.length];
            for(int j=0; j<values1.length; j++) {
                values11[j] = (float) values1[j];
            }
            for(int j=0; j<values2.length; j++){
                values12[j] = (float) values2[j];
            }
            trainingData.add(values11);
            trainingData.add(values12);
        }
        return trainingData;
    }

    public static Matrix2D GenerateAutoEncoderTrainingData(String inputFileName){
        Matrix2D trainingData = new Matrix2D();
        Matrix2D inputData = Matrix2D.loadFromFile(inputFileName);
        for(int i=0; i<inputData.length; i++){
            float[] inputOutput = inputData.getArrayAt(i);
            for(int j=0; j<inputOutput.length; j++)
                inputOutput[j]/=255;
            trainingData.add(inputOutput);
            trainingData.add(inputOutput);
        }
        return trainingData;
    }
    public static Matrix2D GenerateImageTrainingDataFromCSV(String inputFileName, String outputFilename){
        Matrix2D trainingData = new Matrix2D();
        Matrix2D inputData = Matrix2D.loadFromFile(inputFileName);
        Matrix2D outputData = Matrix2D.loadFromFile(outputFilename);
        String[] stringOutputs = new String[outputData.length];
        for(int i=0; i<outputData.length; i++){
            float[] outputs = outputData.getArrayAt(i);
            for(int j=0; j<outputs.length; j++)
                stringOutputs[i] = Float.toString(outputs[j]);
        }
        String[] unique = Arrays.stream(stringOutputs).distinct().toArray(String[]::new);
        for(int i=0; i<inputData.length; i++){
            float[] inputs = inputData.getArrayAt(i);
            float[] outputs = outputData.getArrayAt(i);
            int[] binaryOutputs = new int[unique.length];
            for(int j=0; j<inputs.length; j++)
                inputs[j]/=255;
            for(int j=0; j<outputs.length; j++){
                for(int k=0; k<unique.length; k++){
                    if(Float.toString(outputs[j]).equals(unique[k])){
                        binaryOutputs[(int) outputs[j]]=1;
                        break;
                    }
                }
            }
            trainingData.add(inputs);
            trainingData.add(binaryOutputs);
        }
        return trainingData;
    }
}
