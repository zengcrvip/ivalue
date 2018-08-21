package com.axon.market.core.monitor;

/**
 * Created by chenyu on 2016/8/19.
 */
public class FilePreProcessImpl implements IFilePreProcess
{
    private IFilePreProcess filePreProcess;

    public void setFilePreProcess(IFilePreProcess filePreProcess)
    {
        this.filePreProcess = filePreProcess;
    }

    @Override
    public int getColumnCount(String line)
    {
        return filePreProcess.getColumnCount(line);
    }

    @Override
    public String tagFileLinePreProcess(String line)
    {
        return filePreProcess.tagFileLinePreProcess(line);
    }

    @Override
    public String modelFileLinePreProcess(String line)
    {
        return filePreProcess.modelFileLinePreProcess(line);
    }

    @Override
    public String dmcFileLinePreProcess(String line)
    {
        return filePreProcess.dmcFileLinePreProcess(line);
    }
}
