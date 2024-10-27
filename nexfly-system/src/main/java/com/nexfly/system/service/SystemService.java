package com.nexfly.system.service;

import com.nexfly.system.model.Account;

import java.io.Serializable;
import java.util.List;

public interface SystemService {

    Account getAccountByEmail(String email);

    Account findById(Long userId);

    List<Org> getUserOrgList(Long userId);

    Org getUserOrg(Long userId);

    Long getOrgId(Long userId);

    Boolean update(AccountUpdateRequest accountUpdateRequest);

    record AccountUpdateRequest(String avatar, String color_schema, String language, String nickname, String timezone) {

    }

    class Org implements Serializable {
        Long orgId;
        String orgName;

        String parserIds;

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

        public String getParserIds() {
            return parserIds == null ? "naive:General,qa:Q&A,resume:Resume,manual:Manual,table:Table,paper:Paper,book:Book,laws:Laws,presentation:Presentation,picture:Picture,one:One,audio:Audio,knowledge_graph:Knowledge Graph,email:Email" : parserIds;
        }

        public void setParserIds(String parserIds) {
            this.parserIds = parserIds;
        }
    }

}
