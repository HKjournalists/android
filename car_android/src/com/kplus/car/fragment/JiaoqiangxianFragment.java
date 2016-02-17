package com.kplus.car.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.model.RemindChexian;
import com.kplus.car.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JiaoqiangxianFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JiaoqiangxianFragment extends Fragment {
    private static final String REMIND_JIAOQIANGXIAN = "remind_jiaoqiangxian";

    private RemindChexian mRemindJiaoqiangxian;
    private TextView mDate;
    private TextView mDateLeft;
    private TextView mTian;
    private ProgressBar mProgress;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param remindJiaoqiangxian Parameter 1.
     * @return A new instance of fragment JiaoqiangxianFragment.
     */
    public static JiaoqiangxianFragment newInstance(RemindChexian remindJiaoqiangxian) {
        JiaoqiangxianFragment fragment = new JiaoqiangxianFragment();
        Bundle args = new Bundle();
        args.putSerializable(REMIND_JIAOQIANGXIAN, remindJiaoqiangxian);
        fragment.setArguments(args);
        return fragment;
    }

    public JiaoqiangxianFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRemindJiaoqiangxian = (RemindChexian) getArguments().getSerializable(REMIND_JIAOQIANGXIAN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_jiaoqiangxian, container, false);
        mDate = (TextView) layout.findViewById(R.id.chexian_date);
        mDateLeft = (TextView) layout.findViewById(R.id.chexian_date_left);
        Typeface typeface = ((KplusApplication)getActivity().getApplication()).mDin;
        mDateLeft.setTypeface(typeface);
        mTian = (TextView) layout.findViewById(R.id.tian);
        mTian.setTypeface(typeface);
        mProgress = (ProgressBar) layout.findViewById(R.id.chexian_progress);
        updateUI();
        return layout;
    }

    private void updateUI(){
        mDate.setVisibility(View.GONE);
        mDate.setText("(" + mRemindJiaoqiangxian.getDate().replaceAll("-", "/") + ")");
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindJiaoqiangxian.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), date);
        if (gap <= 7){
            mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progress_bar3_red));
            int color = getResources().getColor(R.color.daze_darkred2);
            mDateLeft.setTextColor(color);
            mTian.setTextColor(color);
        }
        else {
//            mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progress_bar3));
            mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progress_bar3_orangered5));
            int color = getResources().getColor(R.color.daze_black2);
            mDateLeft.setTextColor(color);
            mTian.setTextColor(color);
        }
        mDateLeft.setText(String.valueOf(gap));
        mProgress.setProgress(gap);
    }
}
