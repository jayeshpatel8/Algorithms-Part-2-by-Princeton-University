import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static char[] init2Ascii() {
        char[] letters = new char[256];
        for (int i = 0; i < letters.length; i++) {
            letters[i] = (char) i;
        }
        return letters;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {

        char[] letters = init2Ascii();
        char lastLetter = 0;
        int pos = 0;
        while (!BinaryStdIn.isEmpty()) {
            char currentLetter = BinaryStdIn.readChar();
            if (lastLetter == currentLetter) {
                pos = 0;
            } else {
                for (int j = 0; j < 256; j++) {
                    if (letters[j] == currentLetter) {
                        pos = j;
                        break;
                    }
                }
            }
            BinaryStdOut.write((byte) pos);
            if (pos != 0) {
                System.arraycopy(letters, 0, letters, 1, pos);
                letters[0] = currentLetter;
            }
            lastLetter = currentLetter;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] letters = init2Ascii();
        while (!BinaryStdIn.isEmpty()) {
            short currentCharVal = (short) BinaryStdIn.readChar();

            BinaryStdOut.write(letters[currentCharVal], 8);
            char c = letters[currentCharVal];
            System.arraycopy(letters, 0, letters, 1, currentCharVal);
            letters[0] = c;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected + or -\n");
        }
        String first = args[0];
        if (first.equals("+")) {
            decode();
        } else if (first.equals("-")) {
            encode();
        } else {
            throw new IllegalArgumentException("Unknown argument: " + first + "\n");
        }
    }
}
