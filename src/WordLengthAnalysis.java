import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class WordLengthAnalysis {
    public static void main(String[] args) {
//        String text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text.";
        String fileName = "C:\\Users\\mrkru\\lab3\\src\\file.txt";
        String text = readFileAsString(fileName);
        List<String> words = Arrays.asList(text.split(" "));
        words = words.stream().map(x -> x.replaceAll("[^a-zA-Z]", "")).toList();
        WordLengthTask.THRESHOLD = words.size() / 12;
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        WordLengthTask task = new WordLengthTask(words);

        long startTime = System.nanoTime();

//        Map<Integer, Long> wordLengthFrequencies = forkJoinPool.invoke(task);
        Map<Integer, Long> wordLengthFrequencies2 = words.stream().collect(Collectors.groupingBy(String::length, Collectors.counting()));

        long endTime = System.nanoTime();

        long duration = endTime - startTime;
        System.out.println("Execution time in nanoseconds: " + duration);

        long startTime2 = System.nanoTime();

        Map<Integer, Long> wordLengthFrequencies = forkJoinPool.invoke(task);

        long endTime2 = System.nanoTime();

        long duration2 = endTime2 - startTime2;
        System.out.println("Execution time in nanoseconds: " + duration2);

        wordLengthFrequencies.forEach((length, frequency) -> {
            System.out.println("Length: " + length + ", Frequency: " + frequency);
        });

        double mean = computeMean(wordLengthFrequencies);
        double variance = computeVariance(wordLengthFrequencies, mean);
        double standardDeviation = Math.sqrt(variance);

        System.out.println("Mean: " + mean);
        System.out.println("Variance: " + variance);
        System.out.println("Standard Deviation: " + standardDeviation);
    }

    private static double computeMean(Map<Integer, Long> frequencies) {
        long totalWords = frequencies.values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();
        long totalLength = frequencies.entrySet()
                .stream()
                .mapToLong(e -> e.getKey() * e.getValue())
                .sum();
        return (double) totalLength / totalWords;
    }

    private static double computeVariance(Map<Integer, Long> frequencies, double mean) {
        long totalWords = frequencies.values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();
        double variance = frequencies.entrySet()
                .stream()
                .mapToDouble(e -> Math.pow(e.getKey() - mean, 2) * e.getValue())
                .sum();
        return variance / totalWords;
    }

    public static String readFileAsString(String fileName) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {

        }
        return content;
    }
}