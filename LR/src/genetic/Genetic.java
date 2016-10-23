package genetic;

import Util.Flat;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ilya239 on 23.10.2016.
 */
public class Genetic {
    Random random = new Random();
    ArrayList<Flat> flats;


    public double[] evolve(ArrayList<Flat> flats) {
        this.flats = flats;
        ArrayList<Vector3> stado = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stado.add(getRandomVector());
        }
        double koff = 10;
        for (int i = 0; i < 1000; i++) {

            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 100; k++) {
                    stado.add(stado.get(j).mutate(koff));
                }
            }
            stado.sort((o1, o2) -> Double.compare(averageQDiff(flats, o1), averageQDiff(flats, o2)));
            stado = new ArrayList<>(stado.subList(0, 10));
            System.out.println(stado.get(0).toString());
        }
        return new double[]{stado.get(0).b0, stado.get(0).b1, stado.get(0).b2};
    }

    public double averageQDiff(ArrayList<Flat> flats, Vector3 vector3) {
        double diff = 0;
        for (Flat flat : flats) {
            diff += ((vector3.b0 * 800000 + flat.area * vector3.b1 * 5000 + flat.roomsCount * vector3.b2 * 5) / 800000 - flat.price)
                    * ((vector3.b0 * 800000 + flat.area * vector3.b1 * 5000 + flat.roomsCount * vector3.b2 * 5) / 800000 - flat.price);
        }
        diff /= flats.size();
        return Math.sqrt(diff);
    }

    private Vector3 getRandomVector() {
        return new Vector3(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    public class Vector3 {
        double b0;
        double b1;
        double b2;

        Vector3(double b0, double b1, double b2) {
            this.b0 = b0;
            this.b1 = b1;
            this.b2 = b2;
        }

        public Vector3 mutate(double k) {
            return new Vector3(b0 + random.nextDouble() * k - k / 2, b1 + random.nextDouble() * k - k / 2, b2 + random.nextDouble() * 1000 * k - 1000 * k / 2);
        }

        @Override
        public String toString() {
            return b0 + " " + b1 + " " + b2 + " " + averageQDiff(flats, this);
        }

    }
}
