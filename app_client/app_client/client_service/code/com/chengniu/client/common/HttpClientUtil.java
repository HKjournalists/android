package com.chengniu.client.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import com.kplus.orders.execption.DisposeException;

public class HttpClientUtil {
	/**
	 * 提供给阳光的请求
	 * 
	 * @param url
	 * @param content
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, String content, String charset)
			throws Exception {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		if (!StringUtils.hasText(charset))
			charset = "gbk";
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		RequestConfig config = RequestConfig.custom().setSocketTimeout(250250)
				.setConnectTimeout(250250).setConnectionRequestTimeout(250250)
				.build();
		HttpPost post = new HttpPost(url);
		post.setConfig(config);
		post.setHeader("Content-Type", "application/xml");
		if (StringUtils.hasText(content)) {
			StringEntity stringentity = new StringEntity(content, charset);
			post.setEntity(stringentity);
		}
		try {
			HttpResponse httpResponse = closeableHttpClient.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				return EntityUtils.toString(entity, charset);
			} else {
				HttpEntity entity = httpResponse.getEntity();
				String mes = EntityUtils.toString(entity, charset);
				throw new DisposeException(mes, httpResponse.getStatusLine()
						.getStatusCode());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			closeableHttpClient.close();
		}
	}

	public static String doPost(String url, String content) throws Exception {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		RequestConfig config = RequestConfig.custom().setSocketTimeout(250250)
				.setConnectTimeout(250250).setConnectionRequestTimeout(250250)
				.build();
		HttpPost post = new HttpPost(url);
		post.setConfig(config);
		post.setHeader("Content-Type", "application/json;charset=UTF-8");
		if (StringUtils.hasText(content)) {
			StringEntity stringentity = new StringEntity(content, "utf-8");
			post.setEntity(stringentity);
		}
		try {
			HttpResponse httpResponse = closeableHttpClient.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				return EntityUtils.toString(entity, "utf-8");
			} else {
				HttpEntity entity = httpResponse.getEntity();
				String mes = EntityUtils.toString(entity, "utf-8");
				throw new DisposeException(mes, httpResponse.getStatusLine()
						.getStatusCode());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			closeableHttpClient.close();
		}
	}

	public static String doGet(String url) throws Exception {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		RequestConfig config = RequestConfig.custom().setSocketTimeout(250250)
				.setConnectTimeout(250250).setConnectionRequestTimeout(250250)
				.build();
		HttpGet get = new HttpGet(url);
		get.setConfig(config);
		get.setHeader("Content-Type", "application/json;charset=UTF-8");
		try {
			HttpResponse httpResponse = closeableHttpClient.execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				return EntityUtils.toString(entity, "utf-8");
			} else {
				HttpEntity entity = httpResponse.getEntity();
				String mes = EntityUtils.toString(entity, "utf-8");
				throw new DisposeException(mes, httpResponse.getStatusLine()
						.getStatusCode());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			closeableHttpClient.close();
		}
	}

	public static byte[] doDown(String url) throws Exception {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		RequestConfig config = RequestConfig.custom().setSocketTimeout(250250)
				.setConnectTimeout(250250).setConnectionRequestTimeout(250250)
				.build();
		HttpGet get = new HttpGet(url);
		get.setConfig(config);
		try {
			HttpResponse httpResponse = closeableHttpClient.execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				InputStream inputStream = entity.getContent();
				byte[] b = new byte[1024];
				int t = -1;
				while ((t = inputStream.read(b)) != -1)
					bos.write(b, 0, t);
				return bos.toByteArray();
			} else {
				HttpEntity entity = httpResponse.getEntity();
				String mes = EntityUtils.toString(entity, "utf-8");
				throw new DisposeException(mes, httpResponse.getStatusLine()
						.getStatusCode());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			closeableHttpClient.close();
		}
	}

}