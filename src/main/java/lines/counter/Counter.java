package lines.counter;

import java.io.IOException;

/**
 * @author Valeriy Knyazhev valeriy.knyazhev@yandex.ru
 */
public interface Counter {

    int calculate(String filePath) throws IOException;
}
