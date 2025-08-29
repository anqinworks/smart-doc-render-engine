package cc.anqin.doc.utils;

import cn.hutool.core.builder.CompareToBuilder;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * 通用键值对容器类，实现了 Map.Entry 接口
 * 用于存储两个相关联的值（左值和右值），支持比较和序列化
 *
 * @param <L> 左值类型
 * @param <R> 右值类型
 */
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class Pair<L, R> implements Map.Entry<L, R>, Comparable<Pair<L, R>>, Serializable {

    /**
     * 空数组常量，用于返回空的 Pair 数组
     * 使用通配符类型确保类型安全
     */
    public static final Pair<?, ?>[] EMPTY_ARRAY = new Pair[0];

    /**
     * 序列化版本UID，用于保证序列化兼容性
     * 如果类结构发生变化，需要更新此值
     */
    private static final long serialVersionUID = 4954918890077093841L;

    /**
     * 左值，通常用作键或第一个元素
     * 使用 @Getter 注解自动生成getter方法
     */
    @Getter
    public L left;

    /**
     * 右值，通常用作值或第二个元素
     * 使用 @Getter 和 @Setter 注解自动生成getter和setter方法
     */
    @Setter
    @Getter
    public R right;

    /**
     * 返回一个空的泛型 Pair 数组
     * 避免了创建新数组的开销，提供了类型安全的空数组
     *
     * @param <L> 左值类型
     * @param <R> 右值类型
     * @return 空的 Pair 数组
     */
    @SuppressWarnings("unchecked")
    public static <L, R> Pair<L, R>[] emptyArray() {
        return (Pair<L, R>[]) EMPTY_ARRAY;
    }

    /**
     * 工厂方法：通过左值和右值创建 Pair 实例
     * 提供更简洁的创建方式，避免直接调用构造函数
     *
     * @param left  左值，不能为null（根据具体实现决定）
     * @param right 右值，可以为null
     * @param <L>   左值类型
     * @param <R>   右值类型
     * @return 包含指定左值和右值的 Pair 实例
     */
    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<L, R>(left, right);
    }

    /**
     * 工厂方法：从 Map.Entry 创建 Pair 实例
     * 用于将现有的 Map.Entry 转换为 Pair
     *
     * @param pair Map.Entry 实例，可以为null
     * @param <L>  左值类型
     * @param <R>  右值类型
     * @return 如果输入为null则返回包含null值的Pair，否则返回转换后的Pair
     */
    public static <L, R> Pair<L, R> of(Map.Entry<L, R> pair) {
        L left;
        R right;
        if (pair != null) {
            left = (L) pair.getKey();
            right = (R) pair.getValue();
        } else {
            left = null;
            right = null;
        }

        return new Pair<L, R>(left, right);
    }

    /**
     * 实现 Map.Entry 接口的 getKey 方法
     * 返回左值作为键
     *
     * @return 左值，可能为null
     */
    @Override
    public L getKey() {
        return left;
    }

    /**
     * 实现 Map.Entry 接口的 getValue 方法
     * 返回右值作为值
     *
     * @return 右值，可能为null
     */
    @Override
    public R getValue() {
        return right;
    }

    /**
     * 实现 Map.Entry 接口的 setValue 方法
     * 设置右值并返回之前的值
     *
     * @param value 要设置的新右值
     * @return 设置前的右值，可能为null
     * @throws UnsupportedOperationException 如果右值不支持修改
     */
    public R setValue(R value) {
        R result = (R) this.getValue();
        this.setRight(value);
        return result;
    }

    /**
     * 实现 Comparable 接口的 compareTo 方法
     * 比较规则：先比较左值，如果左值相等再比较右值
     * 使用 CompareToBuilder 确保null安全和正确的比较顺序
     *
     * @param other 要比较的另一个 Pair 实例
     * @return 负整数、零或正整数，分别表示小于、等于或大于
     * @throws NullPointerException 如果other为null
     * @throws ClassCastException 如果左值或右值不可比较
     */
    @Override
    public int compareTo(Pair<L, R> other) {
        return (new CompareToBuilder())
                .append(this.getLeft(), other.getLeft())   // 先比较左值
                .append(this.getRight(), other.getRight()) // 左值相等时比较右值
                .toComparison();
    }
}