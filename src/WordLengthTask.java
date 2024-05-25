import java.util.concurrent.RecursiveTask;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class WordLengthTask extends RecursiveTask<Map<Integer, Long>> {
    private final List<String> words;
    public static int THRESHOLD = 1000;

    public WordLengthTask(List<String> words) {
        this.words = words;
    }

    @Override
    protected Map<Integer, Long> compute() {
        if (words.size() <= THRESHOLD) {
            return words.stream()
                .collect(Collectors.groupingBy(String::length, Collectors.counting()));
        }
        int mid = words.size() / 2;
        WordLengthTask leftTask = new WordLengthTask(words.subList(0, mid));
        WordLengthTask rightTask = new WordLengthTask(words.subList(mid, words.size()));

        leftTask.fork();
//        rightTask.fork();
        Map<Integer, Long> rightResult = rightTask.compute();
        Map<Integer, Long> leftResult = leftTask.join();
//        Map<Integer, Long> rightResult = leftTask.join();

        return mergeResults(leftResult, rightResult);
    }

    private Map<Integer, Long> mergeResults(Map<Integer, Long> leftResult, Map<Integer, Long> rightResult) {
        IntStream left = leftResult.keySet().stream().mapToInt(Integer::intValue);
        IntStream right = rightResult.keySet().stream().mapToInt(Integer::intValue);
        return IntStream.concat(left, right)
                .distinct()
                .boxed()
                .collect(Collectors.toMap(
                        key -> key,
                        key -> leftResult.getOrDefault(key, 0L) + rightResult.getOrDefault(key, 0L)
                ));
    }
}



