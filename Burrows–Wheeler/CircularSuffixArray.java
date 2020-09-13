import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CircularSuffixArray {
    private class CircularString implements Comparable<CircularString> {

        private final String original;
        private final int offset;

        public CircularString(String original, int offset) {
            this.original = original;
            this.offset = offset;

        }

        public int getOffset() {
            return offset;
        }

        public int length() {
            return original.length();
        }

        public char charAt(int i) {
            return original.charAt((i + offset) % length());
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(length());
            for (int i = 0; i < length(); i++) {
                sb.append(charAt(i));
            }
            return sb.toString();
        }

        @Override
        public int compareTo(CircularString other) {
            int i = 0;
            boolean same = true;

            while (i < length() && same) {
                if (charAt(i) < other.charAt(i)) {
                    return -1;
                } else if (charAt(i) > other.charAt(i)) {
                    return 1;
                }
                same = (charAt(i) == other.charAt(i));
                i++;
            }
            if (length() < other.length()) {
                return -1;
            } else if (length() > other.length()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private final int len;
    private final List<CircularString> orderedList = new ArrayList<>();

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("args null");
        }
        len = s.length();

        for (int i = 0; i < s.length(); i++) {
            orderedList.add(new CircularString(s, i));
        }
        Collections.sort(orderedList);
    }

    // length of s
    public int length() {
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > orderedList.size() - 1) {
            throw new IllegalArgumentException("Input out of range");
        }
        return orderedList.get(i).getOffset();
    }

    // unit testing (required)
    public static void main(String[] args) {
        int SCREEN_WIDTH = 80;
        String s = StdIn.readString();
        int n = s.length();
        int digits = (int) Math.log10(n) + 1;
        String fmt = "%" + (digits == 0 ? 1 : digits) + "d ";
        StdOut.printf("String length: %d\n", n);
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < n; i++) {
            StdOut.printf(fmt, i);
            for (int j = 0; j < (SCREEN_WIDTH - digits - 1) && j < n; j++) {
                char c = s.charAt((j + csa.index(i)) % n);
                if (c == '\n')
                    c = ' ';
                StdOut.print(c);
            }
            StdOut.printf(fmt, csa.index(i));
            StdOut.println();
        }
    }
}
