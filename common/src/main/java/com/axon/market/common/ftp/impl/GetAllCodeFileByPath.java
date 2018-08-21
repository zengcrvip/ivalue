package com.axon.market.common.ftp.impl;

import com.axon.market.common.ftp.IFtpAction;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhuwne on 2017/8/4.
 */
public class GetAllCodeFileByPath implements IFtpAction {
    private String filePath;

    private List<String> ftpFiles = new ArrayList<String>();

    public GetAllCodeFileByPath(String filePath)
    {
        this.filePath = filePath;
    }

    public List<String> getFtpFiles()
    {
        return ftpFiles;
    }

    @Override
    public boolean action(FTPClient ftpClient) throws IOException
    {
        if (filePath.startsWith("/"))
        {
            //更换目录到当前目录
            ftpClient.changeWorkingDirectory(filePath);

            ftpClient.enterLocalPassiveMode();
            ftpClient.configure(new FTPClientConfig("com.axon.market.common.ftp.MyUnixFTPEntryParser"));
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files)
            {
                if (file.isFile())
                {
                    if (file.getName().startsWith("dixiao"))
                    {
                        ftpFiles.add(filePath + file.getName());
                    }
                }
            }
        }
        return true;
    }
}
