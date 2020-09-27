package dfh.action.points.vo;

/**
 * Created by Qcon on 2017/9/19.
 */
public class PointsPresentVo {

    /*private static final Gson gson = new Gson();

    private Integer id;
    private String name; // 奖品名称
    private String img; // 配置的展示图片
    private String type; // 积分奖品类型
    private List<RemarkDetail> remark; // 实物奖品的具体数据json
    private Integer order; // 展示顺序
    private Integer status; // 奖品是否存在，逻辑删除   存在为1  删除为0
    private String range;
    private String levelRange; // 折扣之后的范围

    private static class RemarkDetail {
        private String property;
        private Double point;
        private Double levelPoint;
        private Integer multi;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public Double getPoint() {
            return point;
        }

        public void setPoint(Double point) {
            this.point = point;
        }

        public Double getLevelPoint() {
            return levelPoint;
        }

        public void setLevelPoint(Double levelPoint) {
            this.levelPoint = levelPoint;
        }

        public Integer getMulti() {
            return multi;
        }

        public void setMulti(Integer multi) {
            this.multi = multi;
        }
    }

    public static PointsPresentVo transferToVo (PointsPresent pointsPresent, Users user){
        PointsPresentVo pointsPresentVo = new PointsPresentVo();
        pointsPresentVo.setId(pointsPresent.getId());
        pointsPresentVo.setName(pointsPresent.getName());
        pointsPresentVo.setImg(pointsPresent.getImg());
        pointsPresentVo.setType(pointsPresent.getType());
        List<RemarkDetail> remarkDetails = gson.fromJson(pointsPresent.getRemark(), new TypeToken<List<RemarkDetail>>(){}.getType());
        if (user != null){
            Double multi = PointsAction.levelConfig.get(user.getLevel());
            if (pointsPresent.getType().equals(PointsPresentType.LuckyDraw.getName())){
                multi = 1.00;
            }
            for (RemarkDetail remarkDetail : remarkDetails){
                remarkDetail.setPoint(remarkDetail.getPoint());
                remarkDetail.setLevelPoint(remarkDetail.getPoint() * multi);
            }

            String range = pointsPresent.getRange();
            if (StringUtils.isNotBlank(range)){
                pointsPresentVo.setRange(range);
                String[] ranges = range.split("~");
                if (ranges.length > 1){
                    Double min = Double.parseDouble(ranges[0]) * multi;
                    Double max = Double.parseDouble(ranges[1].substring(0, ranges[1].length() - 1)) * multi;
                    pointsPresentVo.setLevelRange(String.format("%.0f", min) + "~" + String.format("%.0f", max) + "分");
                } else if (ranges.length == 1){
                    Double cost = Double.parseDouble(ranges[0].substring(0, ranges[0].length() - 1)) * multi;
                    pointsPresentVo.setLevelRange(String.format("%.0f", cost) + "分");
                }
            } else {
                pointsPresentVo.setRange("0分");
                pointsPresentVo.setLevelRange("0分");
            }
        } else {
            String range = pointsPresent.getRange();
            if (StringUtils.isNotBlank(range)){
                for (RemarkDetail remarkDetail : remarkDetails){
                    remarkDetail.setLevelPoint(remarkDetail.getPoint());
                }
                pointsPresentVo.setRange(pointsPresent.getRange());
                pointsPresentVo.setLevelRange(pointsPresent.getRange());
            } else {
                pointsPresentVo.setRange("0分");
                pointsPresentVo.setLevelRange("0分");
            }

        }
        pointsPresentVo.setRemark(remarkDetails);
        pointsPresentVo.setOrder(pointsPresent.getOrder());
        pointsPresentVo.setStatus(pointsPresent.getStatus());
        return pointsPresentVo;
    }

    public static List<PointsPresentVo> transferToVoList (List<PointsPresent> pointsPresents, Users user){
        List<PointsPresentVo> pointsPresentVos = new ArrayList<PointsPresentVo>();
        for (PointsPresent pointsPresent : pointsPresents){
            pointsPresentVos.add(transferToVo(pointsPresent, user));
        }
        return pointsPresentVos;
    }

    public static PointsPresent transferToModel (PointsPresentVo pointsPresentVo){
        PointsPresent pointsPresent = new PointsPresent();
        pointsPresent.setId(pointsPresentVo.getId());
        pointsPresent.setName(pointsPresentVo.getName());
        pointsPresent.setImg(pointsPresentVo.getImg());
        pointsPresent.setType(pointsPresentVo.getType());
        pointsPresent.setRemark(gson.toJson(pointsPresentVo.getRemark()));
        pointsPresent.setOrder(pointsPresentVo.getOrder());
        pointsPresent.setStatus(pointsPresentVo.getStatus());

        List<Double> points = new ArrayList<Double>();
        for (RemarkDetail remarkDetail : pointsPresentVo.getRemark()){
            points.add(remarkDetail.getPoint());
        }
        Collections.sort(points);

        if (points.size() > 1){
            pointsPresent.setRange(points.get(0) + "~" + points.get(points.size() - 1) + "分");
        } else if (points.size() == 1){
            pointsPresent.setRange(points.get(0) + "分");
        }
        return pointsPresent;
    }

    public static List<PointsPresent> transferToModelList (List<PointsPresentVo> pointsPresentVos){
        List<PointsPresent> pointsPresents = new ArrayList<PointsPresent>();
        for (PointsPresentVo pointsPresentVo : pointsPresentVos){
            pointsPresents.add(transferToModel(pointsPresentVo));
        }
        return pointsPresents;
    }

    public static Gson getGson() {
        return gson;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<RemarkDetail> getRemark() {
        return remark;
    }

    public void setRemark(List<RemarkDetail> remark) {
        this.remark = remark;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getLevelRange() {
        return levelRange;
    }

    public void setLevelRange(String levelRange) {
        this.levelRange = levelRange;
    }*/
}
