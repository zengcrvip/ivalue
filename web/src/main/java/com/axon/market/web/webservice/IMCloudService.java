package com.axon.market.web.webservice;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.domain.webservice.*;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.service.iwebservice.CloudService;
import com.axon.market.dao.mapper.iwebservice.ICloudMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.io.Serializable;

/**
 * 精细化平台智能云任务透传接口
 * Created by zengcr on 2017/5/25.
 */
@WebService(name = "CloudSendACTInfoSV", portName = "CloudSendACTInfoSVImplPort", serviceName = "CloudSendACTInfoSVImplService", targetNamespace = "http://interfaces.webservice.cloud.asiainfo.com/")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE, style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class IMCloudService
{
    private CloudService cloudService;
    private CloudService getCloudService()
    {
       if(cloudService == null)
       {
           cloudService = (CloudService)SpringUtil.getSingletonBean("cloudService");
       }
        return cloudService;
    }

    private static final Logger LOG = Logger.getLogger(IMCloudService.class.getName());

    @WebMethod
    @WebResult(name = "putMarketingInfoToControlCentreResponse", partName = "putMarketingInfoToControlCentreResponse")
    public OperationOutResult putMarketingInfoToControlCentre(@WebParam(name = "putMarketingInfoToControlCentre", partName = "putMarketingInfoToControlCentre") IMCloudDomain imCloudDomain) throws JsonProcessingException
    {
        LOG.info("cloud putMarketingInfoToControlCentre begin");
        LOG.info("webservice params(OperationIn)【" + JsonUtil.objectToString(imCloudDomain) + "】");
        OperationOutResult result = new OperationOutResult();
        OperationOut out = new OperationOut();
        OperationIn operationIn = imCloudDomain.getOperationIn();
        try
        {
            getCloudService().insertData(operationIn);
        }
        catch (Exception e)
        {
            LOG.error("精细化智能云接口任务新增失败",e);
            out.setResultCode(2);
            out.setResultDesc("智能云接口任务新增失败");
        }

        result.setOperationOut(out);
        return result;
    }

    @WebMethod
    @WebResult(name = "changeMarketingInfoStatusResponse", partName = "changeMarketingInfoStatusResponse")
    public SessionStateChageOutResult changeMarketingInfoStatus(@WebParam(name = "changeMarketingInfoStatus", partName = "changeMarketingInfoStatus") IMCloudStatusDomain imCloudStatusDomain) throws JsonProcessingException
    {
        LOG.info("cloud changeMarketingInfoStatus begin");
        LOG.info("webservice params(status)【" + JsonUtil.objectToString(imCloudStatusDomain) + "】");
        SessionStateChageOutResult result = new SessionStateChageOutResult();
        SessionStateChageOut out = new SessionStateChageOut();
        ServiceResult changeResult = null;
        try
        {
            changeResult = getCloudService().changeMarketingInfoStatus(imCloudStatusDomain.getArg0());
        }
        catch (Exception e)
        {
            LOG.error("changeMarketingInfoStatus入库处理失败",e);
            out.setResultCode(2);
            out.setResultDesc("智能云接口任务终止失败");
        }

        if (changeResult.getRetValue() != 0)
        {
            out.setResultCode(2);
            out.setResultDesc(changeResult.getDesc());
        }
        result.setSessionStateChageOut(out);
        return result;
    }
}
