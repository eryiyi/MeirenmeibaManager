package com.liangxunwang.unimanager.mvc.app;

import com.liangxunwang.unimanager.model.Order;
import com.liangxunwang.unimanager.mvc.vo.MemberVO;
import com.liangxunwang.unimanager.service.FindService;
import com.liangxunwang.unimanager.service.SaveService;
import com.liangxunwang.unimanager.service.ServiceException;
import com.liangxunwang.unimanager.util.ControllerConstants;
import com.liangxunwang.unimanager.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2015/8/17.
 * 生成定向卡订单
 */
@Controller
public class AppOrderDxkController extends ControllerConstants {

    @Autowired
    @Qualifier("orderDxkService")
    private SaveService orderDxkServiceSave;

    @Autowired
    @Qualifier("memberInfoService")
    private FindService memberInfoServiceFind;

    /**
     *  生成定向卡订单
     * @return
     */
    @RequestMapping(value = "/appSaveDxkOrder", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String appSaveDxkOrder(Order order){
        if(StringUtil.isNullOrEmpty(order.getEmp_id())){
            return toJSONString(ERROR_2);//会员ID为空
        }
        if(StringUtil.isNullOrEmpty(order.getSeller_emp_id())){
            return toJSONString(ERROR_3);//卖家会员ID为空
        }
        //根据emp_id查询该会员是不是定向卡会员
        MemberVO memberVO = (MemberVO) memberInfoServiceFind.findById(order.getEmp_id());
        if(memberVO != null){
            if(memberVO.getIs_card_emp().equals("1")){
                try {
                    orderDxkServiceSave.save(order);
                    return toJSONString(SUCCESS);
                }catch (ServiceException e){
                    if (e.getMessage().equals("has_exist")){
                        return toJSONString(ERROR_4);
                    }else if (e.getMessage().equals("has_exist_class")){
                        return toJSONString(ERROR_5);
                    }
                    return toJSONString(ERROR_1);
                }
            }else {
                return toJSONString(ERROR_1);
            }

        }else{
            return toJSONString(ERROR_1);
        }
    }

}
