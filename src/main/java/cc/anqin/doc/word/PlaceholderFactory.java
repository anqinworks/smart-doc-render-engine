package cc.anqin.doc.word;

import cc.anqin.doc.entity.AsposePlaceholder;
import cc.anqin.doc.ex.DocumentException;
import cc.anqin.doc.utils.FileUtils;
import cc.anqin.doc.word.placeholder.DynamicRowPlaceholderFiller;
import cc.anqin.doc.word.placeholder.ImagePlaceholderFiller;
import cc.anqin.doc.word.placeholder.PlaceholderFillerService;
import cc.anqin.doc.word.placeholder.TextPlaceholderFiller;
import cc.anqin.processor.base.ConvertMap;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 单词模板占位符填充工厂
 *
 * @author Mr.An
 * @date 2024/11/22
 */
@Slf4j
@UtilityClass
public class PlaceholderFactory {


    public <T extends AsposePlaceholder> Pair<File, File> fillTemplate(T source, File templateFile, File outputFile) {
        Pair<File, File> pair = fillTemplate(source, templateFile);
        FileUtil.copy(pair.getValue(), outputFile, false);
        pair.setValue(outputFile);
        return pair;
    }


    /**
     * 填充 Word 模板
     *
     * @param source       源数据
     * @param templateFile 模板文件
     * @return {@link Pair }<{@link File }, {@link File }> key 作为 docx 模板记录，value 作为 要转换的 PDF 文件
     */
    public <T extends AsposePlaceholder> Pair<File, File> fillTemplate(T source, File templateFile) {
        Map<String, Object> targetMap = ConvertMap.toMap(source, source.getClass());

        File templateRecord = FileUtils.getTemporaryFile(String.format("-random-%s.docx", RandomUtil.randomNumbers(5)));

        // 获取清空变量的文件
        File clearVariable = FileUtils.getTemporaryFile(String.format("-random-%s.docx", RandomUtil.randomNumbers(5)));

        // 加载模板并处理
        try {

            Field[] fields = ReflectUtil.getFields(source.getClass());

            Document doc = doc(templateFile);

            parallelExecuteStrategy(doc, source);

            doc.save(Files.newOutputStream(templateRecord.toPath()), SaveFormat.DOCX);
            log.info("用户:{} 文档记录生成成功：{}", source.getFileName(), templateRecord.getAbsolutePath());


            // 清除变量
            Document docClearVariable = doc(templateRecord);

            executeClearVariable(fields, docClearVariable);

            docClearVariable.save(Files.newOutputStream(clearVariable.toPath()), SaveFormat.DOCX);

            log.info("用户:{} 文档生成成功：{}", source.getFileName(), clearVariable.getAbsolutePath());

            // key 作为 docx 模板记录，value 作为 要转换的 PDF 文件
            return MutablePair.of(templateRecord, clearVariable);
        } catch (Exception e) {
            log.error("文件:{} 文档生成失败，error：{}", source.getFileName(), ExceptionUtil.stacktraceToString(e));
            throw new DocumentException(e);
        }
    }

    /**
     * doc
     *
     * @param file 文件
     * @return {@link Document }
     * @throws Exception 例外
     */
    private Document doc(File file) throws Exception {
        return new Document(FileUtil.getInputStream(file));
    }

    /**
     * 并行执行策略
     *
     * @param doc     doc
     * @param entity 数据
     */
    private <T extends AsposePlaceholder> void
    parallelExecuteStrategy(Document doc, T entity) {
        getStrategy().parallelStream().forEach(filler -> filler.create(doc, entity).process());
    }

    /**
     * 并行执行清除变量
     *
     * @param fields 领域
     * @param doc    doc
     */
    private void executeClearVariable(Field[] fields, Document doc) {
        getStrategy().parallelStream().forEach(r -> r.empty(doc, r.supports(fields)));
    }

    /**
     * 获取策略
     *
     * @return {@link List }<{@link PlaceholderFillerService }>
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



