# Smart Doc Render Engine

## 项目介绍

Smart Doc Render Engine 是一个基于 Java 的智能文档渲染引擎，支持 DOCX 模板 和 HTML 动态渲染，并导出为 PDF 或其他格式。它提供灵活的模板变量替换、动态内容生成，适用于报告生成、合同自动化、电子发票等场景。

## 功能特点

- **多格式支持**：支持 DOCX 模板和 HTML 动态渲染
- **灵活的占位符替换**：支持文本、图片、动态表格行等多种占位符类型
- **格式转换**：支持将文档转换为 PDF 等多种格式
- **链式调用**：API 设计采用流畅的链式调用风格，使用简单直观
- **类型安全**：使用泛型确保类型安全的模板数据填充

## 支持的格式分类：

<ul>
  <li><strong>Microsoft Word格式</strong> - DOC、DOCX、DOT、DOTX等，支持度最高</li>
  <li><strong>固定布局格式</strong> - PDF、XPS、SVG等，适合打印和精确布局</li>
  <li><strong>流式布局格式</strong> - HTML、EPUB、Markdown等，适合网页和电子书</li>
  <li><strong>图像格式</strong> - PNG、JPEG、TIFF等，适合图片导出</li>
  <li><strong>OpenDocument格式</strong> - ODT、OTT等，开源文档格式</li>
  <li><strong>纯文本格式</strong> - TXT、Markdown等，适合纯文本内容</li>
</ul>
<html lang="zh-CN"><body> <div class="container"> <div class="content"> <h2>一、支持的输入格式（可读取）</h2> <div class="table-container"> <table> <thead> <tr> <th>格式类型</th> <th>具体格式</th> <th>支持程度</th> </tr> </thead> <tbody> <tr> <td class="format-type">Word 文档</td> <td class="format-list">DOC, DOCX, DOT, DOTX, DOTM, DOCM</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">富文本格式</td> <td class="format-list">RTF</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">网页格式</td> <td class="format-list">HTML, XHTML, MHTML</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">OpenDocument</td> <td class="format-list">ODT, OTT</td> <td class="rating">⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">文本格式</td> <td class="format-list">TXT</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">电子书</td> <td class="format-list">EPUB, FictionBook (FB2)</td> <td class="rating">⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">XML 格式</td> <td class="format-list">WordML, Office MathML</td> <td class="rating">⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">其他格式</td> <td class="format-list">PDF, XPS, PostScript (PS)</td> <td class="rating">⭐⭐⭐⭐</td> </tr> </tbody> </table> </div> <h2>二、支持的输出格式（可转换至）</h2> <div class="table-container"> <table> <thead> <tr> <th>格式类型</th> <th>具体格式</th> <th>支持程度</th> </tr> </thead> <tbody> <tr> <td class="format-type">Word 文档</td> <td class="format-list">DOC, DOCX, DOT, DOTX, DOTM, DOCM</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">固定布局</td> <td class="format-list">PDF, PDF/A, XPS</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">网页格式</td> <td class="format-list">HTML, XHTML, MHTML</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">图像格式</td> <td class="format-list">JPEG, PNG, BMP, TIFF, SVG, EMF</td> <td class="rating">⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">文本格式</td> <td class="format-list">TXT</td> <td class="rating">⭐⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">OpenDocument</td> <td class="format-list">ODT</td> <td class="rating">⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">电子书</td> <td class="format-list">EPUB</td> <td class="rating">⭐⭐⭐⭐</td> </tr> <tr> <td class="format-type">其他格式</td> <td class="format-list">XML, RTF, Markdown (MD)</td> <td class="rating">⭐⭐⭐⭐</td> </tr> </tbody> </table> </div> </div> </div></body></html>

## 技术栈

- Java 8+
- Aspose Words 文档处理
- Apache Tika 文件类型检测
- Hutool 工具集
- Lombok 简化代码
- auto-mapping-map 映射工具

## 安装说明

### Maven

```xml
<dependency>
    <groupId>io.github.anqinworks</groupId>
    <artifactId>smart-doc-render-engine</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 快速开始

### 基本用法

一、文档转换

```java
File pdf = CF.create("奖励金申报表.docx").toFile(DocumentFormat.PDF);

System.out.println("PDF文档生成成功：" + pdf.getAbsolutePath());
```
二、文档填充并转换

```java
ClassPathResource resource = new ClassPathResource("template/奖励金申报表.docx");

File tamplateFile = resource.getFile();

FT<RewardReturn> ft = FT.of(get(), tamplateFile);

File recordFile = ft.getRecordFile();

System.out.println("记录文档生成成功：" + recordFile.getAbsolutePath());
File currentFile = ft.getCurrentFile();

System.out.println("当前文档生成成功：" + currentFile.getAbsolutePath());
File pdf = ft.convert(DocumentFormat.PDF);

System.out.println("转换文档生成成功：" + pdf.getAbsolutePath());
```

### 自定义模板

继承 `Template` 类创建自定义模板：

```java
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class MyTemplate extends Template {
    private String name;
    private String author;
    private String content;
    private List<Product> products;
    
    @Override
    public String getFileName() {
        return "自定义模板";
    }
}
```

## 占位符使用

### 文本占位符

在 Word 文档中使用 `${变量名}` 格式的占位符，例如：

```
文档名称：${name}
作者：${author}
内容：${content}
```

### 图片占位符

使用特定注解标记图片字段：

```java
/**
 * 头像
 */
@Placeholder(value = PlaceholderType.PIC, pic = @Placeholder.PicWord(width = 90, height = 120))
private String avatar;
```

### 动态表格行

使用集合类型和特定注解处理表格行：

```java
/**
 * 孩子表格
 */
@Placeholder(value = PlaceholderType.DYNAMIC_ROW,
        dynamicRow = @Placeholder.DynamicRow(ChildrenTable.class))
private List<ChildrenTable> childrenTable;
```

## 许可证

本项目采用 Apache License 2.0 许可证。详情请参阅 [LICENSE](http://www.apache.org/licenses/LICENSE-2.0.txt) 文件。