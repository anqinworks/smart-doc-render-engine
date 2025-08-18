package cc.anqin.doc.convert;

import cc.anqin.doc.convert.strategy.DocConverter;
import cc.anqin.doc.convert.strategy.HtmlConverter;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.List;

/**
 * 转换器文件工厂
 *
 * @author Mr.An
 * @date 2024/11/29
 */
@UtilityClass
public class ConverterFileFactory {

    /** 转换 器 */
    private static final List<FileConverter> CONVERTERS;

    static {
        List<FileConverter> dataList = ListUtil.toList(
                new DocConverter(),
                new HtmlConverter()
        );
        CONVERTERS = ListUtil.toList(dataList);
    }

    /**
     * 获取转换器
     *
     * @param inputFile 输入文件
     * @return {@link FileConverter }
     */
    public static FileConverter getConverter(File inputFile) {
        String type = FileUtil.getType(inputFile);
        return CONVERTERS.stream()
                .filter(converter -> converter.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported file type: " + type));
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
    public static File executeConverter(File inputFile, int width, int height) throws Exception {
        return getConverter(inputFile)
                .convert(inputFile, width, height);
    }
}
