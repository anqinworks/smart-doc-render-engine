package cc.anqin.doc.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 变量工具
 *
 * @author Mr.An
 * @date 2024/12/20
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
