package com.nnti.common.model.vo;

import java.util.Date;

public class CreditLogExtend {
	
	// 编号，对应creditlogs表主键
    private Long id;
    // 订单号
    private String orderId;
    // 创建时间
    private Date createTime;
    // 备注
    private String remark;
    // 扩展字段1
    private String ext1;
    // 扩展字段2
    private Double ext2;
    // 扩展字段3
    private Integer ext3;
    // 扩展字段4
    private String ext4;
    
    public CreditLogExtend() {}
    
    public CreditLogExtend(String orderId) {
        this.orderId = orderId;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public Double getExt2() {
        return ext2;
    }

    public void setExt2(Double ext2) {
        this.ext2 = ext2;
    }

    public Integer getExt3() {
        return ext3;
    }

    public void setExt3(Integer ext3) {
        this.ext3 = ext3;
    }

    public String getExt4() {
        return ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }
}