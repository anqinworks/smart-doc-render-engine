package cc.anqin.doc.test.entity;

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
public class RewardReturn extends Template {



    /**
     * 政务国家标准名称
     */
    protected String nationalStandardName;

    /**
     * 提交日期
     */
    protected String submissionDate;

    /**
     * 生日
     */
    protected String birthday;

    /**
     * 当前地址
     */
    protected String currentAddress;

    /**
     * 性别
     */
    protected String gender;

    /**
     * 户籍地址
     */
    protected String householdAddress;

    /**
     * 电话
     */
    protected String phone;

    /**
     * 身份证
     */
    protected String idCard;

    /**
     * 民族
     */
    protected String nation;

    /**
     * 姓名
     */
    protected String personName;

    /**
     * 籍贯
     */
    protected String domicile;

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
    @Placeholder(value = PlaceholderType.PIC, pic = @Placeholder.PicWord(width = 90, height = 120))
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
     * 宽度
     */
    @Override
    public double getConvertWidth() {
        return 200;
    }

    /**
     * 高度
     */
    @Override
    public double getConvertHeight() {
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
