// Created by Brendan C. Reidy
// Created on April 26, 2019
// Vector Object
// Desc: These objects are essentially just arrays of floats, but their functionality allows for any type of input to be
// converted into a float array. These arrays are then stored in a Matrix2D to allow for Matrices with varying lengths at each index

// Last Modified 4/26/19

public class Vector {
    float[] pair;
    int length;
    public Vector(float[] set){
        this.pair = set;
        this.length = set.length;
    }
    public Vector(int[] set){
        float[] emptySet = new float[set.length];
        for(int i=0; i<set.length; i++)
            emptySet[i] = (float) set[i];
        this.pair = emptySet;
        this.length = set.length;
    }
    public Vector(double[] set){
        float[] emptySet = new float[set.length];
        for(int i=0; i<set.length; i++)
            emptySet[i] = (float) set[i];
        this.pair = emptySet;
        this.length = set.length;
    }
    public void set(float value, int index){
        pair[index]=value;
    }
    public float[] toArray(){
        return this.pair;
    }
    public String toString(){
        String str = pair.toString();
        return str;
    }
}
