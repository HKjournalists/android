package com.kplus.car.carwash.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.reflect.InvocationTargetException;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/8/3.
 * <br/><br/>
 */
public class CNWebview extends WebView {

    private IWebViewCallbackListener mListener = null;

    public void setWebViewCallbackListener(IWebViewCallbackListener listener) {
        mListener = listener;
    }

    public CNWebview(Context context) {
        this(context, null, 0);
    }

    public CNWebview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CNWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSettings();
    }

    private class WebviewWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (null != mListener) {
                mListener.onProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (null != mListener) {
                mListener.onWebTitle(title);
            }
            super.onReceivedTitle(view, title);
        }
    }

    private class WebviewWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (null != mListener) {
                mListener.setOnLoad();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (null != mListener) {
                mListener.onCanGoBack(canGoBack());
                mListener.onCanGoForward(canGoForward());
                mListener.setFinishLoad();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.canGoBack()) {
            this.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initSettings() {
        WebSettings settings = this.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setAllowFileAccess(true);
        settings.setBuiltInZoomControls(false);
        // 下面这两行代码一定要加，不然会使字体显示很大
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBlockNetworkImage(false);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            settings.setPluginState(WebSettings.PluginState.ON);//支持所有版本
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setLayerType(WebView.LAYER_TYPE_HARDWARE, null);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        this.setWebViewClient(new WebviewWebClient());
        this.setWebChromeClient(new WebviewWebChromeClient());
    }

    public void onWebResume() {
        try {
            this.getClass().getMethod("onResume").invoke(this, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void onWebPause() {
        try {
            this.getClass().getMethod("onPause").invoke(this, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public interface IWebViewCallbackListener {
        /**
         * 是否可以回退
         *
         * @param isGoBack 是否可以回退
         */
        void onCanGoBack(boolean isGoBack);

        /**
         * 是否可以前进
         *
         * @param isGoForward 是否可以前进
         */
        void onCanGoForward(boolean isGoForward);

        /**
         * 页面加载了多少
         *
         * @param newProgress 进度，0到100
         */
        void onProgress(int newProgress);

        /**
         * 获取网页的标题
         *
         * @param title 标题
         */
        void onWebTitle(String title);

        /**
         * 设置图标正在加载
         */
        void setOnLoad();

        /**
         * 设置图标完成加载
         */
        void setFinishLoad();
    }
}
