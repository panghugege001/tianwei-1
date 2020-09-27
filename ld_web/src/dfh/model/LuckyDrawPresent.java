package dfh.model;


public class LuckyDrawPresent {


    // 编号，主键，自动增长
    private Long id;
    // 显示名称
    private String title;
    // 显示图片
    private String imageUrl;
    // 商品信息对象
    private PointsPresent pointsPresent;
    // 抽奖属性
    private String property;
    // 权重
    private Long weight;
    // 创建人
    private String createUser;
    // 创建时间
    private String createTime;
    // 修改人
    private String updateUser;
    // 修改时间
    private String updateTime;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PointsPresent getPointsPresent() {
        return pointsPresent;
    }

    public void setPointsPresent(PointsPresent pointsPresent) {
        this.pointsPresent = pointsPresent;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }



}
