/******************************************************************************
 *  Compilation:  javac PrintEnergy.java
 *  Execution:    java PrintEnergy input.png
 *  Dependencies: SeamCarver.java
 *                
 *
 *  Read image from file specified as command line argument. Print energy
 *  of each pixel as calculated by SeamCarver object. 
 * 
 ******************************************************************************/

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class PrintEnergy {

    public static void main(String[] args) {
        Picture picture = new Picture(SCUtility.createPicture());
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());
        SeamCarver sc = new SeamCarver(picture);
        
        for (int j = 0; j < picture.height(); j++) {
            for (int i = 0; i < picture.width(); i++)
                StdOut.printf("%d,%d,%d ", picture.get(i, j).getRed(), picture.get(i, j).getGreen(), picture.get(i, j).getBlue());
            StdOut.println();
        }

/*        for (int j = 0; j < sc.height(); j++) {
            for (int i = 0; i < sc.width(); i++)
                StdOut.printf("%d,%d,%d ", sc.picture().get(i, j).getRed(), sc.picture().get(i, j).getGreen(), sc.picture().get(i, j).getBlue());
            StdOut.println();
        }*/
            int[] verticalSeam = sc.findHorizontalSeam();
            verticalSeam = sc.findHorizontalSeam();
            sc.removeHorizontalSeam(verticalSeam);

          for (int j = 0; j < sc.height(); j++) {
            for (int i = 0; i < sc.width(); i++)
                StdOut.printf("%d,%d,%d ", sc.picture().get(i, j).getRed(), sc.picture().get(i, j).getGreen(), sc.picture().get(i, j).getBlue());
            StdOut.println();
        } 
            verticalSeam = sc.findVerticalSeam();
            sc.removeVerticalSeam(verticalSeam);


          for (int j = 0; j < sc.height(); j++) {
            for (int i = 0; i < sc.width(); i++)
                StdOut.printf("%d,%d,%d ", sc.picture().get(i, j).getRed(), sc.picture().get(i, j).getGreen(), sc.picture().get(i, j).getBlue());
            StdOut.println();
        }     
        
        StdOut.printf("Printing energy calculated for each pixel.\n");      

                StdOut.printf("Horizontal seam: { ");
        int[] horizontalSeam = sc.findVerticalSeam();
        for (int y : horizontalSeam)
            StdOut.print(y + " ");
        StdOut.println("}");
        PrintSeams.printSeam(sc, horizontalSeam, false);
  

/*        for (int j = 0; j < sc.height(); j++) {
            for (int i = 0; i < sc.width(); i++)
                System.out.print(sc.energy(i, j) + " ");
            StdOut.println();
        }*/
    }

}
