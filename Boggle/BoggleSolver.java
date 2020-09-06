import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    private static final int R = 26;

    private static class Node {
        private Node[] next = new Node[R];        // R-way trie node
        private boolean isString;
    }

    private class TrieSET {

        private Node root;      // root of trie

        private void add(String key) {
            if (key == null) throw new IllegalArgumentException("argument to add() is null");
            root = add(root, key, 0);
        }

        private Node add(Node x, String key, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                if (!x.isString)
                    x.isString = true;
            } else {
                char c = key.charAt(d);
                x.next[c - 'A'] = add(x.next[c - 'A'], key, d + 1);
            }
            return x;
        }

        private boolean contains(String key) {
            if (key == null) throw new IllegalArgumentException("argument to contains() is null");
            Node x = get(root, key, 0);
            if (x == null) return false;
            return x.isString;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c - 'A'], key, d + 1);
        }

        private void search(int i, int j, BoggleBoard board, boolean[][] visited, Set<String> validWords) {
            char c = board.getLetter(i, j);
            if (c != 'Q') {
                String prefix = "" + c;
                Node node = root.next[board.getLetter(i, j) - 'A'];
                dfs(i, j, node, prefix, board, visited, validWords);
            } else {
                Node n1 = root.next[c - 'A'];
                if (n1 != null) {
                    Node n2 = n1.next['U' - 'A'];
                    if (n2 != null) {
                        dfs(i, j, n2, "QU", board, visited, validWords);
                    }
                }
            }
        }

        private void dfs(int i, int j, Node node, String prefix, BoggleBoard board, boolean[][] visited, Set<String> validWords) {
            visited[i][j] = true;
            if (node.isString && prefix.length() > 2) validWords.add(prefix);
            for (int x = i - 1; x < i + 2; x++) {
                for (int y = j - 1; y < j + 2; y++) {
                    if (x >= 0 && x < board.rows() && y >= 0 && y < board.cols() && !visited[x][y]) {
                        char nextChar = board.getLetter(x, y);
                        if (nextChar != 'Q') {
                            Node nextNode = node.next[nextChar - 'A'];
                            if (nextNode != null) {
                                dfs(x, y, nextNode, prefix + nextChar, board, visited, validWords);
                            }
                        } else {
                            Node nextNode = node.next['Q' - 'A'];
                            if (nextNode != null) {
                                Node nextNext = nextNode.next['U' - 'A'];
                                if (nextNext != null) {
                                    dfs(x, y, nextNext, prefix + 'Q' + 'U', board, visited, validWords);
                                }
                            }
                        }
                    }
                }
            }
            visited[i][j] = false;
        }
    }


    private final TrieSET trie;

    public BoggleSolver(String[] dictionary) {
        String[] copy = Arrays.copyOf(dictionary, dictionary.length);
        trie = new TrieSET();
        for (String word : copy) {
            trie.add(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        Set<String> validWords = new HashSet<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                trie.search(i, j, board, visited, validWords);
            }
        }
        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int score;
        int len = word.length();
        if (!trie.contains(word) || len < 3) {
            score = 0;
        } else if (len < 5) {
            score = 1;
        } else if (len == 5) {
            score = 2;
        } else if (len == 6) {
            score = 3;
        } else if (len == 7) {
            score = 5;
        } else {
            score = 11;
        }
        return score;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
