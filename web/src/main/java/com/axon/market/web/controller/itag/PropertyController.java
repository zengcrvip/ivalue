package com.axon.market.web.controller.itag;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.icommon.CategoryDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.domain.itag.PropertyDomain;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.greenplum.GreenPlumMetaDataService;
import com.axon.market.core.service.itag.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyang on 2016/1/25.
 */
@Controller("propertyController")
public class PropertyController
{
    @Autowired()
    @Qualifier("propertyService")
    private PropertyService propertyService;

    @Autowired()
    @Qualifier("greenPlumMetaDataService")
    private GreenPlumMetaDataService greenPlumMetaDataService;

    @RequestMapping(value = "queryColumnInfoAndDimension.view")
    @ResponseBody
    public Table queryColumnInfoAndDimension(@RequestParam Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        String schema = String.valueOf(param.get("schema"));
        String tableName = String.valueOf(param.get("tableName"));
        return greenPlumMetaDataService.getColumnInfoAndDimension(schema, tableName);
    }

    @RequestMapping(value = "queryPropertiesByPage.view")
    @ResponseBody
    public Table queryPropertiesByPage(@RequestParam Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer start = Integer.parseInt(param.get("start"));
        Integer length = Integer.parseInt(param.get("length"));
        String nameSearch = param.get("nameSearch");
        String tableNameSearch = param.get("tableNameSearch");
        String columnNameSearch = param.get("columnNameSearch");
        String userNameSearch = param.get("userNameSearch");
        return propertyService.queryPropertiesByPage(start, length, nameSearch, tableNameSearch, columnNameSearch, userNameSearch);
    }

    @RequestMapping(value = "createProperties.view")
    @ResponseBody
    public Operation createProperties(@RequestBody List<PropertyDomain> propertyDomains, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        if (propertyDomains != null)
        {
            String dateTime = TimeUtil.formatDate(new Date());
            for (PropertyDomain metaPropertyDomain : propertyDomains)
            {
                metaPropertyDomain.setCreateUser(userDomain.getId());
                metaPropertyDomain.setCreateTime(dateTime);
            }
        }
        return propertyService.createProperties(propertyDomains);
    }

    @RequestMapping(value = "updateProperty.view")
    @ResponseBody
    public Operation updateProperty(@RequestBody PropertyDomain propertyDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        propertyDomain.setLastUpdateUser(userDomain.getId());
        propertyDomain.setLastUpdateTime(TimeUtil.formatDate(new Date()));
        return propertyService.updateProperty(propertyDomain);
    }

    @RequestMapping(value = "deleteProperty.view")
    @ResponseBody
    public Operation deleteProperty(@RequestBody PropertyDomain propertyDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return propertyService.deleteProperty(propertyDomain.getId(), userDomain.getId());
    }

    @RequestMapping(value = "queryAllPropertiesUnderCategory.view")
    @ResponseBody
    public List<CategoryDomain> queryAllPropertiesUnderCategory(HttpSession session)
    {
        return propertyService.queryAllPropertiesUnderCategory();
    }

    @RequestMapping(value = "queryAllPropertiesAndImportModelUnderCategory.view")
    @ResponseBody
    public List<CategoryDomain> queryAllPropertiesAndImportModelUnderCategory(HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return propertyService.queryAllPropertiesAndImportModelUnderCategory(userDomain);
    }

    @RequestMapping(value = "queryDataRefreshTime.view")
    @ResponseBody
    public Map<String,Object> queryDataRefreshTime(@RequestBody Map<String,Object> params, HttpSession session)
    {
        Map<String,Object> result = new HashMap<String,Object>();
        String propertyId = String.valueOf(params.get("propertyId"));
        String refreshTime =  propertyService.queryDataRefreshTime(Integer.valueOf(propertyId));
        result.put("refreshTime",refreshTime);
        return result;
    }
}
