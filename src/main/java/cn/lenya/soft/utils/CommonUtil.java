package cn.lenya.soft.utils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.regex.Pattern;

import cn.lenya.soft.core.bean.Base;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CommonUtil extends Base {

	public <T> Object parseObject(Map<String, Object> obj, Class<T> clzz, Map<String, Object> orm) {
		return initBean(obj, clzz, orm);
	}

	public static void setNULL(Object... args) {
		for (Object clazz : args) {
			if (null != clazz) {
				clazz = null;
			}
		}
	}

	/**
	 * 功能描述：对参数值进行判断，判断值否为空
	 * 
	 * @param args
	 */
	public static void isNULL(Object... args) throws NullPointerException {
		for (Object clazz : args) {
			if (null == clazz || "".equals(clazz)) {
				throw new NullPointerException("There is some NULL parameter , Please check it.");
			}
		}
	}

	public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
	public static final String REGEX_VFT_CODE = "[0-9]{6}";

	public static boolean isMobile(String mobile) {
		return Pattern.matches(REGEX_MOBILE, mobile);
	}

	public static boolean isLegalCode(String code) {
		return Pattern.matches(REGEX_VFT_CODE, code);
	}

	// 加密
	public static String forBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	// 解密
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String getLocalIP() throws UnknownHostException {
		InetAddress address;
		address = InetAddress.getLocalHost();
		return address.getHostAddress();
	}

}
