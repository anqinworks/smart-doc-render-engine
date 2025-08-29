package cc.anqin.doc.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件下载器
 *
 * @author <a href="https://blog.anqin.cc/">Mr.An</a>
 * @date 2025/08/28
 */
@Slf4j
public class FileDownloader {




    /**
     * 从URL下载文件到指定目录
     * @param fileUrl 文件的URL地址
     * @param saveDir 保存文件的目录
     * @param fileName 保存的文件名，如果为null则从URL推断
     * @return 下载的文件对象，如果下载失败则为null
     */
    public static File downloadFile(String fileUrl, String saveDir, String fileName) throws IOException {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            // 创建URL对象
            URL url = new URL(fileUrl);

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 5秒连接超时
            connection.setReadTimeout(10000);   // 10秒读取超时

            // 获取响应码
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                log.info("下载失败，响应码: {}", responseCode);
                return null;
            }

            // 获取文件大小
            int fileSize = connection.getContentLength();
            log.info("文件大小: {}", formatFileSize(fileSize));

            // 如果未指定文件名，则从URL中获取
            if (fileName == null || fileName.trim().isEmpty()) {
                String urlPath = url.getPath();
                fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1);
                // 如果文件名仍然为空，使用默认名称
                if (fileName.isEmpty()) {
                    fileName = "downloaded_file";
                }
            }

            // 创建保存目录
            File saveDirectory = new File(saveDir);
            if (!saveDirectory.exists()) {
                saveDirectory.mkdirs();
            }

            // 创建文件输出流
            File file = new File(saveDirectory, fileName);
            out = new FileOutputStream(file);

            // 读取网络数据并写入文件
            in = connection.getInputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            int totalRead = 0;

            log.info("开始下载...");
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalRead += bytesRead;

                // 显示下载进度
                if (fileSize > 0) {
                    int progress = (totalRead * 100) / fileSize;
                    log.info("\r下载进度: {}%", progress);
                }
            }

            log.info("\n下载完成, 文件已保存至: {}", file.getAbsolutePath());
            return file;

        } catch (Exception e) {
            log.error("下载过程中发生错误: {}", e.getMessage());
            throw e;
        } finally {
            // 关闭流
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
                log.error("关闭流时发生错误: {}", e.getMessage());
            }
        }
    }

    /**
     * 格式化文件大小显示
     */
    private static String formatFileSize(long size) {
        if (size <= 0) return "0 B";

        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = (int) (Math.log10(size) / Math.log10(1024));
        double formattedSize = size / Math.pow(1024, unitIndex);

        return String.format("%.2f %s", formattedSize, units[unitIndex]);
    }
}
