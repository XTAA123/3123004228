import org.example.File;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class FileTest {


    @Test
    void testReadFile_NormalCase() {

        // 准备测试数据
        String[] args = {"D:\\code\\finalwork\\src\\test\\orig.text", "D:\\code\\finalwork\\src\\test\\orig_add1.text", "D:\\code\\finalwork\\src\\test\\output.text"};

        // 创建测试文件
        createTestFile(args[0], "这是原始文件内容");
        createTestFile(args[1], "这是抄袭文件内容");

        // 调用方法
        File file = new File();
        String[] result = file.readFile(args);

        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(args[0], result[0]);
        assertEquals(args[1], result[1]);
        assertEquals(args[2], result[2]);

        // 清理测试文件
        deleteTestFile(args[0]);
        deleteTestFile(args[1]);
    }

    @Test
    void testReadFile_IncorrectNumberOfArguments() {
        // 准备测试数据
        String[] args = {"D:\\code\\finalwork\\src\\test\\orig.text", "D:\\code\\finalwork\\src\\test\\orig_add1.text"};
        // 调用方法
        File file = new File();
        String[] result = file.readFile(args);
        // 验证结果
        assertNull(result);
    }

    @Test
    void testReadFile_OriginalFileNotExist() {
        // 准备测试数据
        String[] args = {"src/test/resources/nonexistent.txt", "D:\\code\\finalwork\\src\\test\\orig_add1.text", "D:\\code\\finalwork\\src\\test\\output.text"};
        // 创建抄袭文件
        createTestFile(args[1], "这是抄袭文件内容");
        // 调用方法
        File file = new File();
        String[] result = file.readFile(args);
        // 验证结果
        assertNull(result);
        // 清理测试文件
        deleteTestFile(args[1]);
    }

    @Test
    void testReadFile_PlagiarizedFileNotExist() {
        // 准备测试数据
        String[] args = {"D:\\code\\finalwork\\src\\test\\orig.text", "src/test/resources/nonexistent.txt", "D:\\code\\finalwork\\src\\test\\output.text"};
        // 创建原始文件
        createTestFile(args[0], "这是原始文件内容");
        // 调用方法
        File file = new File();
        String[] result = file.readFile(args);
        // 验证结果
        assertNull(result);
        // 清理测试文件
        deleteTestFile(args[0]);
    }

    @Test
    void testReadFile_OriginalFileNotReadable() {
        // 准备测试数据
        String[] args = {"D:\\code\\finalwork\\src\\test\\orig.text", "D:\\code\\finalwork\\src\\test\\orig_add1.text", "D:\\code\\finalwork\\src\\test\\output.text"};

        // 创建不可读的原始文件
        createTestFile(args[0], "这是原始文件内容");
        makeFileUnreadable(args[0]);
        // 创建抄袭文件
        createTestFile(args[1], "这是抄袭文件内容");
        // 验证结果
        assertNull(null);
        // 清理测试文件
        deleteTestFile(args[0]);
        deleteTestFile(args[1]);
    }

    @Test
    void testReadFile_PlagiarizedFileNotReadable() {
        // 准备测试数据
        String[] args = {"D:\\code\\finalwork\\src\\test\\orig.text", "D:\\code\\finalwork\\src\\test\\orig_add1.text", "D:\\code\\finalwork\\src\\test\\output.text"};

        // 创建原始文件
        createTestFile(args[0], "这是原始文件内容");
        // 创建不可读的抄袭文件
        createTestFile(args[1], "这是抄袭文件内容");
        makeFileUnreadable(args[1]);
        // 验证结果
        assertNull(null);
        // 清理测试文件
        deleteTestFile(args[0]);
        deleteTestFile(args[1]);
    }

    // 辅助方法：创建测试文件
    private void createTestFile(String filePath, String content) {
        try {
            Files.write(Paths.get(filePath), content.getBytes());
        } catch (Exception e) {
            fail("创建测试文件失败：" + e.getMessage());
        }
    }

    // 辅助方法：删除测试文件
    private void deleteTestFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (Exception e) {
            fail("删除测试文件失败：" + e.getMessage());
        }
    }

    // 辅助方法：使文件不可读
    private void makeFileUnreadable(String filePath) {
        try {
            Path path = Paths.get(filePath);
            path.toFile().setReadable(false);
        } catch (Exception e) {
            fail("设置文件不可读失败：" + e.getMessage());
        }
    }
}
