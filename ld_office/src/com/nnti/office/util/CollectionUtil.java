package com.nnti.office.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtil {
	
	public static boolean isEmpty(Collection<?> collection) {
		if(collection == null) {
			return true;
		}
		if(collection.size() == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isNotEmpty(Collection<?> collection) {
		return ! isEmpty(collection);
	}
	
	public static List<Integer> getDeleteList(List<Integer> oldList,List<Integer> newList) {
		List<Integer> deleteList = new ArrayList<Integer>();
		for(Integer oldInteger : oldList) {
			if(! newList.contains(oldInteger)) {
				deleteList.add(oldInteger);
			}
		}
		return deleteList;
	}
	
	public static List<Integer> getAddList(List<Integer> oldList,List<Integer> newList) {
		List<Integer> addList = new ArrayList<Integer>();
		for(Integer newInteger : newList) {
			if(! oldList.contains(newInteger)) {
				addList.add(newInteger);
			}
		}
		return addList;
	}
	
}
