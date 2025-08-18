package cc.anqin.doc.convert;

import java.io.File;

/**
 * 文件转换器
 *
 * @author Mr.An
 * @date 2024/11/29
 */
public interface FileConverter {


    /**
     * 转换
     *
     * @param inputFile 输入文件
     * @param width     宽度
     * @param height    高度
     * @return {@link File }
     */
    File convert(File inputFile, int width, int height) throws Exception;


    /**
     * 是否支持文件
     *
     * @param fileType 文件类型
     * @return boolean
     */
    boolean supports(String fileType);


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
