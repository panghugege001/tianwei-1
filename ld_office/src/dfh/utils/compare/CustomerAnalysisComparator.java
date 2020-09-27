package dfh.utils.compare;

import java.util.Comparator;

import dfh.model.notdb.CustomerAnalysis;

public class CustomerAnalysisComparator implements Comparator<CustomerAnalysis> {

	@Override
	public int compare(CustomerAnalysis o1, CustomerAnalysis o2) {
		return (o1.getProfitAllAmount()-o2.getProfitAllAmount())>0?1:-1;
	}

}
