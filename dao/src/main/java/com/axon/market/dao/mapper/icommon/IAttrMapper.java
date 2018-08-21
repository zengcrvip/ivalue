//package com.axon.market.dao.mapper.icommon;
//
//import com.axon.market.common.domain.icommon.AttrValueDomain;
//import com.axon.market.dao.base.IMyBatisMapper;
//import org.apache.ibatis.annotations.Param;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Duzm on 2017/8/3.
// */
//@Component("attrDao")
//public interface IAttrMapper extends IMyBatisMapper
//{
//
//    List<AttrValueDomain> queryAttrValueByAttrCode(@Param(value = "attrCode") String attrCode);
//
//    List<Map<String, Object>> queryAttrValueByAttrId(@Param(value = "attrId") Integer attrId);
//
//    List<Map<String, Object>> queryAttrValueByAttrIdAndInnerValue(@Param(value = "attrId") Integer attrId);
//
//    String queryAttrValueDisplayValue(@Param(value = "attrCode")String attrCode, @Param(value = "innerValue")String innerValue);
//}
