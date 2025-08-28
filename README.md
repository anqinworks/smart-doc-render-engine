# Smart Doc Render Engine

Smart Doc Render Engine 是一个基于 Java 的智能文档渲染引擎，支持 DOCX 模板和 HTML 动态渲染，并导出为 PDF 或其他格式。

## 主要特性

- 智能模板填充
- 多格式转换
- 高性能处理
- 字体管理
- 链式API

## 快速开始

```java
// 创建模板数据
ContractTemplate template = new ContractTemplate()
    .setCompanyName("示例公司")
    .setContractNumber("CT-2024-001");

// 执行模板填充
File templateFile = new File("contract_template.docx");
Pair<File, File> result = FT.of(template, templateFile)
    .fillAndConvert();

// 转换为PDF
File pdfFile = FT.of(template, templateFile)
    .convert(DocumentFormat.PDF);
```

## 安装

```xml
<dependency>
    <groupId>cc.anqin</groupId>
    <artifactId>smart-doc-render-engine</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 文档

详细文档请参考项目源码中的JavaDoc注释。