package com.chengniu.client.common;

import java.security.Key;
import java.security.Security;
import java.util.ResourceBundle;

import javax.crypto.Cipher;

/**
 * ec给的解密类
 *
 */
public class EncryptDecryptData {

	private static String strDefaultKey = "national";

	private Cipher encryptCipher = null;

	private Cipher decryptCipher = null;

	private final static String APPLICATIONSREOURCE = "ApplicationResources";

	/**
	 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和private static byte[]
	 * hexStr2ByteArr(String strIn) 互为可逆的转换过程
	 * 
	 * @param arrB
	 *            需要转换的byte数组
	 * @return 转换后的字符串
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 */
	private static String byteArr2HexStr(byte[] arrB) throws Exception {

		int iLen = arrB.length;

		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);

		for (int i = 0; i < iLen; i++) {

			int intTmp = arrB[i];

			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}

			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}

			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/**
	 * 将表示16进制值的字符串转换为byte数组， 和private static String byteArr2HexStr(byte[] arrB)
	 * 互为可逆的转换过程
	 * 
	 * @param strIn
	 *            需要转换的字符串
	 * @return 转换后的byte数组
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>
	 */
	private static byte[] hexStr2ByteArr(String strIn) throws Exception {

		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];

		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}

		return arrOut;
	}

	/**
	 * 默认构造方法，使用默认密钥
	 * 
	 * @throws Exception
	 */
	private EncryptDecryptData() throws Exception {
		this(strDefaultKey);
	}

	/**
	 * 指定密钥构造方法
	 * 
	 * @param strKey
	 *            指定的密钥
	 * @throws Exception
	 */
	@SuppressWarnings("restriction")
	private EncryptDecryptData(String strKey) throws Exception {

		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		Key key = getKey(strKey.getBytes());

		encryptCipher = Cipher.getInstance("DES");
		encryptCipher.init(Cipher.ENCRYPT_MODE, key);

		decryptCipher = Cipher.getInstance("DES");
		decryptCipher.init(Cipher.DECRYPT_MODE, key);
	}

	/**
	 * 加密字节数组
	 * 
	 * @param arrB
	 *            需加密的字节数组
	 * @return 加密后的字节数组
	 * @throws Exception
	 */
	private byte[] encrypt(byte[] arrB) throws Exception {
		return encryptCipher.doFinal(arrB);
	}

	/**
	 * 加密字符串
	 * 
	 * @param strIn
	 *            需加密的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	private String encrypt(String strIn) throws Exception {
		return byteArr2HexStr(encrypt(strIn.getBytes()));
	}

	/**
	 * 加密字符串
	 * 
	 * @param strIn
	 *            需加密的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public static String encode(String strIn) throws Exception {

		// 密钥
		String secretKey = "areaCode";

		EncryptDecryptData des = new EncryptDecryptData(secretKey);
		if (strIn == null) {
			strIn = "";
		}
		return des.encrypt(strIn);
	}

	/**
	 * 加密字符串 解决汉字夸平台乱码问题
	 * 
	 * @param strIn
	 * @return
	 * @throws Exception
	 */
	public static String encodeChinese(String strIn) throws Exception {
		// 将字符串转换为16进制字符串
		return encode(enUnicode(strIn));
	}

	/**
	 * 加密字符串
	 * 
	 * @param strIn
	 *            需加密的字符串
	 * @param secretKey
	 *            密钥
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public static String encode(String strIn, String secretKey)
			throws Exception {

		EncryptDecryptData des = new EncryptDecryptData(secretKey);
		if (strIn == null) {
			strIn = "";
		}
		return des.encrypt(strIn);
	}

	/**
	 * 解密字节数组
	 * 
	 * @param arrB
	 *            需解密的字节数组
	 * @return 解密后的字节数组
	 * @throws Exception
	 */
	private byte[] decrypt(byte[] arrB) throws Exception {
		return decryptCipher.doFinal(arrB);
	}

	/**
	 * 解密字符串
	 * 
	 * @param strIn
	 *            需解密的字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	private String decrypt(String strIn) throws Exception {
		return new String(decrypt(hexStr2ByteArr(strIn)));
	}

	/**
	 * 解密字符串
	 * 
	 * @param strIn
	 *            需解密的字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public static String decode(String strIn) throws Exception {

		ResourceBundle resourceBundle = ResourceBundle
				.getBundle(APPLICATIONSREOURCE);

		// 密钥
		String secretKey = resourceBundle.getString("secret.key");

		EncryptDecryptData des = new EncryptDecryptData(secretKey);
		return des.decrypt(strIn);

	}

	/**
	 * 解密字符串 解决汉字夸平台乱码问题
	 * 
	 * @param strIn
	 * @return
	 * @throws Exception
	 */
	public static String decodeChinese(String strIn) throws Exception {
		// 将返回16进制串转换为正常字符串
		return deUnicode(decode(strIn));
	}

	/**
	 * 解密字符串
	 * 
	 * @param strIn
	 *            需解密的字符串
	 * @param secretKey
	 *            密钥
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public static String decode(String strIn, String secretKey)
			throws Exception {

		if (secretKey == null)
			secretKey = "";

		EncryptDecryptData des = new EncryptDecryptData(secretKey);

		return des.decrypt(strIn);
	}

	/**
	 * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
	 * 
	 * @param arrBTmp
	 *            构成该字符串的字节数组
	 * @return 生成的密钥
	 * @throws java.lang.Exception
	 */
	private Key getKey(byte[] arrBTmp) throws Exception {

		// 创建一个空的8位字节数组（默认值为0）
		byte[] arrB = new byte[8];

		// 将原始字节数组转换为8位
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}

		// 生成密钥
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

		return key;
	}

	// 将16进制数转换为汉字
	private static String deUnicode(String content) {// 将16进制数转换为汉字
		String enUnicode = null;
		String deUnicode = null;
		for (int i = 0; i < content.length(); i++) {
			if (enUnicode == null) {
				enUnicode = String.valueOf(content.charAt(i));
			} else {
				enUnicode = enUnicode + content.charAt(i);
			}
			if (i % 4 == 3) {
				if (enUnicode != null) {
					if (deUnicode == null) {
						deUnicode = String.valueOf((char) Integer.valueOf(
								enUnicode, 16).intValue());
					} else {
						deUnicode = deUnicode
								+ String.valueOf((char) Integer.valueOf(
										enUnicode, 16).intValue());
					}
				}
				enUnicode = null;
			}

		}
		return deUnicode;
	}

	// 将汉字转换为16进制数
	private static String enUnicode(String content) {
		String enUnicode = null;
		for (int i = 0; i < content.length(); i++) {
			if (i == 0) {
				enUnicode = getHexString(Integer.toHexString(content.charAt(i))
						.toUpperCase());
			} else {
				enUnicode = enUnicode
						+ getHexString(Integer.toHexString(content.charAt(i))
								.toUpperCase());
			}
		}
		return enUnicode;
	}

	// 将汉字转换为16进制数
	private static String getHexString(String hexString) {
		String hexStr = "";
		for (int i = hexString.length(); i < 4; i++) {
			if (i == hexString.length())
				hexStr = "0";
			else
				hexStr = hexStr + "0";
		}
		return hexStr + hexString;
	}
}