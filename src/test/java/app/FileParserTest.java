package app;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;

import static junit.framework.TestCase.assertEquals;

public class FileParserTest {

    private FileParser fileParser;
    private static final String testFileName = "Nodes_7_OutTree.dot";
    private File file;

    @Before
    public void setUp() throws URISyntaxException, FileNotFoundException {
        file = new File(this.getClass().getResource(testFileName).toURI());
        fileParser = new FileParser(file);
    }

    @Test
    public void testContainsAllNodes() {
        assertEquals(fileParser.getTaskModelFromFile().getTasks().size(), 7);
    }

    @Test
    public void testOpenFile() throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
    }
}