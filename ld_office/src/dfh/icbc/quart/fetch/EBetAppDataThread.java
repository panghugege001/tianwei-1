package dfh.icbc.quart.fetch;

import dfh.model.PlatformData;
import dfh.service.interfaces.IGetdateService;
import dfh.utils.EBetAppUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class EBetAppDataThread implements Callable<List<PlatformData>> {

    private static Logger log = Logger.getLogger(EBetAppDataThread.class);
    private String executeTime = null;
    private final Date queryDate;

    private IGetdateService getdateService;

    public EBetAppDataThread(IGetdateService getdateService , Date queryDate) {
        this.getdateService = getdateService;
        this.queryDate = queryDate;
    }

    public void setGetdateService(IGetdateService getdateService) {
        this.getdateService = getdateService;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    @Override
    public List<PlatformData> call() throws Exception {
        List<PlatformData> dailyProfit = EBetAppUtil.getDailyProfit(queryDate);
        if (dailyProfit == null || dailyProfit.isEmpty()) {
            log.info("EBetApp 没有投注记录");
            log.info("EBetAppDataThread processing .... finished");
            dailyProfit = new ArrayList<PlatformData>();
        }
        return dailyProfit;
    }
}
