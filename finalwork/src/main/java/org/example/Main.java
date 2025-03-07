package org.example;

import com.hankcs.hanlp.HanLP;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // 检查参数数量
        if (args.length != 3) {
            System.out.println("错误：程序需要三个参数：");
            System.out.println("1. 原始文件路径");
            System.out.println("2. 抄袭文件路径");
            System.out.println("3. 输出结果文件路径");
            return;
        }

        try {
            // 检查文件是否存在
            Path originalPath = Paths.get(args[0]);
            Path plagiarizedPath = Paths.get(args[1]);
            Path outputPath = Paths.get(args[2]);

            if (!Files.exists(originalPath)) {
                System.out.println("错误：原始文件不存在：" + args[0]);
                return;
            }
            if (!Files.exists(plagiarizedPath)) {
                System.out.println("错误：抄袭文件不存在：" + args[1]);
                return;
            }

            // 检查文件是否可读
            if (!Files.isReadable(originalPath)) {
                System.out.println("错误：无法读取原始文件：" + args[0]);
                return;
            }
            if (!Files.isReadable(plagiarizedPath)) {
                System.out.println("错误：无法读取抄袭文件：" + args[1]);
                return;
            }

            // 读取并处理文件
            List<String> original = preprocessText(Files.readString(originalPath, StandardCharsets.UTF_8));
            List<String> plagiarized = preprocessText(Files.readString(plagiarizedPath, StandardCharsets.UTF_8));
            double similarity = calculateSimilarity(original, plagiarized);
            
            // 输出结果
            String result = String.format("查重率：%.2f%%", similarity * 100);
            System.out.println(result);
            
            // 写入结果文件
            try {
                Files.writeString(outputPath, result, StandardCharsets.UTF_8);
                System.out.println("结果已保存到：" + args[2]);
            } catch (IOException e) {
                System.out.println("错误：无法写入结果文件：" + args[2]);
                System.out.println("错误详情：" + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("错误：程序执行出错");
            System.out.println("错误详情：" + e.getMessage());
            e.printStackTrace();
        }
    }

    //文件内容读取
    public static String readFile(String filePath)throws Exception{
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = br.readLine()) != null){
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }

    //查重率写入文件
    public static void writeFile(String filePath,double similarity)throws Exception{
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
        String result =String.format("查重率：%.2f%%",similarity * 100);
        bw.write(result);
        bw.close();
    }

    //文本预处理
    public static List<String> preprocessText(String text){
        // 使用 HanLP 分词才能精确计算出查重率
        return HanLP.segment(text)//对text分词
                .stream()
                // 移除标点符号和数字，并转换为小写
                .map(term -> term.word.replaceAll("[^\\p{L}\\p{N}]", "").toLowerCase())
                //除去空字符串
                .filter(word -> !word.isEmpty())
                //收集处理后的词
                .collect(Collectors.toList());
    }

    //计算查重率(采用计算余弦相似度的值)
    public static double calculateSimilarity(List<String> originalWords, List<String> plagiarizedWords){

        if (originalWords.isEmpty() || plagiarizedWords.isEmpty()) {
            return 0.0;//其中任一文本为空，则查重率为0
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
    public static Map<String, Integer> getWordFrequency(List<String> words) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }
        return frequencyMap;
    }
}