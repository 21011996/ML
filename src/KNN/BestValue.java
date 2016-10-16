package KNN;

/**
 * Created by Ilya239 on 18.09.2016.
 */
public class BestValue {
    public static void main(String args[]) {
        int best_foldNumber = 0;
        int best_KNN_k = 0;
        double best_accuracy = 0;
        double[][] accuracy = new double[118][118];
        for (int i = 10; i < 11; i++) {
            for (int j = 1; j < 118; j++) {
                double tmp = 0;
                for (int o = 0; o < 10; o++) {
                    tmp += new Main().run(i, j);
                }
                accuracy[i][j] = tmp * 10;
                if (accuracy[i][j] > best_accuracy) {
                    best_foldNumber = i;
                    best_KNN_k = j;
                    best_accuracy = accuracy[i][j];
                }
            }
        }
        System.out.println(best_accuracy + ":" + best_foldNumber + ":" + best_KNN_k);
    }
}
