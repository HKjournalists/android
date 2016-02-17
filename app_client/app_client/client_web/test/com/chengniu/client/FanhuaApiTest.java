package com.chengniu.client;

import com.opensymphony.xwork2.XWorkJUnit4TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.StringUtils;

/**
 * Created by haiyang on 16/2/1.
 */
public class FanhuaApiTest extends XWorkJUnit4TestCase {

    @Test
    public  void testRemoveKG() {
        String fullLoad = "1000KG";
        if (!StringUtils.isEmpty(fullLoad)) {
            fullLoad = fullLoad.replaceAll("[kKgG]", "");
            Assert.assertEquals("", "1000", fullLoad);
        }

        fullLoad = "1000kg";
        if (!StringUtils.isEmpty(fullLoad)) {
            fullLoad = fullLoad.replaceAll("[kKgG]", "");
            Assert.assertEquals("", "1000", fullLoad);
        }

        fullLoad = "1000Kg";
        if (!StringUtils.isEmpty(fullLoad)) {
            fullLoad = fullLoad.replaceAll("[kKgG]", "");
            Assert.assertEquals("", "1000", fullLoad);
        }
    }
}
