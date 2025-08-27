package cc.anqin.doc.convert;

import cc.anqin.doc.utils.FileUtils;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

/**
 * 文件转换器接口
 * <p>
 * 该接口定义了文件格式转换的核心功能，是Smart Doc Render Engine转换系统的基础。
 * 所有具体的文件转换器实现都需要实现该接口，以提供特定格式间的转换能力。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>文件格式转换 - 将一种格式的文件转换为另一种格式</li>
 *   <li>格式支持检查 - 判断是否支持特定格式间的转换</li>
 *   <li>字体路径配置 - 设置和获取字体资源路径，确保文档中字体正确显示</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * FileConverter converter = ConverterFileFactory.getConverter(FileType.DOCX, FileType.PDF);
 * File pdfFile = converter.convert(docxFile, 210, 297);
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2024/11/29
 * @see AbstractFileConverter 文件转换器抽象实现
 * @see ConverterFileFactory 转换器工厂类
 * @see DocumentFormat 文件类型枚举
 * @see CF 文件转换工具类
 */
public interface FileConverter {



    File convert(Document doc, DocumentFormat type);


    File convert(File outputFile, Document doc, double width, double height, DocumentFormat type);


    /**
     * 获取支持文件类型
     *
     * @return {@link Set }<{@link DocumentFormat }>
     */
    Set<DocumentFormat> getSupports();


    /**
     * 获取目标类型
     *
     * @return {@link DocumentFormat }
     */
    DocumentFormat getTargetType();

    /**
     * 判断当前转换器是否支持指定的源文件类型到目标文件类型的转换
     * <p>
     * 该方法检查给定的源文件类型是否在当前转换器支持的文件类型列表中，
     * 并且目标文件类型是否与当前转换器的目标类型匹配。如果任一参数为null，
     * 则返回false表示不支持转换。
     * </p>
     *
     * @param source 源文件类型
     * @param target 目标文件类型
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
     *
     * @param fontsPath 字体路径
     */
    void setFontsPath(String fontsPath);


    /**
     * 获取字体路径
     *
     * @return {@link String }
     */
    String getFontsPath();
}
