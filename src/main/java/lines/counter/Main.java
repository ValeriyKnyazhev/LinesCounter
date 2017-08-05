package lines.counter;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author Valeriy Knyazhev valeriy.knyazhev@yandex.ru
 */
public class Main {

    private static Map<String, String> options = new HashMap<>();

    public static void main(String[] args) {
        parseParameters(args);
        execute();
    }

    private static void execute() {
        String path = options.getOrDefault("p", null);
        if (path == null) {
            System.err.println("Argument '-p' not found");
        } else {
            Counter counter = new LinesCounter();
            try {
                System.out.println(counter.calculate(path));
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void parseParameters(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i].charAt(0)) {
                case '-':
                    if (args[i].length() != 2) {
                        throw new IllegalArgumentException("Not a valid argument: " + args[i]);
                    } else if (args.length - 1 == i) {
                        throw new IllegalArgumentException("Expected argument after: " + args[i]);
                    } else {
                        options.put(args[i].substring(1, 2), args[i + 1]);
                        i++;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Expected arguments for start application");
            }
        }
    }

}