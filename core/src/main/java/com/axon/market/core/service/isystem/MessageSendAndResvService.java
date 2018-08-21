package com.axon.market.core.service.isystem;

import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/5/15.
 */
@Service("messageSendAndResvService")
public class MessageSendAndResvService
{
    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    Logger LOG = Logger.getLogger(GreenPlumOperateService.class.getName());

    AxonEncryptUtil axon = AxonEncryptUtil.getInstance();

    /**
     * 查询短信上下文内容
     * @param phone
     * @param limit
     * @param offset
     * @return
     */
    public List<Map<String,Object>> queryMessageSendAndRecv(String phone,Integer limit,Integer offset){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        if(StringUtils.isEmpty(phone)){
            return list;
        }else{
            String encryptedPhone  = axon.encrypt(phone);//手机加密
            String sql = " SELECT row_number() over(order by a.time) as rows,a.* FROM\n" +
                        " (SELECT timest, phone, spnum, message, \"time\", result, id,0 as type\n" +
                        " FROM dware.push_sms_send \n" +
                        " UNION\n" +
                        " SELECT timest,phone,spnum,message,\"time\",111 as result,id,1 as type FROM dware.push_sms_recv) as a\n" +
                        " WHERE a.phone = "+encryptedPhone+"\n" +
                        " order by a.time "+
                        " limit "+limit+" offset "+offset;
            try
            {
                list = greenPlumOperateService.query(sql);
            }
            catch (Exception e)
            {
                LOG.error("查询短信上下文异常",e);
                return list;
            }
            return list;
        }
    }

    /**
     * 查询短信上下行分页数
     * @param phone
     * @return
     */
    public int queryMessageCount(String phone){
        if(StringUtils.isEmpty(phone)){
            return 0;
        }else{
            String encryptedPhone  = axon.encrypt(phone);
            String sql = " SELECT count(a.*) FROM\n" +
                        " (SELECT timest, phone, spnum, message, \"time\", result, id,0 as type\n" +
                        " FROM dware.push_sms_send \n" +
                        " UNION\n" +
                        " SELECT timest,phone,spnum,message,\"time\",111 as result,id,1 as type FROM dware.push_sms_recv) as a\n" +
                        " WHERE a.phone = "+encryptedPhone+"\n";
            try
            {
               return greenPlumOperateService.queryRecordCount(sql);
            }
            catch (Exception e)
            {
                LOG.error("查询短信上下行分页异常",e);
                return 0;
            }
        }
    }
}
