package com.axon.market.common.jsch;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.IOException;

/**
 * Created by chenyu on 2016/8/17.
 */
public interface IJschAction
{
    String action(Session session) throws IOException, JSchException, SftpException;
}
