/******************************************************************************
 *  Compilation:  javac SCUtility.java
 *  Execution:    none
 *  Dependencies: SeamCarver.java
 *
 *  Some utility functions for testing SeamCarver.java.
 *
 ******************************************************************************/

import java.awt.Color;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdRandom;

public class SCUtility {


    // create random W-by-H array of tiles
    public static Picture randomPicture(int W, int H) {
        Picture p = new Picture(W, H);
        for (int i = 0; i < W; i++)
            for (int j = 0; j < H; j++) {
                int r = StdRandom.uniform(255);
                int g = StdRandom.uniform(255);
                int b = StdRandom.uniform(255);
                Color c = new Color(r, g, b);
                p.set(i, j, c);
            }
        return p;
    }
    public static Picture createPicture() {
        Picture p = new Picture(8, 8);
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                int r = array[j][3 * i];
                int g = array[j][3 * i + 1];
                int b = array[j][3 * i + 2];
                Color c = new Color(r, g, b);
                p.set(i, j, c);
            }
        return p;
    }

    private static int[][] array = {{  9,   1,   8,  9,   9,   4,  9,   4,   9,  5,   2,   8,  0,   5,   3,  5,   7,   9,  0,   6,   2,  6,   7,   4}, 
                                    {  3,   8,   2,  6,   5,   2,  4,   7,   7,  9,   8,   8,  7,   0,   3,  3,   4,   5,  2,   3,   5,  0,   6,   8}, 
                                    {  7,   8,   9,  8,   9,   9,  5,   0,   6,  8,   0,   4,  4,   7,   3,  9,   5,   4,  1,   7,   8,  9,   6,   1}, 
                                    {  3,   5,   4,  1,   8,   3,  1,   4,   1,  2,   8,   7,  6,   0,   9,  6,   8,   7,  0,   8,   6,  2,   6,   0}, 
                                    {  0,   6,   7,  8,   5,   7,  7,   3,   5,  2,   8,   9,  4,   9,   7,  9,   3,   5,  2,   3,   3,  5,   1,   0}, 
                                    {  0,   0,   1,  7,   4,   9,  3,   5,   7,  3,   1,   8,  7,   3,   0,  5,   8,   4,  6,   3,   8,  1,   2,   1}, 
                                    {  5,   2,   7,  0,   9,   5,  7,   2,   4,  7,   9,   6,  0,   5,   6,  6,   6,   9,  0,   3,   4,  4,   0,   8}, 
                                    {  3,   0,   0,  6,   5,   2,  7,   1,   5,  2,   9,   6,  1,   2,   5,  4,   7,   1,  4,   5,   6,  1,   0,   1} };


    public static double[][] toEnergyMatrix(SeamCarver sc) {
        double[][] returnDouble = new double[sc.width()][sc.height()];
        for (int i = 0; i < sc.width(); i++)
            for (int j = 0; j < sc.height(); j++)
                returnDouble[i][j] = sc.energy(i, j);
    
        return returnDouble;        
    }

    // displays grayvalues as energy (converts to picture, calls show)
    public static void showEnergy(SeamCarver sc) {
        doubleToPicture(toEnergyMatrix(sc)).show();
    }

    public static Picture toEnergyPicture(SeamCarver sc) {
        double[][] energyMatrix = toEnergyMatrix(sc);
        return doubleToPicture(energyMatrix);
    }

    // converts a double matrix of values into a normalized picture
    // values are normalized by the maximum grayscale value
    public static Picture doubleToPicture(double[][] grayValues) {

        // each 1D array in the matrix represents a single column, so number
        // of 1D arrays is the width, and length of each array is the height
        int width = grayValues.length;
        int height = grayValues[0].length;

        Picture p = new Picture(width, height);

        double maxVal = 0;
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                if (grayValues[i][j] > maxVal)
                    maxVal = grayValues[i][j];
            
        if (maxVal == 0)
            return p; //return black picture

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                float normalizedGrayValue = (float) grayValues[i][j] / (float) maxVal;
                p.set(i, j, new Color(normalizedGrayValue, normalizedGrayValue, normalizedGrayValue));
            }

        return p;
    }


    // This method is useful for debugging seams. It overlays red
    // pixels over the calculate seam. Due to the lack of a copy
    // constructor, it also alters the original picture.

    public static Picture seamOverlay(Picture p, boolean horizontal, int[] seamIndices) {
        Picture overlaid = new Picture(p.width(), p.height());

        for (int i = 0; i < p.width(); i++)
            for (int j = 0; j < p.height(); j++)
                overlaid.set(i, j, p.get(i, j));
        
        int width = p.width();
        int height = p.height();

        //if horizontal seam, then set one pixel in every column
        if (horizontal)
            for (int i = 0; i < width; i++)
                overlaid.set(i, seamIndices[i], Color.RED);
        else // if vertical, put one pixel in every row
            for (int j = 0; j < height; j++)
                overlaid.set(seamIndices[j], j, Color.RED);

        return overlaid;
    }

}
