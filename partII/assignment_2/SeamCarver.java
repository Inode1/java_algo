import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private static final int GETCOLOR  = 0xff;

    private int height;
    private int width;
    private int widthAfterRemove;
    private byte[] redArray; 
    private byte[] greenArray;
    private byte[] blueArray;

    //private int[] pictureArray;
    private float[] energyArray;

    private float[] distToFirst;
    private float[] distToSecond;

    private int[]    edgeTo;

    public SeamCarver(Picture picture) {               // create a seam carver object based on the given picture
        if (picture == null) {
            throw new java.lang.NullPointerException();
        }
        height = picture.height();
        width  = picture.width();
        widthAfterRemove = width;
        redArray = new byte[height * width];
        greenArray = new byte[height * width];
        blueArray = new byte[height * width];
        energyArray  = new float[height * width];
        edgeTo  = new int[height * width];
        int bigger = (height > width ? height : width);
        distToFirst = new float[bigger];
        distToSecond = new float[bigger];

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                // sequence RGB
                redArray[i * width + j] = (byte) picture.get(j, i).getRed();
                greenArray[i * width + j] = (byte) picture.get(j, i).getGreen();
                blueArray[i * width + j] = (byte) picture.get(j, i).getBlue();
            }
        }
        computeEnergy();
    }
    
    public Picture picture() {                         // current picture
        Picture picture = new Picture(widthAfterRemove, height);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < widthAfterRemove; ++j) {
                // sequence RGB
                picture.set(j, i, new Color(redArray[i * width + j] & GETCOLOR, 
                    greenArray[i * width + j] & GETCOLOR, blueArray[i * width + j] & GETCOLOR)); 
            }
        }
        return picture;
    }
    
    public     int width() {                           // width of current picture
        return widthAfterRemove;
    }
    
    public     int height() {                          // height of current picture
        return height;
    }
    
    public  double energy(int x, int y) {              // energy of pixel at column x and row y
        if (x < 0 || x >= widthAfterRemove) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        if (y < 0 || y >= height) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        if (x == 0 || x == widthAfterRemove - 1 || 
            y == 0 || y == height - 1) {
            return 1000;
        }
        return Math.sqrt(yGradient(y * width + x) + xGradient(y * width + x));
    }
    
    // sequence of indices for horizontal seam
    public   int[] findHorizontalSeam() { 
        int[] result = new int[widthAfterRemove];
        if (height <= 3 || widthAfterRemove <= 2) {
            for (int i = 0; i < widthAfterRemove; ++i) {
                result[i] = height / 2;
            }
            return result;    
        }

        // array for check distance
        for (int i = 1; i < height - 1; ++i) {
            distToFirst[i] = energyArray[i * width + 1];
        }

        for (int j = 1; j < widthAfterRemove - 2; ++j) {
            for (int i = 1; i < height - 1; ++i) {
                relax(i * width + j, true, i);
            }
            float[] temp = distToFirst;
            distToFirst = distToSecond;
            distToSecond = temp;
        }
        getSeam(result, true);
        return result;
    }
    
    public   int[] findVerticalSeam() {                // sequence of indices for vertical seam
        int[] result = new int[height];
        if (widthAfterRemove <= 3 || height <= 2) {
            for (int i = 0; i < height; ++i) {
                result[i] = widthAfterRemove / 2;
            }
            return result;    
        }

        // array for check distance
        for (int i = 1; i < widthAfterRemove - 1; ++i) {
            distToFirst[i] = energyArray[width + i];
        }

        for (int j = 1; j < height - 2; ++j) {
            for (int i = 1; i < widthAfterRemove - 1; ++i) {
                relax(j * width + i, false, i);
            }
            float[] temp = distToFirst;
            distToFirst = distToSecond;
            distToSecond = temp;
        }
        getSeam(result, false);
        return result;
    }
    
    public    void removeHorizontalSeam(int[] seam) {  // remove horizontal seam from current picture
        if (widthAfterRemove != seam.length) {
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
            if (seam[i] < 0 || seam[i] >= widthAfterRemove) {
                throw new java.lang.IllegalArgumentException();
            }
            if (i != 0) {
                if (seam[i] < seam[i - 1] - 1 || seam[i] > seam[i - 1] + 1) {
                    throw new java.lang.IllegalArgumentException();
                }
            }
        }

        int countDelete = 0;
        for (int removeElement: seam) {
            if (removeElement != widthAfterRemove - 1)
            {
                System.arraycopy(redArray, countDelete * width + removeElement + 1, redArray, 
                                 countDelete * width + removeElement, widthAfterRemove - removeElement - 1);
                System.arraycopy(greenArray, countDelete * width + removeElement + 1, greenArray, 
                                 countDelete * width + removeElement, widthAfterRemove - removeElement - 1);
                System.arraycopy(blueArray, countDelete * width + removeElement + 1, blueArray, 
                                 countDelete * width + removeElement, widthAfterRemove - removeElement - 1);
                System.arraycopy(energyArray, countDelete * width + removeElement + 1, energyArray, 
                                 countDelete * width + removeElement, widthAfterRemove - removeElement - 1);
            }
            ++countDelete;
        }
        --widthAfterRemove;
        computeEnergy();
    }

    private void removeVerticalPixel(int[] seam) {
        for (int i = 0; i < seam.length; ++i) {
            int position = i + seam[i] * width;
            while (position / width != height - 1) {
                int next = position + width;
                redArray[position] = redArray[next];
                greenArray[position] = greenArray[next];
                blueArray[position] = blueArray[next];
                energyArray[position]  = energyArray[next];
                position = next;
            }
        }
    }

    private void computeEnergy() {
        for (int i = 1; i < height - 1; ++i) {
            for (int j = 1; j < widthAfterRemove - 1; ++j) {
                energyArray[i * width + j] = (float) Math.sqrt(yGradient(i * width + j) + xGradient(i * width + j));
            }
        }
    }

    private float xGradient(int position) {
/*        int red  = (pictureArray[position - 1] & GETCOLOR) - (pictureArray[position + 1] & GETCOLOR);
        int blue = ((pictureArray[position - 1] >> 8) & GETCOLOR) - ((pictureArray[position + 1] >> 8) & GETCOLOR);
        int green = ((pictureArray[position - 1] >> 16) & GETCOLOR) - ((pictureArray[position + 1] >> 16) & GETCOLOR);*/
        int red = (redArray[position - 1] & GETCOLOR) - (redArray[position + 1] & GETCOLOR);
        int blue = (blueArray[position - 1] & GETCOLOR) - (blueArray[position + 1] & GETCOLOR);
        int green = (greenArray[position - 1] & GETCOLOR) - (greenArray[position + 1] & GETCOLOR);
        return red * red + blue * blue + green * green;
    }
    
    private float yGradient(int position) {
        //return redArray[]
        int red = (redArray[position - width] & GETCOLOR) - (redArray[position + width] & GETCOLOR);
        int blue = (blueArray[position - width] & GETCOLOR) - (blueArray[position + width] & GETCOLOR);
        int green = (greenArray[position - width] & GETCOLOR) - (greenArray[position + width] & GETCOLOR);
        return red * red + blue * blue + green * green;
    }

    private void relax(int from, boolean isHorizontal, int to) {
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

    private void getSeam(int[] result, boolean isHorizontal) {
        int lastElement = 0;
        float minElement = Float.POSITIVE_INFINITY;
        int rightInterval = (isHorizontal ? height : widthAfterRemove);
        for (int i = 1; i < rightInterval - 1; ++i) {
            if (minElement > distToFirst[i]) {
                minElement = distToFirst[i];
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
        char he = 245;
        char p = 233;
        int c = p - he;
        System.out.print(c); 
    }
}