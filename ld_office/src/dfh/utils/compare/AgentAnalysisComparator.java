package dfh.utils.compare;

import java.util.Comparator;

import dfh.model.notdb.AgentAnalysis;
import dfh.model.notdb.CustomerAnalysis;

public class AgentAnalysisComparator implements Comparator<AgentAnalysis> {

	@Override
	public int compare(AgentAnalysis o1, AgentAnalysis o2) {
		return (o1.getProfitall()-o2.getProfitall())>0?1:-1;
	}

}
