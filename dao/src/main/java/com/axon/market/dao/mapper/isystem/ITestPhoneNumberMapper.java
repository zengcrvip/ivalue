package com.axon.market.dao.mapper.isystem;

import com.axon.market.common.domain.isystem.TestPhoneNumberDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by chenyu on 2016/11/8.
 */
@Component("testPhoneNumberDao")
public interface ITestPhoneNumberMapper extends IMyBatisMapper
{
    /**
     * @return
     */
    Integer queryTestPhoneNumberCounts();

    /**
     * @param offset
     * @param limit
     * @return
     */
    List<TestPhoneNumberDomain> queryTestPhoneNumbersByPage(@Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    /**
     * 根据号码查询测试号domain
     * @param phoneNumber
     * @return
     */
    TestPhoneNumberDomain queryTestPhoneNumberDomainByNumber(@Param(value = "phoneNumber") String phoneNumber);

    /**
     * @param testNumberDomain
     * @return
     */
    int createTestPhoneNumber(@Param(value = "info") TestPhoneNumberDomain testNumberDomain);

    /**
     * @param testNumberDomain
     * @return
     */
    int updateTestPhoneNumber(@Param(value = "info") TestPhoneNumberDomain testNumberDomain);

    /**
     * @param id
     * @param userId
     * @param time
     * @return
     */
    int deleteTestPhoneNumber(@Param(value = "id") Integer id, @Param(value = "userId") Integer userId, @Param(value = "time") String time);

}
