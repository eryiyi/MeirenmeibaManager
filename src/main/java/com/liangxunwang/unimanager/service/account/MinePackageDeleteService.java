package com.liangxunwang.unimanager.service.account;

import com.liangxunwang.unimanager.dao.MinePackageDao;
import com.liangxunwang.unimanager.model.MinePackage;
import com.liangxunwang.unimanager.service.*;
import com.liangxunwang.unimanager.util.UUIDFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by zhl on 2015/3/3.
 * 我的钱包管理
 */
@Service("minePackageDeleteService")
public class MinePackageDeleteService implements  UpdateService {
    @Autowired
    @Qualifier("minePackageDao")
    private MinePackageDao minePackageDao;


    //后台金额扣除
    @Override
    public Object update(Object object) {
        MinePackage adObj = (MinePackage) object;
        minePackageDao.updateDel(adObj);
        return null;
    }
}
