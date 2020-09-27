
package dfh.action.vo.ebetapp;

import dfh.model.PlatformData;
import dfh.utils.Arith;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class EBetAppHistory {

    private Integer count;
    private List<BetHistory> betHistories = new ArrayList<BetHistory>();
    private Integer status;


    public static EBetAppHistory convertFromJson(String result) {
        EBetAppHistory vo = new EBetAppHistory();
        JSONObject jsonObject = JSONObject.fromObject(result);
        vo.setStatus(jsonObject.getInt("status"));
        if (jsonObject.has("count")) {
            vo.setCount(jsonObject.getInt("count"));
        }
        if (jsonObject.has("betHistories") && jsonObject.getJSONArray("betHistories") != null) {
            List<JSONObject> jsonObjects = getJsonObjectList(jsonObject.getJSONArray("betHistories"));
            List<BetHistory> betHistories = convertToBetBetHistory(jsonObjects);
            vo.setBetHistories(betHistories);
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

    private static List<BetHistory> convertToBetBetHistory(List<JSONObject> historiesArray) {
        List<BetHistory> betHistories = new LinkedList<BetHistory>();
        for (JSONObject jsonObject : historiesArray) {
            BetHistory vo = BetHistory.convert(jsonObject);
            betHistories.add(vo);
        }
        return new ArrayList<BetHistory>(betHistories);
    }

    public List<PlatformData> convertToProfits(Date startTime, Date endTime) {
        if (getBetHistories() == null || getBetHistories().isEmpty()) {
            return null;
        }
        Map<String, List<BetHistory>> historyMap = splitByUserName();
        return convertToProfits(historyMap, startTime, endTime);
    }

    public Map<String, List<BetHistory>> splitByUserName() {
        Map<String, List<BetHistory>> historyMap = new HashMap<String, List<BetHistory>>();
        for (BetHistory history : betHistories) {
            String userName = history.getUsername();
            if (userName == null) {
                continue;
            }
            int dashCount = StringUtils.countMatches(userName, "-");
            //跳过游客资料
            if (userName.length() > 30 && dashCount > 3) {
                continue;
            }
            if (historyMap.get(userName) == null) {
                List<BetHistory> list = new ArrayList<BetHistory>();
                list.add(history);
                historyMap.put(userName, list);
            } else {
                historyMap.get(userName).add(history);
            }
        }
        return historyMap;
    }

    public List<PlatformData> convertToProfits(Map<String, List<BetHistory>> historyMap, Date startTime, Date endTime) {
        List<PlatformData> profits = new ArrayList<PlatformData>();
        for (String key : historyMap.keySet()) {
            List<BetHistory> list = historyMap.get(key);
            PlatformData profit = getProfit(list, startTime, endTime);
            profits.add(profit);
        }
        return profits;
    }


    public PlatformData getProfit(Date startTime, Date endTime) {
        if (getBetHistories() == null || getBetHistories().isEmpty()) {
            return null;
        }
        return getProfit(getBetHistories(), startTime, endTime);
    }

    private PlatformData getProfit(List<BetHistory> listByUserName, Date startTime, Date endTime) {
        final String platformCode = "ebetapp";
        Long sumOfBet = 0L;
        Long sumOfVaildBet = 0L;
        Double sumOfPayout = 0.0;
        for (BetHistory tempHistory : listByUserName) {
            sumOfBet += tempHistory.getTotalBet();
            sumOfVaildBet += tempHistory.getValidBet();
            sumOfPayout = Arith.add(sumOfPayout, tempHistory.getPayout());
        }
        BetHistory history = listByUserName.get(0);
        PlatformData profit = PlatformDataBuilder
                .anProfit(platformCode)
                .withLoginname(history.getUsername())
                .withTotalBet(sumOfBet)
                .withVaildBetCredit(sumOfVaildBet.doubleValue())
                .withPayOut(sumOfPayout)
                .withStartTime(startTime)
                .withEndTime(endTime)
                .build();
        return profit;
    }


    /**
     * @return The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return The betHistories
     */
    public List<BetHistory> getBetHistories() {
        return betHistories;
    }

    /**
     * @param betHistories The betHistories
     */
    public void setBetHistories(List<BetHistory> betHistories) {
        this.betHistories = betHistories;
    }

    /**
     * @return The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

}
