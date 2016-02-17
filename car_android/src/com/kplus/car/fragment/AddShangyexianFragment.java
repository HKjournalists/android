package com.kplus.car.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.activity.ChexianEditActivity;
import com.kplus.car.util.ClickUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link com.kplus.car.fragment.AddShangyexianFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddShangyexianFragment extends Fragment implements ClickUtils.NoFastClickListener {
    private String mVehicleNum;

    public AddShangyexianFragment() {
        // Required empty public constructor
    }

    public static AddShangyexianFragment newInstance(String vehicleNum) {
        AddShangyexianFragment fragment = new AddShangyexianFragment();
        Bundle args = new Bundle();
        args.putString("vehicleNum", vehicleNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVehicleNum = getArguments().getString("vehicleNum");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_add_shangyexian, container, false);
        ClickUtils.setNoFastClickListener(layout, this);
        return layout;
    }

    @Override
    public void onNoFastClick(View v) {
        Intent it = new Intent(getActivity(), ChexianEditActivity.class);
        it.putExtra("vehicleNum", mVehicleNum);
        getActivity().startActivityForResult(it, Constants.REQUEST_TYPE_CHEXIAN);
    }
}
