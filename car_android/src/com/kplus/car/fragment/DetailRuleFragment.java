package com.kplus.car.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kplus.car.R;

/**
 * Created by Administrator on 2015/6/30.
 */
public class DetailRuleFragment extends Fragment {
    private String[] mContent;

    public DetailRuleFragment() {
        // Required empty public constructor
    }

    public static DetailRuleFragment newInstance(String[] content) {
        DetailRuleFragment fragment = new DetailRuleFragment();
        Bundle args = new Bundle();
        args.putStringArray("content", content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContent = getArguments().getStringArray("content");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ListView layout = (ListView) inflater.inflate(R.layout.fragment_detail_rule, container, false);
        layout.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_jiazhao_detail_rule, mContent));
        return layout;
    }
}
