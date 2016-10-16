import message.Message;
import message.MessageType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ilya239 on 16.10.2016.
 */
public class NBC {
    private double spamFrequency = 0;
    private double legitFrequency = 0;
    private HashMap<Integer, Double> spamWordsFrequency;
    private HashMap<Integer, Double> legitWordsFrequency;


    public NBC train(ArrayList<Message> messages) {
        double spamFrequency = 0;
        double legitFrequency = 0;
        HashMap<Integer, Double> spamWordsFrequency = new HashMap<>();
        HashMap<Integer, Double> legitWordsFrequency = new HashMap<>();
        for (Message message : messages) {
            if (message.getType() == MessageType.LEGIT) {
                legitFrequency += message.getSubject().size() + message.getBody().size();
                for (Integer word : message.getSubject()) {
                    legitWordsFrequency.put(word, legitWordsFrequency.get(word) != null ? legitWordsFrequency.get(word) + 1 : 1);
                }
                for (Integer word : message.getBody()) {
                    legitWordsFrequency.put(word, legitWordsFrequency.get(word) != null ? legitWordsFrequency.get(word) + 1 : 1);
                }
            } else {
                spamFrequency += message.getSubject().size() + message.getBody().size();
                for (Integer word : message.getSubject()) {
                    spamWordsFrequency.put(word, spamWordsFrequency.get(word) != null ? spamWordsFrequency.get(word) + 1 : 1);
                }
                for (Integer word : message.getBody()) {
                    spamWordsFrequency.put(word, spamWordsFrequency.get(word) != null ? spamWordsFrequency.get(word) + 1 : 1);
                }
            }
        }
        for (Map.Entry<Integer, Double> entry : spamWordsFrequency.entrySet()) {
            spamWordsFrequency.put(entry.getKey(), entry.getValue() / spamFrequency);
        }
        for (Map.Entry<Integer, Double> entry : legitWordsFrequency.entrySet()) {
            legitWordsFrequency.put(entry.getKey(), entry.getValue() / legitFrequency);
        }
        double tmp = spamFrequency + legitFrequency;
        spamFrequency /= tmp;
        legitFrequency /= tmp;
        NBC answer = new NBC();
        answer.setLegitFrequency(legitFrequency);
        answer.setSpamFrequency(spamFrequency);
        answer.setLegitWordsFrequency(legitWordsFrequency);
        answer.setSpamWordsFrequency(spamWordsFrequency);
        return answer;
    }

    public double getSpamFrequency() {
        return spamFrequency;
    }

    public void setSpamFrequency(double spamFrequency) {
        this.spamFrequency = spamFrequency;
    }

    public double getLegitFrequency() {
        return legitFrequency;
    }

    public void setLegitFrequency(double legitFrequency) {
        this.legitFrequency = legitFrequency;
    }

    public HashMap<Integer, Double> getSpamWordsFrequency() {
        return spamWordsFrequency;
    }

    public void setSpamWordsFrequency(HashMap<Integer, Double> spamWordsFrequency) {
        this.spamWordsFrequency = spamWordsFrequency;
    }

    public HashMap<Integer, Double> getLegitWordsFrequency() {
        return legitWordsFrequency;
    }

    public void setLegitWordsFrequency(HashMap<Integer, Double> legitWordsFrequency) {
        this.legitWordsFrequency = legitWordsFrequency;
    }
}
