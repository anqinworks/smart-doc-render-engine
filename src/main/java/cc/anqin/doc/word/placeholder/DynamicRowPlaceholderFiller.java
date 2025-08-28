package cc.anqin.doc.word.placeholder;

import cc.anqin.doc.ex.DocumentException;
import cc.anqin.doc.word.annotation.Placeholder;
import cc.anqin.doc.word.enums.PlaceholderType;
import cc.anqin.processor.base.ConvertMap;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.aspose.words.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 动态行占位符填充器
 * <p>
 * 该类是AbstractPlaceholderFillerService的具体实现，专门用于处理动态行类型的占位符填充。
 * 动态行占位符支持将列表数据动态填充到文档表格中，适用于需要根据数据量动态生成表格行的场景。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>动态表格生成 - 根据数据列表动态生成表格行</li>
 *   <li>复杂数据结构处理 - 支持处理嵌套的对象列表</li>
 *   <li>表格样式保持 - 在动态生成行时保持原表格的样式</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 创建动态行占位符填充器并绑定数据
 * PlaceholderFillerService filler = new DynamicRowPlaceholderFiller()
 *     .create(document, templateData);
 * 
 * // 执行动态行占位符填充
 * filler.filler();
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2024/12/25
 * @see AbstractPlaceholderFillerService 抽象占位符填充服务
 * @see PlaceholderFillerService 占位符填充服务接口
 * @see PlaceholderType#DYNAMIC_ROW 动态行占位符类型
 */
@Slf4j
public class DynamicRowPlaceholderFiller extends AbstractPlaceholderFillerService {


    /**
     * 处理空数据情况，用于覆盖文档中的变量占位符
     * <p>
     * 当没有实际数据需要填充时，此方法会使用空值覆盖文档中的动态行占位符，
     * 确保文档中不会显示原始的占位符文本。
     * </p>
     *
     * @param doc    文档对象，要处理的Word文档
     * @param fields 需要处理的字段数组，包含动态行占位符信息的字段
     * @throws DocumentException 如果在处理过程中发生错误
     */
    @Override
    public void empty(Document doc, Field... fields) {
        try {
            for (Field field : fields) {
                Map<String, Object> defaultCollMap = getFieldsFromList(field);
                dynamicTable(doc, Collections.singletonList(defaultCollMap), defaultCollMap.keySet());
            }
        } catch (Exception e) {
            throw new DocumentException(e);
        }
    }


    /**
     * 判断字段是否是 List 类型，并获取 List 中对象的所有字段
     * <p>
     * 此方法用于分析字段的泛型类型，如果字段是List类型，则获取List中元素类型的所有字段，
     * 并返回一个包含这些字段名称和空值的Map。这对于动态行处理非常重要，因为它提供了
     * 表格列的结构信息。
     * </p>
     *
     * @param field 要分析的字段对象
     * @return 包含字段名称和默认空值的Map，如果字段不是List类型或无法获取泛型信息则返回空Map
     */
    public static Map<String, Object> getFieldsFromList(Field field) {
        // 判断字段是否是 List 类型
        if (List.class.isAssignableFrom(field.getType())) {
            // 获取 List 的泛型类型
            if (field.getGenericType() instanceof ParameterizedType) {
                // 获取泛型类型参数
                Type[] actualTypeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                if (actualTypeArguments.length > 0) {
                    if (actualTypeArguments[0] instanceof Class<?>) {
                        // 获取 List 中元素的类型

                        // 获取该类型的所有字段
                        Field[] itemFields = ((Class<?>) actualTypeArguments[0]).getDeclaredFields();
                        Map<String, Object> fieldMap = new HashMap<>();

                        for (Field itemField : itemFields) {
                            fieldMap.put(itemField.getName(), SPACE);
                        }
                        return fieldMap;
                    }
                }
            }
        }
        return Collections.emptyMap();
    }

    /**
     * 填充文档中的动态行占位符
     * <p>
     * 此方法实现了AbstractPlaceholderFillerService中的抽象方法，用于处理文档中的动态行占位符。
     * 它会遍历所有标记为动态行类型的字段，获取对应的数据（必须是List类型），然后将数据填充到文档的表格中。
     * </p>
     * 
     * @throws DocumentException 如果字段值不是List类型或在处理过程中发生错误
     */
    @Override
    public void filler() {

        if (fieldsEmpty()) {
            return;
        }

        // 处理每个字段
        for (Field field : fields) {

            Object data = dataMap.get(field.getName());

            if (data == null) continue;

            if (data instanceof List) {
                try {
                    processDynamicTable(doc, (List<?>) data);
                } catch (Exception e) {
                    throw new DocumentException(ExceptionUtil.stacktraceToString(e));
                }
            } else {
                throw new DocumentException("Expected a List for field " + field.getName());
            }
        }
    }

    /**
     * 处理动态表格数据
     * <p>
     * 此方法将List类型的数据转换为Map列表，然后调用dynamicTable方法进行实际的表格填充。
     * 如果数据列表为空，则不进行任何操作。
     * </p>
     *
     * @param doc 要处理的Word文档对象
     * @param dataList 包含动态行数据的列表
     * @throws Exception 如果在数据转换或表格处理过程中发生错误
     */
    private void processDynamicTable(Document doc, List<?> dataList) throws Exception {
        if (CollUtil.isEmpty(dataList)) {
            return; // 如果数据为空，则跳过
        }

        // 将数据转化为 Map 列表，便于后续操作
        List<Map<String, Object>> dynamicMap = dataList.stream()
                .map(r -> ConvertMap.toMap(r, r.getClass()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (dynamicMap.isEmpty()) {
            log.warn("数据 Map 列表 为空！");
            return;
        }

        // 获取字段名称
        Set<String> fields = dynamicMap.get(0).keySet();
        dynamicTable(doc, dynamicMap, fields);
    }

    /**
     * 动态生成表格行并填充数据
     * <p>
     * 此方法遍历文档中的所有表格，查找包含指定字段占位符的行，然后根据提供的数据动态生成新行并填充数据。
     * 处理完成后，会删除原始的模板行。
     * </p>
     *
     * @param doc        要处理的Word文档对象
     * @param dynamicMap 包含动态行数据的Map列表，每个Map代表一行数据
     * @param fields     需要处理的字段名称集合，用于识别表格中的占位符
     * @throws Exception 如果在表格处理过程中发生错误
     */
    private void dynamicTable(Document doc, List<Map<String, Object>> dynamicMap, Set<String> fields) throws Exception {
        // 遍历文档中的每个表格
        @SuppressWarnings("unchecked")
        Iterable<Table> childNodes = doc.getChildNodes(NodeType.TABLE, true);

        for (Table table : childNodes) {
            RowCollection rows = table.getRows();
            for (Row templateRow : rows) {

                // 获取表格中一整行的 文本
                String rowText = templateRow.getText();
                // 过滤该行中的包含变量的字段
                Set<String> fieldVariables = fields.stream()
                        .filter(var -> rowText.contains(placeholderText(var)))
                        .collect(Collectors.toSet());
                if (CollUtil.isEmpty(fieldVariables)) continue;

                // 遍历数据
                for (Map<String, Object> dataMap : dynamicMap) {
                    fillCellsByRow(doc, table, templateRow, fieldVariables, dataMap);
                }
                // 全部数据填充完毕后,删除模板行
                templateRow.remove();
            }
        }
    }

    /**
     * 按行填充单元格数据
     * <p>
     * 此方法根据模板行创建新行，并用提供的数据填充新行中的单元格。
     * 它会查找单元格中包含指定字段占位符的文本，并替换为对应的数据值。
     * </p>
     *
     * @param doc         要处理的Word文档对象
     * @param table       要处理的表格对象
     * @param templateRow 作为模板的表格行
     * @param fieldVariables 需要处理的字段变量集合
     * @param dataMap     包含单行数据的Map，键为字段名，值为对应的数据
     * @throws Exception 如果在单元格填充过程中发生错误
     */
    private void fillCellsByRow(Document doc,
                                Table table,
                                Row templateRow,
                                Set<String> fieldVariables,
                                Map<String, Object> dataMap) throws Exception {

        // 向下克隆新的行
        Row newTemplateRow = (Row) templateRow.deepClone(true);
        for (String fieldVariable : fieldVariables) {
            // 填充行中的每个单元格
            for (Cell cell : newTemplateRow.getCells()) {
                String text = cell.getText();
                if (text.contains(placeholderText(fieldVariable))) {
                    // 使用模板行填充数据
                    cell.getFirstParagraph().getRuns().clear();
                    cell.getFirstParagraph().appendChild(new Run(doc, StrUtil.blankToDefault((String) dataMap.get(fieldVariable), StrUtil.EMPTY)));
                }
            }
        }
        table.insertBefore(newTemplateRow, templateRow);
    }


    /**
     * 筛选支持的字段
     * <p>
     * 此方法实现了AbstractPlaceholderFillerService中的抽象方法，用于筛选出标记为DYNAMIC_ROW类型的字段。
     * 它会检查字段是否有@Placeholder注解，以及注解的值是否为DYNAMIC_ROW类型。
     * 如果字段标记为DYNAMIC_ROW类型但没有提供dynamicRow属性，则会抛出异常。
     * </p>
     *
     * @param fields 要筛选的字段数组
     * @return 支持的字段数组，只包含DYNAMIC_ROW类型的字段
     * @throws DocumentException 如果字段标记为DYNAMIC_ROW类型但缺少必要的注解属性
     */
    @Override
    public Field[] supports(Field[] fields) {
        return Arrays.stream(fields).filter(f -> {
            Placeholder placeholder = f.getAnnotation(Placeholder.class);
            if (placeholder != null && placeholder.value().equals(PlaceholderType.DYNAMIC_ROW)) {
                Placeholder.DynamicRow dynamicRow = placeholder.dynamicRow();
                if (dynamicRow == null)
                    throw new DocumentException(String.format("Field '%s' must have @Placeholder.DynamicHeader annotation for type DYNAMIC_ROW.", f.getName()));
                return true;
            }
            return false;
        }).toArray(Field[]::new);
    }
}
