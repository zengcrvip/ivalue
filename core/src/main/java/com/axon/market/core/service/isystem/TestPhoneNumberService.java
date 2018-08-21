package com.axon.market.core.service.isystem;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.TestPhoneNumberDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.dao.mapper.isystem.ITestPhoneNumberMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */
@Component("testPhoneNumberService")
public class TestPhoneNumberService
{
    private static final Logger LOG = Logger.getLogger(TestPhoneNumberService.class.getName());

    @Autowired
    @Qualifier("testPhoneNumberDao")
    private ITestPhoneNumberMapper marketTestNumberDao;

    /**
     * @param start
     * @param length
     * @return
     */
    public Table queryTestPhoneNumbersByPage(Integer start, Integer length)
    {
        try
        {
            Integer count = marketTestNumberDao.queryTestPhoneNumberCounts();
            List<TestPhoneNumberDomain> list = marketTestNumberDao.queryTestPhoneNumbersByPage(start, length);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query Test Phone Numbers Error. ", e);
            return new Table();
        }
    }

    /**
     * 新增/修改
     *
     * @param testPhoneNumberDomain
     * @return
     */
    public Operation addOrEditTestPhoneNumber(TestPhoneNumberDomain testPhoneNumberDomain, UserDomain userDomain)
    {
        TestPhoneNumberDomain phoneNumberDomain =  marketTestNumberDao.queryTestPhoneNumberDomainByNumber(testPhoneNumberDomain.getTestPhoneNumber());
        if (null != phoneNumberDomain && !phoneNumberDomain.getId().equals(testPhoneNumberDomain.getId()))
        {
            return new Operation(false, "测试号已存在");
        }

        try
        {
            Boolean result;
            String message;
            //新增
            if (testPhoneNumberDomain.getId() == null || testPhoneNumberDomain.getId() == 0)
            {
                testPhoneNumberDomain.setCreateUser(userDomain.getId());
                testPhoneNumberDomain.setCreateTime(TimeUtil.formatDate(new Date()));
                result = marketTestNumberDao.createTestPhoneNumber(testPhoneNumberDomain) == 1;
                message = result ? "新增测试号成功" : "新增测试号失败";
            }
            else
            {
                testPhoneNumberDomain.setUpdateUser(userDomain.getId());
                testPhoneNumberDomain.setUpdateTime(TimeUtil.formatDate(new Date()));
                result = marketTestNumberDao.updateTestPhoneNumber(testPhoneNumberDomain) == 1;
                message = result ? "更新测试号成功" : "更新测试号失败";
            }
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Add Or Edit Test Phone Number Error. ", e);
            return new Operation();
        }
    }

    /**
     * @param id
     * @param userId
     * @return
     */
    public Operation deleteTestPhoneNumber(Integer id, Integer userId)
    {
        try
        {
            Boolean result = marketTestNumberDao.deleteTestPhoneNumber(id, userId, TimeUtil.formatDate(new Date())) == 1;
            String message = result ? "删除测试号成功" : "删除测试号失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Delete Test Phone Number Error. ", e);
            return new Operation();
        }
    }
}
