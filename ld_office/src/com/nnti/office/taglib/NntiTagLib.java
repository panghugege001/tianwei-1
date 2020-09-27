package com.nnti.office.taglib;

import java.util.List;

/**
 * nnti self define tag
 * @author michael
 *
 */
public class NntiTagLib {
	
	/**
	 * to check list contains o
	 * @param list
	 * @param o
	 * @return
	 */
	public static boolean contains(List list, Object o) {
      return list.contains(o);
   }
}
