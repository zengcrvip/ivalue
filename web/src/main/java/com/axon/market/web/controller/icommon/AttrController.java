//package com.axon.market.web.controller.icommon;
//
//import com.axon.market.common.domain.icommon.AttrValueDomain;
//import com.axon.market.core.service.icommon.AttrService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Duzm on 2017/8/3.
// */
//@Controller
//public class AttrController
//{
//
//    @Autowired
//    @Qualifier("attrService")
//    private AttrService attrService;
//
//    /**
//     * @return
//     */
//    @RequestMapping(value = "queryAttrValueByAttrCode.view")
//    @ResponseBody
//    public List<AttrValueDomain> queryAttrValueByAttrCode(@RequestBody Map<String, Object> paras)
//    {
//        String attrCode = String.valueOf(paras.get("attrCode"));
//        return attrService.queryAttrValueByAttrCode(attrCode);
//    }
//
//    /**
//     * @param paras
//     * @return
//     */
//    @RequestMapping(value = "queryAttrValueByAttrId.view")
//    @ResponseBody
//    public List<Map<String, Object>> queryAttrValueByAttrId(@RequestBody Map<String, Object> paras)
//    {
//        Integer attrId = Integer.parseInt(String.valueOf(paras.get("attrId")));
//        return attrService.queryAttrValueByAttrId(attrId);
//    }
//
//}
