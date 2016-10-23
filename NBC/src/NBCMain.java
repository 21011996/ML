import message.Message;
import message.MessageType;
import message.MessagesReader;

import java.util.ArrayList;

/**
 * Created by Ilya239 on 16.10.2016.
 */
public class NBCMain {
    public static void main(String[] args) {
        new NBCMain().run();
    }

    public void run() {
        double accuracy = 0;
        double legitAccuracy = 0;
        double spamAccuracy = 0;
        MessagesReader reader = new MessagesReader();
        F1Metric f1Metric = new F1Metric();
        for (int i = 1; i <= 10; i++) {
            ArrayList<Message> test = reader.read("Bayes/pu1/part" + i);
            ArrayList<Message> train = new ArrayList<>();
            for (int j = 1; j <= 10; j++) {
                if (i != j) {
                    train.addAll(reader.read("Bayes/pu1/part" + j));
                }
            }
            NBC nbc = new NBC().train(train);
            ArrayList<MessageType> classified = nbc.classify(test);
            int correct = 0;
            int correctLegit = 0;
            int correctSpam = 0;
            int countLegit = 0;
            int countSpam = 0;
            int count = 0;
            for (int j = 0; j < test.size(); j++) {
                count++;
                if (test.get(j).getType() == MessageType.LEGIT) {
                    countLegit++;
                    if (test.get(j).getType().equals(classified.get(j))) {
                        f1Metric.incTP();
                        correct++;
                        correctLegit++;
                    } else {
                        f1Metric.incFN();
                    }
                } else {
                    countSpam++;
                    if (test.get(j).getType().equals(classified.get(j))) {
                        f1Metric.incTN();
                        correct++;
                        correctSpam++;
                    } else {
                        f1Metric.incFP();
                    }
                }
            }
            accuracy += (double) correct / count;
            legitAccuracy += (double) correctLegit / countLegit;
            spamAccuracy += (double) correctSpam / countSpam;
        }
        System.out.println("Accuracy: " + accuracy * 10 + "%");
        System.out.println("Accuracy Legit: " + legitAccuracy * 10 + "%");
        System.out.println("Accuracy Spam: " + spamAccuracy * 10 + "%");
        System.out.println("F1 metric: " + f1Metric.getF1Metric());
    }
}
