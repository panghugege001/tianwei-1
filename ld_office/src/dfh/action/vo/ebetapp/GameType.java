package dfh.action.vo.ebetapp;

/**
 * 1 百家乐
 * 2 龙虎
 * 3 骰宝
 * 4 轮盘
 * 5 水果机
 * <p>
 * Created by mars on 2016/6/17.
 */
public enum GameType {

    BACCARAT,
    DRAGON_AND_TIGER,
    DICE,
    ROULETTE,
    SLOT;

    public static GameType getType(int sequence) {
        //顺序为0-4 , sequence预设为1-5 , 所以作-1取值
        int index = sequence-1;
        return GameType.values()[index];
    }

}

