package org.example;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        final Tools tools = new Tools();
        File  file = new File();
        String[] filePaths = file.readFile(args);


        // 获取文件路径
        String originalPath = filePaths[0];
        String plagiarizedPath = filePaths[1];
        String outputPath = filePaths[2];

//        String originalPath = "D:\\code\\finalwork\\src\\test\\orig.txt";
//        String plagiarizedPath = "D:\\code\\finalwork\\src\\test\\orig_0.8_dis_1.txt";
//        String outputPath = "D:\\code\\finalwork\\src\\test\\output.txt";

        //预处理文本
        List<String> original = tools.preprocessText(originalPath);
        List<String> plagiarized = tools.preprocessText(plagiarizedPath);
        double similarity = tools.calculateSimilarity(original, plagiarized);


        // 将结果写入文件
        file.writeFile(outputPath, similarity);

    }

}