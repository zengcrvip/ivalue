package com.axon.market.common.util;

import org.apache.log4j.Logger;

import java.security.MessageDigest;

/**
 * Created by chenyu on 2016/4/25.
 */
public class MD5Util
{
    private static final Logger LOG = Logger.getLogger(MD5Util.class.getName());

    /**
     * 全局数组
     */
    private static final String[] STR_DIGITS = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    /**
     * 返回形式为数字跟字符串
     * @param bByte
     * @return
     */
    private static String byteToArrayString(byte bByte)
    {
        int iRet = bByte;
        if (iRet < 0)
        {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return STR_DIGITS[iD1] + STR_DIGITS[iD2];
    }

    /**
     * 转换字节数组为16进制字串
     * @param bByte
     * @return
     */
    private static String byteToString(byte[] bByte)
    {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++)
        {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static String getMD5Code(String strObj)
    {
        String resultString = null;
        try
        {
            resultString = strObj;
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(resultString.getBytes("UTF-8")));
        }
        catch (Exception ex)
        {
            LOG.error("getMD5Code error", ex);
        }
        return resultString;
    }

}
