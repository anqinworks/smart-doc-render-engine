package cc.anqin.doc.convert;

import cn.hutool.core.util.ObjUtil;
import com.aspose.words.Document;
import com.aspose.words.SaveOptions;

import java.io.File;
import java.util.Set;

/**
 * 文件转换器接口
 * <p>
 * 该接口定义了文件格式转换的核心功能，是Smart Doc Render Engine转换系统的基础。
 * 所有具体的文件转换器实现都需要实现该接口，以提供特定格式间的转换能力。
 * 该接口采用策略模式设计，允许系统根据不同的转换需求选择合适的转换器实现。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>文件格式转换 - 将一种格式的文件转换为另一种格式</li>
 *   <li>格式支持检查 - 判断是否支持特定格式间的转换</li>
 *   <li>字体路径配置 - 设置和获取字体资源路径，确保文档中字体正确显示</li>
 *   <li>转换参数控制 - 支持设置页面尺寸、质量等转换参数</li>
 *   <li>异常处理 - 提供统一的异常处理机制</li>
 * </ul>
 * </p>
 * <p>
 * </p>
 *
 * @author Mr.An
 * @date 2024/11/29
 * @see AbstractFileConverter 文件转换器抽象实现
 * @see DocumentFormat 文件类型枚举
 * @see CF 文件转换工具类
 * @see Document Aspose文档对象
 */
public interface FileConverter {

    /**
     * 将Aspose文档对象转换为指定格式
     * <p>
     * 该方法将Aspose Words的Document对象转换为指定的文件格式，使用默认的转换设置。
     * 转换过程中会应用字体设置、页面尺寸等配置，确保输出质量。
     * </p>
     *
     * @param doc 要转换的Aspose文档对象，不能为null
     * @param type 目标文件格式，使用DocumentFormat枚举
     * @return 转换后的文件对象，如果转换失败则返回null
     * @throws IllegalArgumentException 如果doc或type为null
     * @throws RuntimeException 如果转换过程中发生错误
     */
    File convert(Document doc, DocumentFormat type);


    /**
     * 将Aspose文档对象转换为指定格式
     * <p>
     * 该方法将Aspose Words的Document对象转换为指定的文件格式，使用默认的转换设置。
     * 转换过程中会应用字体设置、页面尺寸等配置，确保输出质量。
     * </p>
     *
     * @param doc 要转换的Aspose文档对象，不能为null
     * @param options 选项
     * @return 转换后的文件对象，如果转换失败则返回null
     *
     */
    default File convert(Document doc, SaveOptions options) {
        return convert(doc, DocumentFormat.fromSaveOptions(options));
    }


    /**
     * 将Aspose文档对象转换为指定格式（带自定义参数）
     * <p>
     * 该方法将Aspose Words的Document对象转换为指定的文件格式，允许用户自定义
     * 输出文件路径、页面尺寸等参数。转换过程会保持文档的原始布局和格式。
     * </p>
     *
     * @param outputFile 输出文件路径，指定转换后的文件保存位置
     * @param doc 要转换的Aspose文档对象，不能为null
     * @param width 页面宽度（毫米），用于设置输出文档的页面尺寸
     * @param height 页面高度（毫米），与width配合使用设置页面尺寸
     * @param type 目标文件格式，使用DocumentFormat枚举
     * @return 转换后的文件对象，通常是传入的outputFile参数
     * @throws IllegalArgumentException 如果任何参数为null或尺寸参数无效
     * @throws RuntimeException 如果转换过程中发生错误
     */
    File convert(File outputFile, Document doc, double width, double height, DocumentFormat type);

    /**
     * 获取当前转换器支持的文件类型集合
     * <p>
     * 该方法返回当前转换器能够处理的源文件类型集合。转换器只能处理集合中
     * 包含的文件类型，这确保了转换过程的安全性和可靠性。
     * </p>
     *
     * @return 支持的文件类型集合，不能为null，可以为空集合
     */
    Set<DocumentFormat> getSupports();

    /**
     * 获取当前转换器的目标文件类型
     * <p>
     * 该方法返回当前转换器能够输出的目标文件类型。每个转换器通常只支持
     * 一种目标格式，这简化了转换器的设计和实现。
     * </p>
     *
     * @return 目标文件类型，不能为null
     */
    DocumentFormat getTargetType();

    /**
     * 判断当前转换器是否支持指定的源文件类型到目标文件类型的转换
     * <p>
     * 该方法检查给定的源文件类型是否在当前转换器支持的文件类型列表中，
     * 并且目标文件类型是否与当前转换器的目标类型匹配。如果任一参数为null，
     * 则返回false表示不支持转换。
     * </p>
     * <p>
     * 该方法提供了默认实现，子类可以根据需要重写以提供更复杂的支持判断逻辑。
     * </p>
     *
     * @param source 源文件类型，要转换的原始文件格式
     * @param target 目标文件类型，转换后的目标文件格式
     * @return 如果支持转换返回true，否则返回false
     */
    default boolean supports(DocumentFormat source, DocumentFormat target) {
        if (ObjUtil.hasNull(source, target)) {
            return false;
        }
        return getSupports().contains(source) && getTargetType().equals(target);
    }

    /**
     * 设置字体路径
     * <p>
     * 该方法设置用于文档转换的字体资源目录路径。正确的字体路径对于确保
     * 文档中字体正确显示至关重要，特别是在转换为PDF等固定布局格式时。
     * </p>
     * <p>
     * 字体路径应该指向包含字体文件（如.ttf、.otf等）的目录，系统会递归
     * 搜索该目录下的所有字体文件。
     * </p>
     *
     * @param fontsPath 字体资源目录的绝对路径，不能为null
     * @throws IllegalArgumentException 如果fontsPath为null或空
     */
    void setFontsPath(String fontsPath);

    /**
     * 获取字体路径
     * <p>
     * 该方法返回当前转换器使用的字体资源目录路径。如果未设置自定义字体路径，
     * 则返回系统默认的字体路径。
     * </p>
     *
     * @return 字体资源目录的绝对路径，不能为null
     */
    String getFontsPath();
}
