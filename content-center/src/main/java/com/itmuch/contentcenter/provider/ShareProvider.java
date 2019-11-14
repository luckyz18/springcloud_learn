package com.itmuch.contentcenter.provider;

import org.springframework.web.bind.annotation.RequestParam;

public class ShareProvider {
    public String selecByParam(@RequestParam Integer pageNo,
                               @RequestParam Integer pageSize,
                               @RequestParam String title) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" select * from share where 1=1  ");
        if (title != null && !"".equals(title)) {
            sql.append(" and title like '%" + title + "%' ");
        }
        sql.append(" and audit_status = 'PASS' ");
        int start = (pageNo - 1) * pageSize;
        sql.append(" limit "+start+","+pageSize);
        System.out.println("|| " + sql.toString());
        return sql.toString();
    }
}
