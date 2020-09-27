
package dfh.action.vo.ebetapp;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BetMap {

    private List<Integer> betNumber = new ArrayList<Integer>();
    private Integer betType;
    private Integer betMoney;

    public static BetMap convert(JSONObject jsonObject) {
        BetMap vo = new BetMap();
        if (jsonObject.has("betMoney")) {
            vo.setBetMoney(jsonObject.getInt("betMoney"));
        }
        if (jsonObject.has("betType")) {
            vo.setBetType(jsonObject.getInt("betType"));
        }
        if (jsonObject.has("betNumber")) {
            List<Integer> betNumber = getIntegerList(jsonObject.getJSONArray("betNumber"));
            vo.setBetNumber(betNumber);
        }
        return vo;
    }

    private static List<Integer> getIntegerList(JSONArray array) {
        List<Integer> list = new LinkedList<Integer>();
        Object[] objects = array.toArray();
        for (Object o : objects) {
            list.add((Integer) o);
        }
        return new ArrayList<Integer>(list);
    }


    /**
     * @return The betNumber
     */
    public List<Integer> getBetNumber() {
        return betNumber;
    }

    /**
     * @param betNumber The betNumber
     */
    public void setBetNumber(List<Integer> betNumber) {
        this.betNumber = betNumber;
    }

    /**
     * @return The betMoney
     */
    public Integer getBetMoney() {
        return betMoney;
    }

    /**
     * @param betMoney The betMoney
     */
    public void setBetMoney(Integer betMoney) {
        this.betMoney = betMoney;
    }

    /**
     * @return The betType
     */
    public Integer getBetType() {
        return betType;
    }

    /**
     * @param betType The betType
     */
    public void setBetType(Integer betType) {
        this.betType = betType;
    }

}
