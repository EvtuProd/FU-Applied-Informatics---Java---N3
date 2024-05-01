import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Путь к входному файлу
        String inputFilePath = "D:\\User\\Документы\\input.csv";
        // Путь к файлу для записи результатов
        String outputFilePath = "D:\\User\\Документы\\result.txt";
        //конструкция try-catch для обработки ошибок по чтению-записи файлов
        try {
            // Создание BufferedReader для чтения из файла
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            String line;
            ArrayList<int[]> ballots = new ArrayList<>();
            int numOptions = 0;
            // Чтение каждой строки из файла
            while ((line = reader.readLine()) != null) {
                // Разбиение строки на отдельные части по запятой
                String[] parts = line.split(",");
                // Создание массива для хранения голосов текущего голосующего
                int[] ballot = new int[parts.length];
                // Преобразование строковых представлений номеров вариантов голосования в целые числа
                for (int i = 0; i < parts.length; i++) {
                    ballot[i] = Integer.parseInt(parts[i]);
                }
                // Добавление голосов текущего голосующего в список голосов
                ballots.add(ballot);
                // Определение максимального количества вариантов голосования
                numOptions = Math.max(numOptions, parts.length);
            }
            // Закрытие BufferedReader после чтения файла
            reader.close();

            // Создание объекта VotingSystem для обработки голосов
            VotingSystem votingSystem = new VotingSystem(numOptions);

            // Обработка голосов каждого голосующего
            for (int[] ballot : ballots) {
                votingSystem.processBallot(ballot);
            }

            // Получение ранжированного списка вариантов голосования
            List<List<Integer>> rankedOptions = votingSystem.getRankedOptions();

            // Создание FileWriter для записи результатов в файл
            FileWriter writer = new FileWriter(outputFilePath);
            // Запись ранжированных вариантов голосования в файл
            for (List<Integer> optionGroup : rankedOptions) {
                writer.write(optionGroup.toString());
                writer.write(",");
            }
            // Закрытие FileWriter после записи результатов в файл
            writer.close();
        } catch (IOException e) {
            // Обработка исключений, возникающих при чтении или записи файлов
            e.printStackTrace();
        }
    }

    // Класс представляющий вариант голосования
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

    // Класс представляющий систему голосования
    static class VotingSystem {
        private ArrayList<VotingOption> options;

        public VotingSystem(int numOptions) {
            options = new ArrayList<>();
            // Инициализация списка вариантов голосования
            for (int i = 0; i < numOptions; i++) {
                options.add(new VotingOption(i + 1));
            }
        }

        // Обработка голоса
        public void processBallot(int[] ballot) {
            // Создаем мапу для хранения баллов каждого варианта голосования
            Map<Integer, Integer> optionScores = new HashMap<>();
            // Инициализируем мапу нулевыми баллами для каждого варианта голосования
            for (int i = 0; i < ballot.length; i++) {
                optionScores.put(ballot[i], 0);
            }
            // Присваиваем баллы в зависимости от предпочтений голосующего
            for (int i = 0; i < ballot.length; i++) {
                optionScores.put(ballot[i], optionScores.get(ballot[i]) + ballot.length - i);
            }
            // Присваиваем счета вариантам голосования
            for (int i = 0; i < options.size(); i++) {
                options.get(i).addScore(optionScores.get(i + 1));
            }
        }

        // Получение ранжированного списка вариантов голосования
        public List<List<Integer>> getRankedOptions() {
            // Сортировка вариантов голосования по очкам
            Collections.sort(options, (o1, o2) -> o2.getScore() - o1.getScore());
            List<List<Integer>> rankedOptions = new ArrayList<>();
            // Добавление вариантов голосования в ранжированный список
            int i = 0;
            while (i < options.size()) {
                List<Integer> optionGroup = new ArrayList<>();
                optionGroup.add(options.get(i).getOptionNumber());
                int score = options.get(i).getScore();
                int j = i + 1;
                while (j < options.size() && options.get(j).getScore() == score) {
                    optionGroup.add(options.get(j).getOptionNumber());
                    j++;
                }
                rankedOptions.add(optionGroup);
                i = j;
            }
            return rankedOptions;
        }
    }
}
