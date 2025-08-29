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
 *   <li>序列化支持 - 实现Serializable接口，支持对象序列化</li>
 *   <li>配置灵活性 - 允许子类自定义各种模板参数</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * public class MyTemplate extends Template {
 *     private String name;
 *     private String address;
 *     
 *     &#064;Override
 *     public String getFileName() {
 *         return "自定义模板";
 *     }
 *     
 *     // 自定义转换尺寸
 *     public MyTemplate() {
 *         setConvertWidth(800);
 *         setConvertHeight(600);
 *         setPrefix("{{");
 *         setSuffix("}}");
 *     }
 * }
 * </pre>
 * </p>
 * <p>
 * 设计特点：
 * <ul>
 *   <li>抽象类设计 - 强制子类实现必要的抽象方法</li>
 *   <li>默认值提供 - 为常用属性提供合理的默认值</li>
 *   <li>链式调用 - 支持流畅的API调用方式</li>
 *   <li>可扩展性 - 子类可以根据需要添加新的属性和方法</li>
 * </ul>
 * </p>
 *
 * @author Mr.An
 * @date 2024/11/18
 * @see TemplateInterface 模板接口
 * @see AsposePlaceholder 模板占位符接口
 * @see Serializable Java序列化接口
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public abstract class Template implements TemplateInterface, Serializable {

    /**
     * 转换宽度 - 模板内容在转换过程中的宽度设置
     * <p>
     * 该字段控制模板内容在转换为其他格式（如PDF、图片）时的宽度。
     * 宽度值通常以像素为单位，影响最终输出文档的页面布局和内容显示。
     * </p>
     * <p>
     * 使用场景：
     * <ul>
     *   <li>PDF转换 - 控制PDF页面的宽度</li>
     *   <li>图片导出 - 控制导出图片的宽度</li>
     *   <li>页面布局 - 影响文档的整体页面布局</li>
     * </ul>
     * </p>
     * <p>
     * 建议根据实际内容需求设置合理的宽度值，避免内容显示异常或布局混乱。
     * 默认值为0，表示使用系统默认设置。
     * </p>
     */
    private int convertWidth;

    /**
     * 转换高度 - 模板内容在转换过程中的高度设置
     * <p>
     * 该字段控制模板内容在转换为其他格式时的高度。高度值通常以像素为单位，
     * 与宽度配合使用，确保内容的正确显示和页面布局。
     * </p>
     * <p>
     * 使用场景：
     * <ul>
     *   <li>PDF转换 - 控制PDF页面的高度</li>
     *   <li>图片导出 - 控制导出图片的高度</li>
     *   <li>分页控制 - 影响文档的分页和页面布局</li>
     * </ul>
     * </p>
     * <p>
     * 高度值应该与宽度值保持合理的比例，确保内容的视觉效果良好。
     * 默认值为0，表示使用系统默认设置。
     * </p>
     */
    private int convertHeight;

    /**
     * 变量前缀 - 模板中占位符变量的前缀标识
     * <p>
     * 该字段定义模板中占位符变量的前缀字符串，用于在模板文档中识别占位符的开始位置。
     * 前缀通常是一个或多个特殊字符，如"${"、"#"、"<<"等。
     * </p>
     * <p>
     * 前缀的作用：
     * <ul>
     *   <li>占位符识别 - 系统通过前缀和后缀的组合来识别完整的占位符</li>
     *   <li>模板解析 - 在解析模板时，前缀帮助定位占位符的边界</li>
     *   <li>替换操作 - 在替换占位符时，前缀用于精确定位替换位置</li>
     *   <li>格式一致性 - 确保模板中所有占位符使用统一的格式</li>
     * </ul>
     * </p>
     * <p>
     * 默认值为"${"，这是一个常用的占位符前缀格式。子类可以根据需要修改此值。
     * </p>
     */
    private String prefix = "${";

    /**
     * 变量后缀 - 模板中占位符变量的后缀标识
     * <p>
     * 该字段定义模板中占位符变量的后缀字符串，用于在模板文档中识别占位符的结束位置。
     * 后缀通常是一个或多个特殊字符，如"}"、">>"等，与前缀配合使用。
     * </p>
     * <p>
     * 后缀的作用：
     * <ul>
     *   <li>占位符边界 - 与前缀配合，确定占位符的完整范围</li>
     *   <li>内容提取 - 在提取占位符内容时，后缀帮助确定内容的结束位置</li>
     *   <li>替换验证 - 在替换过程中，后缀用于验证替换操作的正确性</li>
     *   <li>格式规范 - 确保占位符格式的规范性和一致性</li>
     * </ul>
     * </p>
     * <p>
     * 默认值为"}"，与前缀"${"形成匹配的字符对。子类可以根据需要修改此值，
     * 但应该确保前缀和后缀形成合理的配对。
     * </p>
     */
    private String suffix = "}";

    /**
     * 创建默认模板实例
     * <p>
     * 该静态方法创建一个默认的模板实例，提供标准的模板配置。
     * 默认模板使用标准的占位符格式（"${"和"}"），适用于大多数常见的模板场景。
     * </p>
     * <p>
     * 使用场景：
     * <ul>
     *   <li>测试环境 - 在测试过程中使用标准配置</li>
     *   <li>默认配置 - 当不需要特殊配置时使用</li>
     *   <li>示例代码 - 作为示例代码的参考实现</li>
     *   <li>向后兼容 - 确保旧版本代码的兼容性</li>
     * </ul>
     * </p>
     * <p>
     * 返回的模板实例是一个匿名内部类，实现了getFileName方法，返回"默认模板"。
     * 其他属性使用类中定义的默认值。
     * </p>
     *
     * @return 默认模板实例，包含标准的配置设置
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
