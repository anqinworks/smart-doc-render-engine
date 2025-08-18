package cc.anqin.doc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 生成模板
 *
 * @author Mr.An
 * @date 2024/11/18
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public abstract class Template implements TemplateInterface, Serializable {


    /** 转换宽度 */
    private int convertWidth;

    /** 转换高度 */
    private int convertHeight;

    /** 变量前缀 */
    private String prefix;

    /** 变量后缀 */
    private String suffix;


    /**
     * 默认模板
     *
     * @return {@link Template }
     */
    public static Template defaultTemplate() {
        return new Template() {
            @Override
            public String getFileName() {
                return "默认模板";
            }
        };
    }
}
