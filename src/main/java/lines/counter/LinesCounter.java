package lines.counter;

import java.io.*;

/**
 * @author Valeriy Knyazhev valeriy.knyazhev@yandex.ru
 */
public class LinesCounter implements Counter {

    @Override
    public int calculate(String filePath) throws FileNotFoundException {
        try (final BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int count = 0;
            while(reader.readLine() != null)
            {
                ++count;
            }
            return count;
        } catch (IOException e) {
            throw new FileNotFoundException("File " + filePath + " not found");
        }
    }
}
