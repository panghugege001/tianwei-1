package dfh.utils.compare;

import java.util.Comparator;

import dfh.model.notdb.DataImportBL;

public class DataImportBLComparator implements Comparator<DataImportBL> {

	@Override
	public int compare(DataImportBL o1, DataImportBL o2) {
		return (o1.getProfitAllAmount()-o2.getProfitAllAmount())>0?1:-1;
	}

}
