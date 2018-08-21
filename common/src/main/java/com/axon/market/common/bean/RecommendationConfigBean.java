package com.axon.market.common.bean;

import com.axon.market.common.util.SpringUtil;

/**
 * Created by chenyu on 2017/3/16.
 */
public class RecommendationConfigBean
{
    private String serverHost;

    private String serverUser;

    private String serverPassword;

    private String serverPort;

    private String serverConnectType;

    private String iCloudServerHost;

    private String iCloudServerUser;

    private String iCloudServerPassword;

    private String iCloudServerPort;

    private String iCloudServerConnectType;

    private String fengleiServerHost;

    private String fengleiServerUser;

    private String fengleiServerPassword;

    private String fengleiServerPort;

    private String fengleiServerConnectType;

    private String voiceplusServerHost;

    private String voiceplusServerUser;

    private String voiceplusServerPassword;

    private String voiceplusServerPort;

    private String voiceplusServerConnectType;

    public static RecommendationConfigBean getInstance()
    {
        return (RecommendationConfigBean) SpringUtil.getSingletonBean("recommendationConfigBean");
    }

    public String getServerHost()
    {
        return serverHost;
    }

    public void setServerHost(String serverHost)
    {
        this.serverHost = serverHost;
    }

    public String getServerUser()
    {
        return serverUser;
    }

    public void setServerUser(String serverUser)
    {
        this.serverUser = serverUser;
    }

    public String getServerPassword()
    {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword)
    {
        this.serverPassword = serverPassword;
    }

    public String getServerPort()
    {
        return serverPort;
    }

    public void setServerPort(String serverPort)
    {
        this.serverPort = serverPort;
    }

    public String getServerConnectType()
    {
        return serverConnectType;
    }

    public void setServerConnectType(String serverConnectType)
    {
        this.serverConnectType = serverConnectType;
    }

    public String getiCloudServerHost()
    {
        return iCloudServerHost;
    }

    public void setiCloudServerHost(String iCloudServerHost)
    {
        this.iCloudServerHost = iCloudServerHost;
    }

    public String getiCloudServerUser()
    {
        return iCloudServerUser;
    }

    public void setiCloudServerUser(String iCloudServerUser)
    {
        this.iCloudServerUser = iCloudServerUser;
    }

    public String getiCloudServerPassword()
    {
        return iCloudServerPassword;
    }

    public void setiCloudServerPassword(String iCloudServerPassword)
    {
        this.iCloudServerPassword = iCloudServerPassword;
    }

    public String getiCloudServerPort()
    {
        return iCloudServerPort;
    }

    public void setiCloudServerPort(String iCloudServerPort)
    {
        this.iCloudServerPort = iCloudServerPort;
    }

    public String getiCloudServerConnectType()
    {
        return iCloudServerConnectType;
    }

    public void setiCloudServerConnectType(String iCloudServerConnectType)
    {
        this.iCloudServerConnectType = iCloudServerConnectType;
    }

    public String getFengleiServerHost() {
        return fengleiServerHost;
    }

    public void setFengleiServerHost(String fengleiServerHost) {
        this.fengleiServerHost = fengleiServerHost;
    }

    public String getFengleiServerUser() {
        return fengleiServerUser;
    }

    public void setFengleiServerUser(String fengleiServerUser) {
        this.fengleiServerUser = fengleiServerUser;
    }

    public String getFengleiServerPassword() {
        return fengleiServerPassword;
    }

    public void setFengleiServerPassword(String fengleiServerPassword) {
        this.fengleiServerPassword = fengleiServerPassword;
    }

    public String getFengleiServerPort() {
        return fengleiServerPort;
    }

    public void setFengleiServerPort(String fengleiServerPort) {
        this.fengleiServerPort = fengleiServerPort;
    }

    public String getFengleiServerConnectType() {
        return fengleiServerConnectType;
    }

    public void setFengleiServerConnectType(String fengleiServerConnectType) {
        this.fengleiServerConnectType = fengleiServerConnectType;
    }

    public String getVoiceplusServerHost() {
        return voiceplusServerHost;
    }

    public void setVoiceplusServerHost(String voiceplusServerHost) {
        this.voiceplusServerHost = voiceplusServerHost;
    }

    public String getVoiceplusServerUser() {
        return voiceplusServerUser;
    }

    public void setVoiceplusServerUser(String voiceplusServerUser) {
        this.voiceplusServerUser = voiceplusServerUser;
    }

    public String getVoiceplusServerPassword() {
        return voiceplusServerPassword;
    }

    public void setVoiceplusServerPassword(String voiceplusServerPassword) {
        this.voiceplusServerPassword = voiceplusServerPassword;
    }

    public String getVoiceplusServerPort() {
        return voiceplusServerPort;
    }

    public void setVoiceplusServerPort(String voiceplusServerPort) {
        this.voiceplusServerPort = voiceplusServerPort;
    }

    public String getVoiceplusServerConnectType() {
        return voiceplusServerConnectType;
    }

    public void setVoiceplusServerConnectType(String voiceplusServerConnectType) {
        this.voiceplusServerConnectType = voiceplusServerConnectType;
    }
}
