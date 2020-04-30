import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

/*
    By Brendan C. Reidy
    Created 12/10/2019
    Last Modified 12/17/2019
    MatrixIO
        Offloads array/matrix input & output to a separate class
 */

public class MatrixIO {
    public static int getLength(String aFileName)
    {
        if(aFileName==null)
        {
            System.out.println("[FATAL] Input file name is null");
            return -1;
        }
        try {
            System.out.println("Counting file: " + aFileName);
            Scanner fileScanner = new Scanner(new File(aFileName));
            int count = 0;
            while(fileScanner.hasNext())
            {
                fileScanner.nextLine();
                count++;
            }
            fileScanner.close();
            return count;
        }catch(FileNotFoundException e){
            System.out.println("File '" + aFileName + "' does not exist");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return -1;
    }
    static float[][] readTrainingData(String aFileName, int length)
    {
        if(aFileName==null)
        {
            System.out.println("[FATAL] Input file name is null");
            return null;
        }
        try {
            float[][] dataArray = new float[length][];
            System.out.println("Reading file: " + aFileName);
            Scanner fileScanner = new Scanner(new File(aFileName));
            int count = 0;
            while(fileScanner.hasNext())
            {
                String fileLine = fileScanner.nextLine();
                String[] splitLines = fileLine.split(",");
                float[] values = new float[splitLines.length];
                for(int i=0; i<splitLines.length; i++){
                    float current = Float.parseFloat(splitLines[i]);
                    values[i] = current;
                }
                if(count>=dataArray.length) {
                    System.out.println("[WARNING] Reading stopped early. File is larger than specified");
                    break;
                }
                dataArray[count] = values;
                count++;
            }
            fileScanner.close();
            return dataArray;
        }catch(FileNotFoundException e){
            System.out.print("File '" + aFileName + "' does not exit");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static float[] flattenArray(float[][] array)
    {
        float[] returnVal = new float[array.length*array[0].length];
        int k=0;
        for(int i=0; i<array.length; i++)
        {
            for(int j=0; j<array[0].length; j++)
            {
                returnVal[k] = array[i][j];
                k++;
            }
        }
        return returnVal;
    }
    public static int[] flattenArray(int[][] array)
    {
        int[] returnVal = new int[array.length*array[0].length];
        int k=0;
        for(int i=0; i<array.length; i++)
        {
            for(int j=0; j<array[0].length; j++)
            {
                returnVal[k] = array[i][j];
                k++;
            }
        }
        return returnVal;
    }
    public static void saveToFile(LinkedList<Float> list, String aFileName){
        String[] copy = new String[list.size()];
        for(int i=0; i<list.size(); i++)
        {
            copy[i] = Float.toString(list.get(i));
        }
        saveToFile(copy, aFileName);
    }
    public static void saveToFile(double[] array, String aFileName){
        String[] copy = new String[array.length];
        for(int i=0; i<array.length; i++)
        {
            copy[i] = Double.toString(array[i]);
        }
        saveToFile(copy, aFileName);
    }
    public static void saveToFile(float[] array, String aFileName){
        String[] copy = new String[array.length];
        for(int i=0; i<array.length; i++)
        {
            copy[i] = Float.toString(array[i]);
        }
        saveToFile(copy, aFileName);
    }
    public static void saveToFile(int[] array, String aFileName){
        String[] copy = new String[array.length];
        for(int i=0; i<array.length; i++)
        {
            copy[i] = Integer.toString(array[i]);
        }
        saveToFile(copy, aFileName);
    }

    static void saveToFile(String[] aInput, String aFileName)
    {
        if(aFileName==null)
            return;
        try{
            PrintWriter writer = new PrintWriter(aFileName, "UTF-8");
            String str = "";
            for(int index = 0; index<aInput.length; index++)
                str+=aInput[index] + "\n";
            writer.println(str);
            writer.close();
        }catch(FileNotFoundException e){
            System.out.println("File does not exist");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    static void print2D(float[] input, int dimensionSize)
    {
        int k=0;
        for(int x=0; x<input.length/dimensionSize; x++)
        {
            for(int y=0; y<dimensionSize; y++)
            {
                int pixel = 0;
                int pixelRepresentation = (int) (input[k] + 0.5);
                if(pixelRepresentation>=1)
                    pixel = 1;
                System.out.print(pixel + " ");
                k++;
            }
            System.out.println();
        }
    }
}
