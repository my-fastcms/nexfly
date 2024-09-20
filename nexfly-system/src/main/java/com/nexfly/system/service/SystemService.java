package com.nexfly.system.service;

import com.nexfly.system.model.Account;

import java.io.Serializable;
import java.util.List;

public interface SystemService {

    Account getAccountByEmail(String email);

    Account findById(Long userId);

    List<Org> getUserOrgList(Long userId);

    Org getUserOrg(Long userId);

    class Org implements Serializable {
        Long orgId;
        String orgName;

        public Long getOrgId() {
            return orgId;
        }

        public void setOrgId(Long orgId) {
            this.orgId = orgId;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

    }

}
