import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private static final int GETCOLOR  = 0xff;

    private int height;
    private int width;
    private int[] pictureArray;
    private float[] energyArray;

    private int[]    edgeTo;

    public SeamCarver(Picture picture) {               // create a seam carver object based on the given picture
        if (picture == null) {
            throw new java.lang.NullPointerException();
        }
        height = picture.height();
        width  = picture.width();
        pictureArray = new int[height * width];
        energyArray  = new float[height * width];
        edgeTo  = new int[height * width];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                // sequence RGB
                pictureArray[i * width + j] = picture.get(j, i).getRGB();
                energyArray[i * width + j] = 1000;
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
        return (double) energyArray[y * width + x];
    }
    
    // sequence of indices for horizontal seam
    public   int[] findHorizontalSeam() { 
        int[] result = new int[width];
        if (height <= 3 || width <= 2) {
            for (int i = 0; i < width; ++i) {
                result[i] = height / 2;
            }
            return result;    
        }

        // array for check distance
        float[] distToFirst = new float[height];
        for (int i = 0; i < distToFirst.length; ++i) {
            distToFirst[i] = energyArray[i * width + 1];
        }

         float[] distToSecond = new float[height];

        for (int j = 1; j < width - 2; ++j) {
            for (int i = 1; i < height - 1; ++i) {
                relax(distToFirst, distToSecond, i * width + j, true, i);
            }
            float[] temp = distToFirst;
            distToFirst = distToSecond;
            distToSecond = temp;
        }
        getSeam(distToFirst, result, true);
        return result;
    }
    
    public   int[] findVerticalSeam() {                // sequence of indices for vertical seam
        int[] result = new int[height];
        if (width <= 3 || height <= 2) {
            for (int i = 0; i < height; ++i) {
                result[i] = width / 2;
            }
            return result;    
        }

        // array for check distance
        float[] distToFirst = new float[width];
        for (int i = 0; i < distToFirst.length; ++i) {
            distToFirst[i] = energyArray[width + i];
        }
        float[] distToSecond = new float[width];


        for (int j = 1; j < height - 2; ++j) {
            for (int i = 1; i < width - 1; ++i) {
                relax(distToFirst, distToSecond, j * width + i, false, i);
            }
            float[] temp = distToFirst;
            distToFirst = distToSecond;
            distToSecond = temp;
        }
        getSeam(distToFirst, result, false);
        return result;
    }
    
    public    void removeHorizontalSeam(int[] seam) {  // remove horizontal seam from current picture
        if (width != seam.length) {
            throw new java.lang.IllegalArgumentException();
        }

        for (int i = 0; i < seam.length; ++i) {
            if (seam[i] < 0 || seam[i] >= height) {
                throw new java.lang.IllegalArgumentException();
            }
            if (i != 0) {
                if (seam[i] < seam[i - 1] - 1 || seam[i] > seam[i - 1] + 1) {
                    throw new java.lang.IllegalArgumentException();
                }
            }
        }

        removeVerticalPixel(seam);
        --height;
        computeEnergy();
    }
    
    public    void removeVerticalSeam(int[] seam) {    // remove vertical seam from current picture
        if (height != seam.length) {
            throw new java.lang.IllegalArgumentException();
        }
        for (int i = 0; i < seam.length; ++i) {
            if (seam[i] < 0 || seam[i] >= width) {
                throw new java.lang.IllegalArgumentException();
            }
            if (i != 0) {
                if (seam[i] < seam[i - 1] - 1 || seam[i] > seam[i - 1] + 1) {
                    throw new java.lang.IllegalArgumentException();
                }
            }
        }

        int countDelete = 0;
        for (int i = 0; i < pictureArray.length; ++i) {
                System.out.println(pictureArray[i]);
            }
            System.out.println("Out");
        for (int removeElement: seam) {
            for (int i = 0; i < width; ++i) {
                System.out.println(pictureArray[width * countDelete + i]);
            }
            System.out.println("Before");
            if (removeElement != width - 1)
            {
                System.out.println(removeElement);
                System.arraycopy(pictureArray, countDelete * width + removeElement + 1, pictureArray, 
                          countDelete * width + removeElement, width - removeElement - 1);
            } else
            {
                System.out.println(countDelete);
                System.out.println("removeElement" + removeElement);

            }
            for (int i = 0; i < width; ++i) {
                System.out.println(pictureArray[width * countDelete + i]);
            }
            System.out.println("After");
/*            System.arraycopy(energyArray, countDelete * width + removeElement + 1, energyArray, 
                      countDelete * width + removeElement - countDelete, width);*/
            ++countDelete;
        }
        --width;
        for (int i = 0; i < pictureArray.length; ++i) {
                System.out.println(pictureArray[i]);
        }
        System.out.println("Out");
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
                energyArray[i * width + j] = (float) Math.sqrt(yGradient(i * width + j) + xGradient(i * width + j));
            }
        }
    }

    private float xGradient(int position) {
        int red  = (pictureArray[position - 1] & GETCOLOR) - (pictureArray[position + 1] & GETCOLOR);
        int blue = ((pictureArray[position - 1] >> 8) & GETCOLOR) - ((pictureArray[position + 1] >> 8) & GETCOLOR);
        int green = ((pictureArray[position - 1] >> 16) & GETCOLOR) - ((pictureArray[position + 1] >> 16) & GETCOLOR);
        return red * red + blue * blue + green * green;
    }
    
    private float yGradient(int position) {
        int red = (pictureArray[position - width] & GETCOLOR) - (pictureArray[position + width] & GETCOLOR);
        int blue = ((pictureArray[position - width] >> 8) & GETCOLOR) - ((pictureArray[position + width] >> 8) & GETCOLOR);
        int green = ((pictureArray[position - width] >> 16) & GETCOLOR) - ((pictureArray[position + width] >> 16) & GETCOLOR);
        return red * red + blue * blue + green * green;
    }

    private void relax(float[] distToFirst, float[] distToSecond, int from, boolean isHorizontal, int to) {
        int nextFirst  = 0;
        int nextSecond = 0;
        int nextThird  = 0;
        if (isHorizontal) {
            nextFirst  = from - width + 1;
            nextSecond = from + 1;
        }
        else {
            nextFirst  = from + width - 1;
            nextSecond = from + width;
        }
        nextThird  = from + width + 1;

        distToSecond[to + 1] = distToFirst[to] + energyArray[nextThird];
        edgeTo[nextThird] = to;
        if (to == 1) {
            distToSecond[to] = distToFirst[to] + energyArray[nextSecond];
            edgeTo[nextSecond] = to;
            return;            
        }

        if (distToSecond[to - 1] > distToFirst[to] + energyArray[nextFirst]) {
            distToSecond[to - 1] = distToFirst[to] + energyArray[nextFirst];
            edgeTo[nextFirst] = to;
        }
        if (distToSecond[to] > distToFirst[to] + energyArray[nextSecond]) {
            distToSecond[to] = distToFirst[to] + energyArray[nextSecond];
            edgeTo[nextSecond] = to;
        }
    }

    private void getSeam(float[] distTo, int[] result, boolean isHorizontal) {
        int lastElement = 0;
        float minElement = Float.POSITIVE_INFINITY;
        for (int i = 1; i < distTo.length - 1; ++i) {
            if (minElement > distTo[i]) {
                minElement = distTo[i];
                lastElement = i;
            }
        }

        result[result.length - 1] = lastElement;
        
        for (int i = result.length - 2; i > 0; --i) {
            result[i] = lastElement;
            if (isHorizontal) {
                lastElement = edgeTo[lastElement * width + i];
            }
            else {
                lastElement = edgeTo[i * width + lastElement];
            }
        }
        result[0] = result[1];
    }

    public static void main(String[] args) { 
    }
}