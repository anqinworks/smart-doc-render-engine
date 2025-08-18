package cc.anqin.doc.word.annotation;



import cc.anqin.doc.word.enums.PlaceholderType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 占位符
 *
 * @author Mr.An
 * @date 2024/12/24
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Placeholder {

    /**
     * 占位符
     * <p>
     *     当 {@link PlaceholderType} 为 {@link PlaceholderType#PIC} 并且 width 或者 height 小于 1 时，仅作为标识使用
     * </p>
     *
     * @return {@link PlaceholderType }
     */
    PlaceholderType value() default PlaceholderType.TEXT;

    /**
     * 多条文本，并且每条拼接，当 {@link Placeholder#value()} 为 {@link PlaceholderType#TEXT} 时，该注解为选填
     *
     * @return {@link MultipleSplicing }
     */
    MultipleSplicing splicing() default @MultipleSplicing({});


    /**
     * 填充的图片的大小,当
     * {@link Placeholder#value()}
     * 为
     * {@link PlaceholderType#PIC}
     * 时，该注解为必填项
     *
     * @return {@link PicWord }
     */
    PicWord pic() default @PicWord(width = 0.0, height = 0.0);


    /**
     * 填充动态表格行,当 {@link Placeholder#value()} 为 {@link PlaceholderType#DYNAMIC_ROW} 时，该注解为必填项
     *
     * @return {@link DynamicRow }
     */
    DynamicRow dynamicRow() default @DynamicRow(Object.class);


    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface PicWord {
        /**
         * 宽度
         *
         * @return double
         */
        double width();

        /**
         * 高度
         *
         * @return double
         */
        double height();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface DynamicRow {

        /**
         * value
         *
         * @return {@link Class }<{@link ? }>
         */
        Class<?> value();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MultipleSplicing {

        /**
         * <pre>
         * {@code
         * TargetMultipleData:  List<List<String>> dataList = Arrays.asList(
         *                 Arrays.asList("胖胖", "2012-08-15","西安"),
         *                 Arrays.asList("熙熙", "2012-08-15","北京"),
         *         );
         * MultipleSplicing: Arrays.asList("子女姓名", "出身日期","地址")
         *
         * 结果以文本形式展出：[子女姓名, 胖胖, 出身日期, 2012-08-15, 地址, 西安]
         *                  [子女姓名, 熙熙, 出身日期, 2012-08-15, 地址, 北京]}
         * </pre>
         *
         * <pre>
         * {@code
         * TargetMultipleData:  List<String> dataList = Arrays.asList("胖胖","熙熙");
         * MultipleSplicing: Arrays.asList("", "、")
         *
         * 结果以文本形式展出：[胖胖、熙熙、]
         * }
         * </pre>
         *
         * @return {@link String[] }
         */
        String[] value();
    }
}
