package cc.anqin.doc.entity;

/**
 * 模板接口
 * <p>
 * 该接口扩展了AsposePlaceholder接口，为文档模板提供了标准化的属性和行为。
 * 它定义了默认的模板尺寸（A4纸张）和占位符标识（${...}格式），简化了模板实现类的开发。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>标准化尺寸 - 提供A4纸张的标准尺寸（210mm x 297mm）</li>
 *   <li>统一占位符格式 - 定义统一的占位符前缀${和后缀}</li>
 *   <li>简化实现 - 通过默认方法实现减少子类的实现负担</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * public class MyTemplate implements TemplateInterface {
 *     private String fileName = "my_template.docx";
 *     
 *     @Override
 *     public String getFileName() {
 *         return fileName;
 *     }
 *     
 *     // 其他方法已通过默认实现提供
 * }
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2024/12/17
 * @see AsposePlaceholder 基础占位符接口
 * @see cc.anqin.doc.entity.Template 模板抽象类
 */
public interface TemplateInterface extends AsposePlaceholder {


    /**
     * A4 宽度（Width）：210 毫米（约 21 厘米）
     */
    @Override
    default int getConvertWidth() {
        return 210;
    }

    /**
     * A4 长度（Height）：297 毫米（约 29.7 厘米）
     *
     * @return int
     */
    @Override
    default int getConvertHeight() {
        return 297;
    }

    /**
     * 获取前缀
     *
     * @return {@link String }
     */
    @Override
    String getPrefix();

    /**
     * 获取后缀
     *
     * @return {@link String }
     */
    @Override
    String getSuffix();
}
