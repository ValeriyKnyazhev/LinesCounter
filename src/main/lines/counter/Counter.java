package main.lines.counter;

import java.io.FileNotFoundException;

/**
 * @author Valeriy Knyazhev valeriy.knyazhev@yandex.ru
 */
public interface Counter {

    int calculate(String filePath) throws FileNotFoundException;
}
