import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class CommonWordTask extends RecursiveTask<ArrayList<String>> {
    private final ArrayList<String> words;
    private final ArrayList<String> words2;
    public static int THRESHOLD = 1000;

    public CommonWordTask(ArrayList<String> words, ArrayList<String> words2) {

        this.words = words;
        this.words2 = words2;
    }

    @Override
    protected ArrayList<String> compute() {
        if (words.size() <= THRESHOLD) {
            List<String> result = words.stream().filter(words2::contains).distinct().toList();
            return new ArrayList<String>(result);
        }

        int mid = words.size() / 2;
        CommonWordTask leftTask = new CommonWordTask(new ArrayList<String>(words.subList(0, mid)), words2);
        CommonWordTask rightTask = new CommonWordTask(new ArrayList<String>(words.subList(mid, words.size())), words2);

        leftTask.fork();
        ArrayList<String> rightResult = rightTask.compute();
        ArrayList<String> leftResult = leftTask.join();

        return mergeResults(leftResult, rightResult);
    }

    private ArrayList<String> mergeResults(ArrayList<String> leftResult, ArrayList<String> rightResult) {
        leftResult.addAll(rightResult);
        List<String> result =  leftResult.stream()
                    .distinct()
                    .toList();
        return new ArrayList<String>(result);
    }
}



