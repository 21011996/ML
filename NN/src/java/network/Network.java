package network;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import utils.Image;
import utils.Label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * @author Ilya239.
 *         Created on 17.12.2016.
 */
public class Network {
    private static final double EPSILON = 1E-6;
    private static final int DELTA_SIZE = 5;
    public final int frontSize;
    public final int outSize;
    public Weights weights;

    public Network(int frontSize, int outSize) {
        this.frontSize = frontSize;
        this.outSize = outSize;
    }

    public void initWeights() {
        this.weights = new Weights(frontSize, outSize);
    }

    public double calculateDelta(CircularFifoBuffer stab) {
        if (stab.size() < DELTA_SIZE) return Double.MAX_VALUE;
        return (Double) Collections.max(stab) - (Double) Collections.min(stab);
    }

    public void learn(ArrayList<Image> images, ArrayList<Label> labels, double step, double lambda, double reg) {
        Random random = new Random();

        double q = 0d;
        ArrayList<Data> data = new ArrayList<>();
        ArrayList<Integer> probabilities = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            data.add(new Data(images.get(i), labels.get(i), 1d));
            probabilities.add(i);
        }
        Collections.shuffle(data);
        Collections.shuffle(probabilities);

        CircularFifoBuffer buf = new CircularFifoBuffer(DELTA_SIZE);
        double delta;
        do {
            int i = probabilities.get(random.nextInt(probabilities.size()));

            double qi = learnStep(data.get(i).image, data.get(i).label, step, reg);
            q = q * (1d - lambda) + qi * lambda;

            Label result = classify(data.get(i).image);
            if (result.label != data.get(i).label.label) {
                data.get(i).probability++;
                probabilities.add(i);
                Collections.shuffle(probabilities);
            }

            buf.add(q);
            delta = calculateDelta(buf);
            System.out.println(delta);
        } while (delta > EPSILON);
    }

    public double learnStep(Image xi, Label yi, double step, double reg) {
        Image image = new Image(xi);
        //image.addHead(-1);
        double[] ui = new double[outSize + 1];
        ui[0] = -1d;
        for (int i = 0; i < outSize; i++) {
            double sum = 0d;
            for (int j = 0; j < image.size(); j++) {
                sum += weights.weights[j][i] * image.pixels[j];
            }
            ui[i] = activation(sum);
        }
        double[] eim = new double[outSize];
        for (int i = 0; i < outSize; i++) {
            eim[i] = ui[i] - (yi.label == i ? 1d : 0d);
        }
        double qi = 0d;
        for (int i = 0; i < outSize; i++) {
            qi += eim[i] * eim[i];
        }


        for (int i = 0; i < image.pixels.length; i++) {
            for (int j = 0; j < outSize; j++) {
                double s = 0d;
                for (int k = 0; k < outSize; k++) {
                    s += weights.weights[i][k] * image.pixels[i];
                }
                s = getDiff(s);
                weights.weights[i][j] = weights.weights[i][j] * (1 - step * reg) - step * eim[j] * s * image.pixels[i];
            }
        }

        return qi;
    }

    public Label classify(Image x) {
        Image image = new Image(x);
        //image.addHead(-1);
        double[] ui = new double[outSize + 1];
        ui[0] = -1;
        for (int i = 0; i < outSize; i++) {
            double sum = 0d;
            for (int j = 0; j < image.pixels.length; j++) {
                sum += weights.weights[j][i] * image.pixels[j];
            }
            ui[i] = activation(sum);
        }
        int maxM = 0;
        double maxV = Double.MIN_VALUE;
        for (int i = 0; i < outSize; i++) {
            if (ui[i] > maxV) {
                maxV = ui[i];
                maxM = i;
            }
        }
        return new Label(maxM);
    }

    public double activation(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public double getDiff(double x) {
        return activation(x) * (1 - activation(x));
    }

    private class Data {
        public Image image;
        public Label label;
        public double probability;

        public Data(Image image, Label label, double probability) {
            this.image = image;
            this.label = label;
            this.probability = probability;
        }
    }
}
