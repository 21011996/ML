package network;

import java.util.Random;

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
}
