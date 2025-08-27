package cc.anqin.doc.convert;

import cc.anqin.doc.convert.strategy.DefaultFileConvert;
import cc.anqin.doc.ex.DocumentException;
import cn.hutool.core.io.FileUtil;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件转换工具类 - 提供不同格式文件间的转换功能
 * <p>
 * 支持设置转换参数如宽度、高度和字体路径
 * 采用建造者模式设计，支持链式调用
 * </p>
 *
 * @author Mr.An
 * @since 2025/8/19
 */
@Data
@Accessors(chain = true)
public class CF {

    private Document document;

    /** 目标输出文件 */
    private File outputFile;

    /** 目标文件类型 - 转换后的文件格式 */
    private DocumentFormat format;

    /** 宽度 - 输出文件的页面宽度（毫米）默认A4宽度210mm */
    private double width = 210;

    /** 高度 - 输出文件的页面高度（毫米）默认A4高度297mm */
    private double height = 297;

    /** 字体路径 - 自定义字体目录路径（用于确保文档中字体正确显示） */
    private String fontsPath;

    /**
     * 私有构造方法 - 强制使用静态工厂方法创建实例
     */
    private CF() {
    }


    /**
     * 创建CF实例（使用默认A4尺寸）
     *
     * @param fileName 输入文件
     * @return CF实例
     */
    public static CF create(String fileName) {
        return create(FileUtil.file(fileName));
    }

    /**
     * 创建CF实例（使用默认A4尺寸）
     *
     * @param inputFile 输入文件
     * @return CF实例
     */
    public static CF create(File inputFile) {
        return create(FileUtil.getInputStream(inputFile));
    }

    /**
     * 创建CF实例（使用默认A4尺寸）
     *
     * @param inputStream 输入流
     * @return CF实例
     */
    public static CF create(InputStream inputStream) {
        return create(inputStream, 210, 297);
    }

    /**
     * 创建CF实例（使用默认A4尺寸）
     *
     * @param inputStream 输入流
     * @return CF实例
     */
    public static CF create(InputStream inputStream, double width, double height) {
        try {
            return new CF()
                    .setDocument(new Document(inputStream))
                    .setWidth(width)
                    .setHeight(height);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 执行文件转换操作
     *
     * @return 转换后的文件
     */
    public CF output(String fileName) {
        this.outputFile = FileUtil.file(fileName);
        return this;
    }


    /**
     * 执行文件转换操作
     *
     * @return 转换后的文件
     */
    public CF output(File file) {
        this.outputFile = file;
        return this;
    }

    /**
     * 执行文件转换操作
     *
     * @return 转换后的文件
     */
    public File toFile(DocumentFormat format) {
        this.format = format;
        return execute();
    }

    /**
     * 执行文件转换操作
     *
     * @return 转换后的文件
     */
    public <T extends AbstractFileConverter> File toFile(T converter) {
        this.format = converter.getTargetType();
        try {
            return converter.convert(document, format);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行文件转换核心逻辑
     *
     * @return 转换后的文件
     * @throws DocumentException 当转换过程中发生错误时抛出
     */
    private File execute() {
        try {
            return new DefaultFileConvert()
                    .convert(this.outputFile, document, this.width, this.height, format);
        } catch (Exception e) {
            // 转换失败时包装异常并抛出
            throw new DocumentException(e, "文件转换失败");
        }
    }
}