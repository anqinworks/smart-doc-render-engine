package cc.anqin.doc.test;

import cc.anqin.doc.FT;
import cc.anqin.doc.convert.ConverterFileFactory;
import cc.anqin.doc.convert.FileType;
import cc.anqin.doc.word.PlaceholderFactory;
import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.util.Arrays;

/**
 *
 * @author Mr.An
 * @since 2025/8/19
 */
public class Test1 {

    // 默认数据的构造方法
    public static UrumqiCityFamilyPlanningRewardReturn get() {
        UrumqiCityFamilyPlanningRewardReturn defaultData = new UrumqiCityFamilyPlanningRewardReturn();

        // 设置默认值
        defaultData.setDistrict("test区");
        defaultData.setStreet("test号");
        defaultData.setCommunity("test社区");
        defaultData.setSpouseName("张三");
        defaultData.setSpouseBirthday("1990-01-01");
        defaultData.setMarriageTime("2010-05-20");
        defaultData.setAvatar("https://scupload.minio.90tech.cn/smart-community-public/image_1731562814641.jpg");
        defaultData.setCertName("结婚证书");
        defaultData.setCertNo("1234567890");
        defaultData.setIssuanceTime("2010-05-21");
        defaultData.setHowMuchMoney("5000");
        defaultData.setCommunityAgent("https://scupload.minio.90tech.cn/static-file/smart-community-platform/%E7%AD%BE%E5%90%8D.png");
        defaultData.setStreetStamp("https://scupload.minio.90tech.cn/static-file/smart-community-platform/seal.png");
        defaultData.setDistrictSignature("https://scupload.minio.90tech.cn/static-file/smart-community-platform%2F签名-英文.png");
        defaultData.setMarriageState("已婚");
        defaultData.setBankCardNumber("622202******1234");
        defaultData.setApplyDate("2024-12-25");
        defaultData.setChildrenTable(Arrays.asList(
                new UrumqiCityFamilyPlanningRewardReturn.ChildrenTable("胖胖","2012-08-15"),
                new UrumqiCityFamilyPlanningRewardReturn.ChildrenTable("嘿嘿","2012-08-16")
        ));

        return defaultData;
    }

    public static void main(String[] args) throws Exception {

        // 输入和输出文件路径
        File templateFile = new File("/Users/anqin/Documents/company_project/新疆久领科技/山润社区/政务生成-模板文件/城镇计划生育家庭奖励金申报表.docx");

        UrumqiCityFamilyPlanningRewardReturn source = get();

        File file = FT.of(source, templateFile).convert(FileType.PDF);

        System.out.println("文档生成成功：" + file.getAbsolutePath());
    }
}
