package org.example;

import com.hankcs.hanlp.HanLP;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String original = "src/main/java/org/example/orig.text";

        String plagiarizedFile1 = "src/main/java/org/example/orig_add1.text";
        //String plagiarizedFile2 = "src/main/java/org/example/orig_add2.text";
        //String plagiarizedFile3 = "src/main/java/org/example/orig_add3.text";
        //String plagiarizedFile4 = "src/main/java/org/example/orig_add4.text";
        //String plagiarizedFile5 = "src/main/java/org/example/orig_add5.text";

        String outputFile = "src/main/java/org/example/output.text";

        try {
            //读取文本内容
            String originalText = readFile(original);
            String plagiarizedText1 = readFile(plagiarizedFile1);
            //String plagiarizedText2 = readFile(plagiarizedFile2);
            //String plagiarizedText3 = readFile(plagiarizedFile3);
            //String plagiarizedText4 = readFile(plagiarizedFile4);
            //String plagiarizedText5 = readFile(plagiarizedFile5);

            // 预处理文本
            List<String> originalWords = preprocessText(originalText);
            List<String> plagiarizedWords1 = preprocessText(plagiarizedText1);
            //List<String> plagiarizedWords2 = preprocessText(plagiarizedText2);
            //List<String> plagiarizedWords3 = preprocessText(plagiarizedText3);
            //List<String> plagiarizedWords4 = preprocessText(plagiarizedText4);
            //List<String> plagiarizedWords5 = preprocessText(plagiarizedText5);
//
            // 计算查重率
            double similarity1 = calculateSimilarity(originalWords,plagiarizedWords1);
            //double similarity2 = calculateSimilarity(originalWords,plagiarizedWords2);
            //double similarity3 = calculateSimilarity(originalWords,plagiarizedWords3);
            //double similarity4 = calculateSimilarity(originalWords,plagiarizedWords4);
            //double similarity5 = calculateSimilarity(originalWords,plagiarizedWords5);

            System.out.println("文本1的查重率：" + similarity1 * 100 + "%\n");
            //System.out.println("文本2的查重率：" + similarity2 * 100 + "%\n");
            //System.out.println("文本3的查重率：" + similarity3 * 100 + "%\n");
            //System.out.println("文本4的查重率：" + similarity4 * 100 + "%\n");
            //System.out.println("文本5的查重率：" + similarity5 * 100 + "%\n");

            writeFile(outputFile,similarity1);
            //writeFile(outputFile,similarity2);
            //writeFile(outputFile,similarity3);
            //writeFile(outputFile,similarity4);
            //writeFile(outputFile,similarity5);

            System.out.println("查重率写入成功\n");
            System.out.println("查重完成，结果已保存至 " + outputFile);
            System.out.println("\n");

        }catch (Exception e){
            System.out.println("Error:文件读取错误\n");
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
        } else if (originalWords.isEmpty() && plagiarizedWords.isEmpty()) {
            return 1.0;//两个文本都为空，则查重率为1
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
    private static Map<String, Integer> getWordFrequency(List<String> words) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }
        return frequencyMap;
    }
}