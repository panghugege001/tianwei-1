package dfh.utils.compare;

import java.util.Comparator;

import dfh.model.Goddessrecord;

public class GoddessrecordComparator implements Comparator<Goddessrecord> {

	@Override
	public int compare(Goddessrecord o1, Goddessrecord o2) {
		
		return (o1.getFlowernum() == null? 0:o1.getFlowernum()) - (o2.getFlowernum()==null?0:o2.getFlowernum()) > 0? -1 : 1;
	}

}
