package com.axon.market.common.ftp;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

/**
 * Created by chenyu on 2016/10/18.
 */
public interface IFtpAction
{
    boolean action(FTPClient ftpClient) throws IOException;
}
