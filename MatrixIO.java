import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

public class MatrixIO {
    static float[][] readTrainingData(String aFileName, int length)
    {
        if(aFileName==null)
        {
            System.out.println("[FATAL] Input file name is null");
            return null;
        }
        try {
            float[][] dataArray = new float[length*2][];
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
                if(count>=length*2) {
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
    static int coolChar = 222;
    static int cool2 = 175;
    static void print2D(float[] input, int dimensionSize)
    {
        int k=0;
        for(int x=0; x<dimensionSize; x++)
        {
            for(int y=0; y<dimensionSize; y++)
            {
                int pixel = cool2;
                int pixelRepresentation = (int) (input[k] + 0.5);
                if(pixelRepresentation==1)
                    pixel = coolChar;
                System.out.print((char)pixel + " ");
                k++;
            }
            System.out.println();
        }
    }
}
