package com.liangxunwang.unimanager.dao;

import com.liangxunwang.unimanager.model.AdObj;
import com.liangxunwang.unimanager.model.KuaidiCompany;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2015/1/29.
 */
@Repository("kuaidiCompanyDao")
public interface KuaidiCompanyDao {

    /**
     * 查询
     */
    List<KuaidiCompany> lists(Map<String, Object> map);

    //保存
    void save(KuaidiCompany kuaidiCompany);

    //删除
    void delete(String kuaidi_company_id);

    /**
     * 根据ID查找
     * @param kuaidi_company_code
     * @return
     */
    public KuaidiCompany findByCode(String kuaidi_company_code);

    public KuaidiCompany findById(String kuaidi_company_id);

    /**
     * 更新
     * @param adObj
     */
    public void update(KuaidiCompany adObj);
}
