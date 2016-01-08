package com.alibaba.com.webview;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpClientUtil {
    public static String doPost(String fullUrl, String content) {
        InputStreamReader inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(fullUrl);
            Log.e("httpClient", fullUrl + content);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            urlConnection.connect();
            DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
            dos.writeUTF(content);
            dos.flush();
            dos.close();
            inputStream = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(inputStream);
            StringBuilder sb = new StringBuilder();
            if (urlConnection.getResponseCode() == 200) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (Exception e) {
            Log.e("httpClient", "网络连接出错");
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (urlConnection != null)
                    urlConnection.disconnect();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static String doGet(String fullUrl) {
        InputStreamReader inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(fullUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.connect();
            Log.e("httpClient", fullUrl);
            inputStream = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(inputStream);
            StringBuilder sb = new StringBuilder();
            if (urlConnection.getResponseCode() == 200) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (Exception e) {
            Log.e("httpClient", "网络连接出错");
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (urlConnection != null)
                    urlConnection.disconnect();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static byte[] download(String fullUrl) throws Exception {
        URL url = new URL(fullUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(3000);
        urlConnection.setReadTimeout(3000);
        urlConnection.connect();
        InputStream inputStream = urlConnection.getInputStream();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int t = -1;
            while ((t = inputStream.read(b)) != -1)
                bos.write(b, 0, t);
            return bos.toByteArray();
        } catch (Exception e) {
            Log.e("httpClient", "网络连接出错");
            return null;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (urlConnection != null)
                    urlConnection.disconnect();
            } catch (Exception e) {
            }
        }
    }

}