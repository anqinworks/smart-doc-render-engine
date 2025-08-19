package cc.anqin.doc.convert.strategy;


import cc.anqin.doc.convert.AbstractFileConverter;
import cc.anqin.doc.convert.FileType;
import cc.anqin.doc.ex.DocumentException;
import cc.anqin.doc.utils.FileUtils;
import com.aspose.words.*;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.util.Collections;
import java.util.Set;

/**
 * html转换器
 *
 * @author Mr.An
 * @date 2024/11/29
 */
@Slf4j
public class HtmlConverter extends AbstractFileConverter {


    /**
     * 转换
     *
     * @param width     宽度
     * @param height    高度
     * @return {@link File }
     */
    @Override
    public File convert(File htmlFile, int width, int height) {
        try {
            // 创建临时文件来存储生成的 PDF
            File outputFile = FileUtils.getTemporaryFile(".pdf");

            // 加载 HTML 文件
            Document doc = new Document(htmlFile.getAbsolutePath());

            FontSettings fontSettings = new FontSettings();
            String fonts = getFontsPath();

            log.info("设置的字体路径为：{}", fonts);
            fontSettings.setFontsFolders(new String[]{fonts}, true);

            // 应用字体设置到文档
            doc.setFontSettings(fontSettings);

            // 设置文档中每一节的页面宽高（毫米转换为点）
            for (Section section : doc.getSections()) {
                PageSetup pageSetup = section.getPageSetup();
                pageSetup.setPageWidth(convertMmToPoints(width));
                pageSetup.setPageHeight(convertMmToPoints(height));
                pageSetup.setLeftMargin(convertMmToPoints(10));  // 10mm 左边距
                pageSetup.setRightMargin(convertMmToPoints(10)); // 10mm 右边距
                pageSetup.setTopMargin(convertMmToPoints(10));  // 10mm 上边距
                pageSetup.setBottomMargin(convertMmToPoints(10)); // 10mm 下边距
            }

            // 设置 PDF 转换选项
            PdfSaveOptions saveOptions = new PdfSaveOptions();
            saveOptions.setEmbedFullFonts(true);  // 确保字体嵌入 PDF
            saveOptions.setSaveFormat(SaveFormat.PDF);
            saveOptions.setPrettyFormat(true);

            // 保存为 PDF
            doc.save(outputFile.getAbsolutePath(), saveOptions);

            log.info("HTML converted to PDF: {}", outputFile.getAbsolutePath());

            return outputFile;
        } catch (Exception e) {
            throw new DocumentException(e, "Error during HTML to PDF conversion");
        }
    }

    /**
     * 获取支持文件类型
     *
     * @return {@link Set }<{@link FileType }>
     */
    @Override
    public Set<FileType> getSupports() {
        return Collections.singleton(FileType.HTML);
    }

    /**
     * 获取目标类型
     *
     * @return {@link FileType }
     */
    @Override
    public FileType getTargetType() {
        return FileType.PDF;
    }
}