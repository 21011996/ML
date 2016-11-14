/**
 * Created by ilya2 on 17.10.2016.
 */
public class F1Metric {
    private int truePositive;
    private int falsePositive;
    private int trueNegative;
    public int falseNegative;
    private double precision;
    private double recall;
    private double f1Metric;

    public F1Metric() {
        truePositive = 0;
        falsePositive = 0;
        trueNegative = 0;
        falseNegative = 0;
    }

    double getF1Metric() {
        prepare();
        return 2 * getPrecision() * getRecall() / (getPrecision() + getRecall());
    }

    private void prepare() {
        precision = ((double) truePositive) / (truePositive + falsePositive);
        recall = ((double) truePositive) / (truePositive + falseNegative);
    }


    public void incTP() {
        truePositive++;
    }

    public void incFP() {
        falsePositive++;
    }

    public void incTN() {
        trueNegative++;
    }

    public void incFN() {
        falseNegative++;
    }

    private double getPrecision() {
        return precision;
    }

    private double getRecall() {
        return recall;
    }
}
