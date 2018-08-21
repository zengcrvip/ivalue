package com.axon.market.core.monitor.area;

import com.axon.market.common.bean.GreenPlumServerBean;
import com.axon.market.core.monitor.IFilePreProcess;

/**
 * Created by chenyu on 2016/8/19.
 */
public class GuangXiFilePreProcess implements IFilePreProcess
{
    private GreenPlumServerBean greenPlumServerBean;

    public void setGreenPlumServerBean(GreenPlumServerBean greenPlumServerBean)
    {
        this.greenPlumServerBean = greenPlumServerBean;
    }

    private byte b[] = {0x01};

    private String replaceStr = new String(b);

    @Override
    public int getColumnCount(String line)
    {
        return calculateColumnCount(line);
    }

    private int calculateColumnCount(String line)
    {
        if (line.endsWith(replaceStr))
        {
            return calculateColumnCount(line.substring(0, line.length() - replaceStr.length())) + 1;
        }
        else
        {
            return line.split(replaceStr).length;
        }
    }

    @Override
    public String tagFileLinePreProcess(String line)
    {
        return line.replace("\"\"", "").replace("null", "").replace("\"0000-00-00\"", "").replace("0000-00-00", "").replace(",", "，")
                .replace("\"0000-00-00 00:00:00\"", "").replace("0000-00-00 00:00:00", "").replace(replaceStr, "|");
    }

    @Override
    public String modelFileLinePreProcess(String line)
    {
        int delimiterIndex = line.indexOf(greenPlumServerBean.getGpDelimiterChar());
        return line.substring(0, delimiterIndex == -1 ? line.length() : delimiterIndex);
    }

    @Override
    public String dmcFileLinePreProcess(String line)
    {
        return line.replace("|", ",").replace("#", "|");
    }
}
