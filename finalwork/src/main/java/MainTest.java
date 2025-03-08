
import org.example.Tools;
import org.junit.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MainTest {
    private final Tools tools = new Tools();


    //空文本对比
    @Test
    public void testCalculateSimilarityWithEmptyText() {
        List<String> originalWords = List.of();
        List<String> plagiarizedWords = List.of();
        double expectedSimilarity = 0.0;
        double actualSimilarity = tools.calculateSimilarity(originalWords, plagiarizedWords);
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
        assertEquals(expectedFrequencyMap, tools.getWordFrequency(words));
    }

    //正常文本对比
    @Test
    public void testNormalCase() {
        String text1 = "这是一个测试文本";
        String text2 = "这是一个不同的测试文本";

        List<String> words1 = tools.preprocessText(text1);
        List<String> words2 = tools.preprocessText(text2);

        double similarity = tools.calculateSimilarity(words1, words2);
        assertTrue(similarity > 0 && similarity < 1);
    }

    //相同文本
    @Test
    public void testIdenticalText() {
        String text1 = "这是一个测试文本";
        String text2 = "这是一个测试文本";

        List<String> words1 = tools.preprocessText(text1);
        List<String> words2 = tools.preprocessText(text2);

        double similarity = tools.calculateSimilarity(words1, words2);
        assertEquals(1.0, similarity, 0.0001);
    }

    //完全不同文本
    @Test
    public void testCompletelyDifferentText() {
        String text1 = "这是一个测试文本";
        String text2 = "另外完全不同的内容";

        List<String> words1 = tools.preprocessText(text1);
        List<String> words2 = tools.preprocessText(text2);

        double similarity = tools.calculateSimilarity(words1, words2);
        assertEquals(0.0, similarity, 0.0001);
    }

    //空文本
    @Test
    public void testEmptyText() {
        String text1 = "这是一个测试文本";
        String text2 = "";

        List<String> words1 = tools.preprocessText(text1);
        List<String> words2 = tools.preprocessText(text2);

        double similarity = tools.calculateSimilarity(words1, words2);
        assertEquals(0.0, similarity, 0.0001);
    }

    //文本预处理测试
    @Test
    public void testPreprocessText() {
        String text = "这是一个测试文本，有123";
        List<String> words = tools.preprocessText(text);
        assertFalse(words.contains("，")); // 标点符号应被移除
        assertFalse(words.contains("123")); // 数字应被移除
    }

    //大小写测试
    @Test
    public void testCaseSensitivity() {
        String text = "This is a Test Text";

        List<String> words = tools.preprocessText(text);
        assertTrue(words.contains("test")); // 应转换为小写
    }

    //文本顺序测试
    @Test
    public void testDifferentOrder() {
        String text1 = "这是一个测试文本";
        String text2 = "文本测试一个这是";

        List<String> words1 = tools.preprocessText(text1);
        List<String> words2 = tools.preprocessText(text2);

        double similarity = tools.calculateSimilarity(words1, words2);
        assertEquals(1.0, similarity, 0.0001);
    }

    //长文本测试
    @Test
    public void testLongText() {
        String text1 = "这是一个长的测试文本，包含多个句子和词汇。";
        String text2 = "这是一个长的测试文本，但内容不同。";

        List<String> words1 = tools.preprocessText(text1);
        List<String> words2 = tools.preprocessText(text2);

        double similarity = tools.calculateSimilarity(words1, words2);
        assertTrue(similarity > 0 && similarity < 1);
    }

}
