package com.axon.market.core.service.ishop;

import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.dao.mapper.ishop.IMarketRecordMapper;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Administrator on 2017/3/16.
 */
@Component("marketRecordService")
public class MarketRecordService
{
    private static final Logger LOG = Logger.getLogger(MarketRecordService.class.getName());

    @Autowired
    @Qualifier("marketRecordDao")
    private IMarketRecordMapper iMarketRecordDap;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    /**
     * @param paras
     * @return
     */
    public Table queryMarketRecordByPage(Map<String, Object> paras)
    {
        try
        {
            Integer offset = Integer.valueOf((String) paras.get("start"));
            Integer limit = Integer.valueOf((String) paras.get("length"));
            String phone = String.valueOf(paras.get("phone"));
            if (!StringUtils.isEmpty(phone))
            {
                phone = axonEncrypt.encrypt("86" + phone);
            }
            String baseCode = String.valueOf(paras.get("baseCode"));
            String startTime = String.valueOf(paras.get("startTime"));
            String businessCodes = String.valueOf(paras.get("businessCodes"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(TimeUtil.formatDateToYMDDevide(startTime));
            calendar.add(Calendar.DATE, 1);
            String endTime = TimeUtil.formatDateToYMDDevide(calendar.getTime());

            Map<String, Object> parasMap = new HashMap<String, Object>();
            parasMap.put("offset", offset);
            parasMap.put("limit", limit);
            parasMap.put("phone", phone);
            parasMap.put("baseCode", baseCode);
            parasMap.put("startTime", startTime);
            parasMap.put("endTime", endTime);
            parasMap.put("areaCode", paras.get("areaCode"));
            parasMap.put("businessCodes", businessCodes);

            List<Map<String, String>> list = iMarketRecordDap.queryMarketRecordByPage(parasMap);

            for (Map<String, String> item : list)
            {
                String strPhone = getPhone(businessCodes, String.valueOf(item.get("phone")));
                item.put("phone", strPhone);
            }

            Integer count = iMarketRecordDap.queryMarketRecordByCount(parasMap);
            return new Table(list, count);
        }
        catch (Exception ex)
        {
            LOG.error("queryMarketRecordByPage error:" + ex.getMessage());
            return new Table();
        }
    }

    /**
     * @param phone
     * @param businessCodes
     * @return
     */
    private String getPhone(String businessCodes, String phone)
    {
        String strPhone = axonEncrypt.decrypt(phone);
        strPhone = strPhone.substring(2, strPhone.length());
        if (null != businessCodes && !"".equals(businessCodes))
        {
            //营业厅关管理员
            strPhone = strPhone.replaceAll("(?<=\\d{3})\\d(?=\\d{3})", "*");
        }
        return strPhone;
    }
}
