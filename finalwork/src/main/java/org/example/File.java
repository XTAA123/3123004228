package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class File {
    public String[] readFile(String[] args) {
        // 检查参数数量
        if (args.length != 3) {
            System.out.println("错误：程序需要三个参数：");
            System.out.println("1. 原始文件路径");
            System.out.println("2. 抄袭文件路径");
            System.out.println("3. 输出结果文件路径");
            return null; // 返回 null 表示参数错误
        }

        try {
            // 检查文件是否存在
            Path originalPath = Paths.get(args[0]);
            Path plagiarizedPath = Paths.get(args[1]);
            //Path outputPath = Paths.get(args[2]);

            if (!Files.exists(originalPath)) {
                System.out.println("错误：原始文件不存在：" + args[0]);
                return null;
            }
            if (!Files.exists(plagiarizedPath)) {
                System.out.println("错误：抄袭文件不存在：" + args[1]);
                return null;
            }

            // 检查文件是否可读
            if (!Files.isReadable(originalPath)) {
                System.out.println("错误：无法读取原始文件：" + args[0]);
                return null;
            }
            if (!Files.isReadable(plagiarizedPath)) {
                System.out.println("错误：无法读取抄袭文件：" + args[1]);
                return null;
            }

            // 返回文件路径
            return new String[]{args[0], args[1], args[2]};
        } catch (Exception e) {
            System.out.println("错误：读取文件程序执行出错");
            System.out.println("错误详情：" + e.getMessage());
            return null;
        }
    }

    public void writeFile(String filePath, double similarity) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 格式化查重结果
            String result = String.format("查重率：%.2f%%", similarity * 100);
            // 写入文件
            writer.write(result);
            System.out.println("查重结果已写入文件：" + filePath);
        } catch (Exception e) {
            System.out.println("错误：写入程序执行出错");
        }
    }
}
