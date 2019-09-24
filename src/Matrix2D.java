// Created by Brendan C. Reidy
// Created on April 26, 2019
// Two Dimensional Matrix
// Desc: Matrix2D is a matrix containing vectors of varying length

// Last Modified 4/30/19 @ 3:40 PM

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Matrix2D {
    public static final String DELIM = ",";
    public Vector[] matrix;
    public int length;
    public Matrix2D(){
        this.matrix = null;
        int length = 0;
    }
    public Matrix2D(int size){
        this.matrix = new Vector[size];
        this.length = 0;
    }
    public Matrix2D(float[] values){
        this.matrix = new Vector[1];
        this.matrix[0] = new Vector(values);
        this.length = 1;
    }
    public Matrix2D(int[] values){
        this.matrix = new Vector[1];
        this.matrix[0] = new Vector(values);
        this.length = 1;
    }
    public Matrix2D(double[] values){
        this.matrix = new Vector[1];
        this.matrix[0] = new Vector(values);
        this.length = 1;
    }
    public void add(float[] values){
        Vector[] temp = new Vector[this.length+1];
        for(int i=0; i<this.length; i++)
            temp[i] = matrix[i];
        temp[this.length] = new Vector(values);
        this.length = this.length+1;
        this.matrix = temp;
    }
    public void add(int[] values){
        Vector[] temp = new Vector[this.length+1];
        for(int i=0; i<this.length; i++)
            temp[i] = matrix[i];
        temp[this.length] = new Vector(values);
        this.length = this.length+1;
        this.matrix = temp;
    }
    public void add(double[] values){
        Vector[] temp = new Vector[this.length+1];
        for(int i=0; i<this.length; i++)
            temp[i] = matrix[i];
        temp[this.length] = new Vector(values);
        this.length = this.length+1;
        this.matrix = temp;
    }
    public void setValue(float aValue, int x, int y){
        Vector values = matrix[x];
        values.set(aValue, y);
    }
    public float[] getArrayAt(int index){
        return matrix[index].toArray();
    }
    public String toString(){
        String str="";
        for(int x=0; x<this.length; x++){
            float[] current = getArrayAt(x);
            for(int y=0; y<current.length; y++){
                str += current[y] + ",";
            }
            if(x!=this.length-1)
                str += "\n";
        }
        return str;
    }
    public void print(){
        System.out.println(toString());
    }
    public void printInt(){
        String str="";
        for(int x=0; x<this.length; x++){
            float[] current = getArrayAt(x);
            for(int y=0; y<current.length; y++){
                str += (int) (current[y] + 0.5) + ",";
            }
            str += "\n";
        }
        System.out.println(str);
    }
    public void saveToFile(String aFileName){
        if(aFileName==null)
            return;
        try{
            PrintWriter writer = new PrintWriter(aFileName, "UTF-8");
            for(int index = 0; index<this.length; index++){
                String str = "";
                float[] values = getArrayAt(index);
                for(int i=0; i<values.length; i++){
                    str+=values[i] + ",";
                }
                writer.println(str);
            }
            writer.close();
        }catch(FileNotFoundException e){
            System.out.println("File does not exist");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static Matrix2D reverse(Matrix2D aMatrix){
        Matrix2D newMatrix = new Matrix2D();
        for(int i=aMatrix.length-1; i>=0; i--)
            newMatrix.add(aMatrix.getArrayAt(i));
        return newMatrix;
    }
    public static Matrix2D loadFromFile(String aFileName){
        Matrix2D newMatrix = new Matrix2D();
        if(aFileName==null)
            return null;
        try {
            Scanner fileScanner = new Scanner(new File(aFileName));
            String fileLine;
            String[] splitLines;
            while (fileScanner.hasNext()) {
                fileLine = fileScanner.nextLine();
                splitLines = fileLine.split(DELIM);
                float[] values = new float[splitLines.length];
                for(int i=0; i<splitLines.length; i++){
                    float current = Float.parseFloat(splitLines[i]);
                    values[i]=current;
                }
                newMatrix.add(values);
            }
            fileScanner.close();
        }catch(FileNotFoundException e){
            System.out.println("File does not exist");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return newMatrix;
    }
}
