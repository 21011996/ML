import Jama.Matrix;
import Util.Flat;
import Util.Plot;
import Util.Reader;
import genetic.Genetic;

import java.util.ArrayList;

/**
 * Created by shambala on 25/09/16.
 */
public class LRMain {

    public static double ALPHA = 0.0001;

    public static void main(String[] args) {
        new LRMain().run();
    }

    void run() {
        ArrayList<Flat> flats = new Reader().read("prices.txt");
        double[] vec = new Genetic().evolve(flats);
        double[][] X = new double[flats.size()][2];
        double[] y = new double[flats.size()];
        double[] initArg = {100, 100};
        for (int i = 0; i< flats.size(); i++) {
            Flat flat = flats.get(i);
            X[i][0] = flat.area;
            X[i][1] = flat.roomsCount;
            y[i] = flat.price;
        }
        Matrix xMatrix = new Matrix(X);
        Matrix yMatrix = new Matrix(y, flats.size());
        Matrix arg = new Matrix(initArg, 2);
        for (int k = 0; k<47; k++) {
            arg = arg.minus(derivative(xMatrix, yMatrix, arg).times(ALPHA));
        }
        ArrayList<Flat> line = new ArrayList<>();
        for (double a = 0; a < 5000; a += 1) {
            line.add(new Flat(a, a, vec[1] * a + vec[0] * 800000));
        }
        ArrayList<Flat> line2 = new ArrayList<>();
        for (double a = 0; a < 5; a += 0.01) {
            line2.add(new Flat(a, a, vec[2] * a + vec[0] * 3200000));
        }
        ArrayList<Flat> oneroom = new ArrayList<>();
        for (Flat flat : flats) {
            switch ((int) flat.roomsCount) {
                default:oneroom.add(flat);
            }
        }


        new Plot("roomcount", "price").addGraphic(line2, "line", true).addGraphic(oneroom, "flats", true).show();
        new Plot("area", "price").addGraphic(line, "line").addGraphic(oneroom, "flats").show();
    }

    Matrix derivative(Matrix X, Matrix y, Matrix arg) {
        return X.transpose().times(X.times(arg).minus(y));
    }
}
