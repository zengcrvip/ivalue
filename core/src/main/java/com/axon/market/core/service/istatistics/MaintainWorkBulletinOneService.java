package com.axon.market.core.service.istatistics;

import com.axon.market.common.util.MarketTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.axon.market.dao.mapper.istatistics.IMaintainWorkBulletinOneMapper;

/**
 * Created by Chris on 2017/7/20.
 */
@Service("MaintainWorkBulletinOneService")
public class MaintainWorkBulletinOneService
{
    @Autowired(required = true)
    @Qualifier("MaintainWorkBulletinOneDao")
    private IMaintainWorkBulletinOneMapper MaintainWorkBulletinOneDao;

    /**
     * 查询
     *
     * @return
     */
    public List<Map<String,Object>> queryMaintainWorkBulletin(String yearMonth){
        if(org.springframework.util.StringUtils.isEmpty(yearMonth) || "nullnull".equals(yearMonth)){
            return new ArrayList<Map<String, Object>>();
        }
        else
        {
                return MaintainWorkBulletinOneDao.queryMaintainWorkBulletinOne(yearMonth);
        }
    }

    public void downloadMaintainWorkBulletinOne(HttpServletRequest request, HttpServletResponse response, String yearMonth) throws IOException
    {

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream; charset=utf-8");
        String fileName = "维系工作通报一(" + yearMonth.substring(0, 4) + "年" + yearMonth.substring(4) + "月)_" + MarketTimeUtils.formatDateToYMD(new Date());
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".csv\"");

        final OutputStream os = response.getOutputStream();
        List<Map<String, Object>> maintainWorkList = queryMaintainWorkBulletin(yearMonth);

        try
        {
            os.write(("地市,收入保有率(高价值用户),用户保有率(高价值用户),收入保有率(合约用户),用户保有率(合约用户),2G网络用户占比,"
                    + "4G网络用户占比,2G终端占比,4G终端占比,当月ARPU,ARPU较拍照提升值\r\n").getBytes("GBK"));
            for (Map<String, Object> maintainWorkData : maintainWorkList)
            {
                List<String> result = new ArrayList<String>();
                result.add(maintainWorkData.get("cityName")+"");
                result.add(maintainWorkData.get("feeRate")+"");
                result.add(maintainWorkData.get("uvRate")+"");
                result.add(maintainWorkData.get("feeTwoRate")+"");
                result.add(maintainWorkData.get("uvTwoRate")+"");
                result.add(maintainWorkData.get("twoGWang")+"");
                result.add(maintainWorkData.get("fourGWang")+"");
                result.add(maintainWorkData.get("twoZhongd")+"");
                result.add(maintainWorkData.get("fourZhongd")+"");
                result.add(maintainWorkData.get("arpu")+"");
                result.add(maintainWorkData.get("cha")+"");
                os.write((StringUtils.join(result, ",") + "\r\n").getBytes("GBK"));
            }
        } catch (Exception e)
        {

        } finally
        {
            if (os != null)
            {
                os.close();
            }
        }
    }

}
