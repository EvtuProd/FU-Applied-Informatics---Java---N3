import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Путь к входному файлу
        String inputFilePath = "D:\\User\\Документы\\input.csv";
        // Путь к файлу для записи результатов
        String outputFilePath = "D:\\User\\Документы\\result.txt";
        try {
            // Чтение входного файла и обработка данных
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            String line;
            ArrayList<int[]> ballots = new ArrayList<>();
            int numOptions = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int[] ballot = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    ballot[i] = Integer.parseInt(parts[i]);
                }
                ballots.add(ballot);
                numOptions = Math.max(numOptions, parts.length);
            }
            reader.close();

            // Создание объекта системы голосования и обработка голосов
            VotingSystem votingSystem = new VotingSystem(numOptions);
            for (int[] ballot : ballots) {
                votingSystem.processBallot(ballot);
            }

            // Получение ранжированных вариантов голосования
            ArrayList<Map.Entry<Integer, ArrayList<Integer>>> rankedOptions = votingSystem.getRankedOptions();

            // Запись результата в выходной файл
            FileWriter writer = new FileWriter(outputFilePath);
            for (int i = 0; i < rankedOptions.size(); i++) {
                ArrayList<Integer> optionGroup = rankedOptions.get(i).getValue();
                if (optionGroup.size() == 1) {
                    writer.write(optionGroup.get(0).toString());
                } else {
                    writer.write("[");
                    for (int j = 0; j < optionGroup.size(); j++) {
                        writer.write(optionGroup.get(j).toString());
                        if (j < optionGroup.size() - 1) {
                            writer.write(",");
                        }
                    }
                    writer.write("]");
                }
                if (i < rankedOptions.size() - 1) {
                    writer.write(",");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class VotingOption {
        private int optionNumber;
        private int score;

        public VotingOption(int optionNumber) {
            this.optionNumber = optionNumber;
            this.score = 0;
        }

        public int getOptionNumber() {
            return optionNumber;
        }

        public void addScore(int score) {
            this.score += score;
        }

        public int getScore() {
            return score;
        }
    }

    static class VotingSystem {
        private ArrayList<VotingOption> options;

        public VotingSystem(int numOptions) {
            options = new ArrayList<>();
            for (int i = 0; i < numOptions; i++) {
                options.add(new VotingOption(i + 1));
            }
        }

        public void processBallot(int[] ballot) {
            int numOptions = Math.min(ballot.length, options.size());
            for (int i = 0; i < numOptions; i++) {
                options.get(ballot[i] - 1).addScore(numOptions - i);
            }
        }

        public ArrayList<Map.Entry<Integer, ArrayList<Integer>>> getRankedOptions() {
            Collections.sort(options, (o1, o2) -> o2.getScore() - o1.getScore());
            ArrayList<Map.Entry<Integer, ArrayList<Integer>>> rankedOptions = new ArrayList<>();
            int currentScore = Integer.MAX_VALUE;
            ArrayList<Integer> currentGroup = new ArrayList<>();
            for (VotingOption option : options) {
                if (option.getScore() < currentScore) {
                    if (!currentGroup.isEmpty()) {
                        rankedOptions.add(new HashMap.SimpleEntry<>(currentScore, currentGroup));
                        currentGroup = new ArrayList<>();
                    }
                    currentScore = option.getScore();
                }
                currentGroup.add(option.getOptionNumber());
                // Выводим количество баллов для каждого варианта голосования
                System.out.println("Option " + option.getOptionNumber() + ": " + option.getScore() + " points");
            }
            if (!currentGroup.isEmpty()) {
                rankedOptions.add(new HashMap.SimpleEntry<>(currentScore, currentGroup));
            }
            return rankedOptions;
        }
    }
}
