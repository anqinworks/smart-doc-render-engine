package cc.anqin.doc.word.placeholder;

import cc.anqin.doc.ex.DocumentException;
import cc.anqin.doc.utils.FileUtils;
import cc.anqin.doc.utils.VariableUtils;
import cc.anqin.doc.word.annotation.Placeholder;
import cc.anqin.doc.word.enums.PlaceholderType;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Opt;
import com.aspose.words.*;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 图像占位符填充器
 * <p>
 * 该类是AbstractPlaceholderFillerService的具体实现，专门用于处理图像类型的占位符填充。
 * 图像占位符支持将图片文件或图片URL插入到文档中的对应位置，并可以控制图片的尺寸和位置。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>图片插入 - 将图片文件或URL对应的图片插入到文档中</li>
 *   <li>图片尺寸控制 - 支持通过注解设置图片的宽度和高度</li>
 *   <li>多图片处理 - 支持处理图片列表，适用于需要插入多张图片的场景</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 创建图像占位符填充器并绑定数据
 * PlaceholderFillerService filler = new ImagePlaceholderFiller()
 *     .create(document, templateData);
 * 
 * // 执行图像占位符填充
 * filler.filler();
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2024/12/25
 * @see AbstractPlaceholderFillerService 抽象占位符填充服务
 * @see PlaceholderFillerService 占位符填充服务接口
 * @see PlaceholderType#PIC 图像占位符类型
 */
@Slf4j
public class ImagePlaceholderFiller extends AbstractPlaceholderFillerService {


    /**
     * 处理图片占位符插入
     */
    @Override
    public void filler() {

        DocumentBuilder documentBuilder = Opt.ofTry(() -> new DocumentBuilder(doc))
                .orElseThrow(() -> new DocumentException("Failed to create DocumentBuilder"));

        // 获取所有段落节点
        @SuppressWarnings("unchecked")
        NodeCollection<Paragraph> paragraphs = doc.getChildNodes(NodeType.PARAGRAPH, true);
        for (Field field : fields) {
            String fieldName = field.getName();
            String placeholderText = placeholderText(fieldName);
            Placeholder placeholder = field.getAnnotation(Placeholder.class);
            assert placeholder != null;
            Placeholder.PicWord pic = placeholder.pic();

            File image = (File) dataMap.get(field.getName()); // 获取图片File
            // 如果图片URL，则按文本覆盖掉变量
            if (image == null) {
                continue;

            }
            try {
                HashSet<String> filled = new HashSet<>();
                // 遍历段落，查找并替换占位符
                for (Paragraph paragraph : paragraphs) {
                    replacePlaceholderWithImage(filled, paragraph, fieldName, image, documentBuilder, pic);
                }
            } catch (Exception e) {
                throw new DocumentException("插入图片占位符失败: " + placeholderText + ExceptionUtil.stacktraceToString(e));
            }
        }
    }

    /**
     * 支持的
     *
     * @param fields@return {@link Field[] }
     */
    @Override
    public Field[] supports(Field[] fields) {
        return Arrays.stream(fields).filter(f -> {
            Placeholder placeholder = f.getAnnotation(Placeholder.class);
            if (placeholder != null && placeholder.value().equals(PlaceholderType.PIC)) {
                Placeholder.PicWord pic = placeholder.pic();
                if (pic == null)
                    throw new RuntimeException(String.format("Field '%s' must have  @Placeholder.PicWord annotation for type PIC.", f.getName()));
                return pic.width() > 0 && pic.height() > 0;
            }
            return false;
        }).toArray(Field[]::new);
    }

    /**
     * 替换段落中的占位符为图片
     *
     * @param filled    填充
     * @param paragraph 段
     * @param fieldName 字段名称
     * @param image  图像File
     * @param builder   builder
     * @param pic       pic
     * @throws Exception 例外
     */
    private void replacePlaceholderWithImage(HashSet<String> filled, Paragraph paragraph, String fieldName, File image,
                                             DocumentBuilder builder, Placeholder.PicWord pic) throws Exception {


        RunCollection runs = paragraph.getRuns();
        StringBuilder fullText = new StringBuilder();

        // 拼接所有的 Run 文本
        for (Run run : runs) {
            fullText.append(run.getText());
        }

        // 构造占位符格式

        // 如果拼接后的文本不包含占位符，直接返回
        if (!fullText.toString().contains(placeholderText(fieldName))) {
            return;
        }

        if (image == null) return;

        // 校验文件
        validateImageFile(image);

        toPNG(image);

        // 遍历每个 Run，检查是否包含占位符
        StringBuilder currentText = new StringBuilder();  // 用来拼接当前遍历的 Run 文本
        List<Run> variableRuns = new LinkedList<>();
        for (Run run : runs) {
            String runText = run.getText();
            currentText.append(runText);
            variableRuns.add(run);

            // 查找完整的占位符
            int startIndex = currentText.indexOf(entity.getPrefix());
            int endIndex = currentText.indexOf(entity.getSuffix(), startIndex);

            if (startIndex != -1 && endIndex != -1) {
                // 发现占位符
                String placeholderText = currentText.substring(startIndex, endIndex + 1);

                // 检查是否是目标占位符
                if (placeholderText.equals(placeholderText(fieldName))) {
                    // 清除变量
                    List<String> texts = variableRuns.stream().map(Run::getText).collect(Collectors.toList());
                    List<Integer> matchingIndex = VariableUtils.findMatchingIndex(texts, placeholderText);

                    //清空对应的 Run
                    matchingIndex.forEach(index -> {
                        try {
                            Run data = variableRuns.get(index);
                            String text = data.getText();
                            if (text.contains(placeholderText)) {
                                data.setText(text.replace(placeholderText, ""));
                            } else {
                                data.setText(""); // 清除占位符
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

                    // 插入图片
                    builder.moveTo(run);
                    // 如果包含该占位符，说明已经填充过了，返回
                    if (filled.contains(placeholderText)) {
                        return;
                    }
                    // 未填充该占位符，添加
                    filled.add(placeholderText);


                    try {
                        BufferedInputStream inputStream = FileUtil.getInputStream(image);
                        builder.insertImage(inputStream, pic.width(), pic.height());
                    } catch (Exception e) {
                        log.error("插入图片失败 -  内存使用: {}/{} MB, ERROR：{}",
                                Runtime.getRuntime().totalMemory() / 1024 / 1024,
                                Runtime.getRuntime().maxMemory() / 1024 / 1024,
                                ExceptionUtil.stacktraceToString(e)
                        );
                        throw new DocumentException(e);
                    } finally {
                        CompletableFuture.runAsync(() -> FileUtil.del(image));
                    }
                }
            }
        }
    }


    /**
     * 增强版图片验证方法
     *
     * @param file 文件
     * @throws IOException IOException
     */
    private static void validateImageFile(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new FileNotFoundException("图片文件不存在");
        }
        // 检查文件大小限制（例如最大10MB）
        long fileSize = file.length();
        log.info("图片大小: {} KB", fileSize / 1024);
        if (fileSize > 10 * 1024 * 1024) {
            throw new DocumentException("图片文件过大: " + fileSize / 1024 + "KB");
        }
    }


    /**
     * 转换成png
     *
     * @param file 文件
     * @throws Exception 例外
     */
    public static void toPNG(File file) throws Exception {
        File temporaryFile = FileUtils.getTemporaryFile(".png");
        FileUtils.convertToPNG(file, temporaryFile);
        FileUtil.copy(temporaryFile, file, true);
    }
}
