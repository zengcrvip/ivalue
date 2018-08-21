package com.axon.market.core.service.isystem;

import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangtt on 2017/5/16.
 */
@Service("clientOrderRecordService")
public class ClientOrderRecordService
{
    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    Logger LOG = Logger.getLogger(GreenPlumOperateService.class.getName());

    AxonEncryptUtil axon = AxonEncryptUtil.getInstance();



    /**
     * 查询用户订购记录
     * @param phone
     * @param dateTime
     * @param limit
     * @param offset
     * @return
     */
    public List<Map<String,Object>> queryClientOrderRecord(String phone,String dateTime,Integer limit,Integer offset){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(dateTime)){
            return list;
        }else{
            String encryptedPhone  = axon.encrypt(phone);
            try
            {
                Map<String,String> map = getDateRange(dateTime);
                String firstDayOfMonth = map.get("firstDayOfMonth");
                String lastDayOfMonth = map.get("lastDayOfMonth");
                String sql = "SELECT row_number() over(order by b.order_time) as rows,p.name as name,b.product_code as productCode,b.order_channel as orderChannel,\n" +
                            " b.order_time as orderTime,b.order_ip as orderIp,b.result_code as resultCode\n" +
                            "  FROM dware.push_business_product_order as b\n" +
                            "  left join uaide.push_market_product p on b.product_code = p.code\n" +
                            "  where b.phone = "+encryptedPhone+" and b.order_time >= "+firstDayOfMonth + " and b.order_time <= " + lastDayOfMonth +"\n" +
                            "  order by b.order_time "+
                            "  limit "+limit+" offset "+offset;
                list = greenPlumOperateService.query(sql);
            }
            catch (Exception e)
            {
                LOG.error("查询短信上下文异常 ：",e);
                return list;
            }
            return list;
        }
    }

    /**
     * 查询用户订购记录分页数
     * @param phone
     * @param dateTime
     * @return
     */
    public int queryClientOrderCount(String phone,String dateTime){
        if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(dateTime)){
            return 0;
        }else{
            try
            {
            Map<String,String> map = getDateRange(dateTime);
            String firstDayOfMonth = map.get("firstDayOfMonth");
            String lastDayOfMonth = map.get("lastDayOfMonth");
            String encryptedPhone  = axon.encrypt(phone);
            String sql = "SELECT count(*)\n" +
                    "  FROM dware.push_business_product_order as b\n" +
                    "  left join uaide.push_market_product p on b.product_code = p.code\n" +
                    "  where b.phone = "+encryptedPhone+" and b.order_time >= " + firstDayOfMonth + " and b.order_time <= " + lastDayOfMonth;
               return greenPlumOperateService.queryRecordCount(sql);
            }
            catch (Exception e)
            {
                LOG.error("查询用户订购记录分页异常 ：",e);
                return 0;
            }
        }
    }

    /**
     * 根据传入的年月，获取此月份月初和月末的时间戳
     * @param dateTime
     * @return
     * @throws ParseException
     */
    private Map<String,String> getDateRange(String dateTime) throws ParseException{
        Map<String,String> map = new HashMap<String,String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = format.parse(dateTime);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        map.put("firstDayOfMonth", sqlFormat.format(c.getTime()));
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 0);
        map.put("lastDayOfMonth",sqlFormat.format(c.getTime()));
        return map;
    }

}
