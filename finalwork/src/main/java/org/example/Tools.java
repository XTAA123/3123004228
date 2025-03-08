package org.example;

import com.hankcs.hanlp.HanLP;
import java.util.*;
import java.util.stream.Collectors;

public class Tools {

    //文本预处理
    public List<String> preprocessText(String text){
        // 使用 HanLP 分词才能精确计算出查重率
        return HanLP.segment(text)//对text分词
                .stream()
                // 移除标点符号和数字，并转换为小写
                .map(term -> term.word.replaceAll("[^\\p{Script=Han}\\p{L}]", "").toLowerCase())
                //除去空字符串
                .filter(word -> !word.isEmpty())
                //收集处理后的词
                .collect(Collectors.toList());
    }

    //计算查重率(采用计算余弦相似度的值)
    public double calculateSimilarity(List<String> originalWords, List<String> plagiarizedWords){
        //其中任一文本为空，则查重率为0
        if (originalWords.isEmpty() || plagiarizedWords.isEmpty()) {
            return 0.0;
        }

        // 合并所有词汇
        Set<String> vocabulary = new HashSet<>();
        vocabulary.addAll(originalWords);
        vocabulary.addAll(plagiarizedWords);

        //统计两个文本的词频
        Map<String, Integer> freq1 = getWordFrequency(originalWords);
        Map<String, Integer> freq2 = getWordFrequency(plagiarizedWords);

        // 计算点积和模长
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String word : vocabulary) {
            int count1 = freq1.getOrDefault(word, 0);
            int count2 = freq2.getOrDefault(word, 0);
            dotProduct += count1 * count2;
            norm1 += Math.pow(count1, 2);
            norm2 += Math.pow(count2, 2);
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    // 统计词频
    public Map<String, Integer> getWordFrequency(List<String> words) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }
        return frequencyMap;
    }
}
