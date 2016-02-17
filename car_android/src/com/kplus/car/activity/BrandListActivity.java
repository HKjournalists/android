package com.kplus.car.activity;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.adapter.BaoyangBrandAdapter;
import com.kplus.car.asynctask.BaoyangBrandTask;
import com.kplus.car.model.BaoyangBrand;
import com.kplus.car.model.response.BaoyangBrandResponse;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.widget.MyLetterListView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/8/5.
 */
public class BrandListActivity extends BaseActivity implements ClickUtils.NoFastClickListener, AdapterView.OnItemClickListener {
    private View brandLayout;
    private ListView brandListView;
    private MyLetterListView letterListView;
    private HashMap<String, Integer> alphaIndexer = new HashMap<String, Integer>();// 存放存在的汉语拼音首字母和与之对应的列表位置

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_brand_list);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("选择品牌");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);

        brandLayout = findViewById(R.id.brand_select_layout);
        brandListView = (ListView) findViewById(R.id.brand_select_list);
        letterListView = (MyLetterListView) findViewById(R.id.brand_select_list_letter);
    }

    @Override
    protected void loadData() {
        initBrandData();
    }

    @Override
    protected void setListener() {
        brandListView.setOnItemClickListener(this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        letterListView.setOnTouchingLetterChangedListener(new MyLetterListView.OnTouchingLetterChangedListener() {
            public void onTouchingLetterChanged(final String s) {
                if (alphaIndexer.get(s) != null) {
                    int position = alphaIndexer.get(s);
                    brandListView.setSelection(position);
                }
            }
        });
    }

    private void initBrandData() {
        new BaoyangBrandTask(mApplication) {

            @Override
            protected void onPreExecute() {
                showloading(true);
            }

            @Override
            protected void onPostExecute(BaoyangBrandResponse result) {
                showloading(false);
                if (result != null && result.getCode() != null && result.getCode() == 0) {
                    letterListView.setVisibility(View.VISIBLE);
                    List<BaoyangBrand> list = result.getData().getList();
                    for (int i = 0; i < list.size(); i++) {
                        // 当前汉语拼音首字母
                        String currentStr = list.get(i).getInitial();
                        // 上一个汉语拼音首字母，如果不存在为" "
                        String previewStr = (i - 1) >= 0 ? list.get(i - 1).getInitial() : " ";
                        if (!previewStr.equals(currentStr)) {
                            alphaIndexer.put(currentStr, i);
                        }
                    }
                    BaoyangBrandAdapter adapter = new BaoyangBrandAdapter(BrandListActivity.this, list);
                    brandListView.setAdapter(adapter);
                }
            }
        }.execute();
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BaoyangBrand brand = (BaoyangBrand) brandListView.getAdapter().getItem(position);
        Intent it = new Intent();
        it.putExtra("brandName", brand.getName());
        setResult(Constants.RESULT_TYPE_CHANGED, it);
        finish();
    }
}
