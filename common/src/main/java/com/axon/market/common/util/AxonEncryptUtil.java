package com.axon.market.common.util;

//import com.axon.tool.encrypt.AxonEncrypt;
import org.apache.commons.lang.StringUtils;

/**
 * 安讯手机号加密
 * Created by chenyu on 2016/8/11.
 */
public class AxonEncryptUtil //extends AxonEncrypt
{
    public static AxonEncryptUtil getInstance()
    {
        return (AxonEncryptUtil) SpringUtil.getSingletonBean("axonEncrypt");
    }

//    @Override
    public String encrypt(String phone)
    {
        if (phone != null && phone.trim().length() == 11)
        {
            phone = "86" + phone;
        }
        // 加密
//        String encryptPhone = super.encrypt(phone);
        String encryptPhone = decrypt_super(phone);

        if (StringUtils.isEmpty(encryptPhone))
        {
            return phone;
        }
        else
        {
            return encryptPhone;
        }
    }

//    @Override
    public String decrypt(String encryptPhone)
    {
        // 解密
//        String phone = super.decrypt(encryptPhone);
        String phone = decrypt_super(encryptPhone);

        if (StringUtils.isEmpty(phone))
        {
            return encryptPhone;
        }
        else
        {
            return phone;
        }
    }

    public String decryptWithoutCountrycode(String encryptPhone)
    {
        // 解密
//        String phone = super.decrypt(encryptPhone);
        String phone = decrypt_super(encryptPhone);

        if (StringUtils.isEmpty(phone))
        {
            return encryptPhone;
        }
        else
        {
            if (phone.length()>11 && phone.substring(0,2).equals("86")){
                return phone.substring(2);

            }else{
                return phone;
            }

        }
    }

    public String decryptDesensitization(String encryptPhone)
    {
        // 解密
//        String phone = super.decrypt(encryptPhone);
        String phone = decrypt_super(encryptPhone);

        if (StringUtils.isEmpty(phone))
        {
            return encryptPhone;
        }
        else
        {
            if (phone.length() > 11 && phone.substring(0,2).equals("86"))
            {
                return phone.substring(2,5) + "****"+phone.substring(9);

            }else{
                return phone.substring(0,3) + "****"+phone.substring(7);
            }

        }
    }

    private  String encrypt_super(String phone) {
        if(phone != null && phone.trim().length() == 13) {
            StringBuilder sb = new StringBuilder(phone.trim());
            char s1 = sb.charAt(11);
            char s2 = sb.charAt(12);
            int sum = 0;

            int head;
            for(head = 0; head < sb.length(); ++head) {
                sum += sb.charAt(head);
            }

            head = Math.abs(~(s1 * sum) * 151817);
            int r1 = s1 * s2 % 10;
            int offset = sb.length() - 2 - Integer.valueOf(s1 - 48).intValue();
            sb.insert(offset, r1);
            int r2 = (s1 + s2) % 10;
            if(s2 > s1) {
                offset = sb.length() - 1 - 2 - Integer.valueOf(s2 - 48).intValue();
            } else {
                offset = sb.length() - 1 - 2 - Integer.valueOf(s2 - 48).intValue() + 1;
            }

            sb.insert(offset, r2);
            sb.setCharAt(0, (char)(r1 + 48));
            sb.setCharAt(1, (char)(r2 + 48));

            int i;
            for(i = 2; i < 8; ++i) {
                sb.setCharAt(i, (char)((sb.charAt(i) - 48 + r1) % 10 + 48));
            }

            for(i = 8; i < 13; ++i) {
                sb.setCharAt(i, (char)((sb.charAt(i) - 48 + 10 - r2) % 10 + 48));
            }

            sb.insert(0, String.valueOf(head).substring(0, 3));
            return sb.toString();
        } else {
            return null;
        }
    }

    private String decrypt_super(String enphone) {
        if(enphone != null && enphone.trim().length() == 18) {
            StringBuilder sb = new StringBuilder(enphone.trim());
            sb.delete(0, 3);
            int r1 = sb.charAt(0) - 48;
            int r2 = sb.charAt(1) - 48;

            int s1;
            for(s1 = 2; s1 < 8; ++s1) {
                sb.setCharAt(s1, (char)((sb.charAt(s1) - 48 + 10 - r1) % 10 + 48));
            }

            for(s1 = 8; s1 < 13; ++s1) {
                sb.setCharAt(s1, (char)((sb.charAt(s1) - 48 + r2) % 10 + 48));
            }

            s1 = 11 - (sb.charAt(13) - 48);
            int s2 = 11 - (sb.charAt(14) - 48);
            sb.setCharAt(0, '8');
            sb.setCharAt(1, '6');
            if(s1 >= s2) {
                sb.deleteCharAt(s2);
                sb.deleteCharAt(s1);
            } else {
                sb.deleteCharAt(s1);
                sb.deleteCharAt(s2);
            }

            return sb.toString();
        } else {
            return null;
        }
    }
}
