package com.chengniu.client;

import com.chengniu.client.common.CommonUtil;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.XWorkJUnit4TestCase;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by haiyang on 16/1/27.
 */
public class QuoteRobot extends XWorkJUnit4TestCase {

    @Test
    public void testNodejsInterop() throws IOException {
        Map<String, String> params = Maps.newHashMap();
        params.put("vehicleNum", "浙AZW598");
        params.put("tokenNo", "330481198811104229");
        params.put("frameNo", "LSVAG2NG6EN012484");
        params.put("motorNo", "2235845");
        params.put("owner", "占良杰");
        params.put("brand", "斯柯达SVW71613GS轿车");
        params.put("reg", "2014/02/18");
        params.put("issue", "2014-02-18");

        Process process = Runtime.getRuntime().exec(
                new String[] {
                        "node",
                        "piccSpider2.js",
                        CommonUtil.gson().toJson(params)
                },
                null,
                new File("/Users/haiyang/Desktop/work_style/保险/技术/报价机器人/SpiderDemo/"));

            String line;
            boolean exited;
            do {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    process.exitValue();
                    exited = false;
                } catch (Exception e){
                    exited = true;
                }
            } while (!exited);

            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            System.out.println("－－－－－－－－－－－－－－－－－\n结果：" + sb.toString());

    }

}
