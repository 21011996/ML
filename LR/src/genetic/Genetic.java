package genetic;

import Util.Flat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by Ilya239 on 23.10.2016.
 */
public class Genetic {
    Random random = new Random();


    public double[] evolve(ArrayList<Flat> flats) {
        ArrayList<Vector3> stado = new ArrayList<>();
        for (int i = 0; i< 10; i++) {
            stado.add(getRandomVector());
        }

        for (int i = 0; i<1000; i++) {
            for (int j = 0; j<10; j++ ) {
                for (int k = 0; k<6; k++) {
                    stado.add(stado.get(j).mutate());
                }
            }
            stado.sort(new Comparator<Vector3>() {
                @Override
                public int compare(Vector3 o1, Vector3 o2) {
                    return Double.compare(averageQDiff(flats, o1),averageQDiff(flats, o2));
                }
            });
            stado = new ArrayList<>(stado.subList(0, 10));
        }
        return new double[]{stado.get(0).b0,stado.get(0).b1,stado.get(0).b2};
    }

    public double averageQDiff(ArrayList<Flat> flats, Vector3 vector3) {
        double diff = 0;
        for (Flat flat : flats) {
            diff += (vector3.b0 + flat.area*vector3.b1+flat.roomsCount*vector3.b2-flat.price)*(vector3.b0 + flat.area*vector3.b1+flat.roomsCount*vector3.b2-flat.price);
        }
        diff/=flats.size();
        return Math.sqrt(diff);
    }

    private Vector3 getRandomVector() {
        return new Vector3(random.nextDouble(),random.nextDouble(),random.nextDouble());
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

        public Vector3 mutate() {
            return new Vector3(b0+=random.nextDouble()*2-1,            b1+=random.nextDouble()*2-1,            b2+=random.nextDouble()*2-1);
        }


    }
}
