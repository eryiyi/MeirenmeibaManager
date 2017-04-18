package com.liangxunwang.unimanager.service.account;

import com.liangxunwang.unimanager.dao.KuaidiCompanyDao;
import com.liangxunwang.unimanager.model.KuaidiCompany;
import com.liangxunwang.unimanager.query.KuaidiCompanyQuery;
import com.liangxunwang.unimanager.service.*;
import com.liangxunwang.unimanager.util.StringUtil;
import com.liangxunwang.unimanager.util.UUIDFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("kuaidiCompanyService")
public class KuaidiCompanyService implements ListService,SaveService,DeleteService,ExecuteService, UpdateService {
    @Autowired
    @Qualifier("kuaidiCompanyDao")
    private KuaidiCompanyDao kuaidiCompanyDao;

    @Override
    public Object list(Object object) throws ServiceException {
        KuaidiCompanyQuery query = (KuaidiCompanyQuery) object;
        Map<String, Object> map = new HashMap<String, Object>();
        if(!StringUtil.isNullOrEmpty(query.getIs_use())){
            map.put("is_use", query.getIs_use());
        }
        if(!StringUtil.isNullOrEmpty(query.getKuaidi_company_code())){
            map.put("kuaidi_company_code", query.getKuaidi_company_code());
        }
        List<KuaidiCompany> lists = kuaidiCompanyDao.lists(map);
        return lists;
    }

    @Override
    public Object save(Object object) throws ServiceException {
        KuaidiCompany kuaidiCompany = (KuaidiCompany) object;
        kuaidiCompany.setKuaidi_company_id(UUIDFactory.random());
        kuaidiCompanyDao.save(kuaidiCompany);
        return null;
    }

    @Override
    public Object delete(Object object) throws ServiceException {
        String kuaidi_company_id = (String) object;
        kuaidiCompanyDao.delete(kuaidi_company_id);
        return null;
    }

    @Override
    public Object execute(Object object) throws ServiceException {
        return kuaidiCompanyDao.findByCode((String) object);
    }

    @Override
    public Object update(Object object) {
        KuaidiCompany kuaidiCompany = (KuaidiCompany) object;
        kuaidiCompanyDao.update(kuaidiCompany);
        return null;
    }
}
