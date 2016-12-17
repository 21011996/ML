import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

/**
 * Created by Ilya239 on 17.09.2016.
 */
public class KNNMain {
    //75.33,78.6
    private static int foldNumber = 5; // 10,10
    private static int KNN_k = 10;
    private static final int N = 30;

    public static void main(String[] args) {
        new KNNMain().run(foldNumber);
    }

    double run(int foldNumber) {
        ArrayList<Dot> dataSet = new Reader().read("chips.txt");
        double[] C = new double[10];
        double[] g = new double[10];
        C[0] = (double) 1 / 32;
        g[0] = (double) 1 / (8 * 8 * 8 * 8 * 8);
        for (int i = 1; i < 10; i++) {
            C[i] = C[i - 1] * 4;
            g[i] = g[i - 1] * 4;
        }
        double[][] m = new double[10][10];
        double[] svmRes = new double[30];
        double[] knnRes = new double[30];
        SVM svm = new SVM(0.5, 2);

        for (int it = 0; it<N; it++) {
            Collections.shuffle(dataSet);

            int partitionSize = dataSet.size() / foldNumber;
            ArrayList<ArrayList<Dot>> partitions = new ArrayList<>();
            for (int i = 0; i < dataSet.size(); i += partitionSize) {
                partitions.add(new ArrayList<>(dataSet.subList(i,
                        Math.min(i + partitionSize, dataSet.size()))));
            }

            double F1SVMsum = 0;
            double F1kNNsum = 0;
            int[][] confusionSVM = new int[2][2];
            int[][] confusionkNN = new int[2][2];
            double accuracySum = 0;
            for (int i = 0; i < foldNumber; i++) {
                ArrayList<Dot> trainingSet = getTrainingSet(i, partitions);
                ArrayList<Dot> testSet = new ArrayList<>(partitions.get(i));
                svm.train(trainingSet);
                ArrayList<Dot> res = svm.classify(testSet);
                ArrayList<KNN.Label> knnResult = KNN.classifyKNN(trainingSet, testSet, partitionSize > KNN_k ? KNN_k : partitionSize);
                for (int k = 0; k < res.size(); k++) {
                    int x = (testSet.get(k).type == -1) ? 0 : 1;
                    int y = (res.get(k).type == -1) ? 0 : 1;
                    int y2 = (knnResult.get(k).type == -1) ? 0 : 1;
                    confusionSVM[x][y]++;
                    confusionkNN[x][y2]++;
                }
                int correctCount = 0;
                for (int j = 0; j < testSet.size(); j++) {
                    if (testSet.get(j).type == knnResult.get(j).type) {
                        correctCount++;
                    }
                }
                accuracySum += (double) correctCount / testSet.size();

            }
            //System.out.println("Accuracy: " + (accuracySum / foldNumber));
            double F1SVM = calcF1(confusionSVM);
            double F1kNN = calcF1(confusionkNN);

            System.out.println("SVM:");
            System.out.println(Arrays.toString(confusionSVM[0]));
            System.out.println(Arrays.toString(confusionSVM[1]));
            System.out.println("KNN:");
            System.out.println(Arrays.toString(confusionkNN[0]));
            System.out.println(Arrays.toString(confusionkNN[1]));

            F1SVMsum += F1SVM;
            F1kNNsum += F1kNN;
            svmRes[it] = F1SVMsum;
            knnRes[it] = F1kNNsum;
        }
        System.out.println(Arrays.toString(svmRes));
        System.out.println(Arrays.toString(knnRes));

        svm.train(dataSet);
        ArrayList<Dot> res = svm.classify(dataSet);
        int[][] confusion = new int[2][2];
        for (int i = 0; i < res.size(); i++) {
            int x = (dataSet.get(i).type == -1) ? 0 : 1;
            int y = (res.get(i).type == -1) ? 0 : 1;
            confusion[x][y]++;
        }
        System.out.println(Arrays.toString(confusion[0]));
        System.out.println(Arrays.toString(confusion[1]));
        double F1 = calcF1(confusion);
        System.out.println(F1);
        Double[] diff = new Double[30];
        System.out.println(Arrays.toString(knnRes));
        System.out.println(Arrays.toString(svmRes));
        for (int i =0 ;i<30; i++) {
            diff[i] = knnRes[i] - svmRes[i];
        }
        Arrays.sort(diff, (o1, o2) -> Double.compare(Math.abs(o1), Math.abs(o2)));
        double[] r = new double[30];
        int count = 1;
        int sum = 0;
        for (int i = 0; i<N; i++) {
            if (i!=N-1 && Objects.equals(diff[i], diff[i + 1])) {
                sum += (i+1);
                count++;
            } else {
                r[i] = ((double)(sum + (i+1)))/count;
                sum = 0;
                count = 1;
            }
        }
        for (int i = N-1; i>=0; i--) {
            if (i!=0 && r[i-1]==0) {
                r[i-1] = r[i];
            }
            if (diff[i]<0) {
                r[i] *= -1;
            }
        }
        double Rplus = 0;
        double Rminus = 0;
        for (int i = 0; i< N; i++) {
            if (r[i]>0) {
                Rplus += r[i];
            }
            if (r[i]<0) {
                Rminus -= r[i];
            }
        }
        System.out.println("Diff: " + Arrays.toString(diff));
        System.out.println("R+: " + Rplus);
        System.out.println("R-: " + Rminus);
        double T = Math.abs(Math.min(Rplus, Rminus) - (N*(N+1))/4)/Math.sqrt(N*(N+1)*(2*N+1)/24);
        System.out.println("Normalized wilcoxon: " + T);

        double pv = 1 - (double)1/2*(1+erf(T/Math.sqrt(2)));
        System.out.println("Pvalue: " + pv);
/*
        ArrayList<Dot> dot0 = new ArrayList<>();
        ArrayList<Dot> dot1 = new ArrayList<>();
        for (Dot dot : dataSet) {
            if (dot.type == -1) {
                dot0.add(new Dot(dot.x*dot.x, dot.y*dot.y, 0));

            } else {
                dot1.add(new Dot(dot.x*dot.x, dot.y*dot.y, 1));
            }
        }
        ArrayList<Dot> dot2 = new ArrayList<>();
        ArrayList<Dot> dot3 = new ArrayList<>();

        for (double x = 0; x < 1.2; x += 0.008) {
            dot2.add(new Dot(x, -(svm.omega.x*x+svm.b)/svm.omega.y, 1));
        }
        new Plot("x", "y").addGraphic(dot3, "dot0").addGraphic(dot2, "dot1").addGraphic(dot0, "dot2").addGraphic(dot1, "dot3").show();
        //return (accuracySum / foldNumber);
        */
        return 0;
    }

    private ArrayList<Dot> getTrainingSet(int excludeNumber, ArrayList<ArrayList<Dot>> partitions) {
        ArrayList<Dot> trainingSet = new ArrayList<>();
        for (int i = 0; i < partitions.size(); i++) {
            if (i != excludeNumber) {
                trainingSet.addAll(partitions.get(i));
            }
        }
        return trainingSet;
    }

    private double calcF1(int[][] confusion) {
        double precision = (double) confusion[0][0] / (confusion[0][0] + confusion[0][1]);
        precision += (double) confusion[1][1] / (confusion[1][0] + confusion[1][1]);
        precision /= 2;
        double recall = (double) confusion[0][0] / (confusion[0][0] + confusion[1][0]);
        recall += (double) confusion[1][1] / (confusion[0][1] + confusion[1][1]);
        recall /= 2;
        return 2 * (precision * recall) / (precision + recall);
    }

    public static double erf(double x) {
        // constants
        final double a1 =  0.254829592;
        final double a2 = -0.284496736;
        final double a3 =  1.421413741;
        final double a4 = -1.453152027;
        final double a5 =  1.061405429;
        final double p  =  0.3275911;

        // Save the sign of x
        double sign = 1;
        if (x < 0) {
            sign = -1;
        }
        x = Math.abs(x);

        // A&S formula 7.1.26
        double t = 1.0/(1.0 + p*x);
        double y = 1.0 - (((((a5*t + a4)*t) + a3)*t + a2)*t + a1)*t*Math.exp(-x*x);

        return sign*y;
    }

}
