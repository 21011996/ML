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
    public final int insideSize;
    public final int outSize;
    public final int[] sizes = new int[]{28 * 28, 60, 10};
    public Weights[] weights;

    public Network(int frontSize, int insideSize, int outSize) {
        this.frontSize = frontSize;
        this.insideSize = insideSize;
        this.outSize = outSize;
    }

    public static int argMax(double[] a) {
        int maxM = 0;
        double maxV = Double.MIN_VALUE;
        for (int m = 0; m < a.length; m++) {
            if (a[m] > maxV) {
                maxV = a[m];
                maxM = m;
            }
        }
        return maxM;
    }

    public void initWeights() {
        this.weights = new Weights[2];
        this.weights[0] = new Weights(frontSize + 1, insideSize + 1);
        this.weights[1] = new Weights(insideSize + 1, outSize + 1);
    }

    public double calculateDelta(CircularFifoBuffer stab) {
        if (stab.size() < DELTA_SIZE) return Double.MAX_VALUE;
        return (Double) Collections.max(stab) - (Double) Collections.min(stab);
    }

    public void learn(ArrayList<Image> images, ArrayList<Label> labels, double step, double lambda, double reg, ArrayList<Image> testIm, ArrayList<Label> testLab) {
        //lul
        Random random = new Random();
        int k = 0;
        double q = 0d;
        ArrayList<Data> data = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            data.add(new Data(images.get(i), labels.get(i), 1d));
        }
        Collections.shuffle(data);
        CircularFifoBuffer buf = new CircularFifoBuffer(DELTA_SIZE);
        double delta;

        ArrayList<Integer> probabilities = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            probabilities.add(i);
        }
        Collections.shuffle(probabilities);


        do {
            k++;
            int i = probabilities.get(random.nextInt(probabilities.size()));
            if (k % 250000 == 0) {
                step /= 2;
            }

            double qi = learnStep(data.get(i).image, data.get(i).label, step, reg);
            q = q * (1d - lambda) + qi * lambda;

            if (k % 3000 == 0) {
                double accuracy = test(testIm, testLab);
                System.out.println(accuracy + " " + k);
                if (accuracy > 97) {
                    break;
                }
            }
            buf.add(q);
        } while (true); // delta > EPSILON
    }

    public double test(ArrayList<Image> imagesTest, ArrayList<Label> labelsTest) {
        int lul = 0;
        for (int i = 0; i < imagesTest.size(); i++) {
            Label result = classify(imagesTest.get(i));
            if (result.label != labelsTest.get(i).label) {
                lul++;
            }
        }
        return 100 - ((double) lul) / imagesTest.size() * 100;
    }

    public double[] propagade(double[] signal, Weights w, int outSize) {
        double[] ui = new double[outSize + 1];
        ui[0] = -1d;
        for (int h = 1; h <= outSize; h++) {
            double sum = 0d;
            for (int j = 0; j < signal.length; j++) {
                sum += w.get(j, h) * signal[j];
            }
            ui[h] = activation(sum);
        }
        return ui;
    }

    public double learnStep(Image xi, Label yi, double step, double reg) {
        // forward step
        Image xit = new Image(xi);
        xit.addHead(-1d);

        double[][] ui = new double[sizes.length][];
        ui[0] = xit.pixels;
        for (int i = 1; i < sizes.length; i++) {
            ui[i] = propagade(ui[i - 1], weights[i - 1], sizes[i]);
        }

        double[][] errors = new double[sizes.length][];

        double qi = 0d;
        errors[errors.length - 1] = new double[ui[ui.length - 1].length];
        for (int m = 0; m < ui[ui.length - 1].length; m++) {
            errors[errors.length - 1][m] = ui[ui.length - 1][m] - (yi.label == (m - 1) ? 1d : 0d);
            qi += errors[errors.length - 1][m] * errors[errors.length - 1][m];
        }

        for (int i = ui.length - 2; i >= 0; i--) {
            double[] sm = new double[sizes[i + 1] + 1];
            for (int m = 0; m < sizes[i + 1] + 1; m++) {
                double s = 0d;
                for (int hs = 0; hs < ui[i].length; hs++) {
                    s += weights[i].get(hs, m) * ui[i][hs];
                }
                sm[m] = getDiff(s);
            }
            errors[i] = new double[ui[i].length + 1];
            for (int h = 0; h < ui[i].length; h++) {
                double sum = 0d;
                for (int m = 0; m < sizes[i + 1] + 1; m++) {
                    sum += errors[i + 1][m] * sm[m] * weights[i].get(h, m);
                }
                errors[i][h] = sum;
            }
        }

        for (int i = ui.length - 2; i >= 0; i--) {
            double[] sm = new double[sizes[i + 1] + 1];
            for (int m = 0; m < sizes[i + 1] + 1; m++) {
                double s = 0d;
                for (int hs = 0; hs < ui[i].length; hs++) {
                    s += weights[i].get(hs, m) * ui[i][hs];
                }
                sm[m] = getDiff(s);
            }
            for (int h = 0; h < ui[i].length; h++) {
                for (int m = 0; m < sizes[i + 1] + 1; m++) {
                    weights[i].set(h, m, weights[i].get(h, m) * (1 - step * reg) - step * errors[i + 1][m] * sm[m] * ui[i][h]);
                }
            }

        }
        return qi;
    }

    public Label classify(Image x) {
        Image xit = new Image(x);
        xit.addHead(-1d);

        double[][] ui = new double[sizes.length][];
        ui[0] = xit.pixels;
        for (int i = 1; i < sizes.length; i++) {
            ui[i] = propagade(ui[i - 1], weights[i - 1], sizes[i]);
        }

        return new Label(argMax(ui[sizes.length - 1]) - 1);
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
