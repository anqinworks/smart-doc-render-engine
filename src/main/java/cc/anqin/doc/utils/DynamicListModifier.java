package cc.anqin.doc.utils;

import cn.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 动态列表修饰符
 *
 * @author Mr.An
 * @date 2024/12/26
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
