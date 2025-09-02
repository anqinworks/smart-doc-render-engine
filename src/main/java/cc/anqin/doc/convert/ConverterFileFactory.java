package cc.anqin.doc.convert;

import cc.anqin.doc.convert.strategy.DefaultFileConvert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<Class<?>> classes = ClassUtil.scanPackageBySuper(null, AbstractFileConverter.class);

        Set<FileConverter> converters = classes.stream()
                .filter(Objects::nonNull)
                .map(ConverterFileFactory::createConverterInstance)
                .collect(Collectors.toSet());

        CONVERTERS = Collections.unmodifiableSet(converters);
    }

    /**
     * 创建转换器实例
     *
     * @param clazz 转换器类
     * @return 转换器实例
     * @throws RuntimeException 当实例创建失败时抛出
     */
    private static FileConverter createConverterInstance(Class<?> clazz) {
        try {
            return (FileConverter) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("创建转换器实例失败: " + clazz.getName(), e);
        }
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
