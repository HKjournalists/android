package com.kplus.car.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kplus.car.R;
import com.kplus.car.fragment.DetailRuleFragment;
import com.kplus.car.util.ClickUtils;

/**
 * Created by Administrator on 2015/6/29.
 */
public class JiazhaoDetailRuleActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener, ClickUtils.NoFastClickListener {
    private RadioGroup mRadioGroup;
    private ViewPager mViewPager;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_jiazhao_detail_rule);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("驾驶证扣分细则");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);

        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mRadioGroup.check(R.id.radio_1);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    protected void loadData() {
        mViewPager.setAdapter(new DetailRuleAdapter(getSupportFragmentManager()));
    }

    @Override
    protected void setListener() {
        mRadioGroup.setOnCheckedChangeListener(this);
        mViewPager.addOnPageChangeListener(this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton btn = (RadioButton) group.findViewById(checkedId);
        btn.toggle();
        switch (checkedId){
            case R.id.radio_1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.radio_2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.radio_3:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.radio_6:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.radio_12:
                mViewPager.setCurrentItem(4);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                mRadioGroup.check(R.id.radio_1);
                break;
            case 1:
                mRadioGroup.check(R.id.radio_2);
                break;
            case 2:
                mRadioGroup.check(R.id.radio_3);
                break;
            case 3:
                mRadioGroup.check(R.id.radio_6);
                break;
            case 4:
                mRadioGroup.check(R.id.radio_12);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                onBackPressed();
                break;
        }
    }

    class DetailRuleAdapter extends FragmentPagerAdapter{

        public DetailRuleAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String[] content;
            switch (position){
                case 0:
                    content = new String[]{
                            getString(R.string.detail_rule_12_1),
                            getString(R.string.detail_rule_12_2),
                            getString(R.string.detail_rule_12_3),
                            getString(R.string.detail_rule_12_4),
                            getString(R.string.detail_rule_12_5)};
                    break;
                case 1:
                    content = new String[]{
                            getString(R.string.detail_rule_6_1),
                            getString(R.string.detail_rule_6_2),
                            getString(R.string.detail_rule_6_3),
                            getString(R.string.detail_rule_6_4)};
                    break;
                case 2:
                    content = new String[]{
                            getString(R.string.detail_rule_3_1),
                            getString(R.string.detail_rule_3_2),
                            getString(R.string.detail_rule_3_3),
                            getString(R.string.detail_rule_3_4),
                            getString(R.string.detail_rule_3_5),
                            getString(R.string.detail_rule_3_6),
                            getString(R.string.detail_rule_3_7),
                            getString(R.string.detail_rule_3_8),
                            getString(R.string.detail_rule_3_9)};
                    break;
                case 3:
                    content = new String[]{
                            getString(R.string.detail_rule_2_1),
                            getString(R.string.detail_rule_2_2),
                            getString(R.string.detail_rule_2_3),
                            getString(R.string.detail_rule_2_4),
                            getString(R.string.detail_rule_2_5)};
                    break;
                default:
                    content = new String[]{
                            getString(R.string.detail_rule_1_1),
                            getString(R.string.detail_rule_1_2),
                            getString(R.string.detail_rule_1_3),
                            getString(R.string.detail_rule_1_4)};
                    break;
            }
            return DetailRuleFragment.newInstance(content);
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
