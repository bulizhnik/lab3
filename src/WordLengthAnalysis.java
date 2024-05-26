import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class WordLengthAnalysis {
    public static void main(String[] args) {
//        String text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text.";
        String fileName = "C:\\Users\\mrkru\\lab3\\src\\file3.txt";
        String fileName2 = "C:\\Users\\mrkru\\lab3\\src\\file2.txt";
        String text1 = readFileAsString(fileName);
        String text2 = readFileAsString(fileName2);

        List<String> words = Arrays.asList(text1.split(" "));
        words = words.stream().map(x -> x.replaceAll("[^a-zA-Z]", "")).toList();

        List<String> words2 = Arrays.asList(text2.split(" "));
        words2 = words2.stream().map(x -> x.replaceAll("[^a-zA-Z]", "")).toList();

        ArrayList<String> searchWords = new ArrayList<String>();
        searchWords.add("Lorem");
        searchWords.add("is");
        searchWords.add("it");
        searchWords.add("years");

        CommonWordTask.THRESHOLD = 5;
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CommonWordTask task = new CommonWordTask(new ArrayList<String>(words), new ArrayList<String>(searchWords));
        List<String> wordLengthFrequencies = forkJoinPool.invoke(task);

        ForkJoinPool forkJoinPool2 = new ForkJoinPool();
        CommonWordTask task2 = new CommonWordTask(new ArrayList<String>(words2), new ArrayList<String>(searchWords));
        List<String> wordLengthFrequencies2 = forkJoinPool2.invoke(task2);

        wordLengthFrequencies.forEach(x -> {
            System.out.println("Contain word: " + x);
        });
        System.out.println("\n\n\n");
        wordLengthFrequencies2.forEach(x -> {
            System.out.println("Contain word: " + x);
        });
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