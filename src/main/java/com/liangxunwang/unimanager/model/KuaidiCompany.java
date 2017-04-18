package com.liangxunwang.unimanager.model;

/**
 * Created by zhl on 2017/2/23.
 */
public class KuaidiCompany {
    private String kuaidi_company_id;//ID
    private String kuaidi_company_name;//快递公司名称
    private String kuaidi_company_code;//快递公司唯一标识
    private String is_use;//是否使用 0是 1否 ；默认0
    private String top_number;//排序

    public String getTop_number() {
        return top_number;
    }

    public void setTop_number(String top_number) {
        this.top_number = top_number;
    }

    public String getIs_use() {
        return is_use;
    }

    public void setIs_use(String is_use) {
        this.is_use = is_use;
    }

    public String getKuaidi_company_id() {
        return kuaidi_company_id;
    }

    public void setKuaidi_company_id(String kuaidi_company_id) {
        this.kuaidi_company_id = kuaidi_company_id;
    }

    public String getKuaidi_company_name() {
        return kuaidi_company_name;
    }

    public void setKuaidi_company_name(String kuaidi_company_name) {
        this.kuaidi_company_name = kuaidi_company_name;
    }

    public String getKuaidi_company_code() {
        return kuaidi_company_code;
    }

    public void setKuaidi_company_code(String kuaidi_company_code) {
        this.kuaidi_company_code = kuaidi_company_code;
    }
}
