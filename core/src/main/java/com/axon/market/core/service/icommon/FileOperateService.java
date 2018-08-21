package com.axon.market.core.service.icommon;

import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.ftp.FtpOperate;
import com.axon.market.common.ftp.IFtpAction;
import com.axon.market.common.ftp.impl.FtpDownloadFileAction;
import com.axon.market.common.ftp.impl.FtpUploadFileAction;
import com.axon.market.common.ftp.impl.GetAllCodeFileByPath;
import com.axon.market.common.ftp.impl.GetAllFileByPath;
import com.axon.market.common.jsch.IJschAction;
import com.axon.market.common.jsch.JschOperate;
import com.axon.market.common.jsch.impl.SftpJschAction;
import com.axon.market.common.util.EncryptUtil;
import com.axon.market.common.util.SpringUtil;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyu on 2016/10/18.
 */
@Component("fileOperateService")
public class FileOperateService
{
    @Autowired
    @Qualifier("jschOperate")
    private JschOperate jschOperate;

    @Autowired
    @Qualifier("ftpOperate")
    private FtpOperate ftpOperate;

    public static FileOperateService getInstance()
    {
        return (FileOperateService) SpringUtil.getSingletonBean("fileOperateService");
    }

    /**
     * @param remoteServerDomain
     * @param srcFile
     * @param dstFile
     * @return
     */
    public boolean uploadFile(RemoteServerDomain remoteServerDomain, String srcFile, String dstFile)
    {
        boolean result = true;
        String connectType = remoteServerDomain.getConnectType();
        switch (connectType)
        {
            case "ftp":
            {
                IFtpAction ftpAction = new FtpUploadFileAction(srcFile, dstFile);
                result = ftpOperate.doAction(ftpAction, remoteServerDomain.getServerIp(), remoteServerDomain.getServerUser(), EncryptUtil.decryption(remoteServerDomain.getPassword(), "market"), String.valueOf(remoteServerDomain.getPort()));
                break;
            }
            case "sftp":
            {
                IJschAction jschAction = new SftpJschAction("PUT", srcFile, dstFile);
                result = jschOperate.doAction(jschAction, remoteServerDomain.getServerIp(), remoteServerDomain.getServerUser(), EncryptUtil.decryption(remoteServerDomain.getPassword(), "market"), String.valueOf(remoteServerDomain.getPort()));
                break;
            }
            default:
            {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * @param remoteServerDomain
     * @param srcFile
     * @param dstFile
     * @return
     */
    public boolean downloadFile(RemoteServerDomain remoteServerDomain, String srcFile, String dstFile)
    {
        boolean result = true;
        String connectType = remoteServerDomain.getConnectType();
        switch (connectType)
        {
            case "ftp":
            {
                IFtpAction ftpAction = new FtpDownloadFileAction(srcFile, dstFile);
                result = ftpOperate.doAction(ftpAction, remoteServerDomain.getServerIp(), remoteServerDomain.getServerUser(), EncryptUtil.decryption(remoteServerDomain.getPassword(), "market"), String.valueOf(remoteServerDomain.getPort()));
                break;
            }
            case "sftp":
            {
                IJschAction jschAction = new SftpJschAction("GET", srcFile, dstFile);
                result = jschOperate.doAction(jschAction, remoteServerDomain.getServerIp(), remoteServerDomain.getServerUser(), EncryptUtil.decryption(remoteServerDomain.getPassword(), "market"), String.valueOf(remoteServerDomain.getPort()));
                break;
            }
            default:
            {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * @param remoteServerDomain
     * @param dstFile
     * @return
     */
    public boolean deleteFile(RemoteServerDomain remoteServerDomain, String dstFile)
    {
        boolean result = true;
        String connectType = remoteServerDomain.getConnectType();
        switch (connectType)
        {
            case "sftp":
            {
                IJschAction jschAction = new SftpJschAction("DELETE", null, dstFile);
                result = jschOperate.doAction(jschAction, remoteServerDomain.getServerIp(), remoteServerDomain.getServerUser(), EncryptUtil.decryption(remoteServerDomain.getPassword(), "market"), String.valueOf(remoteServerDomain.getPort()));
                break;
            }
            default:
            {
                result = false;
                break;
            }
        }
        return result;
    }

    public List<String> getAllFileByPath(RemoteServerDomain remoteServerDomain, String srcFile)
    {
        List<String> result = new ArrayList<String>();
        String connectType = remoteServerDomain.getConnectType();
        switch (connectType)
        {
            case "ftp":
            {
                GetAllFileByPath ftpAction = new GetAllFileByPath(srcFile);
                if (ftpOperate.doAction(ftpAction, remoteServerDomain.getServerIp(), remoteServerDomain.getServerUser(), EncryptUtil.decryption(remoteServerDomain.getPassword(), "market"), String.valueOf(remoteServerDomain.getPort())))
                {
                    result = ftpAction.getFtpFiles();
                }
                break;
            }
        }
        return result;
    }

    public List<String> getCodeFileByPath(RemoteServerDomain remoteServerDomain, String srcFile)
    {
        List<String> result = new ArrayList<String>();
        String connectType = remoteServerDomain.getConnectType();
        switch (connectType)
        {
            case "ftp":
            {
                GetAllCodeFileByPath ftpAction = new GetAllCodeFileByPath(srcFile);
                if (ftpOperate.doAction(ftpAction, remoteServerDomain.getServerIp(), remoteServerDomain.getServerUser(), EncryptUtil.decryption(remoteServerDomain.getPassword(), "market"), String.valueOf(remoteServerDomain.getPort())))
                {
                    result = ftpAction.getFtpFiles();
                }
                break;
            }
        }
        return result;
    }

    /**
     * @param remoteServerDomain
     * @return
     */
    public boolean testConnection(RemoteServerDomain remoteServerDomain)
    {
        boolean result;
        String connectType = remoteServerDomain.getConnectType();
        switch (connectType)
        {
            case "ftp":
            {
                result = ftpOperate.doAction(new IFtpAction()
                {
                    @Override
                    public boolean action(FTPClient ftpClient) throws IOException
                    {
                        return true;
                    }
                }, remoteServerDomain.getServerIp(), remoteServerDomain.getServerUser(), remoteServerDomain.getPassword(), String.valueOf(remoteServerDomain.getPort()));
                break;
            }
            case "sftp":
            {
                result = jschOperate.doAction(new IJschAction() {
                    @Override
                    public String action(Session session) throws IOException, JSchException, SftpException {
                        return "success";
                    }
                }, remoteServerDomain.getServerIp(), remoteServerDomain.getServerUser(), remoteServerDomain.getPassword(), String.valueOf(remoteServerDomain.getPort()));
                break;
            }
            default:
            {
                result = false;
                break;
            }
        }
        return result;
    }
}
