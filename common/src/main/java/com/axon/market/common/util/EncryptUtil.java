package com.axon.market.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * DES加密算法
 * Created by chenyu on 2016/7/21.
 */
public class EncryptUtil
{
    private static final Logger LOG = Logger.getLogger(EncryptUtil.class.getName());

    private static final String DES_ALGORITHM = "DES";

    /**
     * DES加密
     *
     * @param plainData
     * @param secretKey
     * @return
     */
    public static String encryption(String plainData, String secretKey)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(secretKey));
            byte[] buf = cipher.doFinal(plainData.getBytes("UTF-8"));
            return Base64Utils.encode(buf);
        }
        catch (Exception e)
        {
            LOG.error("EncryptUtil encryption error ", e);
            return null;
        }
    }

    /**
     * DES解密
     *
     * @param secretData
     * @param secretKey
     * @return
     */
    public static String decryption(String secretData, String secretKey)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, generateKey(secretKey));
            byte[] buf = cipher.doFinal(Base64Utils.decode(secretData.toCharArray()));
            return new String(buf, "UTF-8");
        }
        catch (Exception e)
        {
            LOG.error("EncryptUtil decryption error ", e);
            return null;
        }
    }


    /**
     * 获得秘密密钥
     *
     * @param secretKey
     * @return
     */
    private static SecretKey generateKey(String secretKey) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        //防止linux下 随机生成key
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(secretKey.getBytes("UTF-8"));

        // 为我们选择的DES算法生成一个KeyGenerator对象
        KeyGenerator kg = null;
        try
        {
            kg = KeyGenerator.getInstance(DES_ALGORITHM);
        }
        catch (Exception e)
        {
            LOG.error("EncryptUtil generateKey error ", e);
            throw e;
        }
        kg.init(secureRandom);

        // 生成密钥
        return kg.generateKey();
    }

    static class Base64Utils
    {

        static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
        static private byte[] codes = new byte[256];

        static
        {
            for (int i = 0; i < 256; i++)
            {
                codes[i] = -1;
            }
            for (int i = 'A'; i <= 'Z'; i++)
            {
                codes[i] = (byte) (i - 'A');
            }
            for (int i = 'a'; i <= 'z'; i++)
            {
                codes[i] = (byte) (26 + i - 'a');
            }
            for (int i = '0'; i <= '9'; i++)
            {
                codes[i] = (byte) (52 + i - '0');
            }
            codes['+'] = 62;
            codes['/'] = 63;
        }

        /**
         * 将原始数据编码为base64编码
         */
        static public String encode(byte[] data)
        {
            char[] out = new char[((data.length + 2) / 3) * 4];
            for (int i = 0, index = 0; i < data.length; i += 3, index += 4)
            {
                boolean quad = false;
                boolean trip = false;
                int val = (0xFF & (int) data[i]);
                val <<= 8;
                if ((i + 1) < data.length)
                {
                    val |= (0xFF & (int) data[i + 1]);
                    trip = true;
                }
                val <<= 8;
                if ((i + 2) < data.length)
                {
                    val |= (0xFF & (int) data[i + 2]);
                    quad = true;
                }
                out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
                val >>= 6;
                out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
                val >>= 6;
                out[index + 1] = alphabet[val & 0x3F];
                val >>= 6;
                out[index + 0] = alphabet[val & 0x3F];
            }

            return new String(out);
        }

        /**
         * 将base64编码的数据解码成原始数据
         */
        static public byte[] decode(char[] data)
        {
            int len = ((data.length + 3) / 4) * 3;
            if (data.length > 0 && data[data.length - 1] == '=')
            {
                --len;
            }
            if (data.length > 1 && data[data.length - 2] == '=')
            {
                --len;
            }
            byte[] out = new byte[len];
            int shift = 0;
            int accum = 0;
            int index = 0;
            for (int ix = 0; ix < data.length; ix++)
            {
                int value = codes[data[ix] & 0xFF];
                if (value >= 0)
                {
                    accum <<= 6;
                    shift += 6;
                    accum |= value;
                    if (shift >= 8)
                    {
                        shift -= 8;
                        out[index++] = (byte) ((accum >> shift) & 0xff);
                    }
                }
            }
            if (index != out.length)
            {
                throw new Error("miscalculated data length!");
            }
            return out;
        }
    }

    /**
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static String encryptAES(String data, String key, String iv) throws Exception
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;

            if (plaintextLength % blockSize != 0)
            {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new String(new Base64().encode(encrypted)).trim();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param data 密文
     * @param key  密钥，长度16
     * @param iv   偏移量，长度16
     * @return
     * @throws Exception
     */
    public static String decryptAES(String data, String key, String iv) throws Exception
    {
        try
        {
            byte[] encrypted1 = new Base64().decode(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString.trim();
        }
        catch (Exception e)
        {
            LOG.error("AES decrypt Error. ", e);
            return null;
        }
    }

}
