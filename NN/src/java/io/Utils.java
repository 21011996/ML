package io;

import utils.Image;
import utils.Label;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * @author Ilya239.
 *         Created on 17.12.2016.
 */
public class Utils {
    private final int imageMagic = 0x00000803;
    private final int labelMagic = 0x00000801;

    public static ArrayList<Image> readImages(String fileName) throws IOException {
        DataInputStream is = null;
        try {
            is = new DataInputStream(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int magic = is.readInt();

        int count = is.readInt();
        int rows = is.readInt();
        int columns = is.readInt();

        ArrayList<Image> answer = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Image image = new Image(rows, columns);
            for (int j = 0; j < columns * rows; j++) {
                int pixel = is.readUnsignedByte();
                image.pixels[j] = pixel / 255d;
            }
            answer.add(image);
        }

        return answer;
    }

    public static ArrayList<Label> readLabels(String fileName) throws IOException {
        DataInputStream is = null;
        try {
            is = new DataInputStream(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int magic = is.readInt();

        int count = is.readInt();

        ArrayList<Label> answer = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            answer.add(new Label(is.readByte()));
        }
        return answer;
    }

    public static void printImage(String fileName, Image image) throws IOException {
        BufferedImage answer = new BufferedImage(image.width, image.height, BufferedImage.TYPE_BYTE_GRAY);
        for (int k = 0; k < image.height; k++) {
            for (int i = 0; i < image.width; i++) {
                answer.setRGB(i, k, (byte) (image.pixels[i + image.width * k] * 255));
            }
        }
        File file = new File(fileName);
        ImageIO.write(answer, "png", file);
    }

    public static void printImage2(String fileName, Image image) throws IOException {
        BufferedImage answer = new BufferedImage(image.width, image.height, BufferedImage.TYPE_3BYTE_BGR);
        for (int k = 0; k < image.height; k++) {
            for (int i = 0; i < image.width; i++) {
                float colornumber = (int) image.pixels[i + image.width * k] + 100;
                float[] hsv = new float[3];
                Color color = Color.getHSBColor(colornumber / 459, 1, 1);
                answer.setRGB(i, k, color.getRGB());
            }
        }
        File file = new File(fileName);
        ImageIO.write(answer, "png", file);
    }
}
