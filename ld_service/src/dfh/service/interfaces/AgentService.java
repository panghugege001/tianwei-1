package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

public interface AgentService{
  public List<Integer> getAgentSubUserInfo(String agent,Date start,Date end);
  public List<String> queryAgentAddress(String loginname);
}