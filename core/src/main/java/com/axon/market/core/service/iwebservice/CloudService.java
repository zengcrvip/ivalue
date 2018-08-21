package com.axon.market.core.service.iwebservice;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.domain.ischeduling.MarketingTasksDomain;
import com.axon.market.common.domain.webservice.AimList;
import com.axon.market.common.domain.webservice.AimSub;
import com.axon.market.common.domain.webservice.OperationIn;
import com.axon.market.common.domain.webservice.OperationStatus;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.ischeduling.MarketingTasksService;
import com.axon.market.dao.mapper.iwebservice.ICloudMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/5/27.
 */
@Service("cloudService")
public class CloudService
{
    @Autowired
    @Qualifier("cloudDao")
    private ICloudMapper cloudDao;

    @Autowired
    @Qualifier("marketingTasksService")
    private MarketingTasksService marketingTasksService;

    private static final Logger LOG = Logger.getLogger(CloudService.class.getName());

    private static Map<String,String> icloudAccessNum = new HashMap<String,String>()
    {
        {
            put("1","10018888");
            put("2","10018877");
            put("3","100188889");
            put("4","100108866");
        }
    };

    private static Map<String, String> areaNamesMap = new HashMap<String, String>()
    {
        {
            put("0000", "江苏省");
            put("025", "南京市");
            put("0510", "无锡市");
            put("0511", "镇江市");
            put("0512", "苏州市");
            put("0513", "南通市");
            put("0514", "扬州市");
            put("0515", "盐城市");
            put("0516", "徐州市");
            put("0517", "淮安市");
            put("0518", "连云港市");
            put("0519", "常州市");
            put("0523", "泰州市");
            put("0527", "宿迁市");
        }
    };

    @Transactional
    public void insertData(OperationIn operationIn) throws Exception
    {
        //1、入库原始文件
        cloudDao.insertActiveData(operationIn);
        cloudDao.insertActiveAimData(operationIn.getAimList().getAimSub(),operationIn.getSaleId());
        cloudDao.insertActiveProduct(operationIn.getAimList().getAimSub(),operationIn.getSaleId());
        //2、生成待办任务
        AimList aimList = operationIn.getAimList();

        if(aimList != null && aimList.getAimSub().size() > 0)
        {
            for(int i = 0;i<aimList.getAimSub().size();i++)
            {
                MarketingTasksDomain tasksDomain = getMarketingTasksDomain(operationIn,i);
                marketingTasksService.insertMarketingTask(tasksDomain,null);
            }
        }
    }

    /**
     *
     * @param operationStatus
     * @return
     */
    @Transactional
    public ServiceResult changeMarketingInfoStatus(OperationStatus operationStatus) throws Exception
    {
        ServiceResult result = new ServiceResult();
        //修改原数据
        if (cloudDao.changeMarketingInfoStatus(operationStatus) < 1)
        {
            result.setRetValue(-1);
            result.setDesc("波次ID："+operationStatus.getSaleBoidId() +"未入库");
        }else{
            //终止生成的任务
            List<String> taskIds =  cloudDao.queryTaskIdByBoidId(operationStatus);
            if(taskIds != null && taskIds.size() > 0)
            {
                for(String id:taskIds)
                {
                    marketingTasksService.stopMarketingTask(Integer.parseInt(id));
                }
            }
        }
        return result;
    }

    /**
     * 生成本地任务对象
     * @param operationIn
     * @param i
     * @return
     */
    public MarketingTasksDomain getMarketingTasksDomain(OperationIn operationIn,int i)
    {
        MarketingTasksDomain taskDomain = new MarketingTasksDomain();
        AimSub aimSub = operationIn.getAimList().getAimSub().get(i);
        taskDomain.setName(operationIn.getSaleName() + "【" + aimSub.getAimSubName() + "】");
        //Interface_type 活动类型 1实时 2非实时
        String marketType = aimSub.getInterfaceType().equals("2") ? "jxhsms" : "jxhscene";
        taskDomain.setMarketType(marketType);
        String saleTypeInfo = aimSub.getChannel().getSaleTypeInfo();
        taskDomain.setBusinessType(Integer.parseInt(saleTypeInfo));
        taskDomain.setAccessNumber(icloudAccessNum.get(saleTypeInfo));
        taskDomain.setMarketContent(aimSub.getChannel().getChannelSaleInfo());
        String startDate = operationIn.getStartDate();
        String endDate  = operationIn.getEndDate();
        taskDomain.setStartTime(startDate.substring(0, 4) + "-" + startDate.substring(4, 6) + "-" + startDate.substring(6, 8));
        taskDomain.setStopTime(endDate.substring(0, 4) + "-" + endDate.substring(4, 6) + "-" + endDate.substring(6, 8));
        taskDomain.setBeginTime(startDate.substring(8, 10) + ":" + startDate.substring(10, 12));
        taskDomain.setEndTime(endDate.substring(8, 10) + ":" + endDate.substring(10, 12));
        taskDomain.setScheduleType("single");
        taskDomain.setSendInterval(1);
        taskDomain.setRepeatStrategy(3);
        taskDomain.setMarketSegmentIds(aimSub.getAimSubId());
        taskDomain.setMarketSegmentNames(aimSub.getAimSubName());
        taskDomain.setCreateUser(103842);
        taskDomain.setCreateTime(TimeUtil.formatDate(Calendar.getInstance().getTime()));
        taskDomain.setStatus(2);
        String saleEparchyCode = operationIn.getSaleEparchyCode();
        taskDomain.setAreaCodes("0000".equals(saleEparchyCode) ? "99999" : saleEparchyCode);
        taskDomain.setAreaNames(areaNamesMap.get(saleEparchyCode));
        taskDomain.setRemarks(operationIn.getSaleDesc());
        taskDomain.setSaleId(operationIn.getSaleId());
        taskDomain.setSaleBoidId(operationIn.getSaleBoidId());
        taskDomain.setAimSubId(aimSub.getAimSubId());
        taskDomain.setIsBoidSale(0);
        return taskDomain;
    }
}
