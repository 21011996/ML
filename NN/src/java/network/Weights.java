package network;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Ilya239.
 *         Created on 17.12.2016.
 */
public class Weights {
    public double[][] weights;
    public int inSize;
    public int outSize;

    public Weights(int inSize, int outSize) {
        this.inSize = inSize;
        this.outSize = outSize;
        this.weights = new double[inSize][outSize];
        Random random = new Random();
        double min = -1d / (2d * inSize);
        double max = -min;
        for (int i = 0; i < inSize; i++) {
            for (int j = 0; j < outSize; j++) {
                weights[i][j] = min + (max - min) * random.nextGaussian();
            }
        }
    }

    public Weights(int inputSize, int outputSize, File file) {
        Scanner scanner = null;
        DataInputStream is = null;
        this.inSize = inputSize;
        this.outSize = outputSize;
        weights = new double[inputSize][outputSize];
        try {
            //is = new DataInputStream(new FileInputStream(file));
            scanner = new Scanner(new FileInputStream(file));
            for (int i = 0; i < inputSize; i++) {
                for (int j = 0; j < outputSize; j++) {
                    weights[i][j] = Double.parseDouble(scanner.next());
                }
            }
            scanner.close();
        } catch (IOException e) {
        }
    }

    public double get(int i, int j) {
        return weights[i][j];
    }

    public void set(int i, int j, double v) {
        weights[i][j] = v;
    }

    public void writeFile(String fileName) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print(this.toString());
        writer.close();
    }

    @Override
    public String toString() {
        StringBuilder answer = new StringBuilder();
        String result = "";
        for (int i = 0; i < inSize; i++) {
            for (int j = 0; j < outSize; j++) {
                answer.append(String.format("%.50f", weights[i][j]).replace(",", "."));
                if (j != outSize - 1) answer.append(" ");
            }
            answer.append("\n");
        }
        return answer.toString();
    }
}
