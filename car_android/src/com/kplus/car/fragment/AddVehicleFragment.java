package com.kplus.car.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.activity.BaseActivity;
import com.kplus.car.adapter.SpaceItemDecoration;
import com.kplus.car.adapter.VehicleAddAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddVehicleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddVehicleFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private VehicleAddAdapter mAdapter;

    public AddVehicleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_add_vehicle, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.add_recycler_view);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(lm);
        int space = BaseActivity.dip2px(getActivity(), 3);
        mRecyclerView.setPadding(space, 0, space, space);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(space));
        mAdapter = new VehicleAddAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        return layout;
    }
}
