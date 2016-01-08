package com.alibaba.com.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends Activity {
    private Application application;
    private EditText username;
    private EditText password;

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 添加引导图片
     */
    public void addGuideImage(int guideResourceId) {
        View view = getWindow().getDecorView().findViewById(R.id.my_content_view);//查找通过setContentView上的根布局
        if (view == null) return;
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof FrameLayout) {
            final FrameLayout frameLayout = (FrameLayout) viewParent;
            if (guideResourceId != 0) {//设置了引导图片
                final ImageView guideImage = new ImageView(this);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                guideImage.setLayoutParams(params);
                guideImage.setScaleType(ImageView.ScaleType.FIT_XY);
                guideImage.setImageResource(guideResourceId);
                guideImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        frameLayout.removeView(guideImage);
                    }
                });
                frameLayout.addView(guideImage);//添加引导图片
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this, R.style.dialog_style).create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        Window window = alertDialog.getWindow();
////        WindowManager.LayoutParams lp = window.getAttributes();
////        lp.alpha = 0.9f;
////        window.setAttributes(lp);
//        window.setContentView(R.layout.layout);
//        alertDialog.show();
//        TextView tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);
//        tv_message.setText("其实这个只是一个广告弹出框的测试");
//        Intent intent = getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone");
//        this.startActivity(intent);
//        this.finish();
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        Button submit = (Button) findViewById(R.id.submit);
    }

    public void login(View view) {
        if (username.getText() == null || username.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
        }
        if (password.getText() == null || password.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "登录中" + username.getText(), Toast.LENGTH_SHORT).show();
            try {
                StringBuffer sb = new StringBuffer();
                sb.append("http://cunli.zhanyaa.com/yifaapi/app/login.php");
                sb.append("?appkey=sanxing&udid=sanxing&os=sanxing&osversion=sanxing&appversion=sanxing");
                sb.append("&username=");
                sb.append(username.getText().toString().trim());
                sb.append("&password=");
                sb.append(password.getText().toString());
                String result = get(sb.toString());
                application = this.getApplication();
                JSONObject jsonObject = new JSONObject(result);
                jsonObject = new JSONObject(jsonObject.get("userinfo").toString());
                String userId = jsonObject.get("userid").toString();
                String areaids = jsonObject.get("areaids").toString();
                String username = jsonObject.get("username").toString();
                Intent intent = new Intent();
                AlibabaApplication alibabaApplication = (AlibabaApplication) this.getApplication();
                alibabaApplication.setUserId(userId);
                alibabaApplication.setAreaids(areaids);
                alibabaApplication.setUsername(username);
                intent.setClass(LoginActivity.this, AppwebActivity.class);
                this.startActivity(intent);
                this.finish();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "系统忙", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String get(String url) {
        try {
            if (url.startsWith("http://cunli.zhanyaa.com")) {
                String[] pm = url.split("[?]");
                List<String> pmlist = new ArrayList<String>();
                if (pm != null && pm.length >= 2) {
                    String[] param = pm[1].split("[&]");
                    for (String pma : param) {
                        if (!pma.startsWith("sign=")) {
                            pmlist.add(pma.replace("=", ""));
                        }
                    }
                }
                Collections.sort(pmlist);
                StringBuffer sb = new StringBuffer();
                for (String pma : pmlist) {
                    sb.append(pma);
                }
                sb.append("D2F56EAFBQ");
                url = url + "&sign=" + MD5Util.MD5(sb.toString());
            }
            return new AsyncTask<String, Integer, String>() {
                @Override
                protected String doInBackground(String... params) {
                    return HttpClientUtil.doGet(params[0]);
                }
            }.execute(url.toString()).get();
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "系统忙", Toast.LENGTH_SHORT).show();
        }
        return null;

    }

    public void entryUrl(View view) {
        if (username.getText() == null || username.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
        }
        if (password.getText() == null || password.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "登录中" + username.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}