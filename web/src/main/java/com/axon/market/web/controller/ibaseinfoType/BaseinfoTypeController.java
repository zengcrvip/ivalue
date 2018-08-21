package com.axon.market.web.controller.ibaseinfoType;

import com.axon.market.common.domain.ibaseInfoType.baseinfoTypeDomain;
import com.axon.market.core.service.ibaseinfoType.BaseinfoTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 炒店类型表控制器
 * 创建人:邵炜
 * 创建时间:2017年2月20日17:28:52
 * Created by gloomysw on 2017/02/20.
 */
@Controller("baseinfoTypeController")
public class BaseinfoTypeController
{
    @Autowired
    @Qualifier("baseinfoTypeService")
    private BaseinfoTypeService baseinfoTypeService;

    /**
     * 查询炒店类型表所有数据
     * 创建人:邵炜
     * 创建时间:2017年2月20日17:30:56
     * @return 炒店类型表实体类集合
     */
    @RequestMapping(value = "selectBaseinfoTypeAll.view", method = RequestMethod.POST)
    @ResponseBody
    private List<baseinfoTypeDomain> selectBaseinfoTypeAll(){
       return baseinfoTypeService.selectBaseinfoTypeAll();
    }
}
