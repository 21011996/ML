import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by shambala on 15/12/16.
 */
public class SVD {
    public static final double EPS = 0.0001;

    public static final double ETA = 0.005;
    public static final double LAMBDA = 0.005;
    public static final int F = 15;
    public static final int MAX_USER = 500_000;
    public static final int MAX_ITEM = 20_000;
    Iterable<CSVRecord> records;

    double[] bU;
    double[] bV;
    double[][] uF;
    double[][] vF;
    PrintWriter out;
    FileReader r;
    double mu = 0;


    public static void main(String[] args) {
        new SVD().run();
    }

    public double[] generateArray(int length) {
        Random r = new Random();
        double[] arr = new double[length];
        for (int i = 0; i<arr.length; i++) {
            arr[i] = ((double)1/F) * r.nextGaussian();
        }
        return arr;
    }

    public void run() {
        System.out.println(Math.round(0.9));
        try {
            out = new PrintWriter("test-submission.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        out.println("Id,Prediction");

        bU = new double[MAX_USER];
        bV = new double[MAX_ITEM];
        Arrays.fill(bU, 0);
        Arrays.fill(bV, 0);
        uF = new double[MAX_USER][F];
        vF = new double[MAX_ITEM][F];
        for (double[] arr : uF) {
            System.arraycopy(generateArray(arr.length), 0, arr, 0, arr.length);
        }
        for (double[] arr : vF) {
            System.arraycopy(generateArray(arr.length), 0, arr, 0, arr.length);
        }

        train("train.csv");
        System.out.println("MU: " + mu);

        try {
            r = new FileReader("test-ids.csv");
            records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(r);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (CSVRecord record : records) {
            int user = Integer.parseInt(record.get("User"));
            int item = Integer.parseInt(record.get("Item"));
            int id = Integer.parseInt(record.get("Id"));
            long prediction = Math.round((mu + bU[user] + bV[item] + mul(uF[user], vF[item])));
            out.println(id + "," + Math.max(1, Math.min(5, prediction)));
        }
        System.out.println("FINISHED!");
        out.close();
    }

    public void train(String file) {
        int iter = 0;
        try {
            r = new FileReader(file);
            records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(r);
        } catch (IOException e) {
            e.printStackTrace();
        }
        double rmse = 1;
        double oldRmse = 0;
        long t = 1;
        for (CSVRecord record : records) {
            mu += (Integer.parseInt(record.get("rating")) - mu) / t;
            t++;
        }
        while (Math.abs(rmse - oldRmse) > EPS) {

            try {
                r = new FileReader(file);
                records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(r);
            } catch (IOException e) {
                e.printStackTrace();
            }

            oldRmse = rmse;
            rmse = 0;

            long size = 1;
            for (CSVRecord record : records) {
                int rating = Integer.parseInt(record.get("rating"));
                int user = Integer.parseInt(record.get("user"));
                int item = Integer.parseInt(record.get("item"));
                double err = err(rating, user, item);
                rmse += (err * err - rmse) / size;
                size++;
                bU[user] += ETA * (err - LAMBDA * bU[user]);
                bV[item] += ETA * (err - LAMBDA * bV[item]);
                for (int i = 0; i<uF[0].length; i++) {
                    double prevUF = uF[user][i];
                    double prevVF = vF[item][i];
                    uF[user][i] += ETA * (err * prevVF - LAMBDA * prevUF);
                    vF[item][i] += ETA * (err * prevUF - LAMBDA * prevVF);
                }
            }
            iter++;
            rmse = Math.sqrt(rmse);
            System.out.println("Iteration: " + iter);
            System.out.println("RMSE: " + rmse);
        }
    }

    double mul(double[] a, double[] b) {
        double ans = 0;
        for (int i = 0; i<Math.min(a.length, b.length); i++) {
            ans += a[i]*b[i];
        }
        return ans;
    }

    double err(int r, int u, int i) {
        return (r - (mu + bU[u] + bV[i] + mul(uF[u], vF[i])));
    }


}
