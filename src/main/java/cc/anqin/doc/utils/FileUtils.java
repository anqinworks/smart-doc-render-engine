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
 * 文件使用
 *
 * @author Mr.An
 * @date 2024/11/13
 */
@Slf4j
public class FileUtils {


    /**
     * 临时路径
     */
    public static final String TEMP_PATH = FileUtil.getTmpDir().getAbsolutePath();

    /**
     * 获取临时文件
     *
     * @param suffix 后缀
     * @return {@link File }
     */
    public static File getTemporaryFile(String suffix) {
        return new File(TEMP_PATH + File.separator + System.currentTimeMillis() + suffix);
    }


    /**
     * 从 URL 下载文件到指定路径
     *
     * @param fileUrl   文件 URL
     * @param localFile 本地保存
     * @return 下载的 File 对象
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
     *  使用 Tika 获取文件的 MIME 类型
     *
     * @param file 文件
     * @return {@link String }
     * @throws IOException IOException
     */
    public static String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        return tika.detect(file);
    }

    /**
     * Base64 转 File
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
     *
     * @param inputFile  输入文件
     * @param outputFile 输出文件
     * @throws Exception 例外
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
     * 模式1: 带标准前缀（data:image/png;base64,）
     * 模式2: 带简写前缀（image/png;base64,）
     * 模式3: 纯Base64（通过内容检测）
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
     * 纯Base64（通过内容检测）
     *
     * @param decoded 解码
     * @return {@link String }
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