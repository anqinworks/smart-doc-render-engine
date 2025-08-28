package cc.anqin.doc.convert;

import com.aspose.words.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.aspose.words.SaveOptions;
import com.aspose.words.SaveFormat;
import com.aspose.words.*;

/**
 * 文档格式枚举
 * <p>
 * 该枚举定义了Smart Doc Render Engine支持的所有文档格式，包括输入格式和输出格式。
 * 每个格式都包含唯一的整数值、文件扩展名和描述信息，并提供相应的SaveOptions创建方法。
 * 该枚举是文件转换系统的核心，为各种格式间的转换提供了统一的接口。
 * </p>
 * <p>
 * 支持的格式分类：
 * <ul>
 *   <li><strong>Microsoft Word格式</strong> - DOC、DOCX、DOT、DOTX等，支持度最高</li>
 *   <li><strong>固定布局格式</strong> - PDF、XPS、SVG等，适合打印和精确布局</li>
 *   <li><strong>流式布局格式</strong> - HTML、EPUB、Markdown等，适合网页和电子书</li>
 *   <li><strong>图像格式</strong> - PNG、JPEG、TIFF等，适合图片导出</li>
 *   <li><strong>OpenDocument格式</strong> - ODT、OTT等，开源文档格式</li>
 *   <li><strong>纯文本格式</strong> - TXT、Markdown等，适合纯文本内容</li>
 * </ul>
 * </p>
 * <p>
 *
 * <html lang="zh-CN"><body> <div class="container"> <div class="content"> <h2>一、支持的输入格式（可读取）</h2> <div class="table-container"> <table> <thead> <tr> <th>格式类型</th> <th>具体格式</th> <th>支持程度</th> </tr> </thead> <tbody> <tr> <td class="format-type">Word 文档</td> <td class="format-list">DOC, DOCX, DOT, DOTX, DOTM, DOCM</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">富文本格式</td> <td class="format-list">RTF</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">网页格式</td> <td class="format-list">HTML, XHTML, MHTML</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">OpenDocument</td> <td class="format-list">ODT, OTT</td> <td class="rating">⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">文本格式</td> <td class="format-list">TXT</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">电子书</td> <td class="format-list">EPUB, FictionBook (FB2)</td> <td class="rating">⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">XML 格式</td> <td class="format-list">WordML, Office MathML</td> <td class="rating">⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">其他格式</td> <td class="format-list">PDF, XPS, PostScript (PS)</td> <td class="rating">⭐⭐⭐⭐</td> </tr> </tbody> </table> </div> <h2>二、支持的输出格式（可转换至）</h2> <div class="table-container"> <table> <thead> <tr> <th>格式类型</th> <th>具体格式</th> <th>支持程度</th> </tr> </thead> <tbody> <tr> <td class="format-type">Word 文档</td> <td class="format-list">DOC, DOCX, DOT, DOTX, DOTM, DOCM</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">固定布局</td> <td class="format-list">PDF, PDF/A, XPS</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">网页格式</td> <td class="format-list">HTML, XHTML, MHTML</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">图像格式</td> <td class="format-list">JPEG, PNG, BMP, TIFF, SVG, EMF</td> <td class="rating">⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">文本格式</td> <td class="format-list">TXT</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">OpenDocument</td> <td class="format-list">ODT</td> <td class="rating">⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">电子书</td> <td class="format-list">EPUB</td> <td class="rating">⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">其他格式</td> <td class="format-list">XML, RTF, Markdown (MD)</td> <td class="rating">⭐⭐⭐⭐</td> </tr> </tbody> </table> </div> </div> </div></body></html>
 * 使用示例：
 * <pre>
 * // 获取PDF格式
 * DocumentFormat pdfFormat = DocumentFormat.PDF;
 * 
 * // 检查是否为Word格式
 * boolean isWord = pdfFormat.isWordFormat(); // false
 * 
 * // 获取文件扩展名
 * String ext = pdfFormat.getExtensionWithDot(); // ".pdf"
 * 
 * // 创建保存选项
 * SaveOptions options = pdfFormat.createSaveOptions();
 * 
 * // 根据扩展名查找格式
 * DocumentFormat format = DocumentFormat.fromExtension("docx");
 * </pre>
 * </p>
 *
 *
 *
 * @author Mr.An
 * @date 2024/12/25
 * @see SaveOptions Aspose保存选项
 * @see SaveFormat Aspose保存格式
 * @see CF 文件转换工具类
 * @see AbstractFileConverter 抽象文件转换器
 */
@AllArgsConstructor
@Getter
public enum DocumentFormat {

    // Microsoft Word 格式
    /**
     * Word 97-2003 文档格式
     * <p>
     * 这是Microsoft Word的早期二进制格式，兼容性最好，但文件较大。
     * 适用于需要与旧版本Word兼容的场景。
     * </p>
     */
    DOC(10, "doc", "Word 97-2003 Document"),

    /**
     * Word 97-2003 模板格式
     * <p>
     * Word模板文件，包含样式、宏和内容占位符。
     * 用于创建具有统一格式的文档。
     * </p>
     */
    DOT(11, "dot", "Word 97-2003 Template"),

    /**
     * Word 2007+ 文档格式
     * <p>
     * 现代Word文档格式，基于XML，文件较小，功能丰富。
     * 这是当前最常用的Word文档格式。
     * </p>
     */
    DOCX(20, "docx", "Word Document"),

    /**
     * 启用宏的Word文档
     * <p>
     * 包含VBA宏的Word文档，可以执行自动化操作。
     * 注意：某些环境可能出于安全考虑禁用宏。
     * </p>
     */
    DOCM(21, "docm", "Word Macro-Enabled Document"),

    /**
     * Word 2007+ 模板格式
     * <p>
     * 现代Word模板格式，支持丰富的样式和功能。
     * 用于创建具有专业外观的文档模板。
     * </p>
     */
    DOTX(22, "dotx", "Word Template"),

    /**
     * 启用宏的Word模板
     * <p>
     * 包含VBA宏的Word模板，可以执行自动化操作。
     * 适用于需要宏功能的模板场景。
     * </p>
     */
    DOTM(23, "dotm", "Word Macro-Enabled Template"),

    // Flat OPC 格式
    /**
     * Flat OPC 文档格式
     * <p>
     * Office Open XML的平面表示形式，便于程序处理。
     * 适用于需要直接操作XML内容的场景。
     * </p>
     */
    FLAT_OPC(24, "xml", "Flat OPC Document"),

    /**
     * 启用宏的Flat OPC文档
     * <p>
     * 包含宏的Flat OPC格式文档。
     * 适用于需要宏功能的XML处理场景。
     * </p>
     */
    FLAT_OPC_MACRO_ENABLED(25, "xml", "Flat OPC Macro-Enabled Document"),

    /**
     * Flat OPC 模板格式
     * <p>
     * Flat OPC格式的模板文件。
     * 适用于基于XML的模板系统。
     * </p>
     */
    FLAT_OPC_TEMPLATE(26, "xml", "Flat OPC Template"),

    /**
     * 启用宏的Flat OPC模板
     * <p>
     * 包含宏的Flat OPC格式模板。
     * 适用于需要宏功能的XML模板系统。
     * </p>
     */
    FLAT_OPC_TEMPLATE_MACRO_ENABLED(27, "xml", "Flat OPC Macro-Enabled Template"),

    // 其他文本格式
    /**
     * 富文本格式
     * <p>
     * 跨平台的富文本格式，支持基本的文本格式化。
     * 适用于需要在不同系统间共享格式化文本的场景。
     * </p>
     */
    RTF(30, "rtf", "Rich Text Format") {
        @Override
        public SaveOptions createSaveOptions() {
            return new RtfSaveOptions();
        }
    },

    /**
     * WordprocessingML 格式
     * <p>
     * 基于XML的Word文档格式，是DOCX的内部表示形式。
     * 适用于需要直接处理XML内容的场景。
     * </p>
     */
    WORD_ML(31, "xml", "WordprocessingML"),

    // 固定布局格式（打印格式）
    /**
     * 便携式文档格式
     * <p>
     * Adobe开发的跨平台文档格式，保持原始布局和格式。
     * 这是最常用的文档分发和打印格式。
     * </p>
     */
    PDF(40, "pdf", "Portable Document Format") {
        @Override
        public SaveOptions createSaveOptions() {
            return new PdfSaveOptions();
        }
    },

    /**
     * XML纸张规范格式
     * <p>
     * Microsoft开发的固定布局文档格式，类似PDF。
     * 适用于Windows环境下的文档分发。
     * </p>
     */
    XPS(41, "xps", "XML Paper Specification") {
        @Override
        public SaveOptions createSaveOptions() {
            return new XpsSaveOptions();
        }
    },

    /**
     * XAML 固定文档格式
     * <p>
     * 基于XAML的固定布局文档格式。
     * 适用于WPF应用程序的文档输出。
     * </p>
     */
    XAML_FIXED(42, "xaml", "XAML Fixed Document"),

    /**
     * 可缩放矢量图形格式
     * <p>
     * 基于XML的矢量图形格式，支持无损缩放。
     * 适用于需要高质量图形输出的场景。
     * </p>
     */
    SVG(44, "svg", "Scalable Vector Graphics") {
        @Override
        public SaveOptions createSaveOptions() {
            return new SvgSaveOptions();
        }
    },

    /**
     * 固定HTML格式
     * <p>
     * 固定布局的HTML格式，保持精确的页面布局。
     * 适用于需要精确布局的网页内容。
     * </p>
     */
    HTML_FIXED(45, "html", "Fixed HTML"),

    /**
     * 开放XML纸张规范格式
     * <p>
     * 开放标准的固定布局文档格式。
     * 适用于跨平台的文档分发。
     * </p>
     */
    OPEN_XPS(46, "oxps", "Open XML Paper Specification"),

    /**
     * PostScript格式
     * <p>
     * Adobe开发的页面描述语言，广泛用于印刷行业。
     * 适用于专业印刷和出版场景。
     * </p>
     */
    PS(47, "ps", "PostScript"),

    /**
     * 打印机命令语言格式
     * <p>
     * 惠普开发的打印机控制语言。
     * 适用于需要精确控制打印输出的场景。
     * </p>
     */
    PCL(48, "pcl", "Printer Command Language"),

    // 流式布局格式
    /**
     * 超文本标记语言格式
     * <p>
     * 标准的网页标记语言，支持流式布局。
     * 适用于网页发布和在线文档。
     * </p>
     */
    HTML(50, "html", "HyperText Markup Language") {
        @Override
        public SaveOptions createSaveOptions() {
            return new HtmlSaveOptions();
        }
    },

    /**
     * MIME HTML格式
     * <p>
     * 将HTML和相关资源打包为单个文件的格式。
     * 适用于需要完整网页内容的场景。
     * </p>
     */
    MHTML(51, "mhtml", "MIME HTML") {
        @Override
        public SaveOptions createSaveOptions() {
            return new HtmlSaveOptions();
        }
    },

    /**
     * 电子出版物格式
     * <p>
     * 开放标准的电子书格式，支持流式布局。
     * 适用于电子书和数字出版。
     * </p>
     */
    EPUB(52, "epub", "Electronic Publication"),

    // OpenDocument 格式
    /**
     * OpenDocument 文本格式
     * <p>
     * 开放标准的文档格式，由OASIS组织制定。
     * 适用于开源办公套件和跨平台文档交换。
     * </p>
     */
    ODT(60, "odt", "OpenDocument Text") {
        @Override
        public SaveOptions createSaveOptions() {
            return new OdtSaveOptions();
        }
    },

    /**
     * OpenDocument 文本模板格式
     * <p>
     * OpenDocument格式的模板文件。
     * 适用于基于开放标准的模板系统。
     * </p>
     */
    OTT(61, "ott", "OpenDocument Text Template"),

    // 纯文本格式
    /**
     * 纯文本格式
     * <p>
     * 最基本的文本格式，不包含任何格式化信息。
     * 适用于需要纯文本内容的场景。
     * </p>
     */
    TEXT(70, "txt", "Plain Text") {
        @Override
        public SaveOptions createSaveOptions() {
            return new TxtSaveOptions();
        }
    },

    /**
     * XAML 流式文档格式
     * <p>
     * 基于XAML的流式布局文档格式。
     * 适用于WPF应用程序的流式文档输出。
     * </p>
     */
    XAML_FLOW(71, "xaml", "XAML Flow Document"),

    /**
     * XAML 流式文档包格式
     * <p>
     * 包含多个XAML流式文档的包格式。
     * 适用于复杂的流式文档结构。
     * </p>
     */
    XAML_FLOW_PACK(72, "xaml", "XAML Flow Pack Document"),

    /**
     * Markdown 格式
     * <p>
     * 轻量级标记语言，易于阅读和编写。
     * 适用于技术文档和博客内容。
     * </p>
     */
    MARKDOWN(73, "md", "Markdown") {
        @Override
        public SaveOptions createSaveOptions() {
            return new MarkdownSaveOptions();
        }
    },

    // 图像格式
    /**
     * 标签图像文件格式
     * <p>
     * 高质量的无损图像格式，广泛用于印刷行业。
     * 适用于需要高质量图像输出的场景。
     * </p>
     */
    TIFF(100, "tiff", "Tagged Image File Format") {
        @Override
        public SaveOptions createSaveOptions() {
            return new ImageSaveOptions(SaveFormat.TIFF);
        }
    },

    /**
     * 便携式网络图形格式
     * <p>
     * 支持透明度的无损图像格式，适合网页使用。
     * 适用于需要透明背景的图像场景。
     * </p>
     */
    PNG(101, "png", "Portable Network Graphics") {
        @Override
        public SaveOptions createSaveOptions() {
            return new ImageSaveOptions(SaveFormat.PNG);
        }
    },

    /**
     * 位图格式
     * <p>
     * 最基本的图像格式，兼容性最好。
     * 适用于需要最大兼容性的图像场景。
     * </p>
     */
    BMP(102, "bmp", "Bitmap") {
        @Override
        public SaveOptions createSaveOptions() {
            return new ImageSaveOptions(SaveFormat.BMP);
        }
    },

    /**
     * 增强型图元文件格式
     * <p>
     * Windows系统的矢量图像格式，支持无损缩放。
     * 适用于Windows环境下的矢量图形。
     * </p>
     */
    EMF(103, "emf", "Enhanced Metafile") {
        @Override
        public SaveOptions createSaveOptions() {
            return new ImageSaveOptions(SaveFormat.EMF);
        }
    },

    /**
     * 联合图像专家组格式
     * <p>
     * 有损压缩的图像格式，文件较小。
     * 适用于需要小文件大小的图像场景。
     * </p>
     */
    JPEG(104, "jpg", "Joint Photographic Experts Group") {
        @Override
        public SaveOptions createSaveOptions() {
            return new ImageSaveOptions(SaveFormat.JPEG);
        }
    },

    /**
     * 图形交换格式
     * <p>
     * 支持动画的图像格式，文件较小。
     * 适用于简单的动画图像和图标。
     * </p>
     */
    GIF(105, "gif", "Graphics Interchange Format") {
        @Override
        public SaveOptions createSaveOptions() {
            return new ImageSaveOptions(SaveFormat.GIF);
        }
    };

    /**
     * 格式的唯一标识值
     * <p>
     * 该字段存储每个格式的唯一整数值，用于在系统中标识不同的文档格式。
     * 值的设计遵循一定的逻辑分组，便于理解和维护。
     * </p>
     * <p>
     * 值范围说明：
     * <ul>
     *   <li>10-29: Microsoft Word相关格式</li>
     *   <li>30-39: 其他文本格式</li>
     *   <li>40-49: 固定布局格式</li>
     *   <li>50-59: 流式布局格式</li>
     *   <li>60-69: OpenDocument格式</li>
     *   <li>70-79: 纯文本格式</li>
     *   <li>100+: 图像格式</li>
     * </ul>
     * </p>
     */
    private final int value;

    /**
     * 文件扩展名（不含点号）
     * <p>
     * 该字段存储文件的标准扩展名，不包含点号前缀。
     * 扩展名用于文件命名和类型识别。
     * </p>
     * <p>
     * 扩展名特点：
     * <ul>
     *   <li>小写格式 - 统一使用小写字母</li>
     *   <li>标准扩展名 - 使用业界认可的标准扩展名</li>
     *   <li>唯一性 - 每个格式都有独特的扩展名</li>
     * </ul>
     * </p>
     */
    private final String extension;

    /**
     * 格式的详细描述
     * <p>
     * 该字段提供格式的详细描述信息，帮助用户理解每种格式的特点和用途。
     * 描述使用英文，保持专业术语的准确性。
     * </p>
     * <p>
     * 描述内容：
     * <ul>
     *   <li>格式全称 - 提供格式的完整名称</li>
     *   <li>功能说明 - 简要说明格式的主要功能</li>
     *   <li>应用场景 - 暗示格式的适用场景</li>
     * </ul>
     * </p>
     */
    private final String description;

    /**
     * 获取带点号的完整文件扩展名
     * <p>
     * 该方法返回完整的文件扩展名，包含点号前缀。
     * 适用于文件命名和路径构建场景。
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * DocumentFormat pdf = DocumentFormat.PDF;
     * String ext = pdf.getExtensionWithDot(); // ".pdf"
     * 
     * // 构建文件名
     * String fileName = "document" + pdf.getExtensionWithDot(); // "document.pdf"
     * </pre>
     * </p>
     *
     * @return 带点号的完整文件扩展名，如".pdf"、".docx"等
     */
    public String getExtensionWithDot() {
        return "." + extension;
    }

    /**
     * 创建对应的 SaveOptions 实例
     * <p>
     * 该方法为特定的文档格式创建相应的SaveOptions实例，用于配置保存参数。
     * 默认实现返回null，表示使用Aspose的默认选项。
     * 特定格式可以重写此方法以提供自定义的保存选项。
     * </p>
     * <p>
     * 支持的SaveOptions类型：
     * <ul>
     *   <li>PdfSaveOptions - PDF格式的保存选项</li>
     *   <li>HtmlSaveOptions - HTML格式的保存选项</li>
     *   <li>RtfSaveOptions - RTF格式的保存选项</li>
     *   <li>ImageSaveOptions - 图像格式的保存选项</li>
     *   <li>OdtSaveOptions - ODT格式的保存选项</li>
     *   <li>TxtSaveOptions - 文本格式的保存选项</li>
     *   <li>MarkdownSaveOptions - Markdown格式的保存选项</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * DocumentFormat format = DocumentFormat.PDF;
     * SaveOptions options = format.createSaveOptions();
     * if (options instanceof PdfSaveOptions) {
     *     PdfSaveOptions pdfOptions = (PdfSaveOptions) options;
     *     pdfOptions.setEmbedFullFonts(true);
     * }
     * </pre>
     * </p>
     *
     * @return 对应格式的SaveOptions实例，如果没有特定选项则返回null
     */
    public SaveOptions createSaveOptions() {
        return null;
    }

    /**
     * 根据整数值获取枚举实例
     * <p>
     * 该静态方法通过整数值查找对应的DocumentFormat枚举实例。
     * 如果找不到匹配的值，会抛出IllegalArgumentException异常。
     * </p>
     * <p>
     * 使用场景：
     * <ul>
     *   <li>从数据库或配置文件读取格式值</li>
     *   <li>处理外部系统传递的格式标识</li>
     *   <li>格式值的反向查找</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * try {
     *     DocumentFormat format = DocumentFormat.fromValue(40);
     *     System.out.println(format); // "PDF (pdf) - Portable Document Format"
     * } catch (IllegalArgumentException e) {
     *     System.out.println("无效的格式值");
     * }
     * </pre>
     * </p>
     *
     * @param value 要查找的格式整数值
     * @return 对应的DocumentFormat枚举实例
     * @throws IllegalArgumentException 如果找不到匹配的格式值
     */
    public static DocumentFormat fromValue(int value) {
        for (DocumentFormat format : values()) {
            if (format.value == value) {
                return format;
            }
        }
        throw new IllegalArgumentException("未知的文档格式值: " + value);
    }

    /**
     * 根据文件扩展名获取枚举实例（不带点号）
     * <p>
     * 该静态方法通过文件扩展名查找对应的DocumentFormat枚举实例。
     * 扩展名不区分大小写，会自动转换为小写进行比较。
     * 如果找不到匹配的扩展名，会抛出IllegalArgumentException异常。
     * </p>
     * <p>
     * 扩展名处理：
     * <ul>
     *   <li>自动去除点号 - 如果扩展名包含点号会自动去除</li>
     *   <li>大小写不敏感 - 自动转换为小写进行比较</li>
     *   <li>精确匹配 - 必须完全匹配扩展名</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * try {
     *     DocumentFormat format = DocumentFormat.fromExtension("docx");
     *     System.out.println(format); // "DOCX (docx) - Word Document"
     *     
     *     // 带点号的扩展名也可以
     *     DocumentFormat pdf = DocumentFormat.fromExtension(".pdf");
     *     System.out.println(pdf); // "PDF (pdf) - Portable Document Format"
     * } catch (IllegalArgumentException e) {
     *     System.out.println("不支持的扩展名");
     * }
     * </pre>
     * </p>
     *
     * @param extension 文件扩展名，可以带或不带点号
     * @return 对应的DocumentFormat枚举实例
     * @throws IllegalArgumentException 如果找不到匹配的扩展名
     */
    public static DocumentFormat fromExtension(String extension) {
        String ext = extension.toLowerCase().replace(".", "");
        for (DocumentFormat format : values()) {
            if (format.extension.equalsIgnoreCase(ext)) {
                return format;
            }
        }
        throw new IllegalArgumentException("未知的文件扩展名: " + extension);
    }

    /**
     * 检查是否为Word格式
     * <p>
     * 该方法检查当前格式是否属于Microsoft Word文档格式。
     * Word格式包括DOC、DOT、DOCX、DOCM、DOTX、DOTM等。
     * </p>
     * <p>
     * 判断逻辑：
     * <ul>
     *   <li>DOC - Word 97-2003文档</li>
     *   <li>DOT - Word 97-2003模板</li>
     *   <li>DOCX - Word 2007+文档</li>
     *   <li>DOCM - 启用宏的Word文档</li>
     *   <li>DOTX - Word 2007+模板</li>
     *   <li>DOTM - 启用宏的Word模板</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * DocumentFormat docx = DocumentFormat.DOCX;
     * boolean isWord = docx.isWordFormat(); // true
     * 
     * DocumentFormat pdf = DocumentFormat.PDF;
     * boolean isWord = pdf.isWordFormat(); // false
     * </pre>
     * </p>
     *
     * @return 如果是Word格式返回true，否则返回false
     */
    public boolean isWordFormat() {
        return this == DOC || this == DOT || this == DOCX || this == DOCM ||
               this == DOTX || this == DOTM;
    }

    /**
     * 检查是否为图像格式
     * <p>
     * 该方法检查当前格式是否属于图像格式。
     * 图像格式包括TIFF、PNG、BMP、EMF、JPEG、GIF等。
     * </p>
     * <p>
     * 判断逻辑：
     * <ul>
     *   <li>TIFF - 标签图像文件格式</li>
     *   <li>PNG - 便携式网络图形</li>
     *   <li>BMP - 位图格式</li>
     *   <li>EMF - 增强型图元文件</li>
     *   <li>JPEG - 联合图像专家组格式</li>
     *   <li>GIF - 图形交换格式</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * DocumentFormat png = DocumentFormat.PNG;
     * boolean isImage = png.isImageFormat(); // true
     * 
     * DocumentFormat docx = DocumentFormat.DOCX;
     * boolean isImage = docx.isImageFormat(); // false
     * </pre>
     * </p>
     *
     * @return 如果是图像格式返回true，否则返回false
     */
    public boolean isImageFormat() {
        return this == TIFF || this == PNG || this == BMP ||
               this == EMF || this == JPEG || this == GIF;
    }

    /**
     * 检查是否为固定布局格式
     * <p>
     * 该方法检查当前格式是否属于固定布局格式。
     * 固定布局格式保持精确的页面布局，适合打印和精确显示。
     * </p>
     * <p>
     * 判断逻辑：
     * <ul>
     *   <li>PDF - 便携式文档格式</li>
     *   <li>XPS - XML纸张规范</li>
     *   <li>XAML_FIXED - XAML固定文档</li>
     *   <li>SVG - 可缩放矢量图形</li>
     *   <li>HTML_FIXED - 固定HTML</li>
     *   <li>OPEN_XPS - 开放XML纸张规范</li>
     *   <li>PS - PostScript</li>
     *   <li>PCL - 打印机命令语言</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * DocumentFormat pdf = DocumentFormat.PDF;
     * boolean isFixed = pdf.isFixedLayoutFormat(); // true
     * 
     * DocumentFormat html = DocumentFormat.HTML;
     * boolean isFixed = html.isFixedLayoutFormat(); // false
     * </pre>
     * </p>
     *
     * @return 如果是固定布局格式返回true，否则返回false
     */
    public boolean isFixedLayoutFormat() {
        return this == PDF || this == XPS || this == XAML_FIXED ||
               this == SVG || this == HTML_FIXED || this == OPEN_XPS ||
               this == PS || this == PCL;
    }

    /**
     * 检查格式是否支持加密
     * <p>
     * 该方法检查当前格式是否支持文档加密功能。
     * 支持加密的格式可以设置密码保护，提高文档安全性。
     * </p>
     * <p>
     * 支持加密的格式：
     * <ul>
     *   <li>PDF - 支持密码保护和权限控制</li>
     *   <li>DOCX - 支持密码保护</li>
     *   <li>DOCM - 支持密码保护</li>
     *   <li>DOTX - 支持密码保护</li>
     *   <li>DOTM - 支持密码保护</li>
     *   <li>DOC - 支持密码保护</li>
     *   <li>DOT - 支持密码保护</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * DocumentFormat pdf = DocumentFormat.PDF;
     * boolean supportsEnc = pdf.supportsEncryption(); // true
     * 
     * DocumentFormat html = DocumentFormat.HTML;
     * boolean supportsEnc = html.supportsEncryption(); // false
     * </pre>
     * </p>
     *
     * @return 如果支持加密返回true，否则返回false
     */
    public boolean supportsEncryption() {
        return this == PDF || this == DOCX || this == DOCM ||
               this == DOTX || this == DOTM || this == DOC || this == DOT;
    }

    /**
     * 检查该格式是否有特定的 SaveOptions
     * <p>
     * 该方法检查当前格式是否有特定的SaveOptions实现。
     * 有特定SaveOptions的格式可以更精确地控制保存参数。
     * </p>
     * <p>
     * 有特定SaveOptions的格式：
     * <ul>
     *   <li>PDF - PdfSaveOptions</li>
     *   <li>HTML - HtmlSaveOptions</li>
     *   <li>RTF - RtfSaveOptions</li>
     *   <li>ODT - OdtSaveOptions</li>
     *   <li>TEXT - TxtSaveOptions</li>
     *   <li>MARKDOWN - MarkdownSaveOptions</li>
     *   <li>图像格式 - ImageSaveOptions</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * DocumentFormat pdf = DocumentFormat.PDF;
     * boolean hasOptions = pdf.hasSpecificSaveOptions(); // true
     * 
     * DocumentFormat docx = DocumentFormat.DOCX;
     * boolean hasOptions = docx.hasSpecificSaveOptions(); // false
     * </pre>
     * </p>
     *
     * @return 如果有特定的SaveOptions返回true，否则返回false
     */
    public boolean hasSpecificSaveOptions() {
        return createSaveOptions() != null;
    }

    /**
     * 获取所有支持的扩展名
     * <p>
     * 该静态方法返回系统中所有支持的文件扩展名列表。
     * 返回的扩展名不包含点号，并且去除了重复值。
     * </p>
     * <p>
     * 扩展名特点：
     * <ul>
     *   <li>去重处理 - 自动去除重复的扩展名</li>
     *   <li>无点号 - 返回的扩展名不包含点号前缀</li>
     *   <li>小写格式 - 统一使用小写字母</li>
     *   <li>排序 - 按照扩展名字母顺序排列</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * List&lt;String&gt; extensions = DocumentFormat.getAllExtensions();
     * System.out.println("支持的扩展名: " + extensions);
     * // 输出: [bmp, doc, docm, docx, dot, dotm, dotx, epub, gif, html, jpg, md, odt, ott, pdf, png, ps, rtf, svg, tiff, txt, xaml, xps, xml]
     * 
     * // 检查文件扩展名是否支持
     * String fileExt = "docx";
     * boolean supported = extensions.contains(fileExt.toLowerCase());
     * </pre>
     * </p>
     *
     * @return 所有支持的文件扩展名列表，不包含重复值和点号
     */
    public static List<String> getAllExtensions() {
        return Arrays.stream(values())
                .map(DocumentFormat::getExtension)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 显示特定格式的详细信息
     * <p>
     * 该方法在控制台输出当前格式的详细信息，包括名称、值、扩展名、描述等。
     * 主要用于调试和开发过程中的格式信息查看。
     * </p>
     * <p>
     * 输出信息包括：
     * <ul>
     *   <li>格式名称 - 枚举常量的名称</li>
     *   <li>格式值 - 对应的整数值</li>
     *   <li>文件扩展名 - 带点号的扩展名</li>
     *   <li>格式描述 - 详细的格式说明</li>
     *   <li>SaveOptions支持 - 是否有特定的保存选项</li>
     *   <li>格式类型 - 图像、Word文档、固定布局或流式布局</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * DocumentFormat pdf = DocumentFormat.PDF;
     * pdf.printFormatDetails();
     * 
     * // 输出示例:
     * // === 格式详细信息 ===
     * // 名称: PDF
     * // 值: 40
     * // 扩展名: .pdf
     * // 描述: Portable Document Format
     * // 是否有SaveOptions: true
     * // 类型: 固定布局
     * </pre>
     * </p>
     */
    public void printFormatDetails() {
        System.out.println("=== 格式详细信息 ===");
        System.out.println("名称: " + this.name());
        System.out.println("值: " + this.getValue());
        System.out.println("扩展名: " + this.getExtensionWithDot());
        System.out.println("描述: " + this.getDescription());
        System.out.println("是否有SaveOptions: " + this.hasSpecificSaveOptions());
        System.out.println("类型: " +
                           (this.isImageFormat() ? "图像" :
                                   this.isWordFormat() ? "Word文档" :
                                           this.isFixedLayoutFormat() ? "固定布局" : "流式布局"));
    }

    /**
     * 返回格式的字符串表示
     * <p>
     * 该方法重写了Object类的toString方法，返回格式的友好字符串表示。
     * 返回格式为"名称 (扩展名) - 描述"。
     * </p>
     * <p>
     * 字符串格式：
     * <ul>
     *   <li>名称 - 枚举常量的名称</li>
     *   <li>扩展名 - 文件扩展名（带括号）</li>
     *   <li>描述 - 格式的详细描述（带连字符）</li>
     * </ul>
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * DocumentFormat pdf = DocumentFormat.PDF;
     * String str = pdf.toString();
     * System.out.println(str); // "PDF (pdf) - Portable Document Format"
     * 
     * DocumentFormat docx = DocumentFormat.DOCX;
     * String str2 = docx.toString(); // "DOCX (docx) - Word Document"
     * </pre>
     * </p>
     *
     * @return 格式的友好字符串表示
     */
    @Override
    public String toString() {
        return String.format("%s (%s) - %s", name(), extension, description);
    }
}