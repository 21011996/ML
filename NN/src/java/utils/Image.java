package utils;

import java.awt.image.BufferedImage;

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

    public Image(BufferedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();
        pixels = new double[width * height];
        for (int k = 0; k < height; k++) {
            for (int i = 0; i < width; i++) {
                if (image.getRGB(i, k) < -1) {
                    pixels[i + width * k] = 0;
                } else {
                    pixels[i + width * k] = 1;
                }
            }
        }
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

    public void addHead(double v) {
        double[] newX = new double[size() + 1];
        newX[0] = v;
        System.arraycopy(pixels, 0, newX, 1, pixels.length);
        pixels = newX;
    }
}
