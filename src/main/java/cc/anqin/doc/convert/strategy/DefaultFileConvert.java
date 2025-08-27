package cc.anqin.doc.convert.strategy;

import cc.anqin.doc.convert.AbstractFileConverter;
import cc.anqin.doc.convert.DocumentFormat;

import java.util.Collections;
import java.util.Set;

/**
 *
 * @author Mr.An
 * @since 2025/8/27
 */
public class DefaultFileConvert extends AbstractFileConverter {
    /**
     * 获取支持文件类型
     *
     * @return {@link Set }<{@link DocumentFormat }>
     */
    @Override
    public Set<DocumentFormat> getSupports() {
        return Collections.emptySet();
    }

    /**
     * 获取目标类型
     *
     * @return {@link DocumentFormat }
     */
    @Override
    public DocumentFormat getTargetType() {
        return null;
    }
}
