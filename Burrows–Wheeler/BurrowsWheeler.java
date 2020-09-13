import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.NoSuchElementException;

public class BurrowsWheeler {
    private static class MyQueue {
        private final int[] numbers;
        private int queueIndex = 0;
        private int dequeueIndex = 0;

        MyQueue(int size) {
            numbers = new int[size];
        }

        void enqueue(int number) {
            numbers[queueIndex++] = number;
        }

        int dequeue() {
            if (dequeueIndex >= queueIndex) {
                throw new NoSuchElementException();
            }
            return numbers[dequeueIndex++];
        }
    }

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    private static int[] getFreq(char[] extendedAsciiArray) {
        int[] freq = new int[256];
        for (char c : extendedAsciiArray) {
            freq[c]++;
        }
        return freq;
    }

    public static void transform() {
        // read string from std input
        String input = BinaryStdIn.readString();
        CircularSuffixArray cs_array = new CircularSuffixArray(input);
        int i = 0;
        // get first
        for (i = 0; i < input.length(); i++) {
            if (cs_array.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        // get t[]
        for (i = 0; i < input.length(); i++) {
            int index = cs_array.index(i);
            if (index == 0) {
                BinaryStdOut.write(input.charAt(input.length() - 1));
                continue;
            }
            BinaryStdOut.write(input.charAt(index - 1));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String chars = BinaryStdIn.readString();
        char[] t = chars.toCharArray();
        int[] char_freq = getFreq(t);

        MyQueue[] pos = new MyQueue[256];
        for (int i = 0; i < char_freq.length; i++) {
            if (char_freq[i] > 0) {
                pos[i] = new MyQueue(char_freq[i]);
            }
        }
        for (int i = 0; i < t.length; i++) {
            pos[t[i]].enqueue(i);
        }
        // sort t[]
        int Pos = 0;
        for (int i = 0; i < char_freq.length; i++) {
            for (int j = 0; j < char_freq[i]; j++) {
                t[Pos++] = (char) i;
            }
        }
        if (Pos != t.length)
            throw new IllegalArgumentException("Pos != t.length");
        int[] next = new int[t.length];
        for (int i = 0; i < t.length; i++) {
            next[i] = pos[t[i]].dequeue();
        }
        // Construct original text
        for (int i = 0, row = first; i < t.length; i++, row = next[row]) {
            BinaryStdOut.write(t[row]);
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        //BinaryStdOut.write("!ARDRCAAAABB");
        //BinaryStdOut.close();
        if (args.length != 1)
            throw new IllegalArgumentException("Expected + or -\n");
        else if (args[0].equals("-"))
            transform();
        else if (args[0].equals("+"))
            inverseTransform();
        else {
            String msg = "Unknown argument: " + args[0] + "\n";
            throw new IllegalArgumentException(msg);
        }
    }
}
