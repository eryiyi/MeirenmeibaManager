package com.liangxunwang.unimanager.mvc.admin;

import com.liangxunwang.unimanager.model.Admin;
import com.liangxunwang.unimanager.model.KuaidiCompany;
import com.liangxunwang.unimanager.query.KuaidiCompanyQuery;
import com.liangxunwang.unimanager.service.*;
import com.liangxunwang.unimanager.util.ControllerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/kuaidiCompanyController")
public class KuaidiCompanyController extends ControllerConstants {

    @Autowired
    @Qualifier("kuaidiCompanyService")
    private ListService levelService;

    @Autowired
    @Qualifier("kuaidiCompanyService")
    private SaveService levelServiceSave;

    @Autowired
    @Qualifier("kuaidiCompanyService")
    private ExecuteService levelServiceSaveExe;

    @Autowired
    @Qualifier("kuaidiCompanyService")
    private UpdateService levelServiceSaveUpdate;

    @Autowired
    @Qualifier("kuaidiCompanyService")
    private DeleteService levelServiceSaveDel;


    @RequestMapping("list")
    public String list(ModelMap map, KuaidiCompanyQuery query){
        List<KuaidiCompany> list = (List<KuaidiCompany>) levelService.list(query);
        map.put("list", list);
        return "/kuaidiCompany/list";
    }

    @RequestMapping("toAdd")
    public String add(){
        return "/kuaidiCompany/add";
    }

    @RequestMapping("add")
    @ResponseBody
    public String add(KuaidiCompany kuaidiCompany){
        try {
            levelServiceSave.save(kuaidiCompany);
            return toJSONString(SUCCESS);
        }catch (ServiceException e){
            return toJSONString(ERROR_1);
        }
    }

    @RequestMapping("delete")
    @ResponseBody
    public String delete(HttpSession session,String kuaidi_company_id){
        Admin manager = (Admin) session.getAttribute(ACCOUNT_KEY);
        levelServiceSaveDel.delete(kuaidi_company_id);
        return toJSONString(SUCCESS);
    }

    @RequestMapping("/toEdit")
    public String toEdit(HttpSession session,ModelMap map, String kuaidi_company_code) throws Exception {
        Admin manager = (Admin) session.getAttribute(ACCOUNT_KEY);
        KuaidiCompany kuaidiCompany = (KuaidiCompany) levelServiceSaveExe.execute(kuaidi_company_code);
        map.put("kuaidiCompany", kuaidiCompany);
        return "/kuaidiCompany/edit";
    }

    /**
     * 更新
     */
    @RequestMapping("/edit")
    @ResponseBody
    public String edit(KuaidiCompany kuaidiCompany){
        try {
            levelServiceSaveUpdate.update(kuaidiCompany);
            return toJSONString(SUCCESS);
        }catch (ServiceException e){
            return toJSONString(ERROR_1);
        }
    }



}
