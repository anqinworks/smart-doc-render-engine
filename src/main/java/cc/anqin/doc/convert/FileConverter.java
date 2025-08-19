package cc.anqin.doc.convert;

import cn.hutool.core.util.ObjUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.Set;

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
     * 获取支持文件类型
     *
     * @return {@link Set }<{@link FileType }>
     */
    Set<FileType> getSupports();


    /**
     * 获取目标类型
     *
     * @return {@link FileType }
     */
    FileType getTargetType();

    /**
     * 是否支持文件
     *
     * @param source 源文件类型
     * @param target 目标文件类型
     * @return boolean
     */
    default boolean supports(FileType source, FileType target) {
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
