package com.kplus.car.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.model.response.UploadCertImgResponse;
import com.kplus.car.model.response.request.OrderInfoAddRequest;
import com.kplus.car.model.response.request.UploadCertImgRequest;
import com.kplus.car.util.FileItem;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.WebUtils;

import java.util.HashMap;

public class UploadProgressActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ProgressBar uploadProgressBar;
    private TextView tvFailInfo;
    private Button btCancel, btReUpload;
    private int nType, typeParam;
    private byte[] content;
    private long uploadBeginTime;
    private int progress;
    private float fStep;
    private String imageUrl;
    private long orderId;
    private String requestUrl;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(System.currentTimeMillis() - uploadBeginTime > 2*60*1000){
                        uploadProgressBar.setVisibility(View.GONE);
                        tvFailInfo.setVisibility(View.VISIBLE);
                        btReUpload.setTextColor(getResources().getColor(R.color.daze_textcolor1));
                        btReUpload.setEnabled(true);
                        disconnectConnection();
                    }
                    else if((progress + fStep) < 95){
                        progress += fStep;
                        uploadProgressBar.setProgress((int) progress);
                        Message message = new Message();
                        message.what = 1;
                        sendMessageDelayed(message, 200);
                    }
                    break;
                case 2:
                    progress += fStep;
                    if(progress < 100){
                        uploadProgressBar.setProgress((int) progress);
                        Message message = new Message();
                        message.what = 2;
                        sendMessageDelayed(message, 200);
                    }
                    else{
                        progress = 100;
                        uploadProgressBar.setProgress(progress);
                        Message message = new Message();
                        message.what = 3;
                        sendMessage(message);
                    }
                    break;
                case 3:
                    Intent intent = new Intent();
                    intent.putExtra("imageUrl", imageUrl);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.daze_progress_layout);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        uploadProgressBar = (ProgressBar) findViewById(R.id.uploadProgressBar);
        uploadProgressBar.setVisibility(View.VISIBLE);
        tvFailInfo = (TextView) findViewById(R.id.tvFailInfo);
        tvFailInfo.setVisibility(View.GONE);
        btCancel = (Button) findViewById(R.id.btCancel);
        btReUpload = (Button) findViewById(R.id.btReUpload);
        if(android.os.Build.VERSION.SDK_INT >= 11)
            setFinishOnTouchOutside(false);
    }

    @Override
    protected void loadData() {
        try {
            content = getIntent().getByteArrayExtra("content");
            if(content != null && content.length > 0) {
                nType = getIntent().getIntExtra("type", 0);
                typeParam = getIntent().getIntExtra("typeParam", 0);
                switch (nType) {
                    case KplusConstants.UPLOAD_AUTH_LICENCE_PHOTO:
                        uploadPicture(content, 1);
                        break;
                    case KplusConstants.UPLOAD_DRIVING:
                        uploadPicture(content,1);
                        break;
                    case KplusConstants.UPLOAD_DRIVER:
                        uploadPicture(content,2);
                        break;
                    case KplusConstants.UPLOAD_CARD:
                    case KplusConstants.UPLOAD_CARD2:
//                        orderId = getIntent().getLongExtra("orderId", 0);
//                        infoAdd(null, nType, null, content);
                        uploadPicture(content, 3);
                        break;
                    case KplusConstants.UPLOAD_TICKET:
                        uploadPicture(content, typeParam);
                        break;
                    default:
                        finish();
                        break;
                }
            }
            else{
                finish();
            }
        }catch(Exception e){
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void setListener() {
        btCancel.setOnClickListener(this);
        btReUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btCancel:
                disconnectConnection();
                handler.removeMessages(1);
                handler.removeMessages(2);
                handler.removeMessages(3);
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btReUpload:
                switch (nType) {
                    case KplusConstants.UPLOAD_AUTH_LICENCE_PHOTO:
                        uploadPicture(content, 1);
                        break;
                    case KplusConstants.UPLOAD_DRIVING:
                        uploadPicture(content,1);
                        break;
                    case KplusConstants.UPLOAD_DRIVER:
                        uploadPicture(content,2);
                        break;
                    case KplusConstants.UPLOAD_CARD:
                    case KplusConstants.UPLOAD_CARD2:
//                        orderId = getIntent().getLongExtra("orderId", 0);
//                        infoAdd(null, nType, null, content);
                        uploadPicture(content,3);
                        break;
                    case KplusConstants.UPLOAD_TICKET:
                        uploadPicture(content, typeParam);
                        break;
                    default:
                        finish();
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            handler.removeMessages(1);
            handler.removeMessages(2);
            handler.removeMessages(3);
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void uploadPicture(final byte[] content, final int typeParam)
    {
        new AsyncTask<Void, Void, UploadCertImgResponse>(){
            protected void onPreExecute() {
                uploadBeginTime = System.currentTimeMillis();
                fStep = 95/((float)5*content.length/(9*1024));
                progress = 0;
                uploadProgressBar.setProgress(progress);
                uploadProgressBar.setVisibility(View.VISIBLE);
                tvFailInfo.setVisibility(View.GONE);
                btReUpload.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
                btReUpload.setEnabled(false);
                tvTitle.setText("上传中");
                tvTitle.setTextColor(getResources().getColor(R.color.daze_black2));
                Message message = new Message();
                message.what = 1;
                handler.sendMessageDelayed(message,200);
            }

            @Override
            protected UploadCertImgResponse doInBackground(Void... params) {
                try{
                    UploadCertImgRequest request = new UploadCertImgRequest();
                    request.setParams(typeParam);
                    HashMap<String, FileItem> fileParams = new HashMap<String, FileItem>();
                    fileParams.put("file", new FileItem("" + System.currentTimeMillis(), content));
                    requestUrl = request.getServerUrl() + request.getApiMethodName();
                    return mApplication.client.executePostWithParams(request, fileParams);
                }
                catch(Exception e){
                    e.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(UploadCertImgResponse result) {
                handler.removeMessages(1);
                if(result != null && result.getCode() != null && result.getCode() == 0){
                    String imageUrl = result.getData().getValue();
                    if (!StringUtils.isEmpty(imageUrl)) {
                        fStep = (100 - progress)/(2*5);
                        handler.sendMessageDelayed(handler.obtainMessage(2), 200);
                        UploadProgressActivity.this.imageUrl = imageUrl;
                    } else {
                        updateUploadResult(false, result.getMsg());
                    }
                }
                else{
                    updateUploadResult(false, null);
                }
            }

        }.execute();
    }

    private void infoAdd(final String fileUrl, final int certType, final String ownerName, final byte[] content) {
        new AsyncTask<Object, Object, GetResultResponse>() {
            protected void onPreExecute() {
                uploadBeginTime = System.currentTimeMillis();
                fStep = 95/((float)5*content.length/(9*1024));
                progress = 0;
                uploadProgressBar.setProgress(progress);
                uploadProgressBar.setVisibility(View.VISIBLE);
                tvFailInfo.setVisibility(View.GONE);
                btReUpload.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
                btReUpload.setEnabled(false);
                tvTitle.setText("上传中");
                tvTitle.setTextColor(getResources().getColor(R.color.daze_black2));
                Message message = new Message();
                message.what = 1;
                handler.sendMessageDelayed(message,200);
            }
            @Override
            protected GetResultResponse doInBackground(Object... params) {
                OrderInfoAddRequest request = new OrderInfoAddRequest();
                request.setParams(orderId, fileUrl, certType, ownerName);
                try {
                    if (content != null) {
                        HashMap<String, FileItem> fileParams = new HashMap<String, FileItem>();
                        fileParams.put("file", new FileItem("" + System.currentTimeMillis(), content));
                        requestUrl = request.getServerUrl() + request.getApiMethodName();
                        return mApplication.client.executePostWithParams(request, fileParams);
                    } else
                        return mApplication.client.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(GetResultResponse result) {
                handler.removeMessages(1);
                if(result != null && result.getCode() != null && result.getCode() == 0){
                    if (result.getData().getResult()){
                        String imageUrl = result.getData().getUrl();
                        if (!StringUtils.isEmpty(imageUrl)) {
                            fStep = (100 - progress)/(2*5);
                            handler.sendMessageDelayed(handler.obtainMessage(2), 200);
                            UploadProgressActivity.this.imageUrl = imageUrl;
                        }
                        else{
                            updateUploadResult(false, result.getMsg());
                        }
                    }
                    else{
                        updateUploadResult(false, result.getMsg());
                    }
                }
                else{
                    updateUploadResult(false, null);
                }
            }

        }.execute();
    }

    private void updateUploadResult(boolean success, String msg){
        if(!success){
            tvTitle.setText("上传失败");
            tvTitle.setTextColor(getResources().getColor(R.color.daze_orangered5));
            uploadProgressBar.setVisibility(View.GONE);
            if(!StringUtils.isEmpty(msg))
                tvFailInfo.setText(msg);
            else
                tvFailInfo.setText("请检查网络是否正常");
            tvFailInfo.setVisibility(View.VISIBLE);
            btReUpload.setTextColor(getResources().getColor(R.color.daze_textcolor1));
            btReUpload.setEnabled(true);
        }
    }

    private void disconnectConnection(){
        if(WebUtils.connections != null && requestUrl != null && WebUtils.connections.containsKey(requestUrl)){
            try{
                WebUtils.connections.get(requestUrl).disconnect();
                WebUtils.connections.remove(requestUrl);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
