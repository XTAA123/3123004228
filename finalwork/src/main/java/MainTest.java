import org.example.Main;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MainTest {
    //读取文件测试
    @Test
    public void testReadFile() throws Exception {
        String filePath = "src/main/java/org/example/testOrig.text";
        String expectedText = "This is a sample text for test\n";

        String actualText = Main.readFile(filePath);

        assertEquals(expectedText, actualText);
    }
    //写入文件测试
    @Test
    public void testWriteFile() throws Exception {
        String filePath = "src/main/java/org/example/testOutput.text";
        double similarity = 0.8;

        Main.writeFile(filePath, similarity);
        String expectedText = "查重率：80.00%\n";
        String actualText = Main.readFile(filePath);

        assertEquals(expectedText, actualText);
    }
    //文本预处理测试
    @Test
    public void testPreprocessText() {
        String text = "This is a sample text for test.";
        List<String> expectedWords = List.of("this", "is", "a", "sample", "text", "for", "test");
        List<String> actualWords = Main.preprocessText(text);
        assertEquals(expectedWords, actualWords);

    }

    //空文本对比
    @Test
    public void testCalculateSimilarityWithEmptyText() {
        List<String> originalWords = List.of();
        List<String> plagiarizedWords = List.of();
        double expectedSimilarity = 0.0;
        double actualSimilarity = Main.calculateSimilarity(originalWords, plagiarizedWords);
        assertEquals(expectedSimilarity, actualSimilarity, 0.0001);
    }

    //其中为一空文本对比
    @Test
    public void testCalculateSimilarityWithOneEmptyText() {
        List<String> originalWords = List.of("this", "is", "a", "sample", "text", "for", "test");
        List<String> plagiarizedWords = List.of();
        double expectedSimilarity = 0.0;
        double actualSimilarity = Main.calculateSimilarity(originalWords, plagiarizedWords);
        assertEquals(expectedSimilarity, actualSimilarity, 0.0001);
    }

    //相同文本对比
    @Test
    public void testCalculateSimilarityWithSameText() {
        List<String> originalWords = List.of("软件","工程","测试","案例");
        List<String> plagiarizedWords = List.of("软件","工程","测试","案例");
        double expectedSimilarity = 1.0;
        double actualSimilarity = Main.calculateSimilarity(originalWords, plagiarizedWords);
        assertEquals(expectedSimilarity, actualSimilarity, 0.0001);
    }

    //不同文本对比
    @Test
    public void testCalculateSimilarityWithDifferentText() {
        List<String> originalWords = List.of("this", "is", "a", "sample", "text", "for", "test");
        List<String> plagiarizedWords = List.of("this", "is", "a", "different", "text", "for", "test");
        double expectedSimilarity = 0.857142857142857;
        double actualSimilarity = Main.calculateSimilarity(originalWords, plagiarizedWords);
        assertEquals(expectedSimilarity, actualSimilarity, 0.0001);
    }

    //词频统计测试
    @Test
    public void testGetWordFrequency() {
        List<String> words = List.of("this", "is", "is", "a", "sample", "text", "for");
        Map<String, Integer> expectedFrequencyMap = new HashMap<>();
        expectedFrequencyMap.put("this", 1);
        expectedFrequencyMap.put("is", 2);
        expectedFrequencyMap.put("a", 1);
        expectedFrequencyMap.put("sample", 1);
        expectedFrequencyMap.put("text", 1);
        expectedFrequencyMap.put("for", 1);
        assertEquals(expectedFrequencyMap, Main.getWordFrequency(words));
    }
}
