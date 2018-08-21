package com.axon.market.common.util;

/**
 * Created by wangtt on 2017/3/7.
 */
public class Int64
{
    byte[] m_byteData;
    public Int64()
    {
        m_byteData = new byte[8];
    }
    public Int64(String str)
    {
        m_byteData = new byte[8];
        SetValue(str);
    }
    public byte[] Data()
    {
        return m_byteData;
    }
    public void SetValue(String str)
    {
        int len = str.length();
        int count = len / 2;
        String temp = "";
        short num = 0;
        int i = 0;
        for (i = 0; i < count; i ++)
        {
            temp = str.substring(i*2, i*2+1);
            num = (short)Integer.parseInt(temp);
            m_byteData[i] = (byte)(num<<4);
            temp = str.substring(i*2+1, i*2+2);
            num = (short)Integer.parseInt(temp);
            m_byteData[i] += (byte)(num);
        }
        if (len % 2 == 1)   // 最后一个数
        {
            temp = str.substring(len-1, len);
            num = (short)Integer.parseInt(temp);
            m_byteData[i] = (byte)(num<<4);
            num = 0x0f;   // 加入结束标志
            m_byteData[i] += (byte)(num);
        }
        else if (i < 8)   // 加入结束标志
        {
            num = 0xf0;
            m_byteData[i] = (byte)(num);
        }
    }

    public String ToString()
    {
        String str = "";
        short temp;
        for (int i = 0; i < 8; i ++)
        {
            temp = (short)((m_byteData[i] & 0xf0) >> 4);
            if (temp > 9)
            {
                break;
            }
            str += temp;
            temp = (short) (m_byteData[i] & 0x0f);
            if (temp > 9)
            {
                break;
            }
            str += temp;
        }
        return str;
    }
    public static String VauleOf(Int64 value)
    {
        String str = "";
        short temp;
        for (int i = 0; i < 8; i ++)
        {
            temp = (short)((value.m_byteData[i] & 0xf0) >> 4);
            if (temp > 9)
            {
                break;
            }
            str += temp;
            temp = (short) (value.m_byteData[i] & 0x0f);
            if (temp > 9)
            {
                break;
            }
            str += temp;
        }
        return str;
    }
}
