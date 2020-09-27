package dfh.model;


public class PointsPresentRecord {

    // 编号，主键，自动增长
    private Long id;
    // 商品编号，对应points_present表的id
    private Long pointsPresentId;
    // 商品属性
    private String property;
    // 实物奖品收货人姓名
    private String receiverName;
    // 实物奖品收货人电话
    private String phone;
    // 实物奖品收货人地址
    private String address;
    // 实物奖品收货人订单号
    private String orderNo;
    // 状态，已兑换/已抽奖/已寄送/已完成
    private String status;
    // 玩家账号
    private String loginName;
    // 领取商品所耗积分
    private Double points;
    // 领取商品名称
    private String name;
    // 领取商品类型
    private String type;
    // 抽奖编号，对应lucky_draw_present表的id
    private Long luckyDrawPresentId;
    // 商品类型，对应lucky_draw_present表的points_present_type
    private String luckyDrawPresentType;
    // 创建人
    private String createUser;
    // 创建时间
    private String createTime;
    // 修改人
    private String updateUser;
    // 修改时间
    private String updateTime;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPointsPresentId() {
        return pointsPresentId;
    }

    public void setPointsPresentId(Long pointsPresentId) {
        this.pointsPresentId = pointsPresentId;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getLuckyDrawPresentId() {
        return luckyDrawPresentId;
    }

    public void setLuckyDrawPresentId(Long luckyDrawPresentId) {
        this.luckyDrawPresentId = luckyDrawPresentId;
    }

    public String getLuckyDrawPresentType() {
        return luckyDrawPresentType;
    }

    public void setLuckyDrawPresentType(String luckyDrawPresentType) {
        this.luckyDrawPresentType = luckyDrawPresentType;
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
