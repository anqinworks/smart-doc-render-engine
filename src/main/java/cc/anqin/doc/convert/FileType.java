package cc.anqin.doc.convert;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件类型枚举
 *
 * @author Mr.An
 * @since 2025/8/19
 */
@AllArgsConstructor
@Getter
public enum FileType {

    DOC("doc"),
    DOCX("docx"),
    PDF("pdf"),
    HTML("html"),
    PNG("png");


    /** 类型 */
    private final String type;

    /**
     * 根据类型获取枚举
     *
     * @param type 类型
     * @return {@link FileType }
     */
    public static FileType of(String type) {
        for (FileType value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
