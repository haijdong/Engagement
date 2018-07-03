package cn.segi.framework.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	/**
	 * 招商物业车场MD5
	 * @param str
	 * @param key
	 * @return
	 */
	public static String getMD5ForCMP(String str, String key) {
		try {
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update((str).getBytes());
			byte[] md = mdInst.digest();
			String signKey = byteToHexString(md) + "|" + key;
			mdInst.update(signKey.getBytes());
			signKey = byteToHexString(mdInst.digest());
			return signKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static String getMd5Value(String key){
		String value = "";
		try {
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(key.getBytes());
			byte[] md = mdInst.digest();
			value = byteToHexString(md);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	public static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toLowerCase());
		}
		return hexString.toString();
	}
}
