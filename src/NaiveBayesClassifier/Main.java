package NaiveBayesClassifier;

import NaiveBayesClassifier.message.Message;
import NaiveBayesClassifier.message.MessagesReader;

/**
 * Created by Ilya239 on 16.10.2016.
 */
public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        for (Message message : new MessagesReader().read("Bayes/pu1/part1")) {
            System.out.println(message.toString());
        }
    }
}
