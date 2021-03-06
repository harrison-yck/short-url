package app.key;

public class KeyGenerator {
    private static final char[] BASE_62_CHARS = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', '-', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '1', '2', '3', '4', '5', '6', '7', '8', '9', '_'
    };

    public String generate(long key, Integer length) {
        char[] url = new char[length];

        long sourceNumber = key;
        for (int i = length - 1; i >= 0; i--) {
            if (sourceNumber > 0) {
                url[i] = BASE_62_CHARS[(int) (sourceNumber % BASE_62_CHARS.length)];
                sourceNumber /= BASE_62_CHARS.length;
            } else {
                url[i] = '0';
                sourceNumber += (int) (Math.pow(62, length) + Math.random() * 62);
            }
        }

        return String.valueOf(url);
    }
}
