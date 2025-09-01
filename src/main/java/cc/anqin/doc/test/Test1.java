package cc.anqin.doc.test;

import cc.anqin.doc.FT;
import cc.anqin.doc.convert.DocumentFormat;
import cc.anqin.doc.test.entity.RewardReturn;
import cn.hutool.core.io.resource.ClassPathResource;

import java.io.File;
import java.util.Arrays;

/**
 *
 * @author Mr.An
 * @since 2025/8/19
 */
public class Test1 {


    // 默认数据的构造方法
    public static RewardReturn get() {
        RewardReturn defaultData = new RewardReturn();

        // 设置默认值
        defaultData.setDistrict("天山区");
        defaultData.setStreet("幸福街889号");
        defaultData.setCommunity("幸福社区");
        defaultData.setSpouseName("张三");
        defaultData.setSpouseBirthday("1990-01-01");
        defaultData.setMarriageTime("2010-05-20");
        defaultData.setAvatar("https://c-ssl.dtstatic.com/uploads/item/202003/18/20200318091443_yoxzb.thumb.400_0.jpg");
        defaultData.setCertName("结婚证书");
        defaultData.setCertNo("1234567890");
        defaultData.setIssuanceTime("2010-05-21");
        defaultData.setHowMuchMoney("5000");
        defaultData.setCommunityAgent("https://scupload.minio.90tech.cn/static-file/smart-community-platform/%E7%AD%BE%E5%90%8D.png");
        defaultData.setStreetStamp("https://scupload.minio.90tech.cn/static-file/smart-community-platform/seal.png");
        defaultData.setDistrictSignature("https://scupload.minio.90tech.cn/static-file/smart-community-platform%2F.png");
        defaultData.setMarriageState("已婚");
        defaultData.setBankCardNumber("622202******1234");
        defaultData.setApplyDate("2024-12-25");
        defaultData.setChildrenTable(Arrays.asList(
                new RewardReturn.ChildrenTable("胖胖", "2012-08-15"),
                new RewardReturn.ChildrenTable("嘿嘿", "2012-08-16")
        ));
        defaultData.setBirthday("1988-12-24");
        defaultData.setCurrentAddress("幸福社区/幸福小区/第三网格/42号楼/1单元/3层/301");
        defaultData.setGender("男");
        defaultData.setHouseholdAddress("陕西省/西安市/未央区/幸福社区/幸福社区居委会");
        defaultData.setPhone("13800138000");
        defaultData.setIdCard("110101199003071234");
        defaultData.setNation("汉族");
        defaultData.setPersonName("赵六");
        defaultData.setDomicile("西安市");

        return defaultData;
    }

    public static void main(String[] args) {
        System.setProperty("smart.doc.temp.dir", "D:/temp/");

        // 输入和输出文件路径
        ClassPathResource resource = new ClassPathResource("template/奖励金申报表.docx");

        File tamplateFile = resource.getFile();

        FT<RewardReturn> ft = FT.of(get(), tamplateFile, DocumentFormat.PDF).fer();
        try {
            File recordFile = ft.getRecordFile();

            System.out.println("记录文档生成成功：" + recordFile.getAbsolutePath());
            File currentFile = ft.getCurrentFile();

            System.out.println("当前文档生成成功：" + currentFile.getAbsolutePath());
        } finally {
            ft.clearAll();
        }
    }
}