import Jama.Matrix;
import Util.Flat;
import Util.Plot;
import Util.Reader;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by shambala on 25/09/16.
 */
public class LRMain {

    public static final int k = 5;

    public static void main(String[] args) {
        new LRMain().run();
    }
    ArrayList<Flat> flats;
    void run() {
        flats = new Reader().read("prices.txt");

        double[][] X = new double[flats.size()][3];
        double[] y = new double[flats.size()];
        double[] initArg = {100, 100};
        double minX1 = Double.MAX_VALUE;
        double maxX1 = Double.MIN_VALUE;
        double minX2 = Double.MAX_VALUE;
        double maxX2 = Double.MIN_VALUE;
        double sumX1 = 0;
        double sumX2 = 0;
        for (int i = 0; i< flats.size(); i++) {
            Flat flat = flats.get(i);
            X[i][0] = 1;
            X[i][1] = flat.area;
            sumX1 += flat.area;
            if (flat.area<minX1) {
                minX1 = flat.area;
            }
            if (flat.area > maxX1) {
                maxX1 = flat.area;
            }
            X[i][2] = flat.roomsCount;
            sumX2 += flat.roomsCount;
            if (flat.roomsCount<minX2) {
                minX2 = flat.roomsCount;
            }
            if (flat.roomsCount > maxX2) {
                maxX2 = flat.roomsCount;
            }
            y[i] = flat.price;
        }
        double meanX1 = sumX1 / flats.size();
        double meanX2 = sumX2 / flats.size();

        for (int i = 0; i< flats.size(); i++) {
            X[i][1] = (X[i][1] - meanX1) / (maxX1 - minX1);
            X[i][2] = (X[i][2] - meanX2) / (maxX2 - minX2);
            flats.set(i, new Flat(X[i][1], X[i][2], y[i]));
        }

        double[] theta = getCoef(flats);
        ArrayList<Flat> line = new ArrayList<>();
        for (double x = -1; x < 1; x += 0.01) {
            line.add(new Flat(x, x, x * theta[1] + meanX2 + theta[2]));
        }
        ArrayList<Flat> line2 = new ArrayList<>();

        for (double x = -1; x < 1; x += 0.01) {
            line2.add(new Flat(x, x, meanX1 + x * theta[0] + theta[2]));
        }
        ArrayList<Flat> oneroom = new ArrayList<>();
        for (int i = 0; i< flats.size(); i++) {
            oneroom.add(new Flat(X[i][1], X[i][2], y[i]));
        }
        Collections.shuffle(flats);
        int partitionSize = flats.size() / k;
        ArrayList<ArrayList<Flat>> partitions = new ArrayList<>();
        for (int i = 0; i < flats.size(); i += partitionSize) {
            partitions.add(new ArrayList<>(flats.subList(i,
                    Math.min(i + partitionSize, flats.size()))));
        }
        double sum = 0;
        for (int i = 0; i<k; i++) {
            ArrayList<Flat> training = getTrainingSet(i, partitions);
            ArrayList<Flat> test = partitions.get(i);
            double[] coef = getCoef(training);
            sum += J(coef, test);
        }
        System.out.println("CV: " + ((double)1/k * sum));

        new Plot("roomcount", "price").addGraphic(line2, "line", true).addGraphic(oneroom, "flats", true).show();
        new Plot("area", "price").addGraphic(line, "line").addGraphic(oneroom, "flats").show();
    }

    private ArrayList<Flat> getTrainingSet(int excludeNumber, ArrayList<ArrayList<Flat>> partitions) {
        ArrayList<Flat> trainingSet = new ArrayList<>();
        for (int i = 0; i < partitions.size(); i++) {
            if (i != excludeNumber) {
                trainingSet.addAll(partitions.get(i));
            }
        }
        return trainingSet;
    }

    Matrix derivative(Matrix X, Matrix y, Matrix arg) {
        return X.transpose().times(X.times(arg).minus(y));
    }

    Matrix grad(Matrix w, Matrix X, Matrix y) {
        return (X.transpose().times(X.times(w).minus(y))).times((double)2/flats.size());
    }

    double[] getCoef(ArrayList<Flat> training) {
        double mu = 0.5;
        double eps = (double) 1 / (2 * flats.size());
        double a = 0;
        double b = 0;
        double c = 0;
        double last = 0;
        while (true) {
            double aG = 0;
            double bG = 0;
            double cG = 0;

            for (Flat flat : training) {
                double diff = (flat.price - a * flat.roomsCount - b * flat.area - c);
                cG += ((double) -1 / flats.size()) * diff;
                bG += ((double) -1 / flats.size()) * flat.area * diff;
                aG += ((double) -1 / flats.size()) * flat.roomsCount * diff;
            }
            a += -(mu * aG);
            b += -(mu * bG);
            c += -(mu * cG);
            double sum = 0;
            for (Flat flat : training) {
                double inner =  (flat.price - (a * flat.roomsCount + b * flat.area + c));
                sum += inner * inner;
            }
            double cur = Math.sqrt(sum / flats.size());
            if (Math.abs(last - cur) < eps) {
                break;
            }
            last = cur;
        }
        return new double[]{a, b, c};
    }

    double J(double [] theta, ArrayList<Flat> dataset) {
        double sum = 0.0;
        for (Flat flat : dataset) {
            double inner = theta[0] * flat.roomsCount + theta[1] * flat.area + theta[2] - flat.price;
            sum += inner * inner;
        }
        return Math.sqrt((double) 1 / (2 * dataset.size()) * sum);
    }


}
