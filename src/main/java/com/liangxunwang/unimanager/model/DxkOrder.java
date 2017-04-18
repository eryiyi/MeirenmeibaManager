package com.liangxunwang.unimanager.model;

/**
 * Created by zhl on 2017/4/8.
 */
public class DxkOrder {
    private String dxk_order_id;
    private String emp_id;
    private String createtime;
    private String typeid;

    public String getDxk_order_id() {
        return dxk_order_id;
    }

    public void setDxk_order_id(String dxk_order_id) {
        this.dxk_order_id = dxk_order_id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }
}
