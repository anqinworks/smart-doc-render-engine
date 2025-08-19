package cc.anqin.doc.test;

import cc.anqin.doc.entity.Template;
import cc.anqin.doc.word.annotation.Placeholder;
import cc.anqin.doc.word.enums.PlaceholderType;
import cc.anqin.processor.annotation.AutoToMap;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 计划生育奖励
 *
 * @author Mr.An
 * @date 2024/12/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@AutoToMap
public class UrumqiCityFamilyPlanningRewardReturn extends Template {

    /**
     * 区
     */
    private String district;

    /**
     * 街
     */
    private String street;

    /**
     * 社区
     */
    private String community;

    /**
     * 配偶姓名
     */
    private String spouseName;

    /**
     * 配偶生日
     */
    private String spouseBirthday;

    /**
     * 结婚时间
     */
    private String marriageTime;

    /**
     * 头像
     */
    @Placeholder(value = PlaceholderType.PIC, pic = @Placeholder.PicWord(width = 110, height = 120))
    private String avatar;

    /**
     * 证书名称
     */
    private String certName;

    /**
     * 证书号
     */
    private String certNo;

    /**
     * 证书发行时间
     */
    private String issuanceTime;

    /**
     * 已享受奖励金（${alreadyEnjoyed}）元
     */
    private String alreadyEnjoyed;

    /**
     * 本次申报享受（${howMuchMoney}）元
     */
    private String howMuchMoney;

    /**
     * 社区经办人签名
     */
    @Placeholder(value = PlaceholderType.PIC, pic = @Placeholder.PicWord(width = 50, height = 50))
    private String communityAgent;

    /**
     * 街道盖章
     */
    @Placeholder(value = PlaceholderType.PIC, pic = @Placeholder.PicWord(width = 100, height = 100))
    private String streetStamp;

    /**
     * 区（县）卫键委主任签名
     */
    @Placeholder(value = PlaceholderType.PIC, pic = @Placeholder.PicWord(width = 50, height = 50))
    private String districtSignature;

    /**
     * 婚姻状况 离婚/离偶
     */
    private String marriageState;

    /**
     * 银行卡号
     */
    private String bankCardNumber;

    /**
     * 申请日期
     */
    private String applyDate;


    /**
     * 孩子表格
     */
    @Placeholder(value = PlaceholderType.DYNAMIC_ROW,
            dynamicRow = @Placeholder.DynamicRow(ChildrenTable.class))
    private List<ChildrenTable> childrenTable;


    /**
     * 获取文件名称
     *
     * @return {@link String }
     */
    @Override
    public String getFileName() {
        return "计划生育奖励";
    }

    /**
     * 宽度
     */
    @Override
    public int getConvertWidth() {
        return 200;
    }

    /**
     * 高度
     */
    @Override
    public int getConvertHeight() {
        return 500;
    }


    /**
     * 孩子信息
     *
     * @author <a href="https://blog.anqin.cc/">Mr.An</a>
     * @date 2025/08/19
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @AutoToMap
    public static class ChildrenTable implements Serializable {

        /**
         * 孩子名字
         */
        private String childrenName;

        /**
         * 孩子生日
         */
        private String childrenBirthday;
    }
}
