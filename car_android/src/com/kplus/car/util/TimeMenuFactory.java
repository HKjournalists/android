package com.kplus.car.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.kplus.car.R;
import com.kplus.car.widget.SlideUpMenu;
import com.kplus.car.widget.wheelview.AbstractWheelTextAdapter;
import com.kplus.car.widget.wheelview.NumericWheelAdapter;
import com.kplus.car.widget.wheelview.OnWheelScrollListener;
import com.kplus.car.widget.wheelview.WheelView;
import com.kplus.car.widget.wheelview.WheelViewAdapter;


public class TimeMenuFactory {
	private String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	private String[] months_little = { "4", "6", "9", "11" };
	private final List<String> list_big = Arrays.asList(months_big);
	private final List<String> list_little = Arrays.asList(months_little);
	static TimeMenuFactory mFactory;
	Context mContext;

	public static TimeMenuFactory getInstance(Context c) {
		if (mFactory == null)
			synchronized (TimeMenuFactory.class) {
				if (mFactory == null) {
					mFactory = new TimeMenuFactory();
				}
			}
		mFactory.mContext = c;
		return mFactory;
	}

	public interface onIimerMenuClick {
		public void onFinishClick(View v);

		public void onCancelClick(View v);
	}

    public TimeMenu buildDayOfMonthMenu(int contentviewid, final Calendar date, onIimerMenuClick menuclick) {
        NumericWheelAdapter first = new NumericWheelAdapter(mContext, 1, 31, mContext.getString(R.string.format_day));
        return buildBaseMenu(contentviewid, menuclick, first, null, null, date.get(Calendar.DATE) - 1, 0, 0);
    }

    public TimeMenu buildRepeatMenu(int contentviewid, onIimerMenuClick menuclick) {

        final String[] mRepeats = mContext.getResources().getStringArray(R.array.repeat_type);

        AbstractWheelTextAdapter first = new AbstractWheelTextAdapter(mContext) {
            @Override
            public int getItemsCount() {
                return mRepeats.length;
            }

            @Override
            public CharSequence getItemText(int index) {
                return mRepeats[index];
            }
        };

        final TimeMenu m = buildBaseMenu(contentviewid, menuclick, first, null, null, 0, 0, 0);
        OnWheelScrollListener onWheelScrollListener = new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {

            }
        };
        m.getFirst().addScrollingListener(onWheelScrollListener);
        return m;
    }

	public TimeMenu buildRestrictMenu(int contentviewid, String remindDateType, String remindDate, onIimerMenuClick menuclick) {
		final String[] mDates = mContext.getResources().getStringArray(R.array.remind_restrict);
		AbstractWheelTextAdapter first = new AbstractWheelTextAdapter(mContext) {
			@Override
			public int getItemsCount() {
				return mDates.length;
			}

			@Override
			public CharSequence getItemText(int index) {
				return mDates[index];
			}
		};
		NumericWheelAdapter second = new NumericWheelAdapter(mContext, 0, 23);
		NumericWheelAdapter third = new NumericWheelAdapter(mContext, 0, 59);
		int fstart = 1;
		try{
			fstart = Integer.parseInt(remindDateType);
		}catch (Exception e){
			e.printStackTrace();
		}
		Date date = null;
		try {
			date = new SimpleDateFormat("HH:mm").parse(remindDate);
		}catch (Exception e){
			e.printStackTrace();
		}
		final TimeMenu m = buildBaseMenu(contentviewid, menuclick, first, second, third, fstart, date.getHours(), date.getMinutes());
		return m;
	}

	public TimeMenu buildRemindMenu(int contentviewid, int arrayid, Calendar remind, final Calendar date, onIimerMenuClick menuclick) {
		final String[] mDates = mContext.getResources().getStringArray(arrayid);

		AbstractWheelTextAdapter first = new AbstractWheelTextAdapter(mContext) {
			@Override
			public int getItemsCount() {
				return mDates.length;
			}

			@Override
			public CharSequence getItemText(int index) {
				return mDates[index];
			}
		};

		NumericWheelAdapter second = new NumericWheelAdapter(mContext, 0, 23);
		NumericWheelAdapter third = new NumericWheelAdapter(mContext, 0, 59);
		int gap = DateUtil.getGapCount(remind.getTime(), date.getTime());
		switch (gap){
			case 15:
				gap = 8;
				break;
			case 30:
				gap = 9;
				break;
			case 60:
				gap = 10;
				break;
		}
		final TimeMenu m = buildBaseMenu(contentviewid, menuclick, first, second, third, gap, remind.get(Calendar.HOUR_OF_DAY), remind.get(Calendar.MINUTE));
		m.setCalendar(remind);
		OnWheelScrollListener onWheelScrollListener = new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				Calendar cal = Calendar.getInstance();
				if (cal != null)
					cal.setTime(date.getTime());
				switch (m.getFirst().getCurrentItem()){
					case 8:
						cal.add(Calendar.DATE, -15);
						break;
					case 9:
						cal.add(Calendar.DATE, -30);
						break;
					case 10:
						cal.add(Calendar.DATE, -60);
						break;
					default:
						cal.add(Calendar.DATE, -m.getFirst().getCurrentItem());
						break;
				}
				cal.set(Calendar.HOUR_OF_DAY, m.getSecond().getCurrentItem());
				cal.set(Calendar.MINUTE, m.getThird().getCurrentItem());
				m.setCalendar(cal);
			}
		};
		m.getFirst().addScrollingListener(onWheelScrollListener);
		m.getSecond().addScrollingListener(onWheelScrollListener);
		m.getThird().addScrollingListener(onWheelScrollListener);
		return m;
	}

	public TimeMenu buildRemindMenu(int contentviewid, Calendar remind, final Calendar date, onIimerMenuClick menuclick) {
		return buildRemindMenu(contentviewid, R.array.remind_date, remind, date, menuclick);
	}

	public TimeMenu buildDateMenu(int contentViewid, final int startyear, int endyear, Calendar cal, onIimerMenuClick menuclick) {
		return buildDateMenu(contentViewid, startyear, endyear, cal, menuclick, null);
	}

	public interface DataMenuListener {
		public void dateChange(TimeMenu menu);
	}

	public TimeMenu buildDateMenu(int contentViewid, final int startyear, int endyear, Calendar cal, onIimerMenuClick menuclick,
			final DataMenuListener datalistener) {

		Calendar calendar = Calendar.getInstance();
        if (cal != null)
            calendar.setTime(cal.getTime());

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		NumericWheelAdapter second = new NumericWheelAdapter(mContext, 1, 12, mContext.getString(R.string.format_month));
		NumericWheelAdapter first = new NumericWheelAdapter(mContext, startyear, endyear,
				mContext.getString(R.string.format_year));
		NumericWheelAdapter third = new NumericWheelAdapter(mContext, 1, 31, mContext.getString(R.string.format_day));

		final TimeMenu m = buildBaseMenu(contentViewid, menuclick, first, second, third, year - startyear, month,
				day - 1);
		m.setCalendar(calendar);
		if (list_big.contains(String.valueOf(month + 1))) {
			m.getThird().setAdapter(new NumericWheelAdapter(mContext, 1, 31, mContext.getString(R.string.format_day)));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			m.getThird().setAdapter(new NumericWheelAdapter(mContext, 1, 30, mContext.getString(R.string.format_day)));
		} else {
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				m.getThird().setAdapter(
						new NumericWheelAdapter(mContext, 1, 29, mContext.getString(R.string.format_day)));
			else
				m.getThird().setAdapter(
						new NumericWheelAdapter(mContext, 1, 28, mContext.getString(R.string.format_day)));
		}
		m.getFirst().addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				int year_num = m.getFirst().getCurrentItem() + startyear;
				if (list_big.contains(String.valueOf(m.getSecond().getCurrentItem() + 1))) {
					if (m.getThird().getCurrentItem() == m.getThird().getAdapter().getItemsCount() - 1) {
						m.getThird().setAdapter(
								new NumericWheelAdapter(mContext, 1, 31, mContext.getString(R.string.format_day)));
						m.getThird().setCurrentItem(30);
					} else {
						m.getThird().setAdapter(
								new NumericWheelAdapter(mContext, 1, 31, mContext.getString(R.string.format_day)));
					}
				} else if (list_little.contains(String.valueOf(m.getSecond().getCurrentItem() + 1))) {
					if (m.getThird().getCurrentItem() == m.getThird().getAdapter().getItemsCount() - 1) {
						m.getThird().setAdapter(
								new NumericWheelAdapter(mContext, 1, 30, mContext.getString(R.string.format_day)));
						m.getThird().setCurrentItem(29);
					} else {
						m.getThird().setAdapter(
								new NumericWheelAdapter(mContext, 1, 30, mContext.getString(R.string.format_day)));
					}
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
						if (m.getThird().getCurrentItem() == m.getThird().getAdapter().getItemsCount() - 1) {
							m.getThird().setAdapter(
									new NumericWheelAdapter(mContext, 1, 29, mContext.getString(R.string.format_day)));
							m.getThird().setCurrentItem(28);
						} else {
							m.getThird().setAdapter(
									new NumericWheelAdapter(mContext, 1, 29, mContext.getString(R.string.format_day)));
						}
					else if (m.getThird().getCurrentItem() == m.getThird().getAdapter().getItemsCount() - 1) {
						m.getThird().setAdapter(
								new NumericWheelAdapter(mContext, 1, 28, mContext.getString(R.string.format_day)));
						m.getThird().setCurrentItem(27);
					} else {
						m.getThird().setAdapter(
								new NumericWheelAdapter(mContext, 1, 28, mContext.getString(R.string.format_day)));
					}
				}
				m.getCalendar().set(Calendar.YEAR, year_num);
				if (datalistener != null) {
					datalistener.dateChange(m);
				}
			}
		});

		m.getSecond().addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				int month_num = m.getSecond().getCurrentItem() + 1;

				if (list_big.contains(String.valueOf(month_num))) {
					if (m.getThird().getCurrentItem() == m.getThird().getAdapter().getItemsCount() - 1) {
						m.getThird().setAdapter(
								new NumericWheelAdapter(mContext, 1, 31, mContext.getString(R.string.format_day)));
						m.getThird().setCurrentItem(30);
					} else {
						m.getThird().setAdapter(
								new NumericWheelAdapter(mContext, 1, 31, mContext.getString(R.string.format_day)));
					}
				} else if (list_little.contains(String.valueOf(month_num))) {
					if (m.getThird().getCurrentItem() == m.getThird().getAdapter().getItemsCount() - 1) {
						m.getThird().setAdapter(
								new NumericWheelAdapter(mContext, 1, 30, mContext.getString(R.string.format_day)));
						m.getThird().setCurrentItem(29);
					} else {
						m.getThird().setAdapter(
								new NumericWheelAdapter(mContext, 1, 30, mContext.getString(R.string.format_day)));
					}
				} else {
					if (((m.getFirst().getCurrentItem() + startyear) % 4 == 0 && (m.getFirst().getCurrentItem() + startyear) % 100 != 0)
							|| (m.getFirst().getCurrentItem() + startyear) % 400 == 0)
						if (m.getThird().getCurrentItem() == m.getThird().getAdapter().getItemsCount() - 1) {
							m.getThird().setAdapter(
									new NumericWheelAdapter(mContext, 1, 29, mContext.getString(R.string.format_day)));
							m.getThird().setCurrentItem(28);
						} else {
							m.getThird().setAdapter(
									new NumericWheelAdapter(mContext, 1, 29, mContext.getString(R.string.format_day)));
						}
					else if (m.getThird().getCurrentItem() == m.getThird().getAdapter().getItemsCount() - 1) {
						m.getThird().setAdapter(
								new NumericWheelAdapter(mContext, 1, 28, mContext.getString(R.string.format_day)));
						m.getThird().setCurrentItem(27);
					} else {
						m.getThird().setAdapter(
								new NumericWheelAdapter(mContext, 1, 28, mContext.getString(R.string.format_day)));
					}
				}
				m.getCalendar().set(Calendar.MONTH, month_num - 1);
				if (datalistener != null) {
					datalistener.dateChange(m);
				}
			}
		});

		m.getThird().addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				int day_num = m.getThird().getCurrentItem() + 1;
				if (day_num == 1)
					m.getThird().setCurrentItem(m.getThird().getCurrentItem());
				m.getCalendar().set(Calendar.DATE, day_num);
				if (datalistener != null) {
					datalistener.dateChange(m);
				}
			}
		});

		return m;
	}

	private TimeMenu buildBaseMenu(int contentViewId, final onIimerMenuClick menuclick, WheelViewAdapter firstAdapter,
			WheelViewAdapter secondAdapter, WheelViewAdapter thirdAdapter, int fstart, int sstart, int tstart) {
		final TimeMenu m = new TimeMenu();
		m.setSlideMenu(new SlideUpMenu((Activity) mContext, R.color.daze_translucence2));
		m.getSlideMenu().setContentView(contentViewId, new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.menu_finish) {
					menuclick.onFinishClick(v);
				} else if (v.getId() == R.id.menu_cancel) {
					menuclick.onCancelClick(v);
				}
			}
		});
        WheelView wheelView = (WheelView) m.getSlideMenu().getContentView().findViewById(R.id.first);
        if (wheelView != null){
            m.setFirst(wheelView);
            wheelView.setAdapter(firstAdapter);
            wheelView.setCurrentItem(fstart);
        }
        wheelView = (WheelView) m.getSlideMenu().getContentView().findViewById(R.id.second);
        if (wheelView != null){
            m.setSecond(wheelView);
            wheelView.setAdapter(secondAdapter);
            wheelView.setCurrentItem(sstart);
        }
		wheelView = (WheelView) m.getSlideMenu().getContentView().findViewById(R.id.third);
        if (wheelView != null){
            m.setThird(wheelView);
            wheelView.setAdapter(thirdAdapter);
            wheelView.setCurrentItem(tstart);
        }
		return m;
	}
}
