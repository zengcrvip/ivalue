//package com.axon.market.core.service.icommon;
//
//import com.axon.market.common.domain.icommon.AttrValueDomain;
//import com.axon.market.dao.mapper.icommon.IAttrMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Duzm on 2017/8/3.
// */
//@Component("attrService")
//public class AttrService
//{
//
//    @Autowired
//    @Qualifier("attrDao")
//    private IAttrMapper attrDao;
//
//    public List<AttrValueDomain> queryAttrValueByAttrCode(String attrCode)
//    {
//        return attrDao.queryAttrValueByAttrCode(attrCode);
//    }
//
//    public List<Map<String, Object>> queryAttrValueByAttrId(Integer attrId)
//    {
//        return attrDao.queryAttrValueByAttrId(attrId);
//    }
//
//    public String queryAttrValueDisplayValue(String attrCode, String innerValue) {
//        return attrDao.queryAttrValueDisplayValue(attrCode, innerValue);
//    }
//}
