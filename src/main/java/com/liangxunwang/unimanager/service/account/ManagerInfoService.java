package com.liangxunwang.unimanager.service.account;

import com.liangxunwang.unimanager.dao.*;
import com.liangxunwang.unimanager.model.ManagerInfo;
import com.liangxunwang.unimanager.mvc.vo.ManagerInfoVo;
import com.liangxunwang.unimanager.query.ManagerInfoQuery;
import com.liangxunwang.unimanager.service.*;
import com.liangxunwang.unimanager.util.Constants;
import com.liangxunwang.unimanager.util.DateUtil;
import com.liangxunwang.unimanager.util.StringUtil;
import com.liangxunwang.unimanager.util.UUIDFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2015/8/30.
 */
@Service("managerInfoService")
public class ManagerInfoService implements SaveService, FindService, UpdateService, ListService,ExecuteService ,DeleteService{

    @Autowired
    @Qualifier("managerInfoDao")
    private ManagerInfoDao managerInfoDao;
    @Override
    public Object findById(Object object) throws ServiceException {
        if (object instanceof String){
            String id = (String) object;
            ManagerInfo managerInfo = managerInfoDao.findById(id);
            if(managerInfo != null){
                if(!StringUtil.isNullOrEmpty(managerInfo.getDateline())){
                    managerInfo.setDateline(DateUtil.getDate(managerInfo.getDateline(), "yyyy-MM-dd HH:mm"));
                }
            }
            return managerInfo;
        }
        return null;
    }

    @Override
    public Object save(Object object) throws ServiceException {
        if (object instanceof ManagerInfo){
            ManagerInfo info = (ManagerInfo) object;
            if(info != null && !StringUtil.isNullOrEmpty(info.getEmp_id())){
                ManagerInfo managerInfo =  managerInfoDao.findById(info.getEmp_id());
                if(managerInfo != null){
                    //说明已经存在该用户的申请了
                    throw new ServiceException("hax_exist_info");
                }
            }else {
                throw new ServiceException("is_error");
            }
            info.setId(UUIDFactory.random());
            info.setDateline(System.currentTimeMillis() +"");
            managerInfoDao.save(info);
        }
        return null;
    }

    @Override
    public Object update(Object object) {
        if (object instanceof ManagerInfo){
            ManagerInfo info = (ManagerInfo) object;
            managerInfoDao.update(info);
        }
        return null;
    }

    @Override
    public Object list(Object object) throws ServiceException {
        ManagerInfoQuery query = (ManagerInfoQuery) object;
        int index = (query.getIndex() - 1) * query.getSize();
        int size = query.getSize();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("index", index);
        map.put("size", size);

        if (!StringUtil.isNullOrEmpty(query.getKeyWords())) {
            map.put("cont", query.getKeyWords());
        }

        if (!StringUtil.isNullOrEmpty(query.getCompany_name())) {
            map.put("company_name", query.getCompany_name());
        }
        if (!StringUtil.isNullOrEmpty(query.getPhone())) {
            map.put("phone", query.getPhone());
        }

        if(!StringUtil.isNullOrEmpty(query.getIs_card())){
            map.put("is_card", query.getIs_card());
        }
        List<ManagerInfoVo> list = managerInfoDao.lists(map);
        long count = managerInfoDao.count(map);
        return new Object[] {list, count};
    }

    @Autowired
    @Qualifier("goodsCommentDao")
    private GoodsCommentDao goodsCommentDao;

    @Autowired
    @Qualifier("dianpuCommentDao")
    private DianpuCommentDao dianpuCommentDao;

    @Override
    public Object execute(Object object) throws Exception {
        ManagerInfoVo managerInfoVo = managerInfoDao.getEmpMsg((String) object);
        if(managerInfoVo != null){

            if(!StringUtil.isNullOrEmpty(managerInfoVo.getEmp_cover())){
                if (managerInfoVo.getEmp_cover().startsWith("upload")) {
                    managerInfoVo.setEmp_cover(Constants.URL + managerInfoVo.getEmp_cover());
                }else {
                    managerInfoVo.setEmp_cover(Constants.QINIU_URL + managerInfoVo.getEmp_cover());
                }
            }

            if(!StringUtil.isNullOrEmpty(managerInfoVo.getCompany_pic())){
                if (managerInfoVo.getCompany_pic().startsWith("upload")) {
                    managerInfoVo.setCompany_pic(Constants.URL + managerInfoVo.getCompany_pic());
                }else {
                    managerInfoVo.setCompany_pic(Constants.QINIU_URL + managerInfoVo.getCompany_pic());
                }
            }

            Map<String,Object> map1 = new HashMap<String, Object>();
            map1.put("goods_emp_id", managerInfoVo.getEmp_id());
            map1.put("emp_id_seller", managerInfoVo.getEmp_id());
//            DecimalFormat df=new DecimalFormat(".##");
//            DecimalFormat df=new DecimalFormat(".");
            //全部星级
            Long countAllGoods = goodsCommentDao.countStarNumber(map1);
            Long countAllDianpu = dianpuCommentDao.countStarNumber(map1);
            //评论数量
            Long countNumGoods = goodsCommentDao.count(map1);
            Long countNumDianpu = dianpuCommentDao.count(map1);
            if(countNumGoods == 0 && countNumDianpu==0){
                managerInfoVo.setCompany_star("0");
            }else {
                Long countAll = (countAllGoods==null?0:countAllGoods) + (countAllDianpu==null?0:countAllDianpu);
                Long countNum = (countNumGoods==null?0:countNumGoods) + (countNumDianpu==null?0:countNumDianpu);
//                String aDouble =df.format(Double.valueOf(countAll==null?0:countAll)/Double.valueOf(countNum));//这样为保持2位
                String aDouble =String.valueOf((countAll==null?0:countAll)/(countNum==null?0:countNum));//这样为保持2位
                managerInfoVo.setCompany_star(aDouble);
            }

        }
        return managerInfoVo;
    }

    @Autowired
    @Qualifier("adminDao")
    private AdminDao adminDao;

    @Autowired
    @Qualifier("memberDao")
    private MemberDao memberDao;

    @Override
    public Object delete(Object object) throws ServiceException {
        String info_id = (String) object;
        ManagerInfo managerInfo = managerInfoDao.findByInfoId(info_id);
        if(managerInfo != null){
            //删除lx_manager和manager_info表数据
            if(!StringUtil.isNullOrEmpty(managerInfo.getEmp_id())){
                adminDao.delete(managerInfo.getEmp_id());
            }
            managerInfoDao.deleteById(info_id);
            //更新emp_id表
            //更新用户emptype
            memberDao.updateType("0", managerInfo.getEmp_id());
        }
        return 200;
    }
}
