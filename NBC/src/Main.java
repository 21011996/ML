import message.MessagesReader;

import java.util.Map;

/**
 * Created by Ilya239 on 16.10.2016.
 */
public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        NBC nbc = new NBC().train(new MessagesReader().read("Bayes/pu1/part1"));
        System.out.println(nbc.getLegitFrequency());
        System.out.println(nbc.getSpamFrequency());
        for (Map.Entry<Integer, Double> entry : nbc.getLegitWordsFrequency().entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue() + ":" + (nbc.getSpamWordsFrequency().get(entry.getKey()) == null ? "legit" : nbc.getSpamWordsFrequency().get(entry.getKey())));
        }
        for (Map.Entry<Integer, Double> entry : nbc.getSpamWordsFrequency().entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue() + ":" + (nbc.getLegitWordsFrequency().get(entry.getKey()) == null ? "spam" : nbc.getLegitWordsFrequency().get(entry.getKey())));
        }
    }
}
