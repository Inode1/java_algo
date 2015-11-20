import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import edu.princeton.cs.algs4.StdOut;
import java.util.Stack;

public class SeamCarver {
    private final int GETCOLOR  = 0xff;
    private final int NOTVALID    = -1;

    private int height;
    private int width;
    private int[] pictureArray;
    private double[] energyArray;

    private double[] distTo;
    private int[]    edgeTo;

    public SeamCarver(Picture picture) {               // create a seam carver object based on the given picture
        if (picture == null) {
            throw new java.lang.NullPointerException();
        }
        height = picture.height();
        width  = picture.width();
        pictureArray = new int[height * width];
        energyArray  = new double[height * width];
        distTo  = new double[height * width];
        edgeTo  = new int[height * width];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                // sequence RGB
                pictureArray[i * width + j] = picture.get(j, i).getRGB();
                energyArray[i * width + j] = 1000;
                distTo[i * width + j] = Double.POSITIVE_INFINITY; 
            }
        }
        computeEnergy();
    }
    
    public Picture picture() {                         // current picture
        Picture picture = new Picture(width, height);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                // sequence RGB
                picture.set(j, i, new Color(pictureArray[i * width + j])); 
            }
        }
        return picture;
    }
    
    public     int width() {                           // width of current picture
        return width;
    }
    
    public     int height() {                          // height of current picture
        return height;
    }
    
    public  double energy(int x, int y) {              // energy of pixel at column x and row y
        if (x < 0 || x >= width) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        if (y < 0 || y >= height) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return energyArray[y * width + x];
    }
    
    public   int[] findHorizontalSeam() {              // sequence of indices for horizontal seam
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                distTo[i * width + j] = Double.POSITIVE_INFINITY; 
            }
        }

        for (int i = 0; i < height; ++i) {
            distTo[i * width] = 0;
        }

        for (int j = 0; j < width - 1; ++j) {
            for (int i = 1; i < height - 1; ++i) {
                int position = i * width + j;
                relax(position, position + 1);
                relax(position, position + width + 1);
                relax(position, position - width + 1);
                // next position + 1; position + width + 1; position - width + 1 
            }
        }
        return getSeam(true);
    }
    
    public   int[] findVerticalSeam() {                // sequence of indices for vertical seam
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                distTo[i * width + j] = Double.POSITIVE_INFINITY; 
            }
        }

        for (int i = 0; i < width; ++i) {
            distTo[i] = 0;
        }

        for (int j = 0; j < height - 1; ++j) {
            for (int i = 1; i < width - 1; ++i) {
                int position = j * width + i;
                relax(position, position + width);
                relax(position, position + width + 1);
                relax(position, position + width - 1);
                // next position + 1; position + width + 1; position - width + 1 
            }
        }
        return getSeam(false);
    }
    
    public    void removeHorizontalSeam(int[] seam) {  // remove horizontal seam from current picture
        if (width != seam.length) {
            throw new java.lang.IllegalArgumentException();
        }

        int countDelete = 0;
        for (int removeElement: seam) {
            if (removeElement < 0 || removeElement >= height) {
                throw new java.lang.IndexOutOfBoundsException();
            }
            removeVerticalPixel(seam);
        }
        --width;
        computeEnergy();
    }
    
    public    void removeVerticalSeam(int[] seam) {    // remove vertical seam from current picture
        if (height != seam.length) {
            throw new java.lang.IllegalArgumentException();
        }

        int countDelete = 0;
        for (int removeElement: seam) {
            if (removeElement < 0 || removeElement >= width) {
                throw new java.lang.IndexOutOfBoundsException();
            }
            System.arraycopy(pictureArray, countDelete * width + removeElement + 1, pictureArray, 
                      countDelete * width + removeElement - countDelete, width);
            System.arraycopy(energyArray, countDelete * width + removeElement + 1, energyArray, 
                      countDelete * width + removeElement - countDelete, width);
            ++countDelete;
        }
        --height;
        computeEnergy();
    }

    private void removeVerticalPixel(int[] seam) {
        for (int i = 0; i < seam.length; ++i) {
            int position = i + seam[i] * width;
            while (position / width != height - 1) {
                int next = position + width;
                pictureArray[position] = pictureArray[next];
                energyArray[position]  = energyArray[next];
                position = next;
            }
        }
    }

    private void computeEnergy() {
        for (int i = 1; i < height - 1; ++i) {
            for (int j = 1; j < width - 1; ++j) {
                energyArray[i * width + j] = Math.sqrt(yGradient(i * width + j) + xGradient(i * width + j));
            }
        }
    }

    private double xGradient(int position) {
        return Math.pow((pictureArray[position - 1] & GETCOLOR) - (pictureArray[position + 1] & GETCOLOR), 2) + 
               Math.pow(((pictureArray[position - 1] >> 8) & GETCOLOR) - ((pictureArray[position + 1] >> 8) & GETCOLOR), 2) +
               Math.pow(((pictureArray[position - 1] >> 16) & GETCOLOR) - ((pictureArray[position + 1] >> 16) & GETCOLOR), 2);
    }
    
    private double yGradient(int position) {
        return Math.pow((pictureArray[position - width] & GETCOLOR) - (pictureArray[position + width] & GETCOLOR), 2) + 
               Math.pow(((pictureArray[position - width] >> 8) & GETCOLOR) - ((pictureArray[position + width] >> 8) & GETCOLOR), 2) +
               Math.pow(((pictureArray[position - width] >> 16) & GETCOLOR) - ((pictureArray[position + width] >> 16) & GETCOLOR), 2);
    }

    private void relax(int from, int to) {
        if (distTo[to] > distTo[from] + energyArray[from]) {
            distTo[to] = distTo[from] + energyArray[from];
            edgeTo[to] = from;
        }
    }

    private int[] getSeam(boolean isHorizontal) {
        Stack<Integer> result = new Stack<Integer>();
        int lastElement = 0;
        double minElement = Double.POSITIVE_INFINITY;
        if (isHorizontal) {
            for (int i = 2; i < height; ++i) {
                if (distTo[i * width - 1] < minElement) {
                    minElement  = distTo[i * width - 1];
                    lastElement = i * width - 1;
                }
            }
        }
        else {
            for (int i = 1; i < width - 1; ++i) {
                if (distTo[distTo.length - width + i] < minElement) {
                    minElement  = distTo[distTo.length - width + i];
                    lastElement = distTo.length - width + i;
                }
            }
        }
        if (isHorizontal) {
            result.push(lastElement / width);
        } 
        else {
            result.push(lastElement % width);  
        }
        while (distTo[lastElement] != 0) {
            //System.out.println(lastElement);
            lastElement = edgeTo[lastElement];
            //System.out.println(lastElement);
            if (isHorizontal) {
                result.push(lastElement / width);
            } 
            else {
                result.push(lastElement % width);  
            }
        }
        int[] array = new int[result.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = result.get(i).intValue();
        }
        return array;
    }
}