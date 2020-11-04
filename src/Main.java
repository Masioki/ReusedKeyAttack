import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        Map<Character, Double> freq = readFrequencies(args[0]);
        List<List<Character>> cryptograms = readCryptograms(args[1]);
        CipherBreaker cipherBreaker = new CipherBreaker(freq);
        List<Character> key = cipherBreaker.train(cryptograms);
        cryptograms.forEach(c -> System.out.println("\n" + cipherBreaker.decode(c)));

        System.out.println("\n Key: " + key.stream().map(String::valueOf).collect(Collectors.joining()));
    }

    static List<List<Character>> readCryptograms(String path) throws Exception {
        List<List<Character>> result = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(path));

        for (String line : lines) {
            List<Character> temp = new ArrayList<>();
            for (String s : line.strip().split(" "))
                temp.add((char) (Integer.parseInt(s, 2)));
            result.add(temp);
        }
        return result;
    }

    static Map<Character, Double> readFrequencies(String path) throws Exception {
        Map<Character, Double> result = new HashMap<>();
        List<String> lines = Files.readAllLines(Paths.get(path));

        for (String s : lines) {
            String[] pair = s.split(" : ");
            result.put(pair[0].charAt(0), Double.parseDouble(pair[1]));
        }
        return result;
    }


}
