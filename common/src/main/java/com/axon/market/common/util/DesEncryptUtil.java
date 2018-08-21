package com.axon.market.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @project: Mas
 * @description: 报文加解密工具类
 * @author john.zhu (john.zhu@99bill.com)
 */
public class DesEncryptUtil {

	private static String Algorithm = "DESede"; // 定义 加密算法,可用DES,DESede,Blowfish

	/**
	 * 生成密钥
	 *
	 * @param key
	 * @return
	 */
	public static SecretKey getKey(String key) {
		SecretKey deskey = null;
		try {
			KeyGenerator gen = KeyGenerator.getInstance(Algorithm);
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key.getBytes());
			gen.init(112, secureRandom);
			deskey = gen.generateKey();
			gen = null;
			return deskey;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return deskey;
	}

	/**
	 * 加密
	 *
	 * @param key
	 * @param src
	 * @return
	 */
	public static String encrypt(String key, String src) {
		SecretKey deskey = getKey(key);
		// SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
		String destCode = null;
		try {
			Cipher c1 = Cipher.getInstance(Algorithm + "/ECB/PKCS5Padding");
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			byte[] encoded = c1.doFinal(src.getBytes("UTF-8"));
			destCode = new String(Base64.encodeBase64(encoded));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return destCode;
	}

	/**
	 * 解密
	 *
	 * @param key
	 * @param src
	 * @return
	 */
	public static String decrypt(String key, String src) {

		SecretKey deskey = getKey(key);
		String sourceXml = null;
		try {
			Cipher c2 = Cipher.getInstance(Algorithm + "/ECB/PKCS5Padding");
			c2.init(Cipher.DECRYPT_MODE, deskey);
			sourceXml = new String(c2.doFinal(Base64.decodeBase64(src.getBytes())), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sourceXml;
	}

	public static void main(String[] args) {



	}

}
