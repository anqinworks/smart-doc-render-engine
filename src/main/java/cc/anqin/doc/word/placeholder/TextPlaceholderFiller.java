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
 * 文本占位符填充器
 * <p>
 * 该类是AbstractPlaceholderFillerService的具体实现，专门用于处理文本类型的占位符填充。
 * 文本占位符是最基本的占位符类型，支持将字符串值或字符串列表填充到文档中的对应位置。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>单一文本替换 - 将单个字符串值替换到对应占位符位置</li>
 *   <li>列表文本处理 - 支持将字符串列表填充到文档中，适用于动态内容</li>
 *   <li>空值处理 - 对于空值的占位符进行适当处理</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 创建文本占位符填充器并绑定数据
 * PlaceholderFillerService filler = new TextPlaceholderFiller()
 *     .create(document, templateData);
 * 
 * // 执行文本占位符填充
 * filler.filler();
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2024/12/25
 * @see AbstractPlaceholderFillerService 抽象占位符填充服务
 * @see PlaceholderFillerService 占位符填充服务接口
 * @see PlaceholderType#TEXT 文本占位符类型
 */
public class TextPlaceholderFiller extends AbstractPlaceholderFillerService {


    /**
     * 处理文本占位符替换
     * <p>
     * 此方法实现了AbstractPlaceholderFillerService中的抽象方法，用于处理文档中的文本类型占位符。
     * 它会遍历所有标记为文本类型的字段，获取对应的数据，然后将数据填充到文档中的占位符位置。
     * 支持处理单个字符串值和字符串列表两种类型的数据。
     * </p>
     * 
     * @throws DocumentException 如果在占位符替换过程中发生错误
     */
    @Override
    @SuppressWarnings("unchecked")
    public void filler() {

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
    }

    /**
     * 处理多重拼接文本
     * <p>
     * 此方法用于处理需要特殊拼接格式的文本列表数据。它会根据提供的拼接配置，
     * 将多个列表的文本数据按照指定的格式拼接起来，然后替换文档中的占位符。
     * </p>
     *
     * @param splicing        拼接配置，定义了如何拼接多个文本列表
     * @param doc             要处理的Word文档对象
     * @param placeholderText 要替换的占位符文本
     * @param dataMultiple    包含多个文本列表的数据集合
     * @throws Exception 如果在文本拼接或替换过程中发生错误
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
     * 筛选支持的字段
     * <p>
     * 此方法实现了AbstractPlaceholderFillerService中的抽象方法，用于筛选出文本类型的占位符字段。
     * 它会检查字段是否有@Placeholder注解，如果没有注解或注解的值为TEXT类型，则认为该字段是文本类型。
     * 这意味着文本占位符是默认的占位符类型。
     * </p>
     *
     * @param fields 要筛选的字段数组
     * @return 支持的字段数组，只包含文本类型的占位符字段
     */
    @Override
    public Field[] supports(Field[] fields) {
        return Arrays.stream(fields).filter(f -> {
            Placeholder placeholder = f.getAnnotation(Placeholder.class);
            return placeholder == null || placeholder.value().equals(PlaceholderType.TEXT);
        }).toArray(Field[]::new);
    }
}
