package cc.anqin.doc.convert;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件类型枚举
 * <p>
 * 该枚举定义了Smart Doc Render Engine支持的各种文件格式类型，
 * 用于在文件转换过程中标识源文件和目标文件的格式。
 * </p>
 * <p>
 * 支持的文件类型：
 * <ul>
 *   <li>DOC - Microsoft Word 97-2003文档格式</li>
 *   <li>DOCX - Microsoft Word 2007+文档格式</li>
 *   <li>PDF - 可移植文档格式</li>
 *   <li>HTML - 超文本标记语言</li>
 *   <li>PNG - 便携式网络图形格式</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 获取特定类型
 * FileType docxType = FileType.DOCX;
 * 
 * // 根据字符串获取类型
 * FileType pdfType = FileType.of("pdf");
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @since 2025/8/19
 * @see FileConverter 文件转换器接口
 * @see CF 文件转换工具类
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
