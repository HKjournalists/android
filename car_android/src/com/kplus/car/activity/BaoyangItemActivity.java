package com.kplus.car.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.asynctask.BaoyangItemDeleteTask;
import com.kplus.car.asynctask.BaoyangItemSaveTask;
import com.kplus.car.asynctask.BaoyangItemTask;
import com.kplus.car.model.BaoyangItem;
import com.kplus.car.model.response.BaoyangItemDeleteResponse;
import com.kplus.car.model.response.BaoyangItemResponse;
import com.kplus.car.model.response.BaoyangItemSaveResponse;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.FlowLayout;
import com.kplus.car.widget.SlideDownMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Description 保养项目UI
 * <br/><br/>Created by FU ZHIXUE on 2015/7/8.
 * <br/><br/>
 */
public class BaoyangItemActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    public static final String KEY_ITEM_VALUE = "key-item-value";
    public static final String KEY_ITEM_IDS_VALUE = "key-item-ids-value";

    private static final int TYPE_ROUTINE_ITEM = 0;
    private static final int TYPE_MORE_ITEM = 1;
    private static final int TYPE_CUSTOM_ITEM = 2;

    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private InputMethodManager mIMM = null;

    private ArrayList<String> mSelectedBaoyangItemIds = null;
    private ArrayList<String> mSelectedBaoyangItems = null;

    /**
     * 常规项
     */
    private FlowLayout rlRoutineItem = null;
    private View viewMoreTopLine = null;
    private LinearLayout llMoreItem = null;
    /**
     * 更多
     */
    private FlowLayout rlMoreItem = null;
    /**
     * 自定义
     */
    private FlowLayout rlCustomItem = null;

    private SlideDownMenu mSlideDownMenu;

    private BaoyangItem mBaoyangItemDelete = null;

    @Override
    protected void initView() {
        mContext = this;
        mInflater = LayoutInflater.from(mContext);
        mIMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_baoyang_item);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        findViewById(R.id.title_layout).setBackgroundColor(getResources().getColor(R.color.daze_white_smoke10));
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("保养项目");
        TextView tvSecondTitle = (TextView) findViewById(R.id.tvSecondTitle);
        tvSecondTitle.setText("请选择本次保养的项目");
        tvSecondTitle.setVisibility(View.VISIBLE);
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);

        rlRoutineItem = (FlowLayout) findViewById(R.id.rlRoutineItem);
        viewMoreTopLine = findViewById(R.id.viewMoreTopLine);
        llMoreItem = (LinearLayout) findViewById(R.id.llMoreItem);
        rlMoreItem = (FlowLayout) findViewById(R.id.rlMoreItem);
        rlCustomItem = (FlowLayout) findViewById(R.id.rlCustomItem);
    }

    private ArrayList<BaoyangItem> mRoutineItems = new ArrayList<>();
    private ArrayList<BaoyangItem> mMoreItems = new ArrayList<>();
    private ArrayList<BaoyangItem> mCustomItems = new ArrayList<>();
    private ProgressDialog mProgressDialog = null;

    public synchronized void showProgress() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String msg = "请稍候...";
                    if (null == mProgressDialog || !mProgressDialog.isShowing()) {
                        dismiss();

                        mProgressDialog = new ProgressDialog(mContext);
                        mProgressDialog.setCanceledOnTouchOutside(false);
                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        mProgressDialog.setMessage(msg);
                        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface arg0) {
                            }
                        });
                        mProgressDialog.show();
                    } else {
                        mProgressDialog.setMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void dismiss() {
        try {
            if (null != mProgressDialog) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mSelectedBaoyangItemIds = bundle.getStringArrayList(KEY_ITEM_IDS_VALUE);
            mSelectedBaoyangItems = bundle.getStringArrayList(KEY_ITEM_VALUE);
        }

        fetchData();
    }

    private void fetchData() {
        showProgress();

        List<BaoyangItem> items = mApplication.dbCache.getBaoyangItem();
        if (null != items && items.size() > 0) {
            makeData(items);
            initRoutineItem(mRoutineItems);
            initMoreItem(mMoreItems);
            initCustomItem(mCustomItems);
        }

        new BaoyangItemTask(mApplication) {
            @Override
            protected void onPostExecute(BaoyangItemResponse response) {
                if (response != null && response.getCode() != null && response.getCode() == 0) {
                    List<BaoyangItem> items = response.getData().getList();
                    makeData(items);
                    mApplication.dbCache.saveBaoyangItem(items);
                }

                initRoutineItem(mRoutineItems);
                initMoreItem(mMoreItems);
                initCustomItem(mCustomItems);

                dismiss();
            }
        }.execute();
    }

    private void makeData(List<BaoyangItem> items) {
        if (null != items) {
            mRoutineItems.clear();
            mMoreItems.clear();
            mCustomItems.clear();

            for (BaoyangItem item : items) {
                item.setIsChecked(false);
                if (null != mSelectedBaoyangItemIds && null != mSelectedBaoyangItems) {
                    for (int k = 0; k < mSelectedBaoyangItemIds.size(); k++) {
                        int tempId = Integer.valueOf(mSelectedBaoyangItemIds.get(k));
                        String tempItem = mSelectedBaoyangItems.get(k);
                        if (tempId == item.getId() && item.getItem().equals(tempItem)) {
                            item.setIsChecked(true);
                            break;
                        }
                    }
                }

                switch (item.getType()) {
                    case TYPE_ROUTINE_ITEM: // 常规
                        mRoutineItems.add(item);
                        break; // 更多
                    case TYPE_MORE_ITEM:
                        mMoreItems.add(item);
                        break;
                    case TYPE_CUSTOM_ITEM: // 自定义
                        mCustomItems.add(item);
                        break;
                }
            }
        }
    }

    /**
     * 初始化常规项
     */
    private void initRoutineItem(List<BaoyangItem> items) {
        rlRoutineItem.removeAllViews();
        if (null == items) {
            return;
        }

        int size = items.size();
        for (int i = 0; i < size; i++) {
            final BaoyangItem item = items.get(i);

            View view = mInflater.inflate(R.layout.baoyang_routine_item, rlRoutineItem, false);
            final TextView tvItemName = (TextView) view.findViewById(R.id.tvItemName);
            String itemName = item.getItem();
            tvItemName.setText(itemName);

            // 如果是选择的默认选中
            setSelectedItem(tvItemName, item.isChecked());

            ClickUtils.setNoFastClickListener(tvItemName, new ClickUtils.NoFastClickListener() {
                @Override
                public void onNoFastClick(View v) {
                    item.setIsChecked(!item.isChecked());
                    setSelectedItem(tvItemName, item.isChecked());
                    checkedItem(item);
                }
            });
            rlRoutineItem.addView(view, i);
        }
    }

    /**
     * 初始化更多
     */
    private void initMoreItem(List<BaoyangItem> items) {
        rlMoreItem.removeAllViews();
        viewMoreTopLine.setVisibility(View.VISIBLE);
        llMoreItem.setVisibility(View.VISIBLE);
        if (null != items && items.size() > 0) {
            int size = items.size();
            for (int i = 0; i < size; i++) {
                final BaoyangItem item = items.get(i);
                View view = mInflater.inflate(R.layout.baoyang_routine_item, rlMoreItem, false);
                final TextView tvItemName = (TextView) view.findViewById(R.id.tvItemName);
                String itemName = item.getItem();
                tvItemName.setText(itemName);

                // 如果是选择的默认选中
                setSelectedItem(tvItemName, item.isChecked());

                ClickUtils.setNoFastClickListener(tvItemName, new ClickUtils.NoFastClickListener() {
                    @Override
                    public void onNoFastClick(View v) {
                        item.setIsChecked(!item.isChecked());
                        setSelectedItem((TextView) v, item.isChecked());
                        checkedItem(item);
                    }
                });

                rlMoreItem.addView(view, i);
            }
        } else {
            viewMoreTopLine.setVisibility(View.GONE);
            llMoreItem.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化自定义
     */
    private void initCustomItem(List<BaoyangItem> items) {
        rlCustomItem.removeAllViews();

        int size = 0;
        if (null != items) {
            size = items.size();
            for (int i = 0; i < size; i++) {
                final BaoyangItem item = items.get(i);
                final View view = mInflater.inflate(R.layout.baoyang_custom_item, rlCustomItem, false);
                final TextView tvItemName = (TextView) view.findViewById(R.id.tvItemName);
                ImageView ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
                String itemName = item.getItem();
                tvItemName.setText(itemName);

                // 如果是选择的默认选中
                setSelectedItem(view, tvItemName, item.isChecked());
                // 选择item
                ClickUtils.setNoFastClickListener(tvItemName, new ClickUtils.NoFastClickListener() {
                    @Override
                    public void onNoFastClick(View v) {
                        item.setIsChecked(!item.isChecked());
                        setSelectedItem(view, tvItemName, item.isChecked());
                        checkedItem(item);
                    }
                });
                // 删除
                ivDelete.setTag(item);
                ClickUtils.setNoFastClickListener(ivDelete, new ClickUtils.NoFastClickListener() {
                    @Override
                    public void onNoFastClick(View v) {
                        mBaoyangItemDelete = item;

                        Intent alertIntent = new Intent(mContext, AlertDialogActivity.class);
                        alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
                        alertIntent.putExtra("title", "删除自定义保养项目");
                        alertIntent.putExtra("message", "确定要删除吗？");
                        startActivityForResult(alertIntent, Constants.REQUEST_TYPE_DELETE);
                    }
                });
                rlCustomItem.addView(view, i);
            }
        }
        // 最后一个放入 "添加"
        View view = mInflater.inflate(R.layout.baoyang_custom_add_item, rlCustomItem, false);
        TextView tvAddItem = (TextView) view.findViewById(R.id.tvAddItem);
        ClickUtils.setNoFastClickListener(tvAddItem, new ClickUtils.NoFastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                showDialog();
            }
        });
        rlCustomItem.addView(view, size);
    }

    private void setSelectedItem(TextView textView, boolean isSelected) {
        if (isSelected) {
            textView.setBackgroundResource(R.drawable.stroke_corner_orange_line);
            textView.setTextColor(getResources().getColor(R.color.daze_orangered5));
        } else {
            textView.setBackgroundResource(R.drawable.stroke_corner_gray);
            textView.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
        }
    }

    private void setSelectedItem(View view, TextView textView, boolean isSelected) {
        if (isSelected) {
            view.setBackgroundResource(R.drawable.stroke_corner_orange_line);
            textView.setTextColor(getResources().getColor(R.color.daze_orangered5));
        } else {
            view.setBackgroundResource(R.drawable.stroke_corner_gray);
            textView.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
        }
    }

    private void deleteItem(final BaoyangItem item) {
        if (null == mSelectedBaoyangItems) {
            mSelectedBaoyangItems = new ArrayList<>();
        }
        if (null == mSelectedBaoyangItemIds) {
            mSelectedBaoyangItemIds = new ArrayList<>();
        }
        showProgress();
        new BaoyangItemDeleteTask(mApplication) {
            @Override
            protected void onPostExecute(BaoyangItemDeleteResponse response) {
                boolean isError;
                if (response != null && response.getCode() == 0) {
                    // true 成功， false 失败
                    isError = !response.getData().getResult();

                    if (!isError) {
                        item.setIsChecked(false);
                        for (int i = 0; i < mCustomItems.size(); i++) {
                            BaoyangItem tempItem = mCustomItems.get(i);
                            if (item.getItem().equals(tempItem.getItem())) {
                                mCustomItems.remove(i);
                                break;
                            }
                        }
                        initCustomItem(mCustomItems);
                        checkedItem(item);
                        mApplication.dbCache.deleteBaoyangItem(item.getId());
                    }
                } else {
                    isError = true;
                }
                dismiss();
                if (isError) {
                    ToastUtil.makeShortToast(mContext, "删除项目失败");
                } else {
                    ToastUtil.makeShortToast(mContext, "删除项目成功");
                }
            }
        }.execute(item.getId());
    }

    private void addItem(final String itemName) {
        if (null == mSelectedBaoyangItems) {
            mSelectedBaoyangItems = new ArrayList<>();
        }
        if (null == mSelectedBaoyangItemIds) {
            mSelectedBaoyangItemIds = new ArrayList<>();
        }

        showProgress();
        new BaoyangItemSaveTask(mApplication) {
            @Override
            protected void onPostExecute(BaoyangItemSaveResponse response) {
                boolean isError = false;
                if (response != null && response.getCode() == 0) {
                    BaoyangItem item = response.getData();
                    if (null != item) {
                        item.setIsChecked(true);
                        // 添加到第一项
                        mCustomItems.add(0, item);
                        initCustomItem(mCustomItems);
                        checkedItem(item);
                        mApplication.dbCache.addBaoyangItem(item);
                    } else {
                        isError = true;
                    }
                } else {
                    isError = true;
                }
                dismiss();
                if (isError) {
                    ToastUtil.makeShortToast(mContext, "添加项目失败");
                } else {
                    ToastUtil.makeShortToast(mContext, "添加项目成功");
                }
            }
        }.execute(itemName);
    }

    private void checkedItem(BaoyangItem item) {
        if (null == mSelectedBaoyangItems) {
            mSelectedBaoyangItems = new ArrayList<>();
        }
        if (null == mSelectedBaoyangItemIds) {
            mSelectedBaoyangItemIds = new ArrayList<>();
        }

        if (null == item) {
            return;
        }

        if (item.isChecked()) {
            mSelectedBaoyangItems.add(item.getItem());
            mSelectedBaoyangItemIds.add(item.getId() + "");
        } else {
            mSelectedBaoyangItems.remove(item.getItem());
            mSelectedBaoyangItemIds.remove(item.getId() + "");
        }
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rightView), this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hideInput(rlCustomItem);
            if (null != mSlideDownMenu && mSlideDownMenu.isShown()) {
                mSlideDownMenu.hide();
                return false;
            } else {
                onBack();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideInput(rlCustomItem);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.leftView:
                onBack();
                onBackPressed();
                break;
        }
    }

    private void onBack() {
        Intent intent = new Intent();
        intent.putExtra(KEY_ITEM_IDS_VALUE, mSelectedBaoyangItemIds);
        intent.putExtra(KEY_ITEM_VALUE, mSelectedBaoyangItems);
        setResult(Activity.RESULT_OK, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_TYPE_DELETE:
                if (resultCode == RESULT_OK) {
                    deleteItem(mBaoyangItemDelete);
                }
                mBaoyangItemDelete = null;
                break;
        }
    }

    private void showInput(View view) {
        mIMM.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    private void hideInput(View view) {
        mIMM.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showDialog() {
        mSlideDownMenu = new SlideDownMenu(this, R.color.daze_translucence2);
        mSlideDownMenu.setContentView(R.layout.popup_baoyang_add_custom_item, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btnCancel:
                        mSlideDownMenu.hide();
                        hideInput(rlCustomItem);
                        break;
                    case R.id.btnAdd:
                        EditText etItemName = (EditText) mSlideDownMenu.getContentView().findViewById(R.id.etItemName);
                        String itemName = etItemName.getText().toString().trim();
                        int length = itemName.length();
                        if (length == 0) {
                            ToastUtil.makeShortToast(mContext, "请输入保养项目");
                            return;
                        } else if (length > 12) {
                            ToastUtil.makeShortToast(mContext, "请输入12个字以内");
                            return;
                        }

                        if (null == mCustomItems) {
                            mCustomItems = new ArrayList<>();
                        }
                        boolean hasSame = false;
                        // 查找是否有重复的
                        for (BaoyangItem item : mCustomItems) {
                            if (item.getItem().equals(itemName)) {
                                // 相同项目
                                hasSame = true;
                                break;
                            }
                        }

                        if (hasSame) {
                            ToastUtil.makeShortToast(mContext, "已有相同的项目");
                            return;
                        }
                        mSlideDownMenu.hide();
                        hideInput(rlCustomItem);
                        addItem(itemName);
                        break;
                }
            }
        });
        mSlideDownMenu.show();
        EditText etItemName = (EditText) mSlideDownMenu.getContentView().findViewById(R.id.etItemName);
        showInput(etItemName);
    }
}
