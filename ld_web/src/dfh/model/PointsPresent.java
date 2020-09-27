package dfh.model;


import javax.persistence.Transient;

public class PointsPresent {

    // 编号，主键，自动增长
    private Long id;
    // 商品名称
    private String name;
    // 商品图片
    private String imageUrl;
    // 商品类型
    private String type;
    // 商品属性
    private String property;
    // 显示顺序
    private Integer order;
    // 商品区间值
    private String range;
    // 商品简介
    private String summary;
    // 活动开始时间
    private String startTime;
    // 活动结束时间
    private String endTime;
    // 删除标志，Y(已删除)/N(未删除)
    private String isDelete;
    // 显示标示，Y(需要显示)/N(不需要显示)
    private String isShow;
    // 创建人
    private String createUser;
    // 创建时间
    private String createTime;
    // 修改人
    private String updateUser;
    @Transient
    private String vipSaveRange;

    private Double vipSave;

    public Double getVipSave() {
        return vipSave;
    }

    public void setVipSave(Double vipSave) {
        this.vipSave = vipSave;
    }

    public String getVipSaveRange() {
        return vipSaveRange;
    }

    public void setVipSaveRange(String vipSaveRange) {
        this.vipSaveRange = vipSaveRange;
    }

    // 修改时间
    private String updateTime;
    @javax.persistence.Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }



    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }



    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
