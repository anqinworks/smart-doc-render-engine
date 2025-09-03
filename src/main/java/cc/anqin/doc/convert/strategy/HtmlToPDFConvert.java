package cc.anqin.doc.convert.strategy;

import cc.anqin.doc.convert.AbstractFileConverter;
import cc.anqin.doc.convert.DocumentFormat;
import cc.anqin.doc.utils.FileUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import io.woo.htmltopdf.PdfPageSize;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Set;

/**
 * HTML 转 PDF转换
 *
 * @author Mr.An
 * @date 2025/09/02
 */
@NoArgsConstructor
public class HtmlToPDFConvert extends AbstractFileConverter {


    @Override
    public File convert(File outputFile, File inputFile, double width, double height, DocumentFormat type) {

        boolean convert = HtmlToPdf.create()
                .pageSize(PdfPageSize.A4)
                .object(HtmlToPdfObject.forUrl(inputFile.getAbsolutePath()))
                .convert(outputFile.getAbsolutePath());

        Assert.isTrue(convert, "文档转换失败：" + inputFile.getAbsolutePath());

        return outputFile;
    }


    /**
     * 获取当前转换器支持的文件类型集合
     * <p>
     * 该方法返回当前转换器能够处理的源文件类型集合。转换器只能处理集合中
     * 包含的文件类型，这确保了转换过程的安全性和可靠性。
     * </p>
     *
     * @return 支持的文件类型集合，不能为null，可以为空集合
     */
    @Override
    public Set<DocumentFormat> getSupports() {
        return CollUtil.set(false, DocumentFormat.HTML, DocumentFormat.HTML_FIXED);
    }

    /**
     * 获取当前转换器的目标文件类型
     * <p>
     * 该方法返回当前转换器能够输出的目标文件类型。每个转换器通常只支持
     * 一种目标格式，这简化了转换器的设计和实现。
     * </p>
     *
     * @return 目标文件类型，不能为null
     */
    @Override
    public DocumentFormat getTargetType() {
        return DocumentFormat.PDF;
    }
}
