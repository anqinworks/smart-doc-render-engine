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
 * 该类采用了模板方法模式，定义了文件转换的标准流程，子类可以覆盖特定步骤。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>提供字体路径的默认实现 - 默认使用classpath中的fonts/truetype目录</li>
 *   <li>提供转换方法的基本框架 - 子类可以覆盖实现具体转换逻辑</li>
 *   <li>统一的页面设置处理 - 自动设置页面尺寸和字体配置</li>
 *   <li>异常处理和日志记录 - 提供统一的错误处理机制</li>
 *   <li>单位转换工具 - 提供毫米到点的转换功能</li>
 * </ul>
 * </p>
 * <p>
 * 设计模式：
 * <ul>
 *   <li><strong>模板方法模式</strong> - 定义了转换的标准流程</li>
 *   <li><strong>策略模式</strong> - 通过不同的SaveOptions实现不同格式的转换</li>
 *   <li><strong>工厂模式</strong> - 通过DocumentFormat创建对应的SaveOptions</li>
 * </ul>
 * </p>
 * <p>
 * 扩展示例：
 * <pre>
 * public class DocxToPdfConverter extends AbstractFileConverter {
 *     &#064;Override
 *     public File convert(File outputFile, Document doc,
 *                          double width, double height, DocumentFormat type) {
 *         // 实现DOCX到PDF的转换逻辑
 *         return super.convert(outputFile, doc, width, height, type);
 *     }
 *
 *     &#064;Override
 *     public Set&lt;DocumentFormat&gt; getSupports() {
 *         return Collections.singleton(DocumentFormat.DOCX);
 *     }
 *
 *     &#064;Override
 *     public DocumentFormat getTargetType() {
 *         return DocumentFormat.PDF;
 *     }
 * }
 * </pre>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 创建自定义转换器
 * AbstractFileConverter converter = new CustomConverter();
 * converter.setConvertWidth(297);  // A4宽度
 * converter.setConvertHeight(420); // A4高度
 * converter.setFontsPath("/custom/fonts");
 *
 * // 执行转换
 * File result = converter.convert(document, DocumentFormat.PDF);
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2025/08/18
 * @see FileConverter 文件转换器接口
 * @see DocumentFormat 文档格式枚举
 * @see SaveOptions Aspose保存选项
 * @see Document Aspose文档对象
 */
@Setter
@Getter
@Slf4j
public abstract class AbstractFileConverter implements FileConverter {

    /**
     * 字体资源路径
     * <p>
     * 该字段存储用于文档转换的字体资源路径。如果未设置，将使用默认的classpath字体路径。
     * 字体路径对于确保文档中文字的正确显示至关重要，特别是包含中文字符的文档。
     * </p>
     * <p>
     * 字体路径特点：
     * <ul>
     *   <li>可配置性 - 支持运行时动态设置字体路径</li>
     *   <li>默认值 - 使用classpath中的fonts/truetype目录</li>
     *   <li>路径格式 - 支持绝对路径和相对路径</li>
     *   <li>字体类型 - 支持TrueType(.ttf)和OpenType(.otf)字体</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * converter.setFontsPath("/usr/share/fonts");
     * converter.setFontsPath("C:\\Windows\\Fonts");
     * converter.setFontsPath("fonts/custom");
     * </pre>
     * </p>
     */
    private String fontsPath;

    /**
     * 转换后的页面宽度（毫米）
     * <p>
     * 该字段定义转换后文档的页面宽度，以毫米为单位。默认值为210mm（A4纸宽度）。
     * 页面尺寸设置会影响文档的布局和打印效果。
     * </p>
     * <p>
     * 常用页面尺寸：
     * <ul>
     *   <li>A4 - 210 × 297 mm（默认）</li>
     *   <li>A3 - 297 × 420 mm</li>
     *   <li>A5 - 148 × 210 mm</li>
     *   <li>Letter - 216 × 279 mm</li>
     *   <li>Legal - 216 × 356 mm</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * converter.setConvertWidth(297);  // A3宽度
     * converter.setConvertHeight(420); // A3高度
     * </pre>
     * </p>
     */
    private double convertWidth = 210;

    /**
     * 转换后的页面高度（毫米）
     * <p>
     * 该字段定义转换后文档的页面高度，以毫米为单位。默认值为297mm（A4纸高度）。
     * 页面高度设置会影响文档的分页和内容布局。
     * </p>
     * <p>
     * 高度设置注意事项：
     * <ul>
     *   <li>最小高度 - 建议不小于50mm</li>
     *   <li>最大高度 - 建议不大于1000mm</li>
     *   <li>比例关系 - 宽度和高度应保持合理的比例</li>
     *   <li>打印兼容 - 考虑打印设备的纸张限制</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * // 设置为A3尺寸
     * converter.setConvertWidth(297);
     * converter.setConvertHeight(420);
     *
     * // 设置为自定义尺寸
     * converter.setConvertWidth(200);
     * converter.setConvertHeight(300);
     * </pre>
     * </p>
     */
    private double convertHeight = 297;

    /**
     * 将输入文件转换为目标格式
     * <p>
     * 该方法提供了文件转换的基本实现框架。在抽象类中，此方法创建临时文件并调用
     * 重载的convert方法。子类可以覆盖此方法以提供自定义的转换逻辑。
     * </p>
     * <p>
     * 转换流程：
     * <ul>
     *   <li>创建临时输出文件 - 使用目标格式的扩展名</li>
     *   <li>调用具体转换方法 - 使用当前设置的页面尺寸</li>
     *   <li>返回转换结果 - 成功返回文件对象，失败返回null</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * Document doc = new Document("input.docx");
     * File result = converter.convert(doc, DocumentFormat.PDF);
     * if (result != null) {
     *     System.out.println("转换成功: " + result.getAbsolutePath());
     * }
     * </pre>
     * </p>
     *
     * @param doc 要转换的Aspose文档对象
     * @param type 目标文档格式
     * @return 转换后的文件对象，如果转换失败则返回null
     * @see Document Aspose文档对象
     * @see DocumentFormat 文档格式枚举
     * @see FileUtils#getTemporaryFile(String) 临时文件工具
     */
    @Override
    public File convert(Document doc, DocumentFormat type) {
        File temporaryFile = FileUtils.getTemporaryFile("." + type.getExtension());
        return convert(temporaryFile, doc, convertWidth, convertHeight, type);
    }

    /**
     * 将文档转换为指定格式并保存到指定文件
     * <p>
     * 该方法实现了文档转换的核心逻辑，包括页面设置、字体配置和文件保存。
     * 子类可以覆盖此方法以提供自定义的转换逻辑，但建议调用父类方法以保持
     * 统一的配置处理。
     * </p>
     * <p>
     * 转换步骤：
     * <ul>
     *   <li>创建保存选项 - 根据目标格式创建相应的SaveOptions</li>
     *   <li>配置字体设置 - 设置字体路径和字体文件夹</li>
     *   <li>设置页面尺寸 - 为每个节设置页面宽度和高度</li>
     *   <li>应用特殊选项 - 根据格式类型应用特定的保存选项</li>
     *   <li>执行文件保存 - 将文档保存到指定的输出文件</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * Document doc = new Document("input.docx");
     * File outputFile = new File("output.pdf");
     * File result = converter.convert(outputFile, doc, 210, 297, DocumentFormat.PDF);
     * </pre>
     * </p>
     *
     * @param outputFile 输出文件对象，转换后的文档将保存到此文件
     * @param doc 要转换的Aspose文档对象
     * @param width 目标页面宽度（毫米）
     * @param height 目标页面高度（毫米）
     * @param type 目标文档格式
     * @return 转换后的文件对象，通常是传入的outputFile参数
     * @throws RuntimeException 如果转换过程中发生异常
     * @see Document Aspose文档对象
     * @see DocumentFormat 文档格式枚举
     * @see SaveOptions Aspose保存选项
     */
    @Override
    public File convert(File outputFile, Document doc, double width, double height, DocumentFormat type) {

        try {
            doc.save(FileUtil.getOutputStream(outputFile), defaultSetting(doc, type, width, height));
            return outputFile;
        } catch (Exception e) {
            log.error("文档转换失败: {}", e.getMessage(), e);
            throw new RuntimeException("文档转换失败: " + e.getMessage(), e);
        }
    }

    /**
     * 创建默认的保存选项配置
     * <p>
     * 该方法为指定的文档格式创建并配置SaveOptions实例。配置包括字体设置、页面尺寸、
     * 渲染质量等参数。子类可以覆盖此方法以提供自定义的配置逻辑。
     * </p>
     * <p>
     * 配置内容：
     * <ul>
     *   <li>格式特定选项 - 根据DocumentFormat创建对应的SaveOptions</li>
     *   <li>字体配置 - 设置字体路径和字体文件夹</li>
     *   <li>页面设置 - 为每个节设置页面宽度和高度</li>
     *   <li>PDF特殊选项 - 嵌入完整字体、高质量渲染等</li>
     *   <li>通用选项 - 允许嵌入PostScript字体、美化格式等</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * SaveOptions options = converter.defaultSetting(doc, DocumentFormat.PDF, 210, 297);
     * // 自定义选项配置
     * if (options instanceof PdfSaveOptions) {
     *     PdfSaveOptions pdfOptions = (PdfSaveOptions) options;
     *     pdfOptions.setEmbedFullFonts(true);
     *     pdfOptions.setCompliance(PdfCompliance.PDF_17);
     * }
     * </pre>
     * </p>
     *
     * @param doc 要转换的Aspose文档对象
     * @param type 目标文档格式
     * @param convertWidth 目标页面宽度（毫米）
     * @param convertHeight 目标页面高度（毫米）
     * @return 配置完成的SaveOptions实例
     * @throws IllegalArgumentException 如果目标格式不支持或SaveOptions创建失败
     * @see SaveOptions Aspose保存选项
     * @see DocumentFormat#createSaveOptions() 格式特定的保存选项
     * @see FontSettings 字体设置
     * @see PageSetup 页面设置
     */
    protected SaveOptions defaultSetting(Document doc, DocumentFormat type, double convertWidth, double convertHeight) {
        SaveOptions options = type.createSaveOptions();
        if (options == null) {
            throw new IllegalArgumentException("不支持的保存格式: " + type.name());
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

        // PDF格式特殊处理
        if (options instanceof PdfSaveOptions) {
            PdfSaveOptions pdfSaveOptions = (PdfSaveOptions) options;
            pdfSaveOptions.setEmbedFullFonts(true);
        }

        // 设置通用选项
        options.setAllowEmbeddingPostScriptFonts(true);
        options.setSaveFormat(type.getValue());
        options.setUseHighQualityRendering(true);
        options.setPrettyFormat(true);

        return options;
    }

    public static void main(String[] args) {
        SaveOptions options = new PdfSaveOptions();

        SaveOptions options1 = SaveOptions.createSaveOptions(DocumentFormat.PDF.getValue());

        System.out.println(options.getDefaultTemplate());
    }

    /**
     * 将文档保存到输出流
     * <p>
     * 该方法提供了将文档保存到输出流的通用实现。使用指定的SaveOptions配置
     * 执行保存操作，并提供统一的异常处理。
     * </p>
     * <p>
     * 保存特点：
     * <ul>
     *   <li>流式输出 - 支持内存流、网络流等不同类型的输出流</li>
     *   <li>配置灵活 - 使用SaveOptions控制保存参数</li>
     *   <li>异常处理 - 统一的异常处理和日志记录</li>
     *   <li>资源管理 - 自动处理输出流的关闭</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
     *     SaveOptions options = converter.defaultSetting(doc, DocumentFormat.PDF, 210, 297);
     *     converter.save(doc, outputStream, options);
     *     byte[] pdfData = outputStream.toByteArray();
     * }
     * </pre>
     * </p>
     *
     * @param doc 要保存的Aspose文档对象
     * @param outputStream 目标输出流
     * @param options 保存选项配置
     * @throws RuntimeException 如果保存过程中发生异常
     * @see Document Aspose文档对象
     * @see SaveOptions Aspose保存选项
     * @see OutputStream Java输出流
     */
    protected void save(Document doc, OutputStream outputStream, SaveOptions options) {
        try {
            doc.save(outputStream, options);
        } catch (Exception e) {
            log.error("文档保存失败: {}", e.getMessage(), e);
            throw new RuntimeException("文档保存失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取用于文档转换的字体路径
     * <p>
     * 该方法返回用于文档转换过程中的字体资源路径。如果已设置自定义字体路径，
     * 则返回该路径；否则返回默认的字体路径（classpath中的fonts/truetype目录）。
     * 正确的字体路径对于确保文档中的文字正确显示至关重要。
     * </p>
     * <p>
     * 字体路径优先级：
     * <ul>
     *   <li>自定义路径 - 通过setFontsPath()设置的路径</li>
     *   <li>默认路径 - classpath中的fonts/truetype目录</li>
     *   <li>系统字体 - 如果默认路径不存在，可能使用系统字体</li>
     * </ul>
     * </p>
     * <p>
     * 字体目录结构建议：
     * <pre>
     * fonts/
     * ├── truetype/
     * │   ├── simsun.ttc      # 宋体
     * │   ├── simhei.ttf      # 黑体
     * │   ├── simkai.ttf      # 楷体
     * │   └── simfang.ttf     # 仿宋
     * └── opentype/
     *     └── ...
     * </pre>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * String fontsPath = converter.getFontsPath();
     * System.out.println("当前字体路径: " + fontsPath);
     *
     * // 检查字体目录是否存在
     * File fontsDir = new File(fontsPath);
     * if (fontsDir.exists() && fontsDir.isDirectory()) {
     *     System.out.println("字体目录有效");
     * }
     * </pre>
     * </p>
     *
     * @return 字体资源的绝对路径
     * @see ClassPathResource Hutool类路径资源
     * @see Opt Hutool可选值工具
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
     * <p>
     * 单位转换关系：
     * <ul>
     *   <li>1英寸 = 25.4毫米</li>
     *   <li>1英寸 = 72点</li>
     *   <li>1毫米 ≈ 2.834645669点</li>
     *   <li>1点 ≈ 0.352777778毫米</li>
     * </ul>
     * </p>
     * <p>
     * 常用尺寸转换示例：
     * <pre>
     * double a4Width = convertMmToPoints(210);   // A4宽度: 595.2755905511811点
     * double a4Height = convertMmToPoints(297);  // A4高度: 841.8897637795276点
     * double a3Width = convertMmToPoints(297);  // A3宽度: 841.8897637795276点
     * double a3Height = convertMmToPoints(420); // A3高度: 1190.5511811023622点
     * </pre>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * // 设置A4页面尺寸
     * double widthPoints = convertMmToPoints(210);   // 595.28点
     * double heightPoints = convertMmToPoints(297);  // 841.89点
     *
     * // 设置自定义页面尺寸
     * double customWidth = convertMmToPoints(200);   // 566.93点
     * double customHeight = convertMmToPoints(300);  // 850.39点
     * </pre>
     * </p>
     *
     * @param mm 需要转换的毫米值
     * @return 转换后的点值
     * @see PageSetup#setPageWidth(double) 设置页面宽度
     * @see PageSetup#setPageHeight(double) 设置页面高度
     */
    protected static double convertMmToPoints(double mm) {
        return mm * 72 / 25.4;
    }
}
