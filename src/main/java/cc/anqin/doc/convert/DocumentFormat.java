package cc.anqin.doc.convert;

import com.aspose.words.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.aspose.words.SaveOptions;
import com.aspose.words.SaveFormat;
import com.aspose.words.*;

/**
 * 文档格式枚举
 * 对应 Aspose.Words 的 SaveFormat 常量，包含 SaveOptions 创建方法
 */
@AllArgsConstructor
@Getter
public enum DocumentFormat {

    // Microsoft Word 格式
    DOC(10, "doc", "Word 97-2003 Document"),
    DOT(11, "dot", "Word 97-2003 Template"),
    DOCX(20, "docx", "Word Document"),
    DOCM(21, "docm", "Word Macro-Enabled Document"),
    DOTX(22, "dotx", "Word Template"),
    DOTM(23, "dotm", "Word Macro-Enabled Template"),

    // Flat OPC 格式
    FLAT_OPC(24, "xml", "Flat OPC Document"),
    FLAT_OPC_MACRO_ENABLED(25, "xml", "Flat OPC Macro-Enabled Document"),
    FLAT_OPC_TEMPLATE(26, "xml", "Flat OPC Template"),
    FLAT_OPC_TEMPLATE_MACRO_ENABLED(27, "xml", "Flat OPC Macro-Enabled Template"),

    // 其他文本格式
    RTF(30, "rtf", "Rich Text Format") {
        @Override
        public SaveOptions createSaveOptions() {
            return new RtfSaveOptions();
        }
    },
    WORD_ML(31, "xml", "WordprocessingML"),

    // 固定布局格式（打印格式）
    PDF(40, "pdf", "Portable Document Format") {
        @Override
        public SaveOptions createSaveOptions() {
            return new PdfSaveOptions();
        }
    },
    XPS(41, "xps", "XML Paper Specification") {
        @Override
        public SaveOptions createSaveOptions() {
            return new XpsSaveOptions();
        }
    },
    XAML_FIXED(42, "xaml", "XAML Fixed Document"),
    SVG(44, "svg", "Scalable Vector Graphics") {
        @Override
        public SaveOptions createSaveOptions() {
            return new SvgSaveOptions();
        }
    },
    HTML_FIXED(45, "html", "Fixed HTML"),
    OPEN_XPS(46, "oxps", "Open XML Paper Specification"),
    PS(47, "ps", "PostScript"),
    PCL(48, "pcl", "Printer Command Language"),

    // 流式布局格式
    HTML(50, "html", "HyperText Markup Language") {
        @Override
        public SaveOptions createSaveOptions() {
            return new HtmlSaveOptions();
        }
    },
    MHTML(51, "mhtml", "MIME HTML") {
        @Override
        public SaveOptions createSaveOptions() {
            return new HtmlSaveOptions();
        }
    },
    EPUB(52, "epub", "Electronic Publication"),

    // OpenDocument 格式
    ODT(60, "odt", "OpenDocument Text") {
        @Override
        public SaveOptions createSaveOptions() {
            return new OdtSaveOptions();
        }
    },
    OTT(61, "ott", "OpenDocument Text Template"),

    // 纯文本格式
    TEXT(70, "txt", "Plain Text") {
        @Override
        public SaveOptions createSaveOptions() {
            return new TxtSaveOptions();
        }
    },
    XAML_FLOW(71, "xaml", "XAML Flow Document"),
    XAML_FLOW_PACK(72, "xaml", "XAML Flow Pack Document"),
    MARKDOWN(73, "md", "Markdown") {
        @Override
        public SaveOptions createSaveOptions() {
            return new MarkdownSaveOptions();
        }
    },

    // 图像格式
    TIFF(100, "tiff", "Tagged Image File Format") {
        @Override
        public SaveOptions createSaveOptions() {
            return new ImageSaveOptions(SaveFormat.TIFF);
        }
    },
    PNG(101, "png", "Portable Network Graphics") {
        @Override
        public SaveOptions createSaveOptions() {
            return new ImageSaveOptions(SaveFormat.PNG);
        }
    },
    BMP(102, "bmp", "Bitmap") {
        @Override
        public SaveOptions createSaveOptions() {
            return new ImageSaveOptions(SaveFormat.BMP);
        }
    },
    EMF(103, "emf", "Enhanced Metafile") {
        @Override
        public SaveOptions createSaveOptions() {
            return new ImageSaveOptions(SaveFormat.EMF);
        }
    },
    JPEG(104, "jpg", "Joint Photographic Experts Group") {
        @Override
        public SaveOptions createSaveOptions() {
            return new ImageSaveOptions(SaveFormat.JPEG);
        }
    },
    GIF(105, "gif", "Graphics Interchange Format") {
        @Override
        public SaveOptions createSaveOptions() {
            return new ImageSaveOptions(SaveFormat.GIF);
        }
    };

    private final int value;
    private final String extension;
    private final String description;


    public String getExtensionWithDot() {
        return "." + extension;
    }

    /**
     * 创建对应的 SaveOptions 实例
     * 默认返回 null（使用 Aspose 的默认选项）
     * 特定格式重写此方法
     */
    public SaveOptions createSaveOptions() {
        return null;
    }

    /**
     * 根据整数值获取枚举实例
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
     * 根据文件扩展名获取枚举实例（不带点）
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
     */
    public boolean isWordFormat() {
        return this == DOC || this == DOT || this == DOCX || this == DOCM ||
               this == DOTX || this == DOTM;
    }

    /**
     * 检查是否为图像格式
     */
    public boolean isImageFormat() {
        return this == TIFF || this == PNG || this == BMP ||
               this == EMF || this == JPEG || this == GIF;
    }

    /**
     * 检查是否为固定布局格式
     */
    public boolean isFixedLayoutFormat() {
        return this == PDF || this == XPS || this == XAML_FIXED ||
               this == SVG || this == HTML_FIXED || this == OPEN_XPS ||
               this == PS || this == PCL;
    }

    /**
     * 检查格式是否支持加密
     */
    public boolean supportsEncryption() {
        return this == PDF || this == DOCX || this == DOCM ||
               this == DOTX || this == DOTM || this == DOC || this == DOT;
    }

    /**
     * 检查该格式是否有特定的 SaveOptions
     */
    public boolean hasSpecificSaveOptions() {
        return createSaveOptions() != null;
    }

    /**
     * 获取所有支持的扩展名
     */
    public static List<String> getAllExtensions() {
        return Arrays.stream(values())
                .map(DocumentFormat::getExtension)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 显示特定格式的详细信息
     */
    public static void printFormatDetails(DocumentFormat format) {
        System.out.println("=== 格式详细信息 ===");
        System.out.println("名称: " + format.name());
        System.out.println("值: " + format.getValue());
        System.out.println("扩展名: " + format.getExtensionWithDot());
        System.out.println("描述: " + format.getDescription());
        System.out.println("是否有SaveOptions: " + format.hasSpecificSaveOptions());
        System.out.println("类型: " +
                           (format.isImageFormat() ? "图像" :
                                   format.isWordFormat() ? "Word文档" :
                                           format.isFixedLayoutFormat() ? "固定布局" : "流式布局"));
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", name(), extension, description);
    }
}