package cc.anqin.doc;

import cc.anqin.doc.convert.CF;
import cc.anqin.doc.convert.FileType;
import cc.anqin.doc.entity.AsposePlaceholder;
import cc.anqin.doc.word.PlaceholderFactory;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;

/**
 * 模板填充工具类 - 用于处理Aspose模板填充操作
 * <p>
 * 泛型T必须继承自AsposePlaceholder，表示模板占位符实体
 * 采用链式调用风格，支持模板填充和文件格式转换
 * </p>
 *
 * @param <T> 模板占位符实体类型，必须继承自AsposePlaceholder
 * @author Mr.An
 * @date 2025/08/19
 */
@Data
@Accessors(chain = true)
@Setter(AccessLevel.PRIVATE)
public class FT<T extends AsposePlaceholder> {

    /** 模板数据实体 - 包含需要填充到模板中的数据 */
    private T entity;

    /** 模板文件 - 原始模板文件路径 */
    private File templateFile;

    /** 输出文件 - 填充后的文件输出路径（可选） */
    private File outputFile;

    /** 记录文件 - 填充过程中生成的记录版本文件 */
    private File recordFile;

    /** 当前文件 - 填充后生成的最新版本文件 */
    private File currentFile;

    /**
     * 创建FT实例（基础版）
     *
     * @param <T> 模板占位符实体类型
     * @param entity 模板数据实体
     * @param templateFile 模板文件
     * @return FT实例
     */
    public static <T extends AsposePlaceholder> FT<T> of(T entity, File templateFile) {
        return new FT<T>()
                .setEntity(entity)
                .setTemplateFile(templateFile).fill();
    }

    /**
     * 创建FT实例（带输出文件）
     *
     * @param <T> 模板占位符实体类型
     * @param entity 模板数据实体
     * @param templateFile 模板文件
     * @param outputFile 输出文件
     * @return FT实例
     */
    public static <T extends AsposePlaceholder> FT<T> of(T entity, File templateFile, File outputFile) {
        return of(entity, templateFile)
                .setOutputFile(outputFile).fill();
    }

    /**
     * 执行模板填充操作
     *
     * @return 当前FT实例（支持链式调用）
     */
    private FT<T> fill() {
        Pair<File, File> execute = execute();
        this.recordFile = execute.getKey();    // 设置记录文件
        this.currentFile = execute.getValue(); // 设置当前文件
        return this;
    }

    /**
     * 将当前文件转换为指定格式
     *
     * @param fileType 目标文件类型
     * @return 转换后的文件
     */
    public File convert(FileType fileType) {
        return CF.create(currentFile).toFile(fileType);
    }

    /**
     * 将当前文件转换为指定格式
     *
     * @param fileType 目标文件类型
     * @return 转换后的文件
     */
    public File convert(FileType fileType, int width, int height) {
        return CF.create(currentFile, width, height).toFile(fileType);
    }

    /**
     * 执行模板填充核心逻辑
     * <p>
     * 返回Pair对象，其中：
     * Key - 记录版本文件
     * Value - 当前版本文件
     * </p>
     *
     * @return 包含记录文件和当前文件的Pair对象
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