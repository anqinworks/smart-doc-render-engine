# Smart Doc Render Engine

## 项目介绍

Smart Doc Render Engine 是一个基于 Java 的智能文档渲染引擎，支持 DOCX 模板 和 HTML 动态渲染，并导出为 PDF 或其他格式。它提供灵活的模板变量替换、动态内容生成，适用于报告生成、合同自动化、电子发票等场景。

## 功能特点

- **多格式支持**：支持 DOCX 模板和 HTML 动态渲染
- **灵活的占位符替换**：支持文本、图片、动态表格行等多种占位符类型
- **格式转换**：支持将文档转换为 PDF 等多种格式
- **链式调用**：API 设计采用流畅的链式调用风格，使用简单直观
- **类型安全**：使用泛型确保类型安全的模板数据填充

## 技术栈

- Java 8+
- Aspose Words 文档处理
- Apache Tika 文件类型检测
- Hutool 工具集
- Lombok 简化代码

## 安装说明

### Maven

```xml
<dependency>
    <groupId>cc.anqin</groupId>
    <artifactId>smart-doc-render-engine</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 快速开始

### 基本用法

```java
// 创建模板数据实体
MyTemplate template = new MyTemplate()
    .setName("示例文档")
    .setAuthor("张三")
    .setContent("这是文档内容");

// 使用模板填充工具处理文档
Pair<File, File> result = FT.of(template, new File("template.docx"))
    .fillAndConvert();

// 获取生成的文档
File outputFile = result.getValue();
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
@Placeholder(type = PlaceholderType.IMAGE)
private String logo;
```

### 动态表格行

使用集合类型和特定注解处理表格行：

```java
@Placeholder(type = PlaceholderType.DYNAMIC_ROW)
private List<Product> products;
```

## 格式转换

支持将文档转换为多种格式：

```java
// 转换为PDF
File pdfFile = FT.of(template, templateFile)
    .fillAndConvert(FileType.PDF);
```

## 许可证

本项目采用 Apache License 2.0 许可证。详情请参阅 [LICENSE](http://www.apache.org/licenses/LICENSE-2.0.txt) 文件。

## 联系方式

- 作者：Mr.An
- 邮箱：anqin2023@gmail.com
- GitHub：[https://github.com/anqinworks/smart-doc-render-engine](https://github.com/anqinworks/smart-doc-render-engine)