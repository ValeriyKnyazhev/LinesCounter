import lines.counter.Counter;
import lines.counter.LinesCounter;
import org.junit.*;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.nio.file.Files.delete;

/**
 * @author Valeriy Knyazhev valeriy.knyazhev@yandex.ru
 */
public class CounterTest extends Assert {

    private final static int FILES_COUNT = 3;

    private final static int MAX_LINES_COUNT = 100;

    private final static String LINE_SEPARATOR = System.getProperty("line.separator");

    private final static String TEST_DIRECTORY = "." + File.separator;

    private static List<String> fileNames = new ArrayList<>();

    private static String emptyFileName = TEST_DIRECTORY + "empty.txt";

    private Counter counter = new LinesCounter();

    @BeforeClass
    public static void createFiles() {
        for (int i = 0; i < FILES_COUNT; ++i) {
            fileNames.add(generateName() + ".txt");
        }
        fileNames.stream().map(Paths::get).forEach(it -> createFile(it, false));
        createFile(Paths.get(emptyFileName), true);
    }

    private static String generateName() {
        return TEST_DIRECTORY + UUID.randomUUID().toString().replaceAll("-", "");
    }

    private static void createFile(Path path, boolean isEmpty) {
        try {
            Files.createFile(path);
            if (!isEmpty) {
                fillFile(path.getFileName().toString());
            }
            System.out.println(path.getFileName().toString() + " is created");
        } catch (FileAlreadyExistsException e) {
            System.err.println("File " + path.getFileName() + " already exists");
            assert false;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            assert false;
        }
    }

    private static void fillFile(String path) throws FileNotFoundException {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            int numberOfLines = (int) (MAX_LINES_COUNT * Math.random() + 1);
            for (int i = 0; i < numberOfLines; ++i) {
                String content = String.valueOf(Math.random());
                writer.write(content + LINE_SEPARATOR);
            }
        } catch (IOException e) {
            throw new FileNotFoundException("File " + path + " not found");
        }
    }

    @AfterClass
    public static void removeFiles() {
        fileNames.stream().map(Paths::get).forEach(CounterTest::removeFile);
        removeFile(Paths.get(emptyFileName));
    }

    private static void removeFile(Path path) {
        try {
            delete(path);
            System.out.println(path.getFileName().toString() + " is removed");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void testLinesCounterAtEmptyFile() {
        int result = -1;
        int expected  = 0;
        try {
            result = this.counter.calculate(this.emptyFileName);
        } catch (FileNotFoundException e) {
            assert false;
        }
        System.out.println("Expected: " + expected + " and have: " + result);
        assertEquals(expected, result);
    }

    @Test
    public void testLinesCounter() {
        fileNames.forEach(it -> {
            int result = -1;
            int expected  = -2;
            try {
                result = this.counter.calculate(it);
            } catch (FileNotFoundException e) {
                assert false;
            }
            try {
                expected = runWc(it);
            } catch (IOException | InterruptedException e) {
                assert false;
            }
            System.out.println("Expected: " + expected + " and have: " + result);
            assertEquals(expected, result);
        });
    }

    @Test
    public void testLinesCounterFailedAtNotFoundFile() {
        boolean thrown = false;
        String incorrectName = generateName();
        try {
            this.counter.calculate(incorrectName);
        } catch (FileNotFoundException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    private int runWc(String path) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("wc -l " + path);
        process.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String answer = reader.readLine();
        return Integer.valueOf(answer.substring(0, answer.indexOf(" ")));
    }
}