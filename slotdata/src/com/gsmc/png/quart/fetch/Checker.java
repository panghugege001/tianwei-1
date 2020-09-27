package com.gsmc.png.quart.fetch;

import java.util.Date;
import java.util.HashMap;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import com.gsmc.png.model.enums.GamePlatform;

public class Checker {
    private static HashMap<GamePlatform,Date> queryRecordDateMap =new HashMap<>();
    private static HashMap<GamePlatform,TimeInterval> querySystemDateMap =new HashMap<>();
    private Date yesterdayStart = new DateTime().minusDays(1).withTimeAtStartOfDay().toDate();
    private Date yesterdayEnd = new DateTime().withTimeAtStartOfDay().minusSeconds(1).toDate();
    public Checker() {
        /**
         * 游戏平台输赢记录的查询时间。
         */
        queryRecordDateMap.put(GamePlatform.NT, yesterdayEnd);
        queryRecordDateMap.put(GamePlatform.TTG, yesterdayEnd);
        /**
         * 我方系统指定的时间区间，供计时器触发，依据此时间区间捞取昨日游戏平台的输赢记录。
         * ===========================================================================================================
         * NT 游戏平台，系统预设！ 时间区间 (0:0-10|1:0-10|2:0-10|3:0-10) 之前。
         * -------------------------------------------------------------------------------------------------------------
         * TTG 游戏平台，由于第三方归檔需求，需 7.00 过后方可捞取昨日资料！ 时间区间 (7:0-10|8:0-10|9:0-10|10:0-10) 之前。
         * -------------------------------------------------------------------------------------------------------------
         */
        querySystemDateMap.put(GamePlatform.SW, new TimeInterval(0, 7, 0, 10));
        querySystemDateMap.put(GamePlatform.NT, new TimeInterval(0, 3, 0, 10));
        querySystemDateMap.put(GamePlatform.TTG, new TimeInterval(7, 10, 0, 10));
    }

    public Date getYesterdayDate(GamePlatform gamePlatform) {
        return (queryRecordDateMap.get(gamePlatform)==null)? new Date(): queryRecordDateMap.get(gamePlatform);
    }

    /**
     * 为了防止查询时间，由于跨日区间导致地漏单。
     * ============================================================================
     * 每当计时器触发于我方系统设定的时间区间中，再度捞取昨日所有记录写入数据库中。
     * ---------------------------------------------------------------------------
     * @return
     * @param gamePlatform
     */
    public boolean needsCoverUpMissPlatformDataYesterday(GamePlatform gamePlatform) {
        TimeInterval timeInterval = querySystemDateMap.get(gamePlatform);
        return timeInterval != null && timeInterval.isOnTimeNow();
    }
    public class TimeInterval{
        private int startHour;
        private int endHour;
        private int startMinutes;
        private int endMinutes;

        public TimeInterval(int startHour, int endHour, int startMinutes, int endMinutes) {
            this.startHour = startHour;
            this.endHour = endHour;
            this.startMinutes = startMinutes;
            this.endMinutes = endMinutes;
        }

        /**
         * 判断现在时间是否在指定区间之中。
         * @return
         */
        public boolean isOnTimeNow() {
            if(LocalDateTime.now().getHourOfDay() <= endHour && LocalDateTime.now().getHourOfDay() >= startHour ) {
                if (LocalDateTime.now().getMinuteOfHour() >= startMinutes &&LocalDateTime.now().getMinuteOfHour() <= endMinutes){
                    return true;
                }
            }
            return false;
        }
    }
    public static void main(String[] args) {
        System.out.println(new Checker().needsCoverUpMissPlatformDataYesterday(GamePlatform.TTG));
        TimeInterval timeInterval = new Checker().new TimeInterval(14, 15, 0, 50);
        System.out.println(timeInterval.isOnTimeNow());
    }
}
