package cc.anqin.doc.word.placeholder;

import cc.anqin.doc.entity.AsposePlaceholder;
import cn.hutool.core.util.StrUtil;
import com.aspose.words.Document;
import com.aspose.words.FindReplaceOptions;
import com.aspose.words.Range;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 占位符填充服务
 *
 * @author Mr.An
 * @date 2024/12/25
 */
public interface PlaceholderFillerService {


    /**
     * 创建
     *
     * @param doc     doc
     * @param entity 数据
     */
    <T extends AsposePlaceholder> PlaceholderFillerService create(Document doc, T entity);


    /**
     * 过程
     *
     * @return {@link PlaceholderFillerService }
     */
    <T extends AsposePlaceholder> PlaceholderFillerService process();


    /**
     * 支持的
     *
     * @param fields 字段
     * @return {@link Field[] }
     */
    Field[] supports(Field[] fields);


    /**
     * 空的，用于覆盖变量
     *
     * @param doc    doc
     * @param fields 字段Set
     */
    @SneakyThrows
    void empty(Document doc, Field... fields);
}
