package com.liangxunwang.unimanager.service.account;

import com.liangxunwang.unimanager.dao.AppDxkOrderMakeDao;
import com.liangxunwang.unimanager.dao.AppOrderMakeDao;
import com.liangxunwang.unimanager.dao.CardEmpDao;
import com.liangxunwang.unimanager.dao.ManagerInfoDao;
import com.liangxunwang.unimanager.model.CardEmp;
import com.liangxunwang.unimanager.model.DxkOrder;
import com.liangxunwang.unimanager.model.ManagerInfo;
import com.liangxunwang.unimanager.model.Order;
import com.liangxunwang.unimanager.service.SaveService;
import com.liangxunwang.unimanager.service.ServiceException;
import com.liangxunwang.unimanager.util.Constants;
import com.liangxunwang.unimanager.util.DateUtil;
import com.liangxunwang.unimanager.util.StringUtil;
import com.liangxunwang.unimanager.util.UUIDFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 生成定向卡订单
 */
@Service("orderDxkService")
public class OrderDxkService implements  SaveService {

    @Autowired
    @Qualifier("appOrderMakeDao")
    private AppOrderMakeDao appOrderMakeDao;


    @Autowired
    @Qualifier("appDxkOrderMakeDao")
    private AppDxkOrderMakeDao appDxkOrderMakeDao;


    @Autowired
    @Qualifier("managerInfoDao")
    private ManagerInfoDao managerInfoDao;

    @Autowired
    @Qualifier("cardEmpDao")
    private CardEmpDao cardEmpDao;

    @Override
    public Object save(Object object) throws ServiceException {
        Order order = (Order) object;
        ManagerInfo  managerInfo = null;//卖家信息

        if(!StringUtil.isNullOrEmpty(order.getSeller_emp_id())){
            //根据卖家ID查询卖家的店铺类型
            managerInfo = managerInfoDao.findById(order.getSeller_emp_id());
        }
        if(managerInfo != null){
            String typeid = managerInfo.getLx_class_id();
            if(!StringUtil.isNullOrEmpty(typeid)){
                Map<String,Object> mapType = new HashMap<String, Object>();
                mapType.put("emp_id", order.getEmp_id());
                mapType.put("typeid", typeid);
                mapType.put("startTime", DateUtil.getStartDay());
                mapType.put("endTime", DateUtil.getEndDay());
                DxkOrder dxkOrder = appDxkOrderMakeDao.findIsExist(mapType);
                if(dxkOrder != null){
                    //说明存在了 已经消费了该类别下的商品了
                    throw new ServiceException("has_exist_class");
                }
            }
        }
        //判断该会员该店铺是否今天刷过单了 ，一天只能刷一次
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("startTime", DateUtil.getStartDay());
        map.put("endTime", DateUtil.getEndDay());

        map.put("emp_id", order.getEmp_id());
        map.put("seller_emp_id", order.getSeller_emp_id());
        Order order1 = appOrderMakeDao.findIsExist(map);
        if(order1 != null){
            //说明存在了
            throw new ServiceException("has_exist");
        }
        //判断是否超出店铺扫码总次数限制了
        String allcount = managerInfo.getAllcount();//店铺扫码总次数
        String empcount = managerInfo.getEmpcount();//店铺扫码总次数--会员一年的
        Map<String,Object> mapDxkCount = new HashMap<String, Object>();
        mapDxkCount.put("seller_emp_id", order.getSeller_emp_id());
        mapDxkCount.put("is_dxk_order", "1");
        long dxkCount1 = appOrderMakeDao.selectDxkOrderCountById(mapDxkCount);
        if(!StringUtil.isNullOrEmpty(allcount) && !StringUtil.isNullOrEmpty(empcount)){
            if(Integer.parseInt(allcount) > dxkCount1){
                //说明还能扫描
                //判断个人（会员）在店铺消费次数限制，（充值后一年时间）
                CardEmp cardEmp = cardEmpDao.findById(order.getEmp_id());
                if(cardEmp != null){
                    String lx_card_emp_start_time = cardEmp.getLx_card_emp_start_time();
                    mapDxkCount.put("start", lx_card_emp_start_time);
                    mapDxkCount.put("emp_id", order.getEmp_id());
                    long dxkCount2 = appOrderMakeDao.selectDxkOrderCountById(mapDxkCount);//会员在一年之中在该店铺消费的总次数
                    if(Integer.parseInt(empcount) <= dxkCount2){
                        throw new ServiceException("has_dxk_count_out_emp");
                    }
                }
            }else {
                //说明已经超出限制了
                throw new ServiceException("has_dxk_count_out");
            }
        }
        order.setOrder_no(UUIDFactory.random());
        order.setCreate_time(System.currentTimeMillis()+"");
        order.setPay_time(System.currentTimeMillis()+"");
        order.setStatus("2");
        order.setPay_status("1");
        order.setIs_dxk_order("1");
        appOrderMakeDao.saveDxkOrder(order);

        //保存定向卡订单 empid:消费者ID  typeid:店铺类别ID  createtime:订单生成时间
        if(managerInfo != null){
            String typeid = managerInfo.getLx_class_id();
            if(!StringUtil.isNullOrEmpty(typeid)){
                DxkOrder dxkOrder  = new DxkOrder();
                dxkOrder.setCreatetime(System.currentTimeMillis()+"");
                dxkOrder.setEmp_id(order.getEmp_id());
                dxkOrder.setDxk_order_id(UUIDFactory.random());
                dxkOrder.setTypeid(typeid);
                appDxkOrderMakeDao.saveDxkOrder(dxkOrder);
            }
        }

        return 200;
    }
}
