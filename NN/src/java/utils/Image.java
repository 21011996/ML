package utils;

/**
 * @author Ilya239.
 *         Created on 17.12.2016.
 */
public class Image {
    public double[] pixels;
    public int width;
    public int height;

    public Image(int width, int height) {
        this.pixels = new double[width * height];
        this.width = width;
        this.height = height;
    }

    public Image(Image other) {
        this.width = other.width;
        this.height = other.height;
        pixels = new double[other.size()];
        System.arraycopy(other.pixels, 0, pixels, 0, size());
    }

    public void set(int i, double v) {
        pixels[i] = v;
    }

    public void setWithOffset(int i, int k, double v) {
        pixels[i + width * k] = v;
    }

    public int size() {
        return pixels.length;
    }
}
