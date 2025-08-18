package cc.anqin.doc.word.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 占位符类型
 *
 * @author Mr.An
 * @date 2024/12/24
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