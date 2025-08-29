package cc.anqin.doc.word;

import cc.anqin.doc.entity.Template;
import cc.anqin.doc.entity.TemplateInterface;
import cc.anqin.doc.ex.DocumentException;
import cc.anqin.doc.utils.FileUtils;
import cc.anqin.doc.word.annotation.Placeholder;
import cc.anqin.doc.word.placeholder.DynamicRowPlaceholderFiller;
import cc.anqin.doc.word.placeholder.ImagePlaceholderFiller;
import cc.anqin.doc.word.placeholder.PlaceholderFillerService;
import cc.anqin.doc.word.placeholder.TextPlaceholderFiller;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * 文档模板占位符填充工厂
 * <p>
 * 该工具类是Smart Doc Render Engine的核心组件之一，负责处理文档中的各类占位符填充操作。
 * 它提供了一系列静态方法，用于将数据实体中的值填充到文档模板的对应占位符位置。
 * 该类采用策略模式和工厂模式设计，支持多种类型的占位符处理策略。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>文本占位符填充 - 将普通文本值填充到文档占位符中</li>
 *   <li>图片占位符填充 - 将图片数据填充到文档占位符中</li>
 *   <li>动态表格行填充 - 根据集合数据动态生成表格行</li>
 *   <li>反射机制自动处理 - 通过反射自动识别和处理不同类型的占位符</li>
 *   <li>并行处理策略 - 使用并行流提高处理效率</li>
 *   <li>占位符清理 - 自动清理未使用的占位符</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 基础模板填充
 * MyTemplate template = new MyTemplate().setName("示例文档");
 * Pair&lt;File, File&gt; result = PlaceholderFactory.fillTemplate(template, templateFile);
 * 
 * // 指定输出文件的模板填充
 * Pair&lt;File, File&gt; result = PlaceholderFactory.fillTemplate(template, templateFile, outputFile);
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2024/11/22
 * @see Placeholder 模板占位符接口
 * @see PlaceholderFillerService 占位符填充服务接口
 * @see TextPlaceholderFiller 文本占位符填充器
 * @see ImagePlaceholderFiller 图片占位符填充器
 * @see DynamicRowPlaceholderFiller 动态行占位符填充器
 * @see Template 文档模板基类
 */
@Slf4j
@UtilityClass
public class PlaceholderFactory {

    /**
     * 填充模板并输出到指定文件
     * <p>
     * 该方法首先调用基础的fillTemplate方法处理模板填充，然后将结果复制到指定的输出文件路径。
     * 返回的Pair对象中，key为临时生成的docx模板记录文件，value为指定的输出文件。
     * 此方法适用于需要将填充结果保存到特定位置的场景。
     * </p>
     * <p>
     * 处理流程：
     * <ol>
     *   <li>调用基础fillTemplate方法进行模板填充</li>
     *   <li>将填充结果复制到指定的输出文件</li>
     *   <li>更新Pair对象中的value为输出文件</li>
     *   <li>返回包含记录文件和输出文件的Pair对象</li>
     * </ol>
     * </p>
     *
     * @param source 包含填充数据的源对象，必须实现AsposePlaceholder接口
     * @param templateFile 要填充的模板文件，必须存在且可读
     * @param outputFile 填充后的输出文件路径，如果父目录不存在会自动创建
     * @return Pair对象，key为模板记录文件，value为输出文件
     * @param <T> 模板数据类型，必须实现AsposePlaceholder接口
     * @throws IllegalArgumentException 如果任何参数为null
     * @throws DocumentException 如果模板填充过程中发生错误
     */
    public <T extends TemplateInterface> Pair<File, File> fillTemplate(T source, File templateFile, File outputFile) {
        Pair<File, File> pair = fillTemplate(source, templateFile);
        FileUtil.copy(pair.getValue(), outputFile, false);
        pair.setValue(outputFile);
        return pair;
    }

    /**
     * 填充Word模板文件
     * <p>
     * 该方法是模板填充的核心方法，它执行以下步骤：
     * <ol>
     *   <li>创建临时文件用于存储处理结果</li>
     *   <li>加载模板文档</li>
     *   <li>并行执行各种占位符填充策略</li>
     *   <li>保存填充后的文档</li>
     *   <li>清除未替换的变量</li>
     *   <li>生成最终文档</li>
     * </ol>
     * </p>
     * <p>
     * 该方法使用并行流处理多种占位符填充策略，提高处理效率。同时，
     * 它会生成两个文件：一个包含所有占位符替换的记录文件，另一个是清理后的最终文件。
     * </p>
     *
     * @param source 包含填充数据的源对象，必须实现AsposePlaceholder接口
     * @param templateFile 要填充的模板文件，必须存在且可读
     * @return Pair对象，key为模板记录文件，value为清除变量后的文件（可用于后续转换为PDF等格式）
     * @param <T> 模板数据类型，必须实现AsposePlaceholder接口
     * @throws IllegalArgumentException 如果任何参数为null
     * @throws DocumentException 当文档处理过程中发生错误时抛出
     */
    public <T extends TemplateInterface> Pair<File, File> fillTemplate(T source, File templateFile) {

        File templateRecord = FileUtils.getTemporaryFile(String.format("-random-%s.docx", RandomUtil.randomNumbers(5)));

        // 获取清空变量的文件
        File clearVariable = FileUtils.getTemporaryFile(String.format("-random-%s.docx", RandomUtil.randomNumbers(5)));

        // 加载模板并处理
        try {
            // 过滤 final 修饰的属性
            Field[] fields = Arrays.stream(ReflectUtil.getFields(source.getClass()))
                    .filter(field -> !java.lang.reflect.Modifier.isFinal(field.getModifiers()))
                    .toArray(Field[]::new);

            Document doc = doc(templateFile);

            parallelExecuteStrategy(doc, source);

            doc.save(Files.newOutputStream(templateRecord.toPath()), SaveFormat.DOCX);
            log.info("模板:{} 文档记录生成成功：{}", templateFile.getName(), templateRecord.getAbsolutePath());

            // 清除变量
            Document docClearVariable = doc(templateRecord);

            executeClearVariable(fields, docClearVariable, source);

            docClearVariable.save(Files.newOutputStream(clearVariable.toPath()), SaveFormat.DOCX);

            log.info("模板:{} 文档生成成功：{}", templateFile.getName(), clearVariable.getAbsolutePath());

            // key 作为 docx 模板记录，value 作为 要转换的 PDF 文件
            return MutablePair.of(templateRecord, clearVariable);
        } catch (Exception e) {
            log.error("模板:{} 文档生成失败，error：{}", templateFile.getName(), ExceptionUtil.stacktraceToString(e));
            throw new DocumentException(e);
        }
    }

    /**
     * 从文件创建Aspose Document对象
     * <p>
     * 该方法用于从文件创建Aspose Words的Document对象，以便进行后续的文档处理操作。
     * 它使用FileUtil工具类获取文件输入流，然后创建Document对象。
     * 此方法是文档处理的入口点，确保文档能够正确加载。
     * </p>
     *
     * @param file 要加载的文件，必须存在且可读
     * @return 创建的Document对象，包含文档的所有内容和格式信息
     * @throws Exception 当文件加载失败时抛出，包括文件不存在、格式不支持等错误
     */
    private Document doc(File file) throws Exception {
        return new Document(FileUtil.getInputStream(file));
    }

    /**
     * 并行执行所有占位符填充策略
     * <p>
     * 该方法使用并行流同时执行多种占位符填充策略，提高处理效率。
     * 它为每个填充器创建处理上下文，然后执行填充操作。支持的填充策略包括：
     * 文本填充、图片填充、动态表格行填充等。
     * </p>
     * <p>
     * 并行处理可以显著提高大量占位符的处理速度，特别是在处理复杂文档时。
     * 每个填充策略都会在独立的线程中执行，互不干扰。
     * </p>
     *
     * @param doc Aspose文档对象，包含要填充的模板内容
     * @param entity 包含填充数据的实体对象，必须实现AsposePlaceholder接口
     * @param <T> 实体类型，必须实现AsposePlaceholder接口
     */
    private <T extends TemplateInterface> void
    parallelExecuteStrategy(Document doc, T entity) {
        getStrategy().parallelStream().forEach(f -> f.create(f.supports(entity.getClass()), doc, entity).filler());
    }

    /**
     * 执行占位符清理操作（使用默认模板）
     * <p>
     * 该方法使用默认模板清理文档中未使用的占位符，将它们替换为空白内容。
     * 默认模板提供了标准的占位符前缀和后缀配置。
     * </p>
     *
     * @param fields 需要清理的字段数组，通常是通过反射获取的非final字段
     * @param doc 要清理的Aspose文档对象
     */
    private void executeClearVariable(Field[] fields, Document doc) {
        executeClearVariable(fields, doc, Template.defaultTemplate());
    }

    /**
     * 执行占位符清理操作（使用指定模板）
     * <p>
     * 该方法使用指定的模板清理文档中未使用的占位符，将它们替换为空白内容。
     * 清理过程会遍历所有指定的字段，将对应的占位符替换为预定义的空白字符。
     * </p>
     *
     * @param fields 需要清理的字段数组，通常是通过反射获取的非final字段
     * @param doc 要清理的Aspose文档对象
     * @param placeholder 用于清理的占位符模板，定义了占位符的前缀和后缀
     */
    private void executeClearVariable(Field[] fields, Document doc, TemplateInterface placeholder) {
        getStrategy().parallelStream().forEach(r -> r.setEntity(placeholder).empty(doc, r.supports(fields)));
    }

    /**
     * 获取所有可用的占位符填充策略
     * <p>
     * 该方法返回系统中所有可用的占位符填充器实例列表。
     * 每次调用都会创建新的对象实例，以确保线程安全和状态隔离。
     * 当前支持的填充器包括：
     * <ul>
     *   <li>ImagePlaceholderFiller - 处理图片占位符，支持多种图片格式</li>
     *   <li>TextPlaceholderFiller - 处理文本占位符，支持各种文本类型</li>
     *   <li>DynamicRowPlaceholderFiller - 处理动态表格行占位符，支持集合数据</li>
     * </ul>
     * </p>
     * <p>
     * 系统采用策略模式设计，每种填充器负责处理特定类型的占位符，
     * 通过并行执行提高整体处理效率。
     * </p>
     *
     * @return 占位符填充器服务列表，包含所有可用的填充策略
     */
    private List<PlaceholderFillerService> getStrategy() {
        // 每次调用时创建新的对象实例
        return ListUtil.toList(
                new ImagePlaceholderFiller(),
                new TextPlaceholderFiller(),
                new DynamicRowPlaceholderFiller()
        );
    }
}



