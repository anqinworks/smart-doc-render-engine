package cc.anqin.doc.entity;

/**
 * 分配占位符
 *
 * @author Mr.An
 * @date 2024/12/25
 */
public interface AsposePlaceholder {

    /**
     * 获取文件名称
     *
     * @return {@link String }
     */
    String getFileName();

    /**
     * 获取转换宽度
     *
     * @return int
     */
    int getConvertWidth();

    /**
     * 获取转换高度
     *
     * @return int
     */
    int getConvertHeight();

    /**
     * 获取前缀
     *
     * @return {@link String }
     */
    String getPrefix();

    /**
     * 获取后缀
     *
     * @return {@link String }
     */
    String getSuffix();
}
