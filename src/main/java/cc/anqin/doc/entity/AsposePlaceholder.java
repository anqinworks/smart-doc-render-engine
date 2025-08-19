package cc.anqin.doc.entity;

/**
 * Aspose占位符接口
 * <p>
 * 该接口定义了文档模板中占位符的基本属性和行为。所有需要在文档中进行占位符替换的实体类
 * 都应该实现该接口，以提供占位符处理所需的基本信息。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>文件名称 - 获取与占位符关联的文件名</li>
 *   <li>尺寸控制 - 定义占位符内容（如图片）的宽度和高度</li>
 *   <li>占位符标识 - 提供占位符的前缀和后缀，用于在模板中识别占位符</li>
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
 *         return 800;
 *     }
 *     
 *     @Override
 *     public int getConvertHeight() {
 *         return 600;
 *     }
 *     
 *     @Override
 *     public String getPrefix() {
 *         return "${";
 *     }
 *     
 *     @Override
 *     public String getSuffix() {
 *         return "}";
 *     }
 *     
 *     // Getters and setters for name, address, etc.
 * }
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2024/12/25
 * @see cc.anqin.doc.word.placeholder.PlaceholderFillerService 占位符填充服务
 * @see cc.anqin.doc.word.annotation.Placeholder 占位符注解
 */
public interface AsposePlaceholder {

    /**
     * 获取文件名称
     *
     * @return {@link String }
     */
    String getFileName();

    /**
     * 获取转换宽度
     *
     * @return int
     */
    int getConvertWidth();

    /**
     * 获取转换高度
     *
     * @return int
     */
    int getConvertHeight();

    /**
     * 获取前缀
     *
     * @return {@link String }
     */
    String getPrefix();

    /**
     * 获取后缀
     *
     * @return {@link String }
     */
    String getSuffix();
}
