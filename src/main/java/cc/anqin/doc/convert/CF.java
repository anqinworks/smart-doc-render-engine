package cc.anqin.doc.convert;

import cc.anqin.doc.ex.DocumentException;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;

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
    
    /** 输入文件 - 需要转换的源文件 */
    private File inputFile;
    
    /** 目标文件类型 - 转换后的文件格式 */
    private FileType targetFileType;
    
    /** 宽度 - 输出文件的页面宽度（毫米）默认A4宽度210mm */
    private int width = 210;
    
    /** 高度 - 输出文件的页面高度（毫米）默认A4高度297mm */
    private int height = 297;
    
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
     * @param inputFile 输入文件
     * @return CF实例
     */
    public static CF create(File inputFile) {
        return new CF()
                .setInputFile(inputFile)
                .setWidth(210)
                .setHeight(297);
    }

    /**
     * 创建CF实例（自定义尺寸）
     * 
     * @param inputFile 输入文件
     * @param width 页面宽度（毫米）
     * @param height 页面高度（毫米）
     * @return CF实例
     */
    public static CF create(File inputFile, int width, int height) {
        return new CF()
                .setInputFile(inputFile)
                .setWidth(width)
                .setHeight(height);
    }

    /**
     * 执行文件转换操作
     * 
     * @param sourceFileType 源文件类型（实际未使用，考虑移除或重构）
     * @return 转换后的文件
     */
    public File toFile(FileType sourceFileType) {
        this.targetFileType = sourceFileType;
        return execute();
    }

    /**
     * 执行文件转换核心逻辑
     * 
     * @return 转换后的文件
     * @throws DocumentException 当转换过程中发生错误时抛出
     */
    private File execute() {
        try {
            // 委托给ConverterFileFactory执行实际转换操作
            return ConverterFileFactory.executeConverter(
                    this.inputFile, 
                    this.targetFileType, 
                    this.width, 
                    this.height);
        } catch (Exception e) {
            // 转换失败时包装异常并抛出
            throw new DocumentException(e, "文件转换失败");
        }
    }
}