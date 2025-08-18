package cc.anqin.doc.convert.strategy;

import cc.anqin.doc.convert.AbstractFileConverter;
import cc.anqin.doc.ex.DocumentException;
import cc.anqin.doc.utils.FileUtils;
import cn.hutool.core.io.resource.ClassPathResource;
import com.aspose.words.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * doc转换器
 *
 * @author Mr.An
 */
@Slf4j
public class DocConverter extends AbstractFileConverter {


    /**
     * 转换
     *
     * @param inputFile 输入文件
     * @param width     宽度
     * @param height    高度
     * @return {@link File }
     */
    @Override
    public File convert(File inputFile, int width, int height) {
        try {

            // 创建临时文件来存储生成的 PDF
            File outputFile = FileUtils.getTemporaryFile(".pdf");

            Document doc = new Document(inputFile.getAbsolutePath());

            FontSettings fontSettings = new FontSettings();

            String fonts = getFontsPath();

            log.info("设置的字体路径为：{}", fonts);
            fontSettings.setFontsFolders(new String[]{fonts}, true);

            // 应用字体设置到文档
            doc.setFontSettings(fontSettings);

            // 设置文档中每一节的页面宽高
            for (Section section : doc.getSections()) {
                PageSetup pageSetup = section.getPageSetup();
                pageSetup.setPageWidth(convertMmToPoints(width));
                pageSetup.setPageHeight(convertMmToPoints(height));
            }

            // 设置 PDF 转换选项
            PdfSaveOptions saveOptions = new PdfSaveOptions();
            saveOptions.setEmbedFullFonts(true);  // 确保字体嵌入 PDF

            // 保存为 PDF
            doc.save(outputFile.getAbsolutePath(), saveOptions);

            log.info("DOC converted to PDF: {}", outputFile.getAbsolutePath());

            return outputFile;
        } catch (Exception e) {
            throw new DocumentException(e, "Error during DOC to PDF conversion");
        }
    }

    /**
     * 将 mm 转换为点
     *
     * @param mm 毫米
     * @return double
     */
    private double convertMmToPoints(int mm) {
        return mm * 2.83464567; // 1 毫米 ≈ 2.83464567 点
    }


    /**
     * 是否支持文件
     *
     * @param fileType 文件类型
     * @return boolean
     */
    @Override
    public boolean supports(String fileType) {
        return "doc".equalsIgnoreCase(fileType) || "docx".equalsIgnoreCase(fileType);
    }
}
