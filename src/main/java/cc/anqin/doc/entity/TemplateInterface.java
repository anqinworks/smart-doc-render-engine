package cc.anqin.doc.entity;

/**
 * 模板接口
 *
 * @author Mr.An
 * @date 2024/12/17
 */
public interface TemplateInterface extends AsposePlaceholder {


    /**
     * A4 宽度（Width）：210 毫米（约 21 厘米）
     */
    @Override
    default int getConvertWidth() {
        return 210;
    }

    /**
     * A4 长度（Height）：297 毫米（约 29.7 厘米）
     *
     * @return int
     */
    @Override
    default int getConvertHeight() {
        return 297;
    }

    /**
     * 获取前缀
     *
     * @return {@link String }
     */
    @Override
    default String getPrefix() {
        return "${";
    }

    /**
     * 获取后缀
     *
     * @return {@link String }
     */
    @Override
    default String getSuffix() {
        return "}";
    }
}
