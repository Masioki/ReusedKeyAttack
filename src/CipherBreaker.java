import java.util.*;

public class CipherBreaker {
    private final Map<Character, Double> alphabet;
    private final List<Character> key;

    public CipherBreaker(Map<Character, Double> alphabet) {
        this.alphabet = alphabet;
        this.key = new ArrayList<>();
    }

    public List<Character> train(List<List<Character>> trainingSet) {
        if (trainingSet.size() < 2) return new ArrayList<>();
        if (!key.isEmpty()) key.clear();
        int longest = trainingSet.stream().mapToInt(List::size).max().getAsInt();
        for (int j = 0; j < longest; j++) {
            int i = j;
            // Dla wszystkich znaków na danej pozycji we wszystkich kryptogramach tworzymy zbiór (klucz ^ prawdziwa litera) ^ litera
            // Potem robimy część wspólną tych zbiorów. W cześci wspólnej napewno znajdzie się właściwy klucz.
            Set<Character> keyCandidates = new HashSet<>();
            for (List<Character> set : trainingSet)
                if (set.size() > i) {
                    Set<Character> tempCandidates = new HashSet<>();
                    for (char possible : alphabet.keySet())
                        tempCandidates.add((char) (set.get(i) ^ possible));
                    if (keyCandidates.isEmpty()) keyCandidates.addAll(tempCandidates);
                    else keyCandidates.retainAll(tempCandidates);
                }

            // Mając zbiór kandydatów używamy charakterystyki rozkładu liter w danym języku i wyciągamy najbardziej
            // prawdopodobny klucz (nie zawsze poprawny)
            Optional<Character> opt = keyCandidates.stream().max(Comparator.comparingDouble(can -> {
                double counter = 0;
                for (List<Character> set : trainingSet)
                    if (set.size() > i && alphabet.containsKey((char) (set.get(i) ^ can)))
                        counter += alphabet.get((char) (set.get(i) ^ can));
                return counter;
            }));

            if (opt.isPresent()) key.add(opt.get());
            else key.add(' ');
        }
        return key;
    }


    public String decode(List<Character> chars) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.size() && i < key.size(); i++)
            sb.append((char) (key.get(i) ^ chars.get(i)));
        return sb.toString();
    }
}
