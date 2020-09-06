import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet w;

    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        if (wordnet == null) {
            throw new IllegalArgumentException("Arg to Outcast() is null");
        }
        w = wordnet;
    }

    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        if (nouns == null) {
            throw new IllegalArgumentException("Arg to outcast() is null");
        }
        int id = -1;
        int max = -1;
        int[] distSum = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                distSum[i] += w.distance(nouns[i], nouns[j]);
            }
            if (distSum[i] > max) {
                max = distSum[i];
                id = i;
            }
        }
        if (id == -1) {
            throw new IllegalArgumentException("Error");
        }
        return nouns[id];
    }

    public static void main(String[] args)  // see test client below
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
