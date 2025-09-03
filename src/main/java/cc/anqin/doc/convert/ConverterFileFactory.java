package cc.anqin.doc.convert;

import cc.anqin.doc.convert.strategy.DefaultFileConvert;
import cc.anqin.doc.convert.strategy.HtmlToPDFConvert;
import cc.anqin.doc.entity.AddOnlySet;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * converter factory
 *
 * @author Mr.An
 * @date 2024/11/29
 */
@Slf4j
public class ConverterFileFactory {

    private static final Set<FileConverter> CONVERTERS;


    static {
        CONVERTERS = new AddOnlySet<>();
        register(new HtmlToPDFConvert());
        register(new DefaultFileConvert());
    }


    /**
     * 注册方法
     *
     * @param converter 转炉
     */
    public static void register(FileConverter converter) {
        CONVERTERS.add(converter);
    }

    /**
     * 获取转换器
     *
     * @param inputType 输入类型
     * @param targetType 目标类型
     * @return {@link FileConverter }
     */
    public static FileConverter getConverter(DocumentFormat inputType, DocumentFormat targetType) {
        return CONVERTERS.stream()
                .filter(converter -> converter.supports(inputType, targetType))
                .findFirst()
                .orElseGet(() -> {
                    log.warn("Unsupported file type inputType:{}, targetType:{}. now use DefaultFileConvert", inputType, targetType);
                    return new DefaultFileConvert();
                });
    }
}
