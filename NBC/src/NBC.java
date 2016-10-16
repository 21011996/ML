import message.Message;
import message.MessageType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ilya239 on 16.10.2016.
 */
public class NBC {
    static final double laplaceFactor = 0.5;
    private double spamFrequency = 0;
    private double legitFrequency = 0;
    private double spamCount = 0;
    private  double legitCount = 0;
    private HashMap<Integer, Double> spamWordsCount;
    private HashMap<Integer, Double> legitWordsCount;

    public NBC train(ArrayList<Message> messages) {
        double spamFrequency = 0;
        double legitFrequency = 0;
        HashMap<Integer, Double> spamWordsFrequency = new HashMap<>();
        HashMap<Integer, Double> legitWordsFrequency = new HashMap<>();
        for (Message message : messages) {
            double subjectWeight = message.getSubject().size() != 0 ? message.getBody().size() / message.getSubject().size() : 1;
            subjectWeight = subjectWeight < 1 ? 1 : subjectWeight;
            if (message.getType() == MessageType.LEGIT) {
                legitFrequency += message.getSubject().size() * subjectWeight + message.getBody().size();
                for (Integer word : message.getSubject()) {
                    legitWordsFrequency.put(word, legitWordsFrequency.containsKey(word) ? legitWordsFrequency.get(word) + subjectWeight : subjectWeight);
                }
                for (Integer word : message.getBody()) {
                    legitWordsFrequency.put(word, legitWordsFrequency.containsKey(word) ? legitWordsFrequency.get(word) + 1 : 1);
                }
            } else {
                spamFrequency += message.getSubject().size() * subjectWeight + message.getBody().size();
                for (Integer word : message.getSubject()) {
                    spamWordsFrequency.put(word, spamWordsFrequency.containsKey(word) ? spamWordsFrequency.get(word) + subjectWeight : subjectWeight);
                }
                for (Integer word : message.getBody()) {
                    spamWordsFrequency.put(word, spamWordsFrequency.containsKey(word) ? spamWordsFrequency.get(word) + 1 : 1);
                }
            }
        } /*
        for (Map.Entry<Integer, Double> entry : spamWordsFrequency.entrySet()) {
            spamWordsFrequency.put(entry.getKey(), entry.getValue() / spamFrequency);
        }
        for (Map.Entry<Integer, Double> entry : legitWordsFrequency.entrySet()) {
            legitWordsFrequency.put(entry.getKey(), entry.getValue() / legitFrequency);
        } */
        double tmp = spamFrequency + legitFrequency;
        spamCount = spamFrequency;
        legitCount = legitFrequency;
        spamFrequency /= tmp;
        legitFrequency /= tmp;
        NBC answer = new NBC();
        answer.setLegitFrequency(legitFrequency);
        answer.setSpamFrequency(spamFrequency);
        answer.setLegitWordsCount(legitWordsFrequency);
        answer.setSpamWordsCount(spamWordsFrequency);
        answer.setSpamCount(spamCount);
        answer.setLegitCount(legitCount);
        return answer;
    }

    public ArrayList<MessageType> classify(ArrayList<Message> messages) {
        ArrayList<MessageType> result = new ArrayList<>();
        for (Message message : messages) {
            double leg = Math.log(legitFrequency);
            double spam = Math.log(spamFrequency);
            for (Integer word : message.getSubject()) {
                leg += Math.log(((getLegitWordsCount().containsKey(word) ? getLegitWordsCount().get(word) : 0) + laplaceFactor)/(getSpamCount()+laplaceFactor*getUniqueWordsAmount()));
                spam += Math.log(((getSpamWordsCount().containsKey(word) ? getSpamWordsCount().get(word) : 0) + laplaceFactor)/(getLegitCount()+laplaceFactor*getUniqueWordsAmount()));
            }
            if (leg >= spam) {
                result.add(MessageType.LEGIT);
            } else {
                result.add(MessageType.SPAM);
            }
        }
        return result;
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

    public HashMap<Integer, Double> getSpamWordsCount() {
        return spamWordsCount;
    }

    public void setSpamWordsCount(HashMap<Integer, Double> spamWordsCount) {
        this.spamWordsCount = spamWordsCount;
    }

    public HashMap<Integer, Double> getLegitWordsCount() {
        return legitWordsCount;
    }

    public void setLegitWordsCount(HashMap<Integer, Double> legitWordsCount) {
        this.legitWordsCount = legitWordsCount;
    }

    public double getSpamCount() {
        return spamCount;
    }

    public void setSpamCount(double spamCount) {
        this.spamCount = spamCount;
    }

    public double getLegitCount() {
        return legitCount;
    }

    public void setLegitCount(double legitCount) {
        this.legitCount = legitCount;
    }

    public double getUniqueWordsAmount() {
        return getLegitWordsCount().size() + getSpamWordsCount().size();
    }
}
