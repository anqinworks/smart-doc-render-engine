package cc.anqin.doc.ex;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档相关异常类
 * <p>
 * 该异常类是Smart Doc Render Engine的核心异常类，用于封装和处理文档处理过程中可能出现的各种异常情况。
 * 它扩展了RuntimeException，提供了更具体的错误信息和异常处理机制。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>异常封装 - 封装底层异常，提供统一的异常处理机制</li>
 *   <li>错误信息管理 - 提供详细的错误信息描述</li>
 *   <li>多种构造方法 - 支持不同场景下的异常创建</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 创建带有错误信息的异常
 * throw new DocumentException("无法加载模板文件");
 * 
 * // 封装底层异常
 * try {
 *     // 文档处理代码
 * } catch (IOException e) {
 *     throw new DocumentException(e, "文件读取失败");
 * }
 * </pre>
 * </p>
 *
 * @author <a href="https://blog.anqin.cc/">Mr.An</a>
 * @date 2025/08/18
 * @see RuntimeException Java运行时异常
 * @see cc.anqin.doc.word.placeholder.PlaceholderFillerService 占位符填充服务
 * @see cc.anqin.doc.utils.FileUtils 文件工具类
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
