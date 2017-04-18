package com.liangxunwang.unimanager.service.account;

import com.liangxunwang.unimanager.dao.OrderDao;
import com.liangxunwang.unimanager.mvc.vo.OrdersVO;
import com.liangxunwang.unimanager.query.OrdersQuery;
import com.liangxunwang.unimanager.service.ListService;
import com.liangxunwang.unimanager.service.ServiceException;
import com.liangxunwang.unimanager.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2015/8/19.
 */
@Service("orderScanService")
public class OrderScanService implements ListService {

    @Autowired
    @Qualifier("orderDao")
    private OrderDao orderDao;

    @Override
    public Object list(Object object) throws ServiceException {

            OrdersQuery query = (OrdersQuery) object;
            String empId = query.getEmpId();
            int index = (query.getIndex() - 1) * query.getSize();
            int size =  query.getSize();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("index", index);
            map.put("size", size);
            if (!StringUtil.isNullOrEmpty(empId)) {
                map.put("empId", empId);
            }
            if (!StringUtil.isNullOrEmpty(query.getEmpName())){
                map.put("empName", query.getEmpName());
            }
            if (!StringUtil.isNullOrEmpty(query.getEmpPhone())){
                map.put("empPhone", query.getEmpPhone());
            }
            if (!StringUtil.isNullOrEmpty(query.getOrderStatus())){
                map.put("orderStatus", query.getOrderStatus());
            }
            if (!StringUtil.isNullOrEmpty(query.getPayStatus())){
                map.put("payStatus", query.getPayStatus());
            }
            if (!StringUtil.isNullOrEmpty(query.getDistribStatus())){
                map.put("distribStatus", query.getDistribStatus());
            }
            if (!StringUtil.isNullOrEmpty(query.getIs_dxk_order())){
                map.put("is_dxk_order", query.getIs_dxk_order());
            }

            if (!StringUtil.isNullOrEmpty(query.getIs_zhiying())){
                map.put("is_zhiying", query.getIs_zhiying());
            }
            if (!StringUtil.isNullOrEmpty(query.getTrade_type())){
                map.put("trade_type", query.getTrade_type());
            }
            if (!StringUtil.isNullOrEmpty(query.getSeller_name())){
                map.put("seller_name", query.getSeller_name());
            }

            List<OrdersVO> list = orderDao.listOrdersScan(map);
            long count = orderDao.countScan(map);

            return new Object[]{list, count};

    }

}
