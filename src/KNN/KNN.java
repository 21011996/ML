package KNN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Created by Ilya239 on 17.09.2016.
 */
class KNN {
    private static double distance(Dot a, Dot b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z) * (a.z - b.z));
        //return Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z);
    }

    private static ArrayList<Pair> calculateDistances(Dot testPoint, ArrayList<Dot> trainData) {
        return trainData.stream().map(aTrainData -> new Pair(aTrainData, distance(testPoint, aTrainData))).collect(Collectors.toCollection(ArrayList::new));
    }

    static ArrayList<Label> classifyKNN(ArrayList<Dot> trainData, ArrayList<Dot> testData, int k) {
        ArrayList<Label> testLabels = new ArrayList<>();

        for (Dot testPoint : testData) {
            ArrayList<Pair> testDist = calculateDistances(testPoint, trainData);
            Collections.sort(testDist);
            int[] statistics = new int[2];

            for (int i = 0; i < k; i++) {
                statistics[testDist.get(i).dot.type]++;
            }
            double maxDistanceOfKNN = 0;

            if (statistics[0] > statistics[1]) {
                for (int i = k - 1; i >= 0; i--) {
                    if (testDist.get(i).dot.type == 0) {
                        maxDistanceOfKNN = testDist.get(i).distance;
                        break;
                    }
                }
            } else {
                for (int i = k - 1; i >= 0; i--) {
                    if (testDist.get(i).dot.type == 1) {
                        maxDistanceOfKNN = testDist.get(i).distance;
                        break;
                    }
                }
            }
            testLabels.add(new Label(testPoint, statistics, maxDistanceOfKNN));
        }
        return testLabels;
    }

    static class Label {
        Dot dot;
        int[] statistics;
        int type;
        double maxDistanceOfKNN;

        Label(Dot dot, int[] statistics, double maxDistanceOfKNN) {
            this.dot = dot;
            this.statistics = statistics;
            if (statistics[0] > statistics[1]) {
                this.type = 0;
            } else {
                this.type = 1;
            }
            this.maxDistanceOfKNN = maxDistanceOfKNN;
        }
    }

    static class Pair implements Comparable<Pair> {
        Dot dot;
        double distance;

        Pair(Dot dot, double distance) {
            this.dot = dot;
            this.distance = distance;
        }

        @Override
        public int compareTo(Pair o) {
            return Double.compare(this.distance, o.distance);
        }
    }
}
