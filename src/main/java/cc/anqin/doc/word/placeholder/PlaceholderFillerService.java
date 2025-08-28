package cc.anqin.doc.word.placeholder;

import cc.anqin.doc.entity.AsposePlaceholder;
import cn.hutool.core.util.ReflectUtil;
import com.aspose.words.Document;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

/**
 * 占位符填充服务接口
 * <p>
 * 该接口定义了文档占位符填充的核心功能，是Smart Doc Render Engine占位符处理系统的基础。
 * 所有具体的占位符填充器实现都需要实现该接口，以提供特定类型占位符的填充能力。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>占位符填充 - 将数据实体中的值填充到文档中的对应占位符位置</li>
 *   <li>类型支持检查 - 判断填充器是否支持特定字段类型的占位符</li>
 *   <li>占位符清空 - 提供清空特定占位符的功能</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 创建文本占位符填充器
 * PlaceholderFillerService filler = new TextPlaceholderFiller()
 *     .create(document, templateData);
 *
 * // 执行填充
 * filler.filler();
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2024/12/25
 * @see AbstractPlaceholderFillerService 占位符填充服务抽象实现
 * @see TextPlaceholderFiller 文本占位符填充器
 * @see ImagePlaceholderFiller 图片占位符填充器
 * @see DynamicRowPlaceholderFiller 动态行占位符填充器
 */
public interface PlaceholderFillerService {


    <T extends AsposePlaceholder> PlaceholderFillerService setEntity(T entity);


    /**
     * 创建占位符填充服务实例
     * <p>
     * 此方法初始化占位符填充服务，设置文档对象和数据实体。
     * 实现类需要在此方法中完成必要的初始化工作，如解析实体字段、建立数据映射等。
     * </p>
     *
     * @param doc    要处理的Word文档对象
     * @param entity 包含占位符数据的实体对象，必须实现AsposePlaceholder接口
     * @param <T>    实现AsposePlaceholder接口的泛型类型
     * @return 当前占位符填充服务实例，用于链式调用
     */
    <T extends AsposePlaceholder> PlaceholderFillerService create(Field[] fields, Document doc, T entity);


    /**
     * 执行占位符填充操作
     * <p>
     * 此方法是占位符填充的核心逻辑，负责将数据实体中的值填充到文档中的对应占位符位置。
     * 不同类型的占位符填充器会有不同的填充逻辑，例如文本替换、图片插入、表格生成等。
     * </p>
     *
     * @throws RuntimeException 如果在填充过程中发生错误
     */
    void filler();


    default Field[] supports(Class<?> clazz) {
        return supports(ReflectUtil.getFields(clazz));
    }

    Field[] supports(Field[] fields);


    /**
     * 将指定字段的占位符替换为空白
     * <p>
     * 此方法用于清空文档中特定字段的占位符，通常在没有实际数据需要填充时使用。
     * 它确保文档中不会显示原始的占位符文本，而是替换为适当的空白内容。
     * </p>
     *
     * @param doc    要处理的Word文档对象
     * @param fields 需要清空占位符的字段数组
     */
    @SneakyThrows
    void empty(Document doc, Field... fields);
}
