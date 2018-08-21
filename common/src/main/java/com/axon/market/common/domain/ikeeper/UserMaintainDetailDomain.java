package com.axon.market.common.domain.ikeeper;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 末梢用户与客户的关联关系 userId:[phoneList]
 * Created by zengcr on 2017/8/21.
 */
public class UserMaintainDetailDomain {
    private String userId;
    private Set<String> phoneSet;
    private Set<Map<String,String>>  userSet;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<String> getPhoneSet() {
        return phoneSet;
    }

    public void setPhoneSet(Set<String> phoneSet) {
        this.phoneSet = phoneSet;
    }

    public Set<Map<String, String>> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<Map<String, String>> userSet) {
        this.userSet = userSet;
    }
}
