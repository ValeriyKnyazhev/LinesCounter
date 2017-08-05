package lines.counter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author Valeriy Knyazhev valeriy.knyazhev@yandex.ru
 */
public class LinesCounter implements Counter {

    private final static String LINE_SEPARATOR = System.lineSeparator();

    @Override
    public int calculate(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        if (LINE_SEPARATOR.length() == 1) {
            return countUnix(content);
        } else {
            return countWin(content);
        }
    }

    private int countUnix(String content) {
        int count = 0;
        char separator = LINE_SEPARATOR.charAt(0);
        for (char c : content.toCharArray()) {
            if (c == separator) {
                count++;
            }
        }
        return count;
    }

    private int countWin(String content) {
        int separatorSize = LINE_SEPARATOR.length();
        int count = 0;
        for (int i = 0; i < content.length() - separatorSize; ++i) {
            if (Objects.equals(content.substring(i, i + separatorSize), LINE_SEPARATOR)) {
                count++;
            }
        }
        return count;
    }
}
