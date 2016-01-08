package com.alibaba.com.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AppwebActivity extends Activity {
    private EditText webUrlEditText;
    private WebView webView;
    String webUrl = "http://192.168.10.201/index.html";

    @JavascriptInterface
    public void alert(String title, String message) {
        AlertDialog.Builder builer = new AlertDialog.Builder(AppwebActivity.this);
        builer.setTitle(title);
        builer.setMessage(message);
        builer.show();
    }

    @JavascriptInterface
    public void capture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @JavascriptInterface
    public void tip(String message) {
        if (message != null && message.trim().length() > 0)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void refresh(View view) {
        if (this.webUrlEditText.getText() != null) {
            Toast.makeText(this, "加载中" + this.webUrlEditText.getText(), Toast.LENGTH_LONG).show();
            webView.loadUrl(this.webUrlEditText.getText().toString());
        }
    }

    @JavascriptInterface
    public void phone(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivity(intent);
    }

    @JavascriptInterface
    public void video() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @JavascriptInterface
    public String get(String url) {
        try {
            if (url == null || url.trim().length() == 0) {
                return null;
            }
            if (!url.startsWith("http")) {
                url = "http://cunli.zhanyaa.com" + url;
            }
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
            return HttpClientUtil.doGet(url);
        } catch (Exception e) {
            return null;
        }
    }

    @JavascriptInterface
    public String post(String url, String content) {
        try {
            Toast.makeText(this, "加载中" + webUrl, Toast.LENGTH_LONG).show();
            return HttpClientUtil.doPost(url, content);
        } catch (Exception e) {
            return null;
        }
    }

    @JavascriptInterface
    public String md5(String url) {
        try {
            if (url == null || url.trim().length() == 0) {
                return null;
            }
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
            return url;
        } catch (Exception e) {
            return null;
        }
    }

    @JavascriptInterface
    public String getUnSign(String url) {
        try {
            Toast.makeText(this, "加载中" + webUrl, Toast.LENGTH_LONG).show();
            return HttpClientUtil.doGet(url);
        } catch (Exception e) {
            return null;
        }
    }

    @JavascriptInterface
    public String getUserId() {
        return ((AlibabaApplication) this.getApplication()).getUserId();
    }

    @JavascriptInterface
    public String getAreaids() {
        return ((AlibabaApplication) this.getApplication()).getAreaids();
    }

    @JavascriptInterface
    public String getUsername() {
        return ((AlibabaApplication) this.getApplication()).getUsername();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.UrlWeb);
        webUrlEditText = (EditText) findViewById(R.id.webUrlEditText);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.addJavascriptInterface(this, "android");
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Toast.makeText(view.getContext(), "加载中", Toast.LENGTH_SHORT).show();
                view.loadUrl(url);
                return true;
            }
        });
        if (this.webUrl != null)
            webUrlEditText.setText(this.webUrl.toString());
        else
            webUrl = "";
        Toast.makeText(this, "加载中" + this.webUrl, Toast.LENGTH_LONG).show();
        webView.loadUrl(webUrl.toString());
    }
}