package cc.anqin.doc.word.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 占位符类型枚举
 * <p>
 * 该枚举定义了系统支持的所有占位符类型，用于在模板处理过程中标识不同类型的占位符，
 * 并决定使用哪种占位符填充器进行处理。每种类型对应不同的数据处理方式和填充逻辑。
 * </p>
 * <p>
 * 支持的占位符类型：
 * <ul>
 *   <li>TEXT - 普通文本替换，用于替换模板中的文本占位符</li>
 *   <li>TEXT_BOOLEAN - 布尔值文本替换，用于处理布尔值的特殊显示</li>
 *   <li>DYNAMIC_ROW - 动态表格行，用于根据数据列表动态生成表格行</li>
 *   <li>PIC - 图片占位符，用于在文档中插入图片</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * @Placeholder(type = PlaceholderType.TEXT)
 * private String textField;
 * 
 * @Placeholder(type = PlaceholderType.PIC)
 * private String imageUrl;
 * 
 * @Placeholder(type = PlaceholderType.DYNAMIC_ROW)
 * private List&lt;DataItem&gt; dataList;
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2024/12/24
 * @see cc.anqin.doc.word.annotation.Placeholder 占位符注解
 * @see cc.anqin.doc.word.placeholder.PlaceholderFillerService 占位符填充服务
 */
@AllArgsConstructor
@Getter
public enum PlaceholderType {

    /**
     * 普通文本替换
     */
    TEXT,

    /**
     * 文本布尔值
     */
    TEXT_BOOLEAN,

    /**
     * 动态表格行
     */
    DYNAMIC_ROW,

    /**
     * 图片
     */
    PIC;
}