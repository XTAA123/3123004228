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
         final Tools tools = new Tools();
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
            List<String> original = tools.preprocessText(Files.readString(originalPath, StandardCharsets.UTF_8));
            List<String> plagiarized = tools.preprocessText(Files.readString(plagiarizedPath, StandardCharsets.UTF_8));
            double similarity = tools.calculateSimilarity(original, plagiarized);

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
        }
    }

}