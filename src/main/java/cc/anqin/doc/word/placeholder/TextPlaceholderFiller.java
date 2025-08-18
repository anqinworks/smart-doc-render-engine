package cc.anqin.doc.word.placeholder;

import cc.anqin.doc.ex.DocumentException;
import cc.anqin.doc.utils.DynamicListModifier;
import cc.anqin.doc.word.annotation.Placeholder;
import cc.anqin.doc.word.enums.PlaceholderType;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aspose.words.Document;
import com.aspose.words.FindReplaceOptions;
import com.aspose.words.Range;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 文本占位符填充物
 *
 * @author Mr.An
 * @date 2024/12/25
 */
public class TextPlaceholderFiller extends AbstractPlaceholderFillerService {


    /**
     * 处理文本占位符替换
     */
    @Override
    @SuppressWarnings("unchecked")
    public PlaceholderFillerService process() {

        for (Field field : fields) {
            String fieldName = field.getName();
            String placeholderText = placeholderText(fieldName);
            try {
                Range range = doc.getRange();
                Object defaultValue = dataMap.get(fieldName);
                if (defaultValue == null) {
//                    empty(range, placeholderText);
                    continue;
                }

                if (defaultValue instanceof String) {
                    if (range.getText().contains(placeholderText)) {
                        range.replace(placeholderText, (String) defaultValue, new FindReplaceOptions());
                    }
                    continue;
                }
                if (defaultValue instanceof List) {

                    List<?> multiple = (List<?>) defaultValue;

                    if (CollUtil.isEmpty(multiple)) {
                        continue;
                    }
                    Object generic = multiple.stream().filter(ObjectUtil::isNotEmpty).findFirst().orElse(null);
                    if (generic == null) {
                        continue;
                    }

                    Placeholder.MultipleSplicing splicing = field.getAnnotation(Placeholder.class).splicing();

                    List<List<String>> data = new ArrayList<>();
                    multiple.stream()
                            .filter(ObjectUtil::isNotEmpty)
                            .forEach(item -> {
                                if (item instanceof String) {
                                    if (CollUtil.isEmpty(data)) {
                                        data.add(new ArrayList<>());
                                    }
                                    data.get(0).add((String) item);
                                }
                                if (item instanceof List) {
                                    data.add((List<String>) item);
                                }
                            });
                    multipleSplicing(splicing, doc, placeholderText, data);
//                    continue;
                }
//                empty(range, placeholderText);
            } catch (Exception e) {
                throw new DocumentException("替换文本占位符失败：" + placeholderText + ExceptionUtil.stacktraceToString(e));
            }
        }
        return this;
    }

    /**
     * 多重拼接
     *
     * @param splicing        剪接
     * @param doc             doc
     * @param placeholderText 占位符文本
     * @param dataMultiple    数据倍数
     */
    private void multipleSplicing(Placeholder.MultipleSplicing splicing,
                                  Document doc,
                                  String placeholderText,
                                  List<List<String>> dataMultiple) throws Exception {
        Range range = doc.getRange();
        if (range.getText().contains(placeholderText)) {
            List<List<String>> data = DynamicListModifier.normalizeData(dataMultiple, Arrays.asList(splicing.value()));
            range.replace(placeholderText, CollUtil.join(data, ""), new FindReplaceOptions());
        }
    }


    /**
     * 支持的
     *
     * @param fields@return {@link Field[] }
     */
    @Override
    public Field[] supports(Field[] fields) {
        return Arrays.stream(fields).filter(f -> {
            Placeholder placeholder = f.getAnnotation(Placeholder.class);
            return placeholder == null || placeholder.value().equals(PlaceholderType.TEXT);
        }).toArray(Field[]::new);
    }
}
