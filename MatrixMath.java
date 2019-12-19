/*
    By Brendan C. Reidy
    Created 12/10/2019
    Last Modified 12/10/2019
    Matrix Math
        Offloads heavily used math functions to a separate class
 */

public class MatrixMath {
    static float[] dotProduct(float[] multiplier, float[][] multiplicand)
    {
        if(multiplicand==null || multiplier==null)
        {
            System.out.println("[FATAL] Unable to compute dot product: multiplier or multiplicand is null");
            return null;
        }else if(multiplier.length != multiplicand[0].length){
            System.out.println("[FATAL] Unable to compute dot product: incompatible sizes...");
            return null;
        }
        float[] product = new float[multiplicand.length];
        int sizeY = multiplicand.length;
        int sizeX = multiplier.length;
        for(int x=0; x<sizeX; x++)
            for(int y=0; y<sizeY; y++)
                product[y] += multiplicand[y][x] * multiplier[x];
        return product;
    }
    static float[] sum(float[] a, float[] b)
    {
        if(a==null || b==null)
        {
            System.out.println("[FATAL] Unable to compute sum: a or b is null");
            return null;
        }
        if(a.length!=b.length)
        {
            System.out.println("[FATAL] Unable to compute sum: incompatible sizes...");
            return null;
        }
        float[] c = new float[a.length];
        for(int i=0; i<a.length; i++)
        {
            c[i] = a[i] + b[i];
        }
        return c;
    }
}
