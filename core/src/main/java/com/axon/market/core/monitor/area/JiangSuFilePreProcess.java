package com.axon.market.core.monitor.area;

import com.axon.market.common.bean.GreenPlumServerBean;
import com.axon.market.core.monitor.IFilePreProcess;

/**
 * Created by chenyu on 2016/8/19.
 */
public class JiangSuFilePreProcess implements IFilePreProcess
{
    private GreenPlumServerBean greenPlumServerBean;

    /**
     * 注入GP实体类
     *
     * @param greenPlumServerBean
     */
    public void setGreenPlumServerBean(GreenPlumServerBean greenPlumServerBean)
    {
        this.greenPlumServerBean = greenPlumServerBean;
    }

    @Override
    public int getColumnCount(String line)
    {
        return line.split("\t").length;
    }

    @Override
    public String tagFileLinePreProcess(String line)
    {
        return line.replace("\"\"", "").replace("null", "").replace("\"0000-00-00\"", "").replace("0000-00-00", "")
                .replace("\"0000-00-00 00:00:00\"", "").replace("0000-00-00 00:00:00", "");
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
