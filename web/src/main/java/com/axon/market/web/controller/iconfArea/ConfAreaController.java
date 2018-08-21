package com.axon.market.web.controller.iconfArea;

import com.axon.market.common.domain.iconfArea.confAreaDomain;
import com.axon.market.core.service.iConfArea.ConfAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;

/**
 * 城市列表控制器
 * 创建人:邵炜
 * 创建时间:2017年2月20日16:36:50
 * Created by gloomysw on 2017/02/20.
 */
@Controller("confAreaController")
public class ConfAreaController
{
    @Autowired
    @Qualifier("confAreaService")
    private ConfAreaService confAreaService;

    /**
     * 查询城市列表
     * 创建人:邵炜
     * 创建时间:2017年2月20日16:39:16
     * @return 城市列表集合
     */
    @RequestMapping(value = "selectConfAreaList.view", method = RequestMethod.POST)
    @ResponseBody
    private List<confAreaDomain> selectConfAreaList(){
        return confAreaService.selectConfAreaList();
    }
}
