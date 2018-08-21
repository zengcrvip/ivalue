package com.axon.market.common.domain.iconsumption;

import java.util.Date;

/**
 * Created by zhuwen on 2017/7/21.
 */
public class DixiaoCodeDomain {
    //编码
    private String code;
    //名称
    private String name;
    //创建时间
    private Date createtime;
    //是否分配
    private String isallocate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getIsallocate() {
        return isallocate;
    }

    public void setIsallocate(String isallocate) {
        this.isallocate = isallocate;
    }
}
