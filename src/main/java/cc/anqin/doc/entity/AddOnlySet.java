package cc.anqin.doc.entity;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 仅添加集
 *
 * @author <a href="https://blog.anqin.cc/">Mr.An</a>
 * @date 2025/09/03
 */
public class AddOnlySet<E> extends HashSet<E> {


    // 显式不支持删除操作的方法
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Remove operation is not supported");
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Remove operation is not supported");
    }

    public boolean removeIf(Predicate<? super E> filter) {
        throw new UnsupportedOperationException("Remove operation is not supported");
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Remove operation is not supported");
    }

    public void clear() {
        throw new UnsupportedOperationException("Clear operation is not supported");
    }
}