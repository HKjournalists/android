package com.kplus.car.util;

import java.util.Comparator;

import com.kplus.car.model.SelfService;

public class SelfServiceComparator implements Comparator<SelfService> {

	@Override
	public int compare(SelfService o1, SelfService o2) {
		return (int) (o1.getDistanceValue() - o2.getDistanceValue());
	}

}
