package cc.anqin.doc.utils;

import cn.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 动态列表修饰符工具类
 * <p>
 * 该工具类用于处理和转换动态数据列表，主要应用于动态表格数据的格式化和规范化。
 * 它能够将原始数据列表转换为包含表头和数据的标准格式，便于在文档模板中进行动态表格的生成。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>数据规范化 - 将原始数据列表转换为标准格式</li>
 *   <li>表头集成 - 将表头与数据行集成在一起</li>
 *   <li>多行数据处理 - 支持处理多行数据的转换</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 原始数据
 * List&lt;List&lt;String&gt;&gt; dataList = Arrays.asList(
 *     Arrays.asList("胖胖", "2012-08-15", "西安"),
 *     Arrays.asList("熙熙", "2012-08-15", "北京")
 * );
 * 
 * // 表头
 * List&lt;String&gt; headers = Arrays.asList("子女姓名", "出身日期", "地址");
 * 
 * // 规范化数据
 * List&lt;List&lt;String&gt;&gt; normalizedData = DynamicListModifier.normalizeData(dataList, headers);
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2024/12/26
 * @see cc.anqin.doc.word.placeholder.DynamicRowPlaceholderFiller 动态行占位符填充器
 * @see cn.hutool.core.collection.CollUtil Hutool集合工具
 */
public class DynamicListModifier {


    /**
     * 动态列表修饰符
     *
     * <pre>
     * {@code
     * targetDynamicData:  List<List<String>> dataList = Arrays.asList(
     *                 Arrays.asList("胖胖", "2012-08-15","西安"),
     *                 Arrays.asList("熙熙", "2012-08-15","北京")
     *         );
     * dynamicHeader: Arrays.asList("子女姓名", "出身日期","地址")
     *
     * 结果：[子女姓名, 胖胖, 出身日期, 2012-08-15, 地址, 西安]
     *      [子女姓名, 熙熙, 出身日期, 2012-08-15, 地址, 北京]}
     * </pre>
     *
     * @param targetDynamicData 目标动态数据
     * @param dynamicHeader     动态表格头部
     */
    public static List<List<String>> normalizeData(List<List<String>> targetDynamicData, List<String> dynamicHeader) {
        if (CollUtil.isEmpty(targetDynamicData) || CollUtil.isEmpty(dynamicHeader))
             return targetDynamicData;

       return targetDynamicData.stream()
               .map(r -> normalizeRow(r, dynamicHeader)).collect(Collectors.toList());
    }

    /**
     * 规范化行
     *
     * @param data          数据
     * @param dynamicFields 动态字段
     * @return {@link List }<{@link String }>
     */
    private static List<String> normalizeRow(List<String> data, List<String> dynamicFields) {
        List<String> normalized = new ArrayList<>();
        Set<String> existingFields = new HashSet<>(data); // 用于快速判断字段是否已存在

        int dataIndex = 0;
        for (String field : dynamicFields) {
            if (!existingFields.contains(field)) {
                // 如果字段不存在，则添加键和值
                normalized.add(field);
                if (dataIndex < data.size()) {
                    normalized.add(data.get(dataIndex++));
                } else {
                    normalized.add(""); // 若数据不足，用空字符串占位
                }
            } else {
                // 如果字段已存在，直接取下一个数据值
                if (dataIndex < data.size()) {
                    normalized.add(data.get(dataIndex++));
                }
            }
        }

        // 将剩余未处理的值追加
        while (dataIndex < data.size()) {
            normalized.add(data.get(dataIndex++));
        }
        return normalized;
    }
}
