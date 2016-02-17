package com.kplus.car;

/**
 * 公用常量类。
 * 
 * @author carver.gu
 * @since 1.0, Sep 12, 2009
 */
public abstract class Constants {

	/** TOP默认时间格式 **/
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/** TOP Date默认时区 **/
	public static final String DATE_TIMEZONE = "GMT+8";

	/** UTF-8字符集 **/
	public static final String CHARSET_UTF8 = "UTF-8";

	/** GBK字符集 **/
	public static final String CHARSET_GBK = "GBK";

	/** TOP JSON 应格式 */
	public static final String FORMAT_JSON = "json";

	/** TOP XML 应格式 */
	public static final String FORMAT_XML = "xml";

	/** MD5签名方式 */
	public static final String SIGN_METHOD_MD5 = "md5";

	/** HMAC签名方式 */
	public static final String SIGN_METHOD_HMAC = "hmac";

    public static final int REQUEST_TYPE_CUSTOM = 0;
    public static final int REQUEST_TYPE_JIAZHAOFEN = 1;
    public static final int REQUEST_TYPE_NIANJIAN = 2;
    public static final int REQUEST_TYPE_CHEXIAN = 3;
    public static final int REQUEST_TYPE_REMIND = 4;
    public static final int REQUEST_TYPE_CHEDAI = 5;
    public static final int REQUEST_TYPE_BAOYANG = 6;
    public static final int REQUEST_TYPE_WEIZHANG = 7;
    public static final int REQUEST_TYPE_VEHICLE = 8;
    public static final int REQUEST_TYPE_BAOYANG_RECORD = 9;
    public static final int REQUEST_TYPE_AD = 12;
    public static final int REQUEST_TYPE_MAP = 13;
    public static final int REQUEST_TYPE_CLOSE = 15;
    public static final int REQUEST_TYPE_INSURANCE = 16;
    public static final int REQUEST_TYPE_CITY = 17;
    public static final int REQUEST_TYPE_TAKE_PHOTO = 18;
    public static final int REQUEST_TYPE_DELETE = 19;
    public static final int REQUEST_TYPE_GET_PHOTO = 20;
    public static final int REQUEST_TYPE_LOGIN = 21;
    public static final int REQUEST_TYPE_SWITCH_CITY = 22;
    public static final int REQUEST_TYPE_RESTRICT = 23;
    public static final int REQUEST_TYPE_MODEL = 24;
    public static final int REQUEST_TYPE_REMARK = 25;
    public static final int REQUEST_TYPE_WEATHER = 26;
    public static final int REQUEST_TYPE_ADD = 27;
    public static final int REQUEST_TYPE_BAOYANG_ITEM = 28;
    public static final int REQUEST_TYPE_BAOYANG_LOCATION = 29;
    public static final int REQUEST_TYPE_GUANJIA_HISTORY = 30;
    public static final int REQUEST_TYPE_REGIST = 31;
    public static final int REQUEST_TYPE_PROMO_CODE = 32;

    public static final int RESULT_TYPE_CHANGED = 1;
    public static final int RESULT_TYPE_REMOVED = 2;
    public static final int RESULT_TYPE_ADDED = 3;
    public static final int RESULT_TYPE_RELOAD = 4;

    public static final int REPEAT_TYPE_NONE = 100;
    public static final int REPEAT_TYPE_MONTH = 101;
    public static final int REPEAT_TYPE_MONTH2 = 102;
    public static final int REPEAT_TYPE_MONTH3 = 103;
    public static final int REPEAT_TYPE_MONTH4 = 104;
    public static final int REPEAT_TYPE_MONTH5 = 105;
    public static final int REPEAT_TYPE_MONTH6 = 106;
    public static final int REPEAT_TYPE_MONTH7 = 107;
    public static final int REPEAT_TYPE_MONTH8 = 108;
    public static final int REPEAT_TYPE_MONTH9 = 109;
    public static final int REPEAT_TYPE_MONTH10 = 110;
    public static final int REPEAT_TYPE_MONTH11 = 111;
    public static final int REPEAT_TYPE_YEAR = 112;
    public static final int REPEAT_TYPE_DAY = 113;
    public static final int REPEAT_TYPE_WEEK = 114;
}
