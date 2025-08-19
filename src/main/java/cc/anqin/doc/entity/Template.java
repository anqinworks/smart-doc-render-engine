package cc.anqin.doc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 文档模板基类
 * <p>
 * 该抽象类是所有文档模板的基础类，实现了TemplateInterface接口和Serializable接口。
 * 提供了模板处理所需的基本属性和方法，如转换尺寸、变量前后缀等。
 * 子类需要继承此类并实现getFileName方法来创建特定的模板类型。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>提供模板基础属性 - 如转换尺寸、变量前后缀等</li>
 *   <li>支持链式调用 - 通过@Accessors(chain = true)注解实现</li>
 *   <li>提供默认模板实现 - 通过defaultTemplate()静态方法</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * public class MyTemplate extends Template {
 *     private String name;
 *     
 *     @Override
 *     public String getFileName() {
 *         return "自定义模板";
 *     }
 * }
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2024/11/18
 * @see TemplateInterface 模板接口
 * @see AsposePlaceholder 模板占位符接口
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
