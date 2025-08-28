package cc.anqin.doc.entity;

/**
 * Aspose占位符接口
 * <p>
 * 该接口定义了文档模板中占位符的基本属性和行为。所有需要在文档中进行占位符替换的实体类
 * 都应该实现该接口，以提供占位符处理所需的基本信息。该接口是Smart Doc Render Engine
 * 模板系统的核心契约，确保了占位符处理的一致性和可扩展性。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>文件名称管理 - 获取与占位符关联的文件名，用于日志记录和文件管理</li>
 *   <li>尺寸控制 - 定义占位符内容（如图片）的宽度和高度，确保内容正确显示</li>
 *   <li>占位符标识 - 提供占位符的前缀和后缀，用于在模板中识别和替换占位符</li>
 *   <li>配置灵活性 - 允许不同的实体类定义自己的占位符格式和尺寸</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * public class MyTemplateData implements AsposePlaceholder {
 *     private String fileName = "template.docx";
 *     private String name;
 *     private String address;
 *     
 *     @Override
 *     public String getFileName() {
 *         return fileName;
 *     }
 *     
 *     @Override
 *     public int getConvertWidth() {
 *         return 800;  // 图片宽度800像素
 *     }
 *     
 *     @Override
 *     public int getConvertHeight() {
 *         return 600;  // 图片高度600像素
 *     }
 *     
 *     @Override
 *     public String getPrefix() {
 *         return "${";  // 占位符前缀
 *     }
 *     
 *     @Override
 *     public String getSuffix() {
 *         return "}";   // 占位符后缀
 *     }
 *     
 *     // Getters and setters for name, address, etc.
 * }
 * </pre>
 * </p>
 * <p>
 * 实现注意事项：
 * <ul>
 *   <li>所有方法都应该返回非null值，确保占位符处理的稳定性</li>
 *   <li>getFileName()方法应该返回有意义的文件名，便于调试和日志记录</li>
 *   <li>尺寸参数应该根据实际内容需求合理设置，避免内容显示异常</li>
 *   <li>前缀和后缀应该与模板文档中的占位符格式保持一致</li>
 * </ul>
 * </p>
 *
 * @author Mr.An
 * @date 2024/12/25
 * @see cc.anqin.doc.word.placeholder.PlaceholderFillerService 占位符填充服务
 * @see cc.anqin.doc.word.annotation.Placeholder 占位符注解
 * @see cc.anqin.doc.word.PlaceholderFactory 占位符处理工厂
 * @see Template 文档模板基类
 */
public interface AsposePlaceholder {

    /**
     * 获取文件名称
     * <p>
     * 该方法返回与当前占位符实体关联的文件名称。文件名称通常用于：
     * <ul>
     *   <li>日志记录 - 在日志中标识正在处理的文档</li>
     *   <li>错误追踪 - 当处理失败时，便于定位问题文档</li>
     *   <li>文件管理 - 用于临时文件的命名和组织</li>
     *   <li>调试信息 - 在开发过程中提供有用的上下文信息</li>
     * </ul>
     * </p>
     * <p>
     * 返回值应该是有意义的文件名，建议包含文件扩展名，如"contract_template.docx"。
     * 如果实体类没有特定的文件名，可以返回一个描述性的名称。
     * </p>
     *
     * @return 文件名称字符串，不能为null，建议包含文件扩展名
     */
    String getFileName();

    /**
     * 获取转换宽度
     * <p>
     * 该方法返回占位符内容在转换过程中的宽度值，主要用于图片占位符的尺寸控制。
     * 宽度值通常以像素为单位，在转换为PDF等固定布局格式时特别重要。
     * </p>
     * <p>
     * 使用场景：
     * <ul>
     *   <li>图片占位符 - 控制插入图片的显示宽度</li>
     *   <li>表格占位符 - 控制动态生成表格的列宽</li>
     *   <li>布局控制 - 影响整体文档的页面布局</li>
     * </ul>
     * </p>
     * <p>
     * 建议根据实际内容需求设置合理的宽度值，避免内容显示异常或布局混乱。
     * </p>
     *
     * @return 转换宽度值，通常以像素为单位，必须大于0
     */
    int getConvertWidth();

    /**
     * 获取转换高度
     * <p>
     * 该方法返回占位符内容在转换过程中的高度值，主要用于图片占位符的尺寸控制。
     * 高度值通常以像素为单位，与宽度配合使用，确保内容的正确显示。
     * </p>
     * <p>
     * 使用场景：
     * <ul>
     *   <li>图片占位符 - 控制插入图片的显示高度</li>
     *   <li>表格占位符 - 控制动态生成表格的行高</li>
     *   <li>页面布局 - 影响文档的整体页面布局和分页</li>
     * </ul>
     * </p>
     * <p>
     * 高度值应该与宽度值保持合理的比例，确保内容的视觉效果良好。
     * </p>
     *
     * @return 转换高度值，通常以像素为单位，必须大于0
     */
    int getConvertHeight();

    /**
     * 获取占位符前缀
     * <p>
     * 该方法返回占位符的前缀字符串，用于在模板文档中识别占位符的开始位置。
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
     * 前缀应该选择在文档内容中不常见的字符组合，避免与正常文本冲突。
     * 常用的前缀包括"${"、"{{"、"<<"等。
     * </p>
     *
     * @return 占位符前缀字符串，不能为null，建议使用不常见的字符组合
     */
    String getPrefix();

    /**
     * 获取占位符后缀
     * <p>
     * 该方法返回占位符的后缀字符串，用于在模板文档中识别占位符的结束位置。
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
     * 后缀应该与前缀形成匹配的字符对，如"${"和"}"、"<<"和">>"等。
     * 这种配对设计有助于系统正确识别和处理占位符。
     * </p>
     *
     * @return 占位符后缀字符串，不能为null，应该与前缀形成匹配的字符对
     */
    String getSuffix();
}
