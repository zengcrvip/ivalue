package com.axon.market.core.monitor;

/**
 * Created by chenyu on 2016/8/19.
 */
public interface IFilePreProcess
{
    int getColumnCount(String line);

    String tagFileLinePreProcess(String line);

    String modelFileLinePreProcess(String line);

    String dmcFileLinePreProcess(String line);
}
