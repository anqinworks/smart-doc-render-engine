package cc.anqin.doc.ex;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档相关异常
 *
 * @author <a href="https://blog.anqin.cc/">Mr.An</a>
 * @date 2025/08/18
 */
@Getter
@AllArgsConstructor
public class DocumentException extends RuntimeException {

    /** 错误消息 */
    private String errMsg;

    public DocumentException(Throwable throwable) {
        super(throwable);
    }

    public DocumentException(Throwable throwable, String errMsg) {
        super(errMsg, throwable);
        this.errMsg = errMsg;
    }
}
