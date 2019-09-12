package wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNet {

    private final Map<String, List<Integer>> nouns = new HashMap<>();

    private final List<String> synsets = new ArrayList<>();

    private final Digraph G;

    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        // read synsets
        In in = new In(synsets);
        String line;
        int vertices = 0;
        while ((line = in.readLine()) != null) {
            String[] str = line.split(",");
            this.synsets.add(str[1]);
            for (String noun : str[1].split(" ")) {
                List<Integer> list = nouns.computeIfAbsent(noun, k -> new ArrayList<Integer>());
                list.add(vertices);
            }
            vertices++;
        }
        // read hypernyms
        in = new In(hypernyms);
        Digraph graph = new Digraph(vertices);
        int count = 0;
        while ((line = in.readLine()) != null) {
            String[] str = line.split(",");
            int hyponym = Integer.parseInt(str[0]);
            for (int i = 1; i < str.length; i++) {
                int hypernym = Integer.parseInt(str[i]);
                graph.addEdge(hyponym, hypernym);
            }
            count++;
        }
        if (vertices - count > 1)
            throw new IllegalArgumentException();
        G = graph;
        sap = new SAP(G);
        if (new DirectedCycle(G).hasCycle())
            throw new IllegalArgumentException();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // check null
    private void checkNullWord(String word) {
        if (word == null)
            throw new IllegalArgumentException();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkNullWord(word);
        return nouns.containsKey(word);
    }

    // check valid noun
    private void checkNoun(String word) {
        checkNullWord(word);
        if (!isNoun(word))
            throw new IllegalArgumentException();
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkNoun(nounA);
        checkNoun(nounB);
        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkNoun(nounA);
        checkNoun(nounB);
        return synsets.get(sap.ancestor(nouns.get(nounA), nouns.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
    }
}