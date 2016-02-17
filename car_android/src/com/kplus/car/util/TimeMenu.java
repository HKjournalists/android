package com.kplus.car.util;

import com.kplus.car.widget.SlideUpMenu;
import com.kplus.car.widget.wheelview.WheelView;

import java.util.Calendar;
import java.util.HashMap;


public class TimeMenu {
	private HashMap<String, Object> data = new HashMap<String, Object>();
	public static int START_YEAR = 1970, END_YEAR = 2050;
	private Calendar calendar;
	private SlideUpMenu slideMenu;
	private WheelView first;
	private WheelView second;
	private WheelView third;

	public TimeMenu() {
		super();
	}

	public SlideUpMenu getSlideMenu() {
		return slideMenu;
	}

	public void setSlideMenu(SlideUpMenu slideMenu) {
		this.slideMenu = slideMenu;
	}

	public WheelView getFirst() {
		return first;
	}

	public void setFirst(WheelView first) {
		this.first = first;
	}

	public WheelView getSecond() {
		return second;
	}

	public void setSecond(WheelView second) {
		this.second = second;
	}

	public WheelView getThird() {
		return third;
	}

	public void setThird(WheelView third) {
		this.third = third;
	}

	public void updateMoreThan(Calendar calendar) {
		if (this.calendar.before(calendar)) {
			this.calendar = (Calendar) calendar.clone();
			upateViewByCalandar();
		}
	}

	private void upateViewByCalandar() {
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		first.setCurrentItem(year - START_YEAR);
		second.setCurrentItem(month - 1);
		third.setCurrentItem(day - 1);
	}

	public void updateLessThan(Calendar calendar) {
		if (this.calendar.after(calendar)) {
			this.calendar = (Calendar) calendar.clone();
			upateViewByCalandar();
		}
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calandar) {
		this.calendar = calandar;
	}
}
