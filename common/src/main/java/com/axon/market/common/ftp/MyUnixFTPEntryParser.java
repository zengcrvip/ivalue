package com.axon.market.common.ftp;

import org.apache.commons.net.ftp.parser.UnixFTPEntryParser;

/**
 * Created by fgtg on 2017/4/11.
 */
public class MyUnixFTPEntryParser extends UnixFTPEntryParser
{
    private static final String REGEX =
            "([bcdlfmpSs-])"
                    //+"(((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-])))\\+?\\s+"
                    //To match the "." of "-rw-rw-rw-."   @author Sun Ying
                    +"(((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-])))\\+?\\.*\\s+"
                    + "(\\d+)\\s+"
                    + "(\\S+)\\s+"
                    + "(?:(\\S+)\\s+)?"
                    + "(\\d+)\\s+"

        /*
          numeric or standard format date
        */
                    //中文匹配问题出在此处，这个匹配只匹配2中形式：
                    //(1)2008-08-03
                    //(2)(Jan  9)  或   (4月 26)
                    //而出错的hp机器下的显示为 8月20日（没有空格分开）
                    //故无法匹配而报错
                    //将下面字符串改为：
                    + "((?:\\S+\\s+\\S+)|(?:\\S+))\\s+"
                    //+ "((?:\\d+[-/]\\d+[-/]\\d+)|(?:\\S+\\s+\\S+)|(?:\\S+))\\s+"
                    //上面这句是原来那位博主改的，但是第三部分(?:\\S+)已经包含了第一部分(?:\\d+[-/]\\d+[-/]\\d+)，
                    //所以我去掉了第一部分.  @author Sun Ying
                    //+ "((?:\\d+[-/]\\d+[-/]\\d+)|(?:\\S+\\s+\\S+))\\s+"

        /*
           year (for non-recent standard format)
                 or time (for numeric or recent standard format
                 */
                    + "(\\d+(?::\\d+)?)\\s+"

                    + "(\\S*)(\\s*.*)";

}
