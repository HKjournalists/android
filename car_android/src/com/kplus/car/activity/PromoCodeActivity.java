package com.kplus.car.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.R;
import com.kplus.car.asynctask.CouponExchangeTask;
import com.kplus.car.model.response.CouponExchangeResponse;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.ToastUtil;

/**
 * Created by Administrator on 2015/11/11.
 */
public class PromoCodeActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private EditText mPromoInput;
    private int mTextLength = 0;
    private TextView tvRight;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_promo_code);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        View layout = findViewById(R.id.title_layout);
        layout.setBackgroundColor(getResources().getColor(R.color.daze_orangered5));
        View ivLeft = findViewById(R.id.ivLeft);
        ivLeft.setVisibility(View.GONE);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("优惠码兑换");
        tvTitle.setTextColor(0xa6ffffff);
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText("取消");
        tvLeft.setTextColor(getResources().getColor(R.color.daze_white));
        tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tvRight = (TextView) findViewById(R.id.tvRight);
        tvRight.setText("兑换");
        tvRight.setTextColor(0x40ffffff);

        mPromoInput = (EditText) findViewById(R.id.promoCodeInput);
        mPromoInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mPromoInput, InputMethodManager.SHOW_FORCED);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rightView), this);
        mPromoInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextLength += (count - before);
                if (mTextLength > 0){
                    tvRight.setTextColor(getResources().getColor(R.color.daze_white));
                }
                else {
                    tvRight.setTextColor(0x40ffffff);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mPromoInput.getWindowToken(), 0);
                finish();
                break;
            case R.id.rightView:
                if (mTextLength > 0){
                    couponExchange();
                }
                break;
        }
    }

    private void couponExchange(){
        new CouponExchangeTask(mApplication){
            @Override
            protected void onPostExecute(CouponExchangeResponse response) {
                if (response != null && response.getCode() != null && response.getCode() == 0){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mPromoInput.getWindowToken(), 0);
                    Intent it = new Intent();
                    it.putExtra("count", response.getData().getValue());
                    setResult(RESULT_OK, it);
                    finish();
                }
                else if (response != null)
                    ToastUtil.TextToast(PromoCodeActivity.this, response.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
            }
        }.execute(mPromoInput.getText().toString());
    }
}
