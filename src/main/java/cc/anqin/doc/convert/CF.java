package cc.anqin.doc.convert;

import cc.anqin.doc.convert.strategy.DefaultFileConvert;
import cc.anqin.doc.ex.DocumentException;
import cc.anqin.doc.utils.FileUtils;
import cn.hutool.core.io.FileUtil;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.aspose.words.SaveOptions;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件转换工具类 - 提供不同格式文件间的转换功能
 * <p>
 * CF（Convert Factory）是Smart Doc Render Engine的核心文件转换工具类，采用建造者模式设计，
 * 支持链式调用，提供灵活的文件格式转换功能。该类封装了复杂的转换逻辑，使文件转换操作
 * 变得简单直观。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>多格式文件转换 - 支持DOCX、PDF、HTML等多种格式间的相互转换</li>
 *   <li>自定义转换参数 - 支持设置页面尺寸、字体路径等转换参数</li>
 *   <li>链式调用支持 - 提供流畅的API调用体验</li>
 *   <li>自动文件管理 - 自动处理临时文件和输出文件</li>
 *   <li>异常处理 - 完善的错误处理和异常包装</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 基础转换
 * File pdfFile = CF.create("document.docx").toFile(DocumentFormat.PDF);
 *
 * // 自定义尺寸转换
 * File customPdf = CF.create("document.docx", 297, 420)  // A3尺寸
 *     .output("output.pdf")
 *     .toFile(DocumentFormat.PDF);
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @since 2025/8/19
 * @see DocumentFormat 文档格式枚举
 * @see AbstractFileConverter 抽象文件转换器
 * @see DefaultFileConvert 默认文件转换器
 * @see DocumentException 文档异常类
 */
@Data
@Accessors(chain = true)
public class CF {

    /**
     * 文档对象 - 存储要转换的文档内容
     * <p>
     * 该字段存储从输入文件或输入流创建的Aspose Words Document对象，
     * 是进行文件格式转换的核心数据源。Document对象包含了文档的所有内容、
     * 格式信息和样式设置。
     * </p>
     */
    private Document document;

    /**
     * 目标输出文件 - 指定转换后的文件保存路径
     * <p>
     * 该字段指定转换完成后的文件保存位置。如果为null，系统会自动生成
     * 临时文件路径。用户可以通过output()方法设置此字段。
     * </p>
     */
    private File outputFile;

    /**
     * 目标文件类型 - 转换后的文件格式
     * <p>
     * 该字段指定要将文档转换成的目标格式，如PDF、HTML、PNG等。
     * 系统会根据此字段选择合适的转换策略和保存选项。
     * </p>
     */
    private DocumentFormat format;

    /**
     * 宽度 - 输出文件的页面宽度（毫米）
     * <p>
     * 该字段控制转换后文档的页面宽度，默认值为210mm（A4纸宽度）。
     * 在转换为固定布局格式（如PDF）时，此参数特别重要。
     * </p>
     */
    private double width = 210;

    /**
     * 高度 - 输出文件的页面高度（毫米）
     * <p>
     * 该字段控制转换后文档的页面高度，默认值为297mm（A4纸高度）。
     * 与width字段配合使用，可以设置任意页面尺寸。
     * </p>
     */
    private double height = 297;

    /**
     * 字体路径 - 自定义字体目录路径
     * <p>
     * 该字段指定用于文档转换的字体资源目录路径。如果为null，
     * 系统会使用默认的字体路径（classpath中的fonts/truetype目录）。
     * 正确的字体路径对于确保文档中字体正确显示至关重要。
     * </p>
     */
    private String fontsPath;

    /**
     * 私有构造方法 - 强制使用静态工厂方法创建实例
     * <p>
     * 该构造方法被设置为私有，强制用户使用静态工厂方法create()来创建CF实例。
     * 这种设计模式确保了对象创建的规范性和一致性。
     * </p>
     */
    private CF() {
    }

    /**
     * 获取 默认文件转换器
     *
     * @return {@link FileConverter }
     */
    public static FileConverter create() {
        return new DefaultFileConvert();
    }

    /**
     * 创建CF实例（使用默认A4尺寸）
     * <p>
     * 该静态工厂方法通过文件名创建CF实例，使用默认的A4尺寸设置（210x297mm）。
     * 系统会自动将文件名转换为File对象，然后调用重载的create方法。
     * </p>
     *
     * @param fileName 输入文件名，可以是相对路径或绝对路径
     * @return CF实例，已加载指定文件并设置默认尺寸
     * @throws IllegalArgumentException 如果fileName为null或空
     * @throws RuntimeException 如果文件加载失败
     */
    public static CF create(String fileName) {
        return create(FileUtil.file(fileName));
    }

    /**
     * 创建CF实例（使用默认A4尺寸）
     * <p>
     * 该静态工厂方法通过File对象创建CF实例，使用默认的A4尺寸设置。
     * 系统会从File对象创建输入流，然后调用重载的create方法。
     * </p>
     *
     * @param inputFile 输入文件对象，必须存在且可读
     * @return CF实例，已加载指定文件并设置默认尺寸
     * @throws IllegalArgumentException 如果inputFile为null或不存在
     * @throws RuntimeException 如果文件加载失败
     */
    public static CF create(File inputFile) {
        return create(FileUtil.getInputStream(inputFile));
    }

    /**
     * 创建CF实例（使用默认A4尺寸）
     * <p>
     * 该静态工厂方法通过输入流创建CF实例，使用默认的A4尺寸设置。
     * 系统会从输入流创建Aspose Document对象，这是进行后续转换操作的基础。
     * </p>
     *
     * @param inputStream 输入流，包含要转换的文档数据
     * @return CF实例，已加载指定文档并设置默认尺寸
     * @throws IllegalArgumentException 如果inputStream为null
     * @throws RuntimeException 如果Document对象创建失败
     */
    public static CF create(InputStream inputStream) {
        return create(inputStream, 210, 297);
    }

    /**
     * 创建CF实例（自定义尺寸）
     * <p>
     * 该静态工厂方法通过输入流创建CF实例，并允许用户自定义页面尺寸。
     * 尺寸参数以毫米为单位，系统会自动转换为相应的点值。
     * </p>
     *
     * @param inputStream 输入流，包含要转换的文档数据
     * @param width 页面宽度（毫米）
     * @param height 页面高度（毫米）
     * @return CF实例，已加载指定文档并设置自定义尺寸
     * @throws IllegalArgumentException 如果inputStream为null或尺寸参数无效
     * @throws RuntimeException 如果Document对象创建失败
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
     * 设置输出文件路径（字符串形式）
     * <p>
     * 该方法通过字符串路径设置输出文件，系统会自动将字符串转换为File对象。
     * 支持相对路径和绝对路径，如果路径不存在会自动创建父目录。
     * </p>
     *
     * @param fileName 输出文件路径，可以是相对路径或绝对路径
     * @return 当前CF实例，支持链式调用
     * @throws IllegalArgumentException 如果fileName为null或空
     */
    public CF output(String fileName) {
        this.outputFile = FileUtil.file(fileName);
        return this;
    }

    /**
     * 设置输出文件路径（File对象形式）
     * <p>
     * 该方法通过File对象设置输出文件，提供更灵活的文件操作能力。
     * 用户可以通过File对象进行更复杂的文件操作，如权限设置、属性修改等。
     * </p>
     *
     * @param file 输出文件对象
     * @return 当前CF实例，支持链式调用
     * @throws IllegalArgumentException 如果file为null
     */
    public CF output(File file) {
        this.outputFile = file;
        return this;
    }

    /**
     * 执行文件转换操作（使用DocumentFormat枚举）
     * <p>
     * 该方法使用DocumentFormat枚举指定目标格式，系统会自动选择合适的转换器。
     * 转换完成后返回转换后的文件对象，如果未指定输出文件，系统会生成临时文件。
     * </p>
     *
     * @param format 目标文件格式，使用DocumentFormat枚举
     * @return 转换后的文件对象
     * @throws IllegalArgumentException 如果format为null
     * @throws DocumentException 如果转换过程中发生错误
     */
    public File toFile(DocumentFormat format) {
        this.format = format;
        return execute();
    }

    /**
     * 执行文件转换操作
     * <p>
     * 转换完成后返回转换后的文件对象，如果未指定输出文件，系统会生成临时文件。
     * </p>
     *
     * @param options 目标文件格式
     * @return 转换后的文件对象
     * @throws IllegalArgumentException 如果format为null
     * @throws DocumentException 如果转换过程中发生错误
     */
    public File toFile(SaveOptions options) {
        this.format = DocumentFormat.fromSaveOptions(options);
        return execute();
    }

    /**
     * 执行文件转换操作（使用自定义转换器）
     * <p>
     * 该方法允许用户使用自定义的文件转换器进行格式转换，提供更大的灵活性。
     * 自定义转换器可以实现特定的转换逻辑、优化策略或特殊格式支持。
     * </p>
     *
     * @param converter 自定义文件转换器，必须继承自AbstractFileConverter
     * @return 转换后的文件对象
     * @param <T> 转换器类型，必须继承自AbstractFileConverter
     * @throws IllegalArgumentException 如果converter为null
     * @throws RuntimeException 如果转换过程中发生错误
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
     * <p>
     * 该私有方法执行文件转换的核心操作，包括：
     * <ol>
     *   <li>检查输出文件设置，如果未指定则生成临时文件</li>
     *   <li>创建默认文件转换器实例</li>
     *   <li>执行实际的格式转换操作</li>
     *   <li>返回转换后的文件对象</li>
     * </ol>
     * </p>
     *
     * @return 转换后的文件对象
     * @throws DocumentException 当转换过程中发生错误时抛出
     */
    private File execute() {
        try {
            if (this.outputFile == null) {
                this.outputFile = FileUtils.getTemporaryFile(format);
            }
            return new DefaultFileConvert()
                    .convert(this.outputFile, document, this.width, this.height, format);
        } catch (Exception e) {
            // 转换失败时包装异常并抛出
            throw new DocumentException(e, "文件转换失败");
        }
    }
}