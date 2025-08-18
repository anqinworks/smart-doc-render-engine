package cc.anqin.doc.convert.strategy;


import cc.anqin.doc.convert.AbstractFileConverter;
import cc.anqin.doc.utils.FileUtils;

import java.io.File;

/**
 * html转换器
 *
 * @author Mr.An
 * @date 2024/11/29
 */
public class HtmlConverter extends AbstractFileConverter {


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

        checkWkhtmltopdfInstallation();

        try {
            // 创建临时文件来存储生成的 PDF
            File file = FileUtils.getTemporaryFile(".pdf");

            // 构建 wkhtmltopdf 命令
            String command = String.format(
                    "wkhtmltopdf --page-width %dmm --page-height %dmm %s %s",
                    width, height, inputFile.getAbsolutePath(), file.getAbsolutePath()
            );

            // 执行命令
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            // 检查生成是否成功
            if (exitCode != 0) {
                throw new RuntimeException("wkhtmltopdf 命令执行失败，退出代码：" + exitCode);
            }

            // 返回生成的 PDF 文件对象
            return file;
        } catch (Exception e) {
            throw new RuntimeException("Error during Html to PDF conversion", e);
        }
    }

    /**
     * 检查 wkhtmltopdf 安装
     */
    public static void checkWkhtmltopdfInstallation() {
        try {
            Process process = Runtime.getRuntime().exec("wkhtmltopdf --version");
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("wkhtmltopdf 未安装或未在 PATH 中找到");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        checkWkhtmltopdfInstallation();
    }


    @Override
    public boolean supports(String fileType) {
        return "html".equalsIgnoreCase(fileType);
    }
}