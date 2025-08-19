package cc.anqin.doc.utils;

import cc.anqin.doc.ex.DocumentException;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

/**
 * 文件工具类
 * <p>
 * 该工具类提供了一系列文件操作的实用方法，包括临时文件创建、URL文件下载、文件类型检测、
 * Base64转换等功能。这些方法在文档处理过程中被广泛使用，特别是在处理图片占位符和文件转换时。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>临时文件管理 - 创建和管理临时文件</li>
 *   <li>文件下载 - 从URL下载文件到本地</li>
 *   <li>文件类型检测 - 使用Apache Tika检测文件MIME类型</li>
 *   <li>Base64转换 - 将Base64编码的数据转换为文件</li>
 *   <li>图片格式转换 - 支持图片格式之间的转换</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 创建临时文件
 * File tempFile = FileUtils.getTemporaryFile(".docx");
 * 
 * // 从URL下载文件
 * File downloadedFile = FileUtils.downloadFile("https://example.com/sample.docx", tempFile);
 * 
 * // 检测文件类型
 * String mimeType = FileUtils.getMimeType(downloadedFile);
 * 
 * // Base64转文件
 * File imageFile = FileUtils.base64ToFile(base64String);
 * </pre>
 * </p>
 *
 * @author Mr.An
 * @date 2024/11/13
 * @see java.io.File Java文件操作
 * @see org.apache.tika.Tika Apache Tika文件类型检测
 * @see cn.hutool.core.io.FileUtil Hutool文件工具
 */
@Slf4j
public class FileUtils {


    /**
     * 临时路径
     */
    public static final String TEMP_PATH = FileUtil.getTmpDir().getAbsolutePath();

    /**
     * 获取临时文件
     * <p>
     * 此方法创建一个带有指定后缀的临时文件，文件名基于当前时间戳生成，确保唯一性。
     * 临时文件位于系统临时目录下，适用于需要临时存储数据的场景。
     * </p>
     *
     * @param suffix 文件后缀，例如 ".docx"、".pdf" 等
     * @return 创建的临时文件对象
     */
    public static File getTemporaryFile(String suffix) {
        return new File(TEMP_PATH + File.separator + System.currentTimeMillis() + suffix);
    }


    /**
     * 从 URL 下载文件到指定路径
     * <p>
     * 此方法从指定的URL下载文件内容，并保存到本地文件中。
     * 适用于需要获取远程文件的场景，如下载网络图片、文档等。
     * </p>
     *
     * @param fileUrl   文件的URL地址，必须是有效的URL格式
     * @param localFile 要保存到的本地文件对象
     * @return 下载完成的本地文件对象
     * @throws RuntimeException 如果下载过程中发生错误
     */
    public static File downloadFile(String fileUrl, File localFile) {
        try {
            URL url = new URL(fileUrl);  // 创建 URL 对象
            InputStream inputStream = url.openStream();
            Files.copy(inputStream, localFile.toPath());
            return localFile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 使用 Tika 获取文件的 MIME 类型
     * <p>
     * 此方法使用Apache Tika库检测文件的MIME类型，无需依赖文件扩展名。
     * MIME类型可用于确定文件的实际格式，对于文件类型验证和处理非常有用。
     * </p>
     *
     * @param file 要检测MIME类型的文件对象
     * @return 文件的MIME类型字符串，例如 "application/pdf"、"image/jpeg" 等
     * @throws IOException 如果文件读取过程中发生错误
     */
    public static String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        return tika.detect(file);
    }

    /**
     * 将Base64编码的字符串转换为文件
     * <p>
     * 此方法将Base64编码的数据解码并保存为临时文件。它支持标准的Base64格式，
     * 包括带有MIME类型前缀的格式（如"data:image/png;base64,"）。
     * 文件扩展名会根据Base64内容自动检测。
     * </p>
     *
     * @param base64 Base64编码的字符串数据
     * @return 保存了解码数据的临时文件对象
     * @throws IllegalArgumentException 如果Base64字符串为空
     * @throws RuntimeException 如果解码或文件写入过程中发生错误
     */
    public static File base64ToFile(String base64) {
        // 参数校验
        if (base64 == null
            || base64.trim().isEmpty()) {
            throw new IllegalArgumentException("Base64字符串不能为空");
        }

        base64 = base64.substring(base64.lastIndexOf(",") + 1);

        byte[] decoded = Base64Decoder.decode(base64);

        File temporaryFile = FileUtils.getTemporaryFile("." + detectExtension(decoded));

        return FileUtil.writeBytes(decoded, temporaryFile);
    }


    /**
     * 将图片文件转换为 PNG 格式
     * <p>
     * 此方法将各种格式的图片文件转换为PNG格式。它首先检测输入文件的MIME类型，
     * 确认是图片文件后，读取图片数据并以PNG格式写入到输出文件。
     * 此方法特别适用于需要统一图片格式的场景。
     * </p>
     *
     * @param inputFile  要转换的源图片文件
     * @param outputFile 转换后的PNG格式输出文件
     * @throws Exception 如果文件不是图片格式或转换过程中发生错误
     */
    public static void convertToPNG(File inputFile, File outputFile) throws Exception {
        // 使用 Apache Tika 获取文件格式
        String mimeType = getMimeType(inputFile);

        // 只支持图片文件转换
        if (mimeType.startsWith("image/")) {

            // 读取输入文件
            String prefix = FileUtil.getPrefix(inputFile);
            File renamed = FileUtil.rename(inputFile, prefix + StrUtil.replace(mimeType, "image/", "."), true);
            // 读取 WebP 文件
            BufferedImage image = ImageIO.read(renamed);

            // 将其保存为 PNG 格式
            ImageIO.write(image, "PNG", outputFile);
        } else {
            throw new DocumentException("不支持的文件类型: " + mimeType);
        }
    }

    /**
     * 智能获取Base64文件后缀（支持三种模式）
     * <p>
     * 此方法通过分析Base64编码的数据，智能检测并返回适当的文件扩展名。支持以下三种模式：
     * <ul>
     *   <li>模式1: 带标准前缀（data:image/png;base64,）</li>
     *   <li>模式2: 带简写前缀（image/png;base64,）</li>
     *   <li>模式3: 纯Base64（通过内容检测）</li>
     * </ul>
     * </p>
     *
     * @param base64Data Base64编码的数据字符串
     * @return 检测到的文件扩展名（不含点号），如 "png"、"jpg" 等
     * @throws RuntimeException 如果内容检测过程中发生错误
     */
    public static String detectExtension(String base64Data) {
        // 尝试从前缀解析
        if (base64Data.contains(";base64,")) {
            String[] parts = base64Data.split(";base64,")[0].split("/");
            if (parts.length > 1) {
                return parts[parts.length - 1].split(";")[0]; // 处理可能的额外参数
            }
        }

        // 内容检测（需要引入tika-core依赖）
        byte[] decoded = Base64.decode(
                base64Data.substring(base64Data.lastIndexOf(",") + 1)
        );
        return detectExtension(decoded);
    }

    /**
     * 通过内容检测纯Base64数据的文件类型
     * <p>
     * 此方法使用Apache Tika库分析已解码的Base64数据的内容，
     * 确定其实际文件类型并返回相应的文件扩展名。
     * 这对于处理没有类型信息的纯Base64数据特别有用。
     * </p>
     *
     * @param decoded 已解码的Base64数据字节数组
     * @return 检测到的文件扩展名（不含点号），如 "png"、"jpg" 等；如果无法检测则返回null
     * @throws RuntimeException 如果内容检测过程中发生错误
     */
    public static String detectExtension(byte[] decoded) {

        // 内容检测（需要引入tika-core依赖）
        Tika tika = new Tika();
        try {
            return Opt.ofBlankAble(tika.detect((new ByteArrayInputStream(decoded))))
                    .map(r -> {
                        String[] f = r.split("/");
                        if (f.length > 1) return f[1];
                        return null;
                    })
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}