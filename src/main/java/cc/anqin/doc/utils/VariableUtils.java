package cc.anqin.doc.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 变量工具类
 * <p>
 * 该工具类提供了处理变量和字符串匹配的实用方法，主要用于在文档模板中查找和处理占位符变量。
 * 核心功能是在字符串列表中查找特定模式的匹配项，这在解析复杂的模板结构时非常有用。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>字符串匹配 - 在字符串列表中查找匹配特定目标的索引</li>
 *   <li>部分匹配支持 - 支持查找部分匹配的字符串序列</li>
 *   <li>空值处理 - 对空值和空白字符进行适当处理</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 查找匹配的索引
 * List&lt;String&gt; parts = Arrays.asList("$", "{", "name", "}");
 * List&lt;Integer&gt; indexes = VariableUtils.findMatchingIndex(parts, "${name}");
 * // 结果: [0, 1, 2, 3]
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2024/12/20
 * @see cn.hutool.core.util.StrUtil Hutool字符串工具
 * @see cn.hutool.core.collection.CollUtil Hutool集合工具
 */
public class VariableUtils {


    /**
     * 提取所有字符索引
     * 对于输入 "$", "{", "name", "}"，输出将是： Indexes: [0, 1, 2, 3]
     *
     * @param dataTarget 列表
     * @return {@link Set }<{@link Integer }>
     */
    public static List<Integer> findMatchingIndex(List<String> dataTarget, String target) {

        // 先检查有没有完整的
        dataTarget = dataTarget.stream().map(StrUtil::trim).collect(Collectors.toList());
        List<String> data = dataTarget.stream().filter(r -> r.contains(target)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(data)) {
            List<Integer> index = data.stream().map(dataTarget::indexOf).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(index)) {
                return index;
            }
        }

        List<Integer> matchedValues = new ArrayList<>();
        int targetLength = target.length();
        int currentIndex = 0;

        while (currentIndex < targetLength) {
            boolean matched = false;

            // Try to find the next matching part in the list
            for (int i = 0; i < dataTarget.size(); i++) {
                String part = dataTarget.get(i);

                // Check if the current substring starts with this part
                if (StrUtil.isNotBlank(part) && target.startsWith(part, currentIndex)) {
                    matchedValues.add(i);
                    currentIndex += part.length();
                    matched = true;
                    break;  // Continue to next substring
                }
            }

            if (!matched) {
                return ListUtil.toList();
            }
        }

        return matchedValues;
    }
}
