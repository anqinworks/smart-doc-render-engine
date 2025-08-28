package cc.anqin.doc;

import cc.anqin.doc.convert.AbstractFileConverter;
import cc.anqin.doc.convert.CF;
import cc.anqin.doc.convert.DocumentFormat;
import cc.anqin.doc.entity.AsposePlaceholder;
import cc.anqin.doc.word.PlaceholderFactory;
import cn.hutool.core.io.FileUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;

/**
 * 模板填充工具类 - 用于处理Aspose模板填充操作
 * <p>
 * 该类是Smart Doc Render Engine的核心工具类，提供了一系列便捷方法用于文档模板的填充和转换。
 * 泛型T必须继承自AsposePlaceholder，表示模板占位符实体。
 * 采用链式调用风格，支持模板填充和文件格式转换，使API调用更加流畅和直观。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>模板变量替换 - 将模板中的占位符替换为实际数据</li>
 *   <li>文件格式转换 - 支持将文档转换为PDF等多种格式</li>
 *   <li>链式操作 - 支持流畅的API调用方式</li>
 *   <li>自动文件管理 - 自动处理临时文件和输出文件</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * MyTemplate template = new MyTemplate().setName("示例文档");
 * Pair&lt;File, File&gt; result = FT.of(template, new File("template.docx"))
 *     .fillAndConvert();
 * </pre>
 * </p>
 *
 * @param <T> 模板占位符实体类型，必须继承自AsposePlaceholder
 * @author Mr.An
 * @date 2025/08/19
 * @see AsposePlaceholder 模板占位符接口
 * @see PlaceholderFactory 占位符处理工厂
 * @see CF 文件转换工厂
 * @see DocumentFormat 文档格式枚举
 */
@Data
@Accessors(chain = true)
@Setter(AccessLevel.PRIVATE)
public class FT<T extends AsposePlaceholder> {

    /**
     * 模板数据实体 - 包含需要填充到模板中的数据
     * <p>
     * 该字段存储实现了AsposePlaceholder接口的实体对象，该对象包含了
     * 所有需要填充到模板中的数据和配置信息，如占位符前缀、后缀等。
     * </p>
     */
    private T entity;

    /**
     * 模板文件 - 原始模板文件路径
     * <p>
     * 该字段指向包含占位符的原始模板文件，通常是DOCX格式的Word文档。
     * 模板文件中包含各种占位符标记，如${name}、${address}等。
     * </p>
     */
    private File templateFile;

    /**
     * 输出文件 - 填充后的文件输出路径（可选）
     * <p>
     * 该字段指定填充完成后的文档保存路径。如果为null，系统会自动生成
     * 临时文件路径。用户可以通过此字段控制输出文件的位置和名称。
     * </p>
     */
    private File outputFile;

    /**
     * 记录文件 - 填充过程中生成的记录版本文件
     * <p>
     * 该字段存储模板填充过程中的中间文件，包含所有占位符替换后的内容。
     * 此文件通常用于调试和记录，保留完整的填充历史。
     * </p>
     */
    private File recordFile;

    /**
     * 当前文件 - 填充后生成的最新版本文件
     * <p>
     * 该字段存储经过占位符清理后的最终文档文件。此文件已经移除了
     * 所有未使用的占位符，可以直接用于后续的格式转换操作。
     * </p>
     */
    private File currentFile;

    /**
     * 创建FT实例（基础版）
     * <p>
     * 该静态工厂方法创建一个基础的FT实例，使用默认的A4尺寸设置。
     * 创建后会自动执行模板填充操作，返回已填充的FT实例。
     * </p>
     *
     * @param <T> 模板占位符实体类型
     * @param entity 模板数据实体，包含需要填充的数据
     * @param templateFile 模板文件，包含占位符的原始文档
     * @return 已填充的FT实例，支持链式调用
     * @throws IllegalArgumentException 如果entity或templateFile为null
     * @throws RuntimeException 如果模板填充过程中发生错误
     */
    public static <T extends AsposePlaceholder> FT<T> of(T entity, File templateFile) {
        return new FT<T>()
                .setEntity(entity)
                .setTemplateFile(templateFile).fill();
    }

    /**
     * 创建FT实例（带输出文件）
     * <p>
     * 该静态工厂方法创建一个FT实例，并指定输出文件路径。
     * 创建后会自动执行模板填充操作，并将结果保存到指定的输出文件。
     * </p>
     *
     * @param <T> 模板占位符实体类型
     * @param entity 模板数据实体，包含需要填充的数据
     * @param templateFile 模板文件，包含占位符的原始文档
     * @param outputFile 输出文件，指定填充后的文档保存路径
     * @return 已填充的FT实例，支持链式调用
     * @throws IllegalArgumentException 如果任何参数为null
     * @throws RuntimeException 如果模板填充过程中发生错误
     */
    public static <T extends AsposePlaceholder> FT<T> of(T entity, File templateFile, File outputFile) {
        return of(entity, templateFile)
                .setOutputFile(outputFile).fill();
    }

    /**
     * 执行模板填充操作
     * <p>
     * 该私有方法执行核心的模板填充逻辑，包括：
     * <ol>
     *   <li>调用PlaceholderFactory进行模板填充</li>
     *   <li>设置记录文件和当前文件路径</li>
     *   <li>返回当前实例以支持链式调用</li>
     * </ol>
     * </p>
     *
     * @return 当前FT实例（支持链式调用）
     * @throws RuntimeException 如果模板填充过程中发生错误
     */
    private FT<T> fill() {
        Pair<File, File> execute = execute();
        this.recordFile = execute.getKey();    // 设置记录文件
        this.currentFile = execute.getValue(); // 设置当前文件
        return this;
    }

    /**
     * 将当前文件转换为指定格式
     * <p>
     * 该方法使用默认的转换器将当前填充完成的文档转换为指定的文件格式。
     * 转换过程会保持文档的原始布局和格式，确保输出质量。
     * </p>
     *
     * @param fileType 目标文件类型，如PDF、HTML等
     * @return 转换后的文件对象
     * @throws IllegalArgumentException 如果fileType为null
     * @throws RuntimeException 如果文件转换过程中发生错误
     */
    public File convert(DocumentFormat fileType) {
        return CF.create(currentFile).toFile(fileType);
    }

    /**
     * 使用自定义转换器将当前文件转换为指定格式
     * <p>
     * 该方法允许用户使用自定义的文件转换器进行格式转换，提供更大的灵活性。
     * 自定义转换器可以实现特定的转换逻辑和优化策略。
     * </p>
     *
     * @param converter 自定义文件转换器，必须继承自AbstractFileConverter
     * @return 转换后的文件对象
     * @param <C> 转换器类型，必须继承自AbstractFileConverter
     * @throws IllegalArgumentException 如果converter为null
     * @throws RuntimeException 如果文件转换过程中发生错误
     */
    public <C extends AbstractFileConverter> File convert(C converter) {
        return CF.create(currentFile).toFile(converter);
    }

    /**
     * 将当前文件转换为指定格式（带尺寸参数）
     * <p>
     * 该方法在转换过程中可以指定输出文件的页面尺寸，适用于需要特定页面大小的场景。
     * 尺寸参数以毫米为单位，系统会自动转换为相应的点值。
     * </p>
     *
     * @param fileType 目标文件类型，如PDF、HTML等
     * @param width 输出文件页面宽度（毫米）
     * @param height 输出文件页面高度（毫米）
     * @return 转换后的文件对象
     * @throws IllegalArgumentException 如果fileType为null或尺寸参数无效
     * @throws RuntimeException 如果文件转换过程中发生错误
     */
    public File convert(DocumentFormat fileType, int width, int height) {
        return CF.create(FileUtil.getInputStream(currentFile), width, height).toFile(fileType);
    }

    /**
     * 执行模板填充核心逻辑
     * <p>
     * 该私有方法执行模板填充的核心操作，根据是否指定输出文件路径
     * 选择不同的填充策略。返回Pair对象包含两个文件：
     * <ul>
     *   <li>Key - 记录版本文件，包含所有占位符替换后的内容</li>
     *   <li>Value - 当前版本文件，已清理未使用占位符，可用于格式转换</li>
     * </ul>
     * </p>
     *
     * @return 包含记录文件和当前文件的Pair对象
     * @throws RuntimeException 如果模板填充过程中发生错误
     */
    private Pair<File, File> execute() {
        if (outputFile == null) {
            // 无指定输出路径时使用默认填充方式
            return PlaceholderFactory.fillTemplate(entity, templateFile);
        }
        // 有指定输出路径时使用带输出路径的填充方式
        return PlaceholderFactory.fillTemplate(entity, templateFile, outputFile);
    }
}