package cc.anqin.doc.word.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 编译验证占位符
 *
 * @author Mr.An
 * @date 2024/12/24
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface CompileValidatedPlaceholder {
}
