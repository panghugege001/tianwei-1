
package dfh.action.vo.ebetapp;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BetHistory {

    private String betHistoryId;
    private Integer subChannelId;
    //游戏类型
    private GameType gameType;
    //用户的有效下注
    private Long validBet;
    //下注项目
    private List<BetMap> betMap = new ArrayList<BetMap>();
    //开牌结果
    private List<Integer> judgeResult = new ArrayList<Integer>();
    //牌局号
    private String roundNo;
    //EBetApp记录的UserId
    private Integer userId;
    private String username;
    //派彩
    private Double payout;
    private long payoutTime;
    private long createTime;
    //轮盘游戏，结果数字
    private Integer number;
    //龙虎游戏，虎的牌型
    private Integer tigerCard;
    //龙虎游戏，龙的牌型
    private Integer dragonCard;
    //闲家牌型，见牌型说明
    private List<Integer> playerCards = new ArrayList<Integer>();
    //庄家牌型，见牌型说明
    private List<Integer> bankerCards = new ArrayList<Integer>();
    //骰宝使用骰子
    private List<Integer> allDices = new ArrayList<Integer>();


    public static BetHistory convert(JSONObject jsonObject) {
        BetHistory vo = new BetHistory();
        vo.setBetHistoryId(jsonObject.getString("betHistoryId"));
        if (jsonObject.has("subChannelId")) {
            vo.setSubChannelId(jsonObject.getInt("subChannelId"));
        }
        if (jsonObject.has("gameType")) {
            vo.setGameType(GameType.getType(jsonObject.getInt("gameType")));
        }
        if (jsonObject.has("validBet")) {
            vo.setValidBet(jsonObject.getLong("validBet"));
        }
        if (jsonObject.has("roundNo")) {
            vo.setRoundNo(jsonObject.getString("roundNo"));
        }
        if (jsonObject.has("userId")) {
            vo.setUserId(jsonObject.getInt("userId"));
        }
        if (jsonObject.has("username")) {
            vo.setUsername(jsonObject.getString("username"));
        }
        if (jsonObject.has("payout")) {
            vo.setPayout(jsonObject.getDouble("payout"));
        }
        if (jsonObject.has("payoutTime")) {
            vo.setPayoutTime(jsonObject.getInt("payoutTime"));
        }
        if (jsonObject.has("createTime")) {
            vo.setCreateTime(jsonObject.getInt("createTime"));
        }
        if (jsonObject.has("number")) {
            vo.setNumber(jsonObject.getInt("number"));
        }
        if (jsonObject.has("tigerCard")) {
            vo.setTigerCard(jsonObject.getInt("tigerCard"));
        }
        if (jsonObject.has("dragonCard")) {
            vo.setDragonCard(jsonObject.getInt("dragonCard"));
        }
        if (jsonObject.has("judgeResult")) {
            List<Integer> judgeResult = getIntegerList(jsonObject.getJSONArray("judgeResult"));
            vo.setJudgeResult(judgeResult);
        }
        if (jsonObject.has("playerCards")) {
            List<Integer> playerCards = getIntegerList(jsonObject.getJSONArray("playerCards"));
            vo.setPlayerCards(playerCards);
        }
        if (jsonObject.has("bankerCards")) {
            List<Integer> bankerCards = getIntegerList(jsonObject.getJSONArray("bankerCards"));
            vo.setBankerCards(bankerCards);
        }
        if (jsonObject.has("allDices")) {
            List<Integer> allDices = getIntegerList(jsonObject.getJSONArray("allDices"));
            vo.setAllDices(allDices);
        }
        if (jsonObject.has("betMap")) {
            List<JSONObject> objList = getJsonObjectList(jsonObject.getJSONArray("betMap"));
            List<BetMap> list = convertToBetMap(objList);
            vo.setBetMap(list);
        }
        return vo;
    }

    private static List<JSONObject> getJsonObjectList(JSONArray array) {
        List<JSONObject> list = new LinkedList<JSONObject>();
        Object[] objects = array.toArray();
        for (Object o : objects) {
            list.add((JSONObject) o);
        }
        return new ArrayList<JSONObject>(list);
    }

    private static List<BetMap> convertToBetMap(List<JSONObject> historiesArray) {
        List<BetMap> list = new LinkedList<BetMap>();
        for (JSONObject jsonObject : historiesArray) {
            BetMap vo = BetMap.convert(jsonObject);
            list.add(vo);
        }
        return new ArrayList<BetMap>(list);
    }

    private static List<Integer> getIntegerList(JSONArray array) {
        List<Integer> list = new LinkedList<Integer>();
        Object[] objects = array.toArray();
        for (Object o : objects) {
            list.add((Integer) o);
        }
        return new ArrayList<Integer>(list);
    }

    public boolean isValid() {
        return getUserId() != null
                && getUserId() != 0
                && getUsername() != null && !getUsername().isEmpty()
                && getRoundNo() != null && !getRoundNo().isEmpty()
                && getRoundNo() != null && !getRoundNo().isEmpty()
                && getBetMap() != null && !getBetMap().isEmpty()
                && getCreateTime() != null && getPayoutTime() != null
                ;
    }

    /**
     * @return The validBet
     */
    public Long getTotalBet() {
        List<BetMap> betMap = getBetMap();
        Long sumOfBetCredit = 0L;
        for (BetMap tempMap : betMap) {
            sumOfBetCredit += tempMap.getBetMoney();
        }
        return sumOfBetCredit;
    }


    /**
     * @return The validBet
     */
    public Long getValidBet() {
        return validBet;
    }


    /**
     * 闲家牌型，见牌型说明
     *
     * @return The playerCards
     */
    public List<Integer> getPlayerCards() {
        return playerCards;
    }

    /**
     * @param playerCards The playerCards
     */
    public void setPlayerCards(List<Integer> playerCards) {
        this.playerCards = playerCards;
    }

    /**
     * 1 百家乐
     * 2 龙虎
     * 3 骰宝
     * 4轮盘
     * 5水果机
     *
     * @return The gameType
     */
    public GameType getGameType() {
        return gameType;
    }

    /**
     * @param gameType The gameType
     */
    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    /**
     * 派彩
     *
     * @return The payout
     */
    public Double getPayout() {
        return payout;
    }

    /**
     * @param payout The payout
     */
    public void setPayout(Double payout) {
        this.payout = payout;
    }

    /**
     * @return The betMap
     */
    public List<BetMap> getBetMap() {
        return betMap;
    }

    /**
     * @param betMap The betMap
     */
    public void setBetMap(List<BetMap> betMap) {
        this.betMap = betMap;
    }

    /**
     * @return The judgeResult
     */
    public List<Integer> getJudgeResult() {
        return judgeResult;
    }

    /**
     * @param judgeResult The judgeResult
     */
    public void setJudgeResult(List<Integer> judgeResult) {
        this.judgeResult = judgeResult;
    }

    /**
     * @return The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId The userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 下注记录ID， 本下注记录的唯一ID
     *
     * @return The betHistoryId
     */
    public String getBetHistoryId() {
        return betHistoryId;
    }

    /**
     * @param betHistoryId The betHistoryId
     */
    public void setBetHistoryId(String betHistoryId) {
        this.betHistoryId = betHistoryId;
    }

    /**
     * @return The payoutTime
     */
    public Long getPayoutTime() {
        return payoutTime;
    }

    /**
     * 对方过来的资料有去除末3码
     * @param payoutTime The payoutTime
     */
    public void setPayoutTime(Integer payoutTime) {
        this.payoutTime = Long.valueOf(payoutTime.toString()+"000");
    }

    /**
     * @return The createTime
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 对方过来的资料有去除末3码
     * @param createTime The createTime
     */
    public void setCreateTime(Integer createTime) {
        this.createTime = Long.valueOf(createTime.toString()+"000");
    }

    /**
     * @return The roundNo
     */
    public String getRoundNo() {
        return roundNo;
    }

    /**
     * @param roundNo The roundNo
     */
    public void setRoundNo(String roundNo) {
        this.roundNo = roundNo;
    }

    /**
     * 庄家牌型，见牌型说明
     *
     * @return The bankerCards
     */
    public List<Integer> getBankerCards() {
        return bankerCards;
    }

    /**
     * @param bankerCards The bankerCards
     */
    public void setBankerCards(List<Integer> bankerCards) {
        this.bankerCards = bankerCards;
    }

    /**
     * @return The subChannelId
     */
    public Integer getSubChannelId() {
        return subChannelId;
    }

    /**
     * @param subChannelId The subChannelId
     */
    public void setSubChannelId(Integer subChannelId) {
        this.subChannelId = subChannelId;
    }

    /**
     * @param validBet The validBet
     */
    public void setValidBet(Long validBet) {
        this.validBet = validBet;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number The number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @return The tigerCard
     */
    public Integer getTigerCard() {
        return tigerCard;
    }

    /**
     * @param tigerCard The tigerCard
     */
    public void setTigerCard(Integer tigerCard) {
        this.tigerCard = tigerCard;
    }

    /**
     * @return The dragonCard
     */
    public Integer getDragonCard() {
        return dragonCard;
    }

    /**
     * @param dragonCard The dragonCard
     */
    public void setDragonCard(Integer dragonCard) {
        this.dragonCard = dragonCard;
    }

    public List<Integer> getAllDices() {
        return allDices;
    }

    public void setAllDices(List<Integer> allDices) {
        this.allDices = allDices;
    }
}
