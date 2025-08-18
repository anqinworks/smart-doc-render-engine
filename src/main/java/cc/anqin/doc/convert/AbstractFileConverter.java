package cc.anqin.doc.convert;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Opt;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * 抽象文件转换器
 *
 * @author Mr.An
 * @date 2025/08/18
 */
@Setter
@Getter
public class AbstractFileConverter implements FileConverter {


    /** 字体路径 */
    private String fontsPath;

    /**
     * 转换
     *
     * @param inputFile 输入文件
     * @param width     宽度
     * @param height    高度
     * @return {@link File }
     */
    @Override
    public File convert(File inputFile, int width, int height) throws Exception {
        return null;
    }

    /**
     * 是否支持文件
     *
     * @param fileType 文件类型
     * @return boolean
     */
    @Override
    public boolean supports(String fileType) {
        return false;
    }

    /**
     * 获取字体路径
     *
     * @return {@link String }
     */
    @Override
    public String getFontsPath() {
        return Opt.ofBlankAble(this.fontsPath)
                .orElse(new ClassPathResource("fonts/truetype").getAbsolutePath());
    }
}
