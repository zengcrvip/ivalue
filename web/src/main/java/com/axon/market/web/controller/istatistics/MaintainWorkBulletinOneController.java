package com.axon.market.web.controller.istatistics;

import com.axon.market.common.bean.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import com.axon.market.core.service.istatistics.MaintainWorkBulletinOneService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2017/7/20.
 */
@Controller("MaintainWorkBulletinOneController")
public class MaintainWorkBulletinOneController
{
    @Autowired
    @Qualifier("MaintainWorkBulletinOneService")
    private MaintainWorkBulletinOneService maintainWorkBulletinOneService;

    @RequestMapping(value = "queryMaintainWorkBulletin.view",method = RequestMethod.POST)
    @ResponseBody
    public Table queryMaintainWorkBulletin(@RequestParam Map<String, Object> paras, HttpSession session){
        String yearMonth = String.valueOf(paras.get("yearMonth"));
        List<Map<String,Object>> maintainWorkBulletinOneList = maintainWorkBulletinOneService.queryMaintainWorkBulletin(yearMonth);
        Integer itemCounts = maintainWorkBulletinOneList.size();
        return new Table(maintainWorkBulletinOneList, itemCounts);
    }

    @RequestMapping(value = "downloadMaintainWorkBulletinOne.view",method = RequestMethod.POST)
    public void downloadMaintainWorkBulletinOne(HttpServletRequest request, HttpServletResponse response, String yearMonth)throws Exception
    {
        maintainWorkBulletinOneService.downloadMaintainWorkBulletinOne(request,response,yearMonth);
    }


}


