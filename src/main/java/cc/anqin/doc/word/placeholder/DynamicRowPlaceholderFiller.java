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
 * 动态表占位符填充器
 *
 * @author Mr.An
 * @date 2024/12/25
 */
@Slf4j
public class DynamicRowPlaceholderFiller extends AbstractPlaceholderFillerService {


    /**
     * 空的，用于覆盖变量
     *
     * @param doc    doc
     * @param fields 字段数组
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
     *
     * @param field 领域
     * @return {@link Map }<{@link String }, {@link Object }>
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
     * 填充文档中的占位符，包括动态表格和普通文本。
     */
    @Override
    public PlaceholderFillerService process() {

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
        return this;
    }

    /**
     * 处理动态表格
     *
     * @param doc 医生
     * @param dataList 数据列表
     * @throws Exception 例外
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
     * 动态表
     *
     * @param doc        doc
     * @param dynamicMap 动态地图
     * @param fields     领域
     * @throws Exception 例外
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
     * 按行填充单元格
     *
     * @param table       表
     * @param templateRow 模板行
     * @param dataMap     资料图
     * @throws Exception 例外
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
     * 支持的
     *
     * @param fields@return {@link Field[] }
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
