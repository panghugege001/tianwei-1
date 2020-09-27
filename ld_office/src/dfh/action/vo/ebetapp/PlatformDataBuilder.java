package dfh.action.vo.ebetapp;

import dfh.model.PlatformData;
import dfh.utils.Arith;

import java.util.Date;
import java.util.UUID;

/**
 * Created by mars on 2016/6/20.
 */
public final class PlatformDataBuilder {
    final String platform;
    private String loginname=""; //用户名
    private Double totalBet =0.0; //总投注额
    private Double vaildBetCredit =0.0; //有效投注额
    private Double payOut=0.0; //赔付
    private Date startTime = new Date();
    private Date endTime = new Date();

    private PlatformDataBuilder(String platform) {
        this.platform = platform;
    }

    public static PlatformDataBuilder anProfit(String platform) {
        return new PlatformDataBuilder(platform);
    }

    public PlatformDataBuilder withLoginname(String loginname) {
        this.loginname = loginname;
        return this;
    }

    public PlatformDataBuilder withVaildBetCredit(Double betCredit) {
        this.vaildBetCredit = betCredit;
        return this;
    }

    public PlatformDataBuilder withPayOut(Double payOut) {
        this.payOut = payOut;
        return this;
    }

    public PlatformDataBuilder withStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public PlatformDataBuilder withEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public PlatformDataBuilder withTotalBet(Long sumOfBet) {
        this.totalBet = sumOfBet.doubleValue();
        return this;
    }

    public PlatformData build() {
        PlatformData data = new PlatformData();
        data.setUuid(UUID.randomUUID().toString());
        data.setPlatform(platform);
        data.setLoginname(loginname);
        data.setBet(vaildBetCredit);
        //盈利额=总投注额-赔付
        Double profit = Arith.sub(totalBet,payOut);
        data.setProfit(profit);
        data.setStarttime(startTime);
        data.setEndtime(endTime);
        data.setUpdatetime(new Date());
        return data;
    }
}
