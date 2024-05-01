import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Путь к входному файлу (относительный путь)

        String inputFilePath = System.getProperty("user.dir") + File.separator + "input.csv";
        //final String inputFilePath = "input.csv";
        // Путь к файлу для записи результатов (относительный путь)
        final String outputFilePath = "result.txt";
        try {
            // Чтение входного файла и обработка данных
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
            // Закрытие FileWriter после записи результатов в файл
            writer.close();
        } catch (IOException e) {
            // Обработка исключений, возникающих при чтении или записи файлов
            e.printStackTrace();
        }
    }

    // Класс представляющий вариант голосования
    static class VotingOption {
        private final int optionNumber; // Номер варианта голосования
        private int score; // Баллы набранные вариантом голосования

        // Конструктор класса
        public VotingOption(int optionNumber) {
            this.optionNumber = optionNumber;
            this.score = 0;
        }

        // Метод получения номера варианта голосования
        public int getOptionNumber() {
            return optionNumber;
        }

        // Метод добавления баллов к варианту голосования
        public void addScore(int score) {
            this.score += score;
        }

        // Метод получения набранных баллов варианта голосования
        public int getScore() {
            return score;
        }
    }

    // Класс представляющий систему голосования
    static class VotingSystem {
        private final List<VotingOption> options; // Список вариантов голосования

        // Конструктор класса
        public VotingSystem(int numOptions) {
            options = new ArrayList<>();
            // Инициализация списка вариантов голосования
            for (int i = 0; i < numOptions; i++) {
                options.add(new VotingOption(i + 1));
            }
        }

        // Метод обработки голоса
        public void processBallot(int[] ballot) {
            int numOptions = Math.min(ballot.length, options.size());
            // Добавление баллов вариантам голосования в соответствии с предпочтениями голосующего
            for (int i = 0; i < numOptions; i++) {
                options.get(ballot[i] - 1).addScore(numOptions - i);
            }
        }

        // Метод получения ранжированного списка вариантов голосования
        public ArrayList<Map.Entry<Integer, ArrayList<Integer>>> getRankedOptions() {
            // Сортировка вариантов голосования по баллам
            options.sort((o1, o2) -> o2.getScore() - o1.getScore());
            ArrayList<Map.Entry<Integer, ArrayList<Integer>>> rankedOptions = new ArrayList<>();
            int currentScore = Integer.MAX_VALUE;
            ArrayList<Integer> currentGroup = new ArrayList<>();
            // Группировка вариантов голосования по баллам
            for (VotingOption option : options) {
                if (option.getScore() < currentScore) {
                    if (!currentGroup.isEmpty()) {
                        rankedOptions.add(Map.entry(currentScore, new ArrayList<>(currentGroup)));
                        currentGroup = new ArrayList<>();
                    }
                    currentScore = option.getScore();
                }
                currentGroup.add(option.getOptionNumber());
                // Вывод количества баллов для каждого варианта голосования
                System.out.println("Option " + option.getOptionNumber() + ": " + option.getScore() + " points");
            }
            if (!currentGroup.isEmpty()) {
                rankedOptions.add(Map.entry(currentScore, currentGroup));
            }
            return rankedOptions;
        }
    }
}
