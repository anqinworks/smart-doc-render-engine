package cc.anqin.doc.convert;

import cc.anqin.doc.utils.FileUtils;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Opt;
import com.aspose.words.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.OutputStream;

/**
 * 抽象文件转换器
 * <p>
 * 该抽象类实现了FileConverter接口的基本功能，为具体的文件转换器实现提供了通用基础。
 * 子类只需要实现特定的转换逻辑和支持的文件类型，而无需关心字体路径等通用配置。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>提供字体路径的默认实现 - 默认使用classpath中的fonts/truetype目录</li>
 *   <li>提供转换方法的基本框架 - 子类可以覆盖实现具体转换逻辑</li>
 * </ul>
 * </p>
 * <p>
 * 扩展示例：
 * <pre>
 * public class DocxToPdfConverter extends AbstractFileConverter {
 *     @Override
 *     public File convert(File inputFile, int width, int height) {
 *         // 实现DOCX到PDF的转换逻辑
 *     }
 *
 *     @Override
 *     public Set&lt;FileType&gt; getSupports() {
 *         return Collections.singleton(FileType.DOCX);
 *     }
 *
 *     @Override
 *     public FileType getTargetType() {
 *         return FileType.PDF;
 *     }
 * }
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2025/08/18
 * @see FileConverter 文件转换器接口
 */
@Setter
@Getter
@Slf4j
public abstract class AbstractFileConverter implements FileConverter {


    /** 字体路径 */
    private String fontsPath;

    /** 转换宽度 */
    private double convertWidth = 210;

    /** 转换高度 */
    private double convertHeight = 297;


    /**
     * 将输入文件转换为目标格式
     * <p>
     * 该方法提供了文件转换的基本实现框架。在抽象类中，此方法返回null，
     * 子类应该覆盖此方法以提供具体的文件格式转换逻辑。转换过程中可以
     * 指定输出文件的宽度和高度参数，通常以毫米为单位。
     * </p>
     *
     * @return 转换后的文件对象，如果转换失败则返回null
     */
    @Override
    public File convert(Document doc, DocumentFormat type) {
        File temporaryFile = FileUtils.getTemporaryFile("." + type.getExtension());
        return convert(temporaryFile, doc, convertWidth, convertHeight, type);
    }


    /**
     * 转换
     *
     * @param outputFile 输出文件
     * @param doc 医生
     * @param width 宽度
     * @param height 高度
     * @param type 类型
     * @return {@link File }
     */
    @Override
    public File convert(File outputFile, Document doc, double width, double height, DocumentFormat type) {

        try {
            doc.save(FileUtil.getOutputStream(outputFile), defaultSetting(doc, type, width, height));
            return outputFile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 默认设置
     *
     * @param doc 医生
     */
    protected SaveOptions defaultSetting(Document doc, DocumentFormat type, double convertWidth, double convertHeight) {
        SaveOptions options = type.createSaveOptions();
        if (options == null) {
            throw new IllegalArgumentException("未知的保存格式");
        }

        // 设置默认字体
        FontSettings fontSettings = new FontSettings();
        String fonts = getFontsPath();
        fontSettings.setFontsFolders(new String[]{fonts}, true);
        doc.setFontSettings(fontSettings);
        // 设置文档中每一节的页面宽高
        for (Section section : doc.getSections()) {
            PageSetup pageSetup = section.getPageSetup();
            pageSetup.setPageWidth(convertMmToPoints(convertWidth));
            pageSetup.setPageHeight(convertMmToPoints(convertHeight));
        }

        if (options instanceof PdfSaveOptions){
            PdfSaveOptions pdfSaveOptions = (PdfSaveOptions) options;
            pdfSaveOptions.setEmbedFullFonts(true);
        }

        options.setAllowEmbeddingPostScriptFonts(true);
        options.setSaveFormat(type.getValue());
        options.setUseHighQualityRendering(true);
        options.setPrettyFormat(true);
        return options;
    }



    /**
     * 保存
     *
     * @param doc 医生
     * @param outputStream 输出流
     * @param options 选项配置
     */
    protected void save(Document doc, OutputStream outputStream, SaveOptions options) {
        try {
            doc.save(outputStream, options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取用于文档转换的字体路径
     * <p>
     * 该方法返回用于文档转换过程中的字体资源路径。如果已设置自定义字体路径，
     * 则返回该路径；否则返回默认的字体路径（classpath中的fonts/truetype目录）。
     * 正确的字体路径对于确保文档中的文字正确显示至关重要。
     * </p>
     *
     * @return 字体资源的绝对路径
     */
    @Override
    public String getFontsPath() {
        return Opt.ofBlankAble(this.fontsPath)
                .orElse(new ClassPathResource("fonts/truetype").getAbsolutePath());
    }


    /**
     * 将毫米单位转换为点（points）单位
     * <p>
     * 在文档处理中，不同的单位系统经常需要相互转换。该方法提供了从毫米（mm）
     * 到点（points）的转换功能。1英寸等于25.4毫米，1英寸等于72点，
     * 因此转换公式为：points = mm * 72 / 25.4
     * </p>
     *
     * @param mm 需要转换的毫米值
     * @return 转换后的点值
     */
    protected static double convertMmToPoints(double mm) {
        return mm * 72 / 25.4;
    }
}
