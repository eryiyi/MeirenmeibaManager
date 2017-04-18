package com.liangxunwang.unimanager.mvc.app;

import com.liangxunwang.unimanager.alipay.AlipayNotify;
import com.liangxunwang.unimanager.model.*;
import com.liangxunwang.unimanager.mvc.vo.MemberVO;
import com.liangxunwang.unimanager.service.*;
import com.liangxunwang.unimanager.util.ControllerConstants;
import com.liangxunwang.unimanager.util.DateUtil;
import com.liangxunwang.unimanager.util.StringUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/17.
 */
@Controller
public class AppPayZfbController extends ControllerConstants {

    /**
     * 支付宝支付回调
     * @return
     */
    @RequestMapping(value = "/payZfbNotifyAction",   produces = "text/html;charset=UTF-8", method={RequestMethod.POST})
    @ResponseBody
    public String payZfbNotifyAction(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {

        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();

        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]:valueStr + values[i] + ",";
            }
//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
//商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

//支付宝交易号
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");


//交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

//异步通知ID
        String notify_id=request.getParameter("notify_id");

//sign
        String sign=request.getParameter("sign");

        if(notify_id!=""&&notify_id!=null){
            if(AlipayNotify.verifyResponse(notify_id).equals("true"))//判断成功之后使用getResponse方法判断是否是支付宝发来的异步通知。
            {
                if(AlipayNotify.getSignVeryfy(params, sign))//使用支付宝公钥验签
                {
                    if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
                        synchronized (this){
                            updateOrder(out_trade_no);
                        }
                    }

                    return "success" ;
                }
                else//验证签名失败
                {
                    return "sign fail" ;
                }
            }
            else//验证是否来自支付宝的通知失败
            {
                return "response fail" ;
            }
        }
        else{
            return "no notify message" ;
        }
    }


    /**
     * 微信支付回调定向卡的
     * @return
     */
    @RequestMapping(value = "/payZfbNotifyActionDxk",   produces = "text/html;charset=UTF-8", method={RequestMethod.POST})
    @ResponseBody
    public String payZfbNotifyActionDxk(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();

        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]:valueStr + values[i] + ",";
            }
//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
//商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

//支付宝交易号
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");


//交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

//异步通知ID
        String notify_id=request.getParameter("notify_id");

//sign
        String sign=request.getParameter("sign");

        if(notify_id!=""&&notify_id!=null){
            if(AlipayNotify.verifyResponse(notify_id).equals("true"))//判断成功之后使用getResponse方法判断是否是支付宝发来的异步通知。
            {
                if(AlipayNotify.getSignVeryfy(params, sign))//使用支付宝公钥验签
                {
                    if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
                        synchronized (this){
                            updateDxk(out_trade_no);
                        }
                    }

                    return "success" ;
                }
                else//验证签名失败
                {
                    return "sign fail" ;
                }
            }
            else//验证是否来自支付宝的通知失败
            {
                return "response fail" ;
            }
        }
        else{
            return "no notify message" ;
        }

    }


    /**
     * 微信支付回调零钱
     * @return
     */
    @RequestMapping(value = "/payZfbNotifyActionLq",   produces = "text/html;charset=UTF-8", method={RequestMethod.POST})
    @ResponseBody
    public String payZfbNotifyActionLq(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();

        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]:valueStr + values[i] + ",";
            }
//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
//商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

//支付宝交易号
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");


//交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

//异步通知ID
        String notify_id=request.getParameter("notify_id");

//sign
        String sign=request.getParameter("sign");

        if(notify_id!=""&&notify_id!=null){
            if(AlipayNotify.verifyResponse(notify_id).equals("true"))//判断成功之后使用getResponse方法判断是否是支付宝发来的异步通知。
            {
                if(AlipayNotify.getSignVeryfy(params, sign))//使用支付宝公钥验签
                {
                    if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
                        synchronized (this){
                            UpdateLq(out_trade_no);
                        }
                    }

                    return "success" ;
                }
                else//验证签名失败
                {
                    return "sign fail" ;
                }
            }
            else//验证是否来自支付宝的通知失败
            {
                return "response fail" ;
            }
        }
        else{
            return "no notify message" ;
        }

    }



    @Autowired
    @Qualifier("appOrderDxkConsumptionService")
    private UpdateService appOrderDxkConsumptionService;

    @Autowired
    @Qualifier("appOrderFindByTradeNoService")
    private ExecuteService appOrderFindByTradeNoService;

    @Autowired
    @Qualifier("dxkChongzhiReturnCountService")
    private ExecuteService dxkChongzhiReturnCountService;

    @Autowired
    @Qualifier("cardEmpService")
    private ExecuteService cardEmpServiceExe;

    @Autowired
    @Qualifier("memberFindByIdService")
    private FindService memberFindByIdService;


    @Autowired
    @Qualifier("cardEmpService")
    private SaveService cardEmpServiceSave;

    @Autowired
    @Qualifier("cardEmpService")
    private UpdateService cardEmpServiceUpdate;

    @Autowired
    @Qualifier("memberUpdateDxkByIdService")
    private UpdateService memberUpdateDxkByIdService;

    @Autowired
    @Qualifier("lxConsumptionService")
    private SaveService lxConsumptionServiceSave;

    int updateDxk(String out_trade_no) throws Exception {
        //根据out_trade_no 查询订单详情
        List<Order> listOrders = (List<Order>) appOrderFindByTradeNoService.execute(out_trade_no);
        if(listOrders != null && listOrders.size() > 0){
            //说明有订单
        }else{
            return 1;
        }
        Order order = listOrders.get(0);//定向卡订单
        if(order != null){
            if("5".equals(order.getStatus())){
                //说明已经处理过了
                return 1;
            }
            //更新订单状态
            appOrderDxkConsumptionService.update(out_trade_no);

//            listOrders.get(0).setStatus("5");
            LxConsumption lxConsumption = new LxConsumption();
            lxConsumption.setLx_consumption_type("3");
            lxConsumption.setOrder_no(order.getOrder_no());
            lxConsumption.setEmp_id(order.getEmp_id());
            lxConsumption.setLx_consumption_count(String.valueOf(order.getPayable_amount()));
            //充值
            if(StringUtil.isNullOrEmpty(lxConsumption.getLx_consumption_count()) || "0".equals(lxConsumption.getLx_consumption_count())){
                return 1;//金额为空或0
            }
            if(StringUtil.isNullOrEmpty(lxConsumption.getEmp_id())){
                return 2;//会员不存在，请检查会员！
            }

            //增加充值记录
            lxConsumption.setLx_consumption_cont("app定向卡充值，金额" + lxConsumption.getLx_consumption_count()+"--支付宝");
            lxConsumptionServiceSave.save(lxConsumption);//增加记录

            //查看该会员是第几次定向卡充值
            CardEmp cardEmp = (CardEmp) cardEmpServiceExe.execute(lxConsumption.getEmp_id());
            if(cardEmp != null){
                //更新定向卡信息
                cardEmp.setLx_card_emp_year(String.valueOf(Integer.parseInt(cardEmp.getLx_card_emp_year()) + 1));//上一年的基础上加1年
                String endDate = DateUtil.getDate(cardEmp.getLx_card_emp_end_time(), "yyyy-MM-dd HH:mm:ss.SSS");//根据毫秒值获得日期
                String year = endDate.substring(0,4);
                int yearInt = Integer.parseInt(year)+1;//获得加一年的年份
                String lx_card_emp_end_time = endDate.replace(year, String.valueOf(yearInt));
                cardEmp.setLx_card_emp_end_time(DateUtil.getMs(lx_card_emp_end_time, "yyyy-MM-dd HH:mm:ss.SSS") + "");
                cardEmpServiceUpdate.update(cardEmp);
            }else{
                //插入定向卡信息
                cardEmp = new CardEmp();
                cardEmp.setEmp_id(lxConsumption.getEmp_id());
                String endDate = DateUtil.getCurrentDateTime();
                String year = endDate.substring(0,4);
                int yearInt = Integer.parseInt(year)+1;//获得加一年的年份
                String lx_card_emp_end_time = endDate.replace(year, String.valueOf(yearInt));
                cardEmp.setLx_card_emp_end_time(DateUtil.getMs(lx_card_emp_end_time, "yyyy-MM-dd HH:mm:ss") + "");
                cardEmpServiceSave.save(cardEmp);
            }
            //更新会员为定向卡会员
            Member member = new Member();
            member.setEmpId(lxConsumption.getEmp_id());
            member.setIs_card_emp("1");//定向卡会员 0否 1是  能购买零元商品
            memberUpdateDxkByIdService.update(member);

            //充值定向卡 给上级定向卡会员返利--返积分
            //1.查询上级
//            MemberVO memberVOUp = (MemberVO) memberFindByIdService.findById(lxConsumption.getEmp_id());
            MemberVO memberVO1 = (MemberVO) memberFindByIdService.findById(member.getEmpId());

            String[] arr = {lxConsumption.getEmp_id(), lxConsumption.getLx_consumption_count(), memberVO1.getLx_dxk_level_id(), memberVO1.getEmpName(), memberVO1.getEmp_up()};//会员ID 充值金额 定向卡等级 会员name, 会员的上级ID
            dxkChongzhiReturnCountService.execute(arr);
        }

        return 200;

    }

    @Autowired
    @Qualifier("appOrderMakeService")
    private UpdateService appOrderUpdateService;
    //普通订单支付成功的处理
    void updateOrder(String out_trade_no){
        Order order = new Order();
        order.setOut_trade_no(out_trade_no);
        appOrderUpdateService.update(order);
    }


    @Autowired
    @Qualifier("appLingqianChongzhiService")
    private ListService appLingqianChongzhiServiceList;

    @Autowired
    @Qualifier("orderUpdateService")
    private UpdateService orderUpdateServiceUpdate;

    @Autowired
    @Qualifier("minePackageService")
    private UpdateService minePackageServiceUpdate;
    @Autowired
    @Qualifier("jifenObjService")
    private ExecuteService jifenObjServiceExe;

    @Autowired
    @Qualifier("countService")
    private UpdateService countServiceUpdate;

    @Autowired
    @Qualifier("countRecordService")
    private SaveService countRecordServiceSave;


    int UpdateLq(String out_trade_no) throws Exception {
            List<Order> orders = (List<Order>) appLingqianChongzhiServiceList.list(out_trade_no);
            if(orders != null && orders.size() > 0){
//            for(Order order:orders){
                Order order = orders.get(0);
                if(order != null){
                    if("5".equals(order.getStatus())){
                        return 1;
                    }
                    //更新这个订单
                    order.setStatus("5");
                    orderUpdateServiceUpdate.update(order);
                    //充值
                    MinePackage minePackage = new MinePackage();
                    minePackage.setEmp_id(order.getEmp_id());//会员ID
                    minePackage.setPackage_money(String.valueOf(order.getPayable_amount()));//充值金额
                    minePackageServiceUpdate.update(minePackage);//零钱更新
                    //增加充值记录
                    LxConsumption lxConsumption = new LxConsumption();
                    lxConsumption.setEmp_id(order.getEmp_id());
                    lxConsumption.setOrder_no(order.getOrder_no());
                    lxConsumption.setLx_consumption_type("2");
                    lxConsumption.setLx_consumption_count(String.valueOf(order.getPayable_amount()));
                    lxConsumption.setLx_consumption_cont("app零钱充值，金额" + order.getPayable_amount()+"--支付宝");
                    lxConsumptionServiceSave.save(lxConsumption);//消费记录

                    //充值成功 ，给上级增加积分
                    //1.查询上级
                    MemberVO memberVO = (MemberVO) memberFindByIdService.findById(lxConsumption.getEmp_id());
                    if(memberVO != null && !StringUtil.isNullOrEmpty(memberVO.getEmp_up())){
                        //说明存在上级
                        //2.更新上级积分
                        List<JifenObj> listJifens = (List<JifenObj>) jifenObjServiceExe.execute("");//查询推广下线后台充值金额 给上级增加积分的百分率
                        if(listJifens != null && listJifens.size()>0){
                            JifenObj jifenObj = listJifens.get(0);//积分规则
                            if(jifenObj != null){
                                String lx_jifen_one = jifenObj.getLx_jifen_one();//推广下线充值金额的X%
                                if(!StringUtil.isNullOrEmpty(lx_jifen_one)){
                                    Double jifenCount = Double.parseDouble(lxConsumption.getLx_consumption_count())*(Double.parseDouble(lx_jifen_one)*0.01);//增加的积分
                                    //更新上级积分
                                    String[] arr = {memberVO.getEmp_up(), String.valueOf(jifenCount)};
                                    countServiceUpdate.update(arr);//更新上级积分
                                    //添加积分变动记录
                                    CountRecord countRecord = new CountRecord();
                                    countRecord.setEmp_id(memberVO.getEmp_up());
                                    countRecord.setLx_count_record_count("+" + String.valueOf(jifenCount));
                                    countRecord.setLx_count_record_cont(memberVO.getEmpName()+"("+memberVO.getEmpMobile()+")app充值零钱" + lxConsumption.getLx_consumption_count()+"元");
                                    countRecordServiceSave.save(countRecord);
                                }
                            }
                        }
                    }
                }
//            }
            }
       return 200;
    }
}
