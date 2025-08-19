package cc.anqin.doc.convert;

import cc.anqin.doc.convert.strategy.DocConverter;
import cc.anqin.doc.convert.strategy.HtmlConverter;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * 转换器文件工厂
 * <p>
 * 该工厂类负责管理和创建适合特定文件类型转换的FileConverter实例。
 * 它维护了一个可用转换器的注册表，并根据输入文件类型和目标文件类型选择合适的转换器。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>转换器注册 - 在静态初始化块中注册所有可用的转换器实现</li>
 *   <li>转换器选择 - 根据源文件类型和目标文件类型选择合适的转换器</li>
 *   <li>转换执行 - 提供便捷方法直接执行文件转换操作</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 获取转换器
 * FileConverter converter = ConverterFileFactory.getConverter(docxFile, FileType.PDF);
 * 
 * // 直接执行转换
 * File pdfFile = ConverterFileFactory.convert(docxFile, FileType.PDF, 210, 297);
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2024/11/29
 * @see FileConverter 文件转换器接口
 * @see FileType 文件类型枚举
 * @see DocConverter Word文档转换器
 * @see HtmlConverter HTML文档转换器
 */

public class ConverterFileFactory {

    /** 转换 器 */
    private static final List<FileConverter> CONVERTERS;

    static {
        List<FileConverter> dataList = ListUtil.toList(
                new DocConverter(),
                new HtmlConverter()
        );
        CONVERTERS = Collections.unmodifiableList(dataList);
    }

    /**
     * 获取转换器
     *
     * @param inputFile 输入文件
     * @return {@link FileConverter }
     */
    public static FileConverter getConverter(File inputFile, FileType targetType) {
        FileType fileType = FileType.of(FileUtil.getType(inputFile));
        return CONVERTERS.stream()
                .filter(converter -> converter.supports(fileType, targetType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported file type: " + fileType));
    }

    /**
     * 执行转换器
     *
     * @param inputFile 输入文件
     * @param width     宽度
     * @param height    高度
     * @return {@link File }
     * @throws Exception 例外
     */
    public static File executeConverter(File inputFile, FileType targetType, int width, int height) throws Exception {
        return getConverter(inputFile, targetType)
                .convert(inputFile, width, height);
    }
}
