package cc.anqin.doc.word.placeholder;

import cc.anqin.doc.entity.AsposePlaceholder;
import cc.anqin.doc.ex.DocumentException;
import cc.anqin.processor.base.ConvertMap;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.aspose.words.Document;
import com.aspose.words.FindReplaceOptions;
import com.aspose.words.Range;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 抽象占位符填充服务
 *
 * @author Mr.An
 * @date 2025/08/18
 */
@Getter
@Setter
public abstract class AbstractPlaceholderFillerService implements PlaceholderFillerService {


    /**
     * 替换为空
     */
    protected static final String SPACE = StrUtil.TAB + StrUtil.TAB + StrUtil.TAB;


    /** 实体 */
    protected AsposePlaceholder entity;

    /** 数据映射，key 为占位符，value 为对应数据（List 或 String） */
    protected Map<String, Object> dataMap;

    /** 字段Set */
    protected Set<Field> fields;

    /** Aspose 文档对象 */
    protected Document doc;

    /**
     * 处理
     *
     * @param doc    doc
     * @param entity 数据
     */
    @Override
    public <T extends AsposePlaceholder>
    PlaceholderFillerService create(Document doc, T entity) {
        this.doc = Assert.notNull(doc, () -> new DocumentException("Document cannot be null"));
        this.entity = Assert.notNull(entity, () -> new DocumentException("Entity cannot be null"));
        this.fields = CollUtil.newHashSet(ReflectUtil.getFields(entity.getClass()));
        this.dataMap = ConvertMap.toMap(entity, entity.getClass());
        return this;
    }

    /**
     *  空的，用于覆盖变量
     *
     * @param doc 医生
     * @param fields 领域
     */
    @Override
    public void empty(Document doc, Field... fields) {
        for (Field field : fields) {
            String placeholderText = placeholderText(field.getName());
            Range range = doc.getRange();
            if (range.getText().contains(placeholderText)) {
                try {
                    range.replace(placeholderText, SPACE, new FindReplaceOptions());
                } catch (Exception e) {
                    throw new DocumentException(e);
                }
            }
        }
    }

    /**
     * 处理
     */
    @Override
    public abstract PlaceholderFillerService process();


    /**
     * 占位符文本
     *
     * @param text 文本
     * @return {@link String }
     */
    protected String placeholderText(String text) {
        return String.format("%s%s%s", entity.getPrefix(), text, entity.getSuffix());
    }


    /**
     * 获取字段
     *
     * @param entity 实体
     * @return {@link Set }<{@link Field }>
     */
    protected <T extends AsposePlaceholder> Set<Field> vGetFields(Document doc, T entity) {

        if (doc == null || entity == null) {
            return Collections.emptySet();
        }
        return CollUtil.newHashSet(ReflectUtil.getFields(entity.getClass()));
    }
}
