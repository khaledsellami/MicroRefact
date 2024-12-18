package org.jeecgframework.web.system.controller.core;
 import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.common.model.json.ValidForm;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.MutiLangUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.NumberComparator;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.SetListSort;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.easyui.ComboTreeModel;
import org.jeecgframework.tag.vo.easyui.TreeGridModel;
import org.jeecgframework.web.system.pojo.base.TSDataRule;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSFunction;
import org.jeecgframework.web.system.pojo.base.TSOperation;
import org.jeecgframework.web.system.pojo.base.TSRole;
import org.jeecgframework.web.system.pojo.base.TSRoleFunction;
import org.jeecgframework.web.system.pojo.base.TSRoleOrg;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.CacheServiceI;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.service.UserService;
import org.jeecgframework.web.system.util.OrgConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
@Controller
@RequestMapping("/roleController")
public class RoleController extends BaseController{

 private  Logger logger;

 private  UserService userService;

 private  SystemService systemService;

@Autowired
 private  CacheServiceI cacheService;


@RequestMapping(params = "delRole")
@ResponseBody
public AjaxJson delRole(TSRole role,HttpServletRequest request){
    String message = null;
    AjaxJson j = new AjaxJson();
    int count = userService.getUsersOfThisRole(role.getId());
    if (count == 0) {
        // 删除角色之前先删除角色权限关系
        delRoleFunction(role);
        // update-start--Author:zhangguoming  Date:20140825 for：添加业务逻辑
        // 删除 角色-机构 关系信息
        systemService.executeSql("delete from t_s_role_org where role_id=?", role.getId());
        // update-end--Author:zhangguoming  Date:20140825 for：添加业务逻辑
        role = systemService.getEntity(TSRole.class, role.getId());
        userService.delete(role);
        message = "角色: " + role.getRoleName() + "被删除成功";
        systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
    } else {
        message = "角色: 仍被用户使用，请先删除关联关系";
    }
    j.setMsg(message);
    logger.info(message);
    return j;
}


@RequestMapping(params = "dataRuleListForFunction")
public ModelAndView dataRuleListForFunction(HttpServletRequest request,String functionId,String roleId){
    CriteriaQuery cq = new CriteriaQuery(TSDataRule.class);
    cq.eq("TSFunction.id", functionId);
    cq.add();
    List<TSDataRule> dataRuleList = this.systemService.getListByCriteriaQuery(cq, false);
    Set<String> dataRulecodes = systemService.getDataRuleIdsByRoleIdAndFunctionId(roleId, functionId);
    request.setAttribute("dataRuleList", dataRuleList);
    request.setAttribute("dataRulecodes", dataRulecodes);
    request.setAttribute("functionId", functionId);
    return new ModelAndView("system/role/dataRuleListForFunction");
}


@Override
public int compare(Object o1,Object o2){
    TSFunction tsFunction1 = (TSFunction) o1;
    TSFunction tsFunction2 = (TSFunction) o2;
    int flag = tsFunction1.getFunctionOrder().compareTo(tsFunction2.getFunctionOrder());
    if (flag == 0) {
        return tsFunction1.getFunctionName().compareTo(tsFunction2.getFunctionName());
    } else {
        return flag;
    }
}


@RequestMapping(params = "role")
public ModelAndView role(){
    return new ModelAndView("system/role/roleList");
}


@RequestMapping(params = "upload")
public ModelAndView upload(HttpServletRequest req){
    req.setAttribute("controller_name", "roleController");
    return new ModelAndView("common/upload/pub_excel_upload");
}


@RequestMapping(params = "exportXls")
public String exportXls(TSRole tsRole,HttpServletRequest request,HttpServletResponse response,DataGrid dataGrid,ModelMap modelMap){
    tsRole.setRoleName(null);
    CriteriaQuery cq = new CriteriaQuery(TSRole.class, dataGrid);
    org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tsRole, request.getParameterMap());
    List<TSRole> tsRoles = systemService.getListByCriteriaQuery(cq, false);
    modelMap.put(NormalExcelConstants.FILE_NAME, "角色表");
    modelMap.put(NormalExcelConstants.CLASS, TSRole.class);
    modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("角色表列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
    modelMap.put(NormalExcelConstants.DATA_LIST, tsRoles);
    return NormalExcelConstants.JEECG_EXCEL_VIEW;
}


@RequestMapping(params = "roleGrid")
public void roleGrid(TSRole role,HttpServletRequest request,HttpServletResponse response,DataGrid dataGrid){
    CriteriaQuery cq = new CriteriaQuery(TSRole.class, dataGrid);
    org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, role);
    // update-begin--author:Yandong Date:20180521 for:TASK #2725 【bug】部门二级管理员，部门角色为什么在系统角色列表显示
    // 默认只查询系统角色
    cq.eq("roleType", OrgConstants.SYSTEM_ROLE_TYPE);
    // update-end--author:Yandong Date:20180521 for:TASK #2725 【bug】部门二级管理员，部门角色为什么在系统角色列表显示
    cq.add();
    this.systemService.getDataGridReturn(cq, true);
    TagUtil.datagrid(response, dataGrid);
    ;
}


@RequestMapping(params = "getUserList")
@ResponseBody
public List<ComboTree> getUserList(TSUser user,HttpServletRequest request,ComboTree comboTree){
    List<ComboTree> comboTrees = new ArrayList<ComboTree>();
    String roleId = request.getParameter("roleId");
    List<TSUser> loginActionlist = new ArrayList<TSUser>();
    if (user != null) {
        List<TSRoleUser> roleUser = systemService.findByProperty(TSRoleUser.class, "TSRole.id", roleId);
        if (roleUser.size() > 0) {
            for (TSRoleUser ru : roleUser) {
                loginActionlist.add(ru.getTSUser());
            }
        }
    }
    ComboTreeModel comboTreeModel = new ComboTreeModel("id", "userName", "TSUser");
    comboTrees = systemService.ComboTree(loginActionlist, comboTreeModel, loginActionlist, false);
    return comboTrees;
}


@RequestMapping(params = "doAddUserToRole")
@ResponseBody
public AjaxJson doAddUserToOrg(HttpServletRequest req){
    String message = null;
    AjaxJson j = new AjaxJson();
    TSRole role = systemService.getEntity(TSRole.class, req.getParameter("roleId"));
    saveRoleUserList(req, role);
    message = MutiLangUtil.paramAddSuccess("common.user");
    // systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
    j.setMsg(message);
    return j;
}


@RequestMapping(params = "updateDataRule")
@ResponseBody
public AjaxJson updateDataRule(HttpServletRequest request){
    AjaxJson j = new AjaxJson();
    String roleId = request.getParameter("roleId");
    String functionId = request.getParameter("functionId");
    // update-begin--Author:chenxu Date:201403024 for：410
    String dataRulecodes = null;
    try {
        dataRulecodes = URLDecoder.decode(request.getParameter("dataRulecodes"), "utf-8");
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    // update-end--Author:chenxu Date:20140324 for：410
    CriteriaQuery cq1 = new CriteriaQuery(TSRoleFunction.class);
    cq1.eq("TSRole.id", roleId);
    cq1.eq("TSFunction.id", functionId);
    cq1.add();
    List<TSRoleFunction> rFunctions = systemService.getListByCriteriaQuery(cq1, false);
    if (null != rFunctions && rFunctions.size() > 0) {
        TSRoleFunction tsRoleFunction = rFunctions.get(0);
        tsRoleFunction.setDataRule(dataRulecodes);
        systemService.saveOrUpdate(tsRoleFunction);
    }
    j.setMsg("数据权限更新成功");
    return j;
}


public void savep(String roleId,String functionid,String ids){
    // String hql = "from TSRoleFunction t where" + " t.TSRole.id=" +
    // oConvertUtils.getInt(roleId,0)
    // + " " + "and t.TSFunction.id=" + oConvertUtils.getInt(functionid,0);
    CriteriaQuery cq = new CriteriaQuery(TSRoleFunction.class);
    cq.eq("TSRole.id", roleId);
    cq.eq("TSFunction.id", functionid);
    cq.add();
    List<TSRoleFunction> rFunctions = systemService.getListByCriteriaQuery(cq, false);
    if (rFunctions.size() > 0) {
        TSRoleFunction roleFunction = rFunctions.get(0);
        roleFunction.setOperation(ids);
        systemService.saveOrUpdate(roleFunction);
    }
}


public void clearp(String roleId){
    List<TSRoleFunction> rFunctions = systemService.findByProperty(TSRoleFunction.class, "TSRole.id", roleId);
    if (rFunctions.size() > 0) {
        for (TSRoleFunction tRoleFunction : rFunctions) {
            tRoleFunction.setOperation(null);
            systemService.saveOrUpdate(tRoleFunction);
        }
    }
}


@RequestMapping(params = "userList")
public ModelAndView userList(HttpServletRequest request){
    // update-start--Author:zhangguoming  Date:20140828 for：bug修复：角色列表，查看用户列表报错
    request.setAttribute("roleId", request.getParameter("roleId"));
    // update-end--Author:zhangguoming  Date:20140828 for：bug修复：角色列表，查看用户列表报错
    return new ModelAndView("system/role/roleUserList");
}


@SuppressWarnings("unchecked")
public ComboTree comboTree(TSFunction obj,ComboTreeModel comboTreeModel,List<TSFunction> in,boolean recursive){
    ComboTree tree = new ComboTree();
    String id = oConvertUtils.getString(obj.getId());
    tree.setId(id);
    tree.setText(oConvertUtils.getString(obj.getFunctionName()));
    if (in == null) {
    } else {
        if (in.size() > 0) {
            for (TSFunction inobj : in) {
                String inId = oConvertUtils.getString(inobj.getId());
                // update-begin--Author:JueYue  Date:20140514 for：==不起作用--------------------
                if (inId.equals(id)) {
                    tree.setChecked(true);
                }
            // update-end--Author:JueYue  Date:20140514 for：==不起作用--------------------
            }
        }
    }
    // update-begin--Author:zhangguoming  Date:20140819 for：递归子节点属性
    List<TSFunction> curChildList = obj.getTSFunctions();
    // update-begin--Author:zhuxiaomeng  Date:20170313 for：排序
    Collections.sort(curChildList, new Comparator<Object>() {

        @Override
        public int compare(Object o1, Object o2) {
            TSFunction tsFunction1 = (TSFunction) o1;
            TSFunction tsFunction2 = (TSFunction) o2;
            int flag = tsFunction1.getFunctionOrder().compareTo(tsFunction2.getFunctionOrder());
            if (flag == 0) {
                return tsFunction1.getFunctionName().compareTo(tsFunction2.getFunctionName());
            } else {
                return flag;
            }
        }
    });
    // update-end--Author:zhuxiaomeng  Date:20170313 for：排序
    if (curChildList != null && curChildList.size() > 0) {
        tree.setState("closed");
        // update-begin--Author:xuelin  Date:20170401 for：[#1714]【功能】角色分配菜单权限的时候，权限树采用ztree重写，不再采用easyui的树   解决保存后父节点未被选中问题--------------------
        // tree.setChecked(false);
        // update-end--Author:xuelin  Date:20170401 for：[#1714]【功能】角色分配菜单权限的时候，权限树采用ztree重写，不再采用easyui的树   解决保存后父节点未被选中问题----------------------
        if (recursive) {
            // 递归查询子节点
            List<ComboTree> children = new ArrayList<ComboTree>();
            for (TSFunction childObj : curChildList) {
                ComboTree t = comboTree(childObj, comboTreeModel, in, recursive);
                children.add(t);
            }
            tree.setChildren(children);
        }
    }
    // update-end--Author:zhangguoming  Date:20140819 for：递归子节点属性
    // update-begin--author:zhangjiaqiang date:20170301 for:通过图标区分菜单和数据权限
    if (obj.getFunctionType() == 1) {
        if (curChildList != null && curChildList.size() > 0) {
            tree.setIconCls("icon-user-set-o");
        } else {
            tree.setIconCls("icon-user-set");
        }
    }
    // update-end--author:zhangjiaqiang date:20170301 for:通过图标区分菜单和数据权限
    // update-begin--Author:scott  Date:20160530 for：清空降低缓存占用
    if (curChildList != null) {
        curChildList.clear();
    }
    // update-end--Author:scott  Date:20140819 for：清空降低缓存占用
    return tree;
}


@RequestMapping(params = "getRoleTree")
@ResponseBody
public List<ComboTree> getRoleTree(HttpServletRequest request){
    ComboTreeModel comboTreeModel = new ComboTreeModel("id", "roleName", "");
    String orgId = request.getParameter("orgId");
    List<TSRole[]> orgRoleArrList = systemService.findHql("from TSRole r, TSRoleOrg ro, TSDepart o WHERE r.id=ro.tsRole.id AND ro.tsDepart.id=o.id AND o.id=?", orgId);
    List<TSRole> orgRoleList = new ArrayList<TSRole>();
    for (Object[] roleArr : orgRoleArrList) {
        orgRoleList.add((TSRole) roleArr[0]);
    }
    List<Object> allRoleList = this.systemService.getList(TSRole.class);
    List<ComboTree> comboTrees = systemService.ComboTree(allRoleList, comboTreeModel, orgRoleList, false);
    return comboTrees;
}


@RequestMapping(params = "checkRole")
@ResponseBody
public ValidForm checkRole(TSRole role,HttpServletRequest request,HttpServletResponse response){
    ValidForm v = new ValidForm();
    String roleCode = oConvertUtils.getString(request.getParameter("param"));
    String code = oConvertUtils.getString(request.getParameter("code"));
    List<TSRole> roles = systemService.findByProperty(TSRole.class, "roleCode", roleCode);
    if (roles.size() > 0 && !code.equals(roleCode)) {
        v.setInfo("角色编码已存在");
        v.setStatus("n");
    }
    return v;
}


public void updateCompare(Set<String> set,TSRole role,Map<String,TSRoleFunction> map){
    List<TSRoleFunction> entitys = new ArrayList<TSRoleFunction>();
    List<TSRoleFunction> deleteEntitys = new ArrayList<TSRoleFunction>();
    for (String s : set) {
        if (map.containsKey(s)) {
            map.remove(s);
        } else {
            TSRoleFunction rf = new TSRoleFunction();
            TSFunction f = this.systemService.get(TSFunction.class, s);
            rf.setTSFunction(f);
            rf.setTSRole(role);
            entitys.add(rf);
        }
    }
    Collection<TSRoleFunction> collection = map.values();
    Iterator<TSRoleFunction> it = collection.iterator();
    for (; it.hasNext(); ) {
        deleteEntitys.add(it.next());
    }
    systemService.batchSave(entitys);
    systemService.deleteAllEntitie(deleteEntitys);
}


@RequestMapping(params = "saveRole")
@ResponseBody
public AjaxJson saveRole(TSRole role,HttpServletRequest request){
    String message = null;
    AjaxJson j = new AjaxJson();
    if (StringUtil.isNotEmpty(role.getId())) {
        message = "角色: " + role.getRoleName() + "被更新成功";
        // update-begin--author:scott -- Date:20180811 -- for:TASK #3065 【严重bug】角色编辑列表丢了--
        role.setRoleType(OrgConstants.SYSTEM_ROLE_TYPE);
        // update-end--author:scott --Date:20180811 -- for:TASK #3065 【严重bug】角色编辑列表丢了--
        userService.saveOrUpdate(role);
        systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
    } else {
        message = "角色: " + role.getRoleName() + "被添加成功";
        // update-begin--author:Yandong Date:20180521 for:TASK #2725 【bug】部门二级管理员，部门角色为什么在系统角色列表显示
        // 默认系统角色
        role.setRoleType(OrgConstants.SYSTEM_ROLE_TYPE);
        // update-end--author:Yandong Date:20180521 for:TASK #2725 【bug】部门二级管理员，部门角色为什么在系统角色列表显示
        userService.save(role);
        systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
    }
    logger.info(message);
    return j;
}


@RequestMapping(params = "saveOperate")
@ResponseBody
public AjaxJson saveOperate(HttpServletRequest request){
    AjaxJson j = new AjaxJson();
    String fop = request.getParameter("fp");
    String roleId = request.getParameter("roleId");
    // 录入操作前清空上一次的操作数据
    clearp(roleId);
    String[] fun_op = fop.split(",");
    String aa = "";
    String bb = "";
    // 只有一个被选中
    if (fun_op.length == 1) {
        bb = fun_op[0].split("_")[1];
        aa = fun_op[0].split("_")[0];
        savep(roleId, bb, aa);
    } else {
        // 至少2个被选中
        for (int i = 0; i < fun_op.length; i++) {
            // 操作id
            String cc = fun_op[i].split("_")[0];
            if (i > 0 && bb.equals(fun_op[i].split("_")[1])) {
                aa += "," + cc;
                if (i == (fun_op.length - 1)) {
                    savep(roleId, bb, aa);
                }
            } else if (i > 0) {
                savep(roleId, bb, aa);
                // 操作ID
                aa = fun_op[i].split("_")[0];
                if (i == (fun_op.length - 1)) {
                    // 权限id
                    bb = fun_op[i].split("_")[1];
                    savep(roleId, bb, aa);
                }
            } else {
                // 操作ID
                aa = fun_op[i].split("_")[0];
            }
            // 权限id
            bb = fun_op[i].split("_")[1];
        }
    }
    return j;
}


@RequestMapping(params = "roleTree")
public ModelAndView roleTree(HttpServletRequest request){
    request.setAttribute("orgId", request.getParameter("orgId"));
    return new ModelAndView("system/role/roleTree");
}


@Autowired
public void setUserService(UserService userService){
    this.userService = userService;
}


@RequestMapping(params = "delUserRole")
@ResponseBody
public AjaxJson delUserRole(String userid,String roleid){
    AjaxJson ajaxJson = new AjaxJson();
    try {
        List<TSRoleUser> roleUserList = this.systemService.findByProperty(TSRoleUser.class, "TSUser.id", userid);
        if (roleUserList.size() == 1) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("不可删除用户的角色关系，请使用修订用户角色关系");
        } else {
            String sql = "delete from t_s_role_user where userid = ? and roleid = ?";
            this.systemService.executeSql(sql, userid, roleid);
            ajaxJson.setMsg("成功删除用户对应的角色关系");
        }
    } catch (Exception e) {
        LogUtil.log("删除用户对应的角色关系失败", e.getMessage());
        ajaxJson.setSuccess(false);
        ajaxJson.setMsg(e.getMessage());
    }
    return ajaxJson;
}


@RequestMapping(params = "refresh")
@ResponseBody
public AjaxJson refresh(HttpServletRequest request,HttpServletResponse response){
    AjaxJson ajaxJson = new AjaxJson();
    try {
        cacheService.clean("sysAuthCache");
        logger.info("-----清空登录用户权限缓存成功--------[sysAuthCache]-----");
        ajaxJson.setMsg("重置用户权限成功");
    } catch (Exception e) {
        ajaxJson.setMsg("重置用户权限失败");
        e.printStackTrace();
    }
    return ajaxJson;
}


public void delRoleFunction(TSRole role){
    List<TSRoleFunction> roleFunctions = systemService.findByProperty(TSRoleFunction.class, "TSRole.id", role.getId());
    if (roleFunctions.size() > 0) {
        for (TSRoleFunction tsRoleFunction : roleFunctions) {
            systemService.delete(tsRoleFunction);
        }
    }
    List<TSRoleUser> roleUsers = systemService.findByProperty(TSRoleUser.class, "TSRole.id", role.getId());
    for (TSRoleUser tsRoleUser : roleUsers) {
        systemService.delete(tsRoleUser);
    }
}


@RequestMapping(params = "updateOperation")
@ResponseBody
public AjaxJson updateOperation(HttpServletRequest request){
    AjaxJson j = new AjaxJson();
    String roleId = request.getParameter("roleId");
    String functionId = request.getParameter("functionId");
    // update-begin--Author:chenxu Date:201403024 for：410
    String operationcodes = null;
    try {
        operationcodes = URLDecoder.decode(request.getParameter("operationcodes"), "utf-8");
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    // update-end--Author:chenxu Date:20140324 for：410
    CriteriaQuery cq1 = new CriteriaQuery(TSRoleFunction.class);
    cq1.eq("TSRole.id", roleId);
    cq1.eq("TSFunction.id", functionId);
    cq1.add();
    List<TSRoleFunction> rFunctions = systemService.getListByCriteriaQuery(cq1, false);
    if (null != rFunctions && rFunctions.size() > 0) {
        TSRoleFunction tsRoleFunction = rFunctions.get(0);
        tsRoleFunction.setOperation(operationcodes);
        systemService.saveOrUpdate(tsRoleFunction);
    }
    j.setMsg("按钮权限更新成功");
    return j;
}


public void saveRoleUserList(HttpServletRequest request,TSRole role){
    String userIds = oConvertUtils.getString(request.getParameter("userIds"));
    List<TSRoleUser> roleUserList = new ArrayList<TSRoleUser>();
    List<String> userIdList = extractIdListByComma(userIds);
    for (String userId : userIdList) {
        TSUser user = new TSUser();
        user.setId(userId);
        TSRoleUser roleUser = new TSRoleUser();
        roleUser.setTSUser(user);
        roleUser.setTSRole(role);
        roleUserList.add(roleUser);
    }
    if (!roleUserList.isEmpty()) {
        systemService.batchSave(roleUserList);
    }
}


@Autowired
public void setSystemService(SystemService systemService){
    this.systemService = systemService;
}


@RequestMapping(params = "addUserToRoleList")
public void addUserToOrgList(TSUser user,HttpServletRequest request,HttpServletResponse response,DataGrid dataGrid){
    String roleId = request.getParameter("roleId");
    CriteriaQuery cq = new CriteriaQuery(TSUser.class, dataGrid);
    org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, user);
    // 获取 当前组织机构的用户信息
    CriteriaQuery subCq = new CriteriaQuery(TSRoleUser.class);
    subCq.setProjection(Property.forName("TSUser.id"));
    subCq.eq("TSRole.id", roleId);
    subCq.add();
    cq.add(Property.forName("id").notIn(subCq.getDetachedCriteria()));
    // update-begin--Author:zhoujf Date:20180803 for：角色增加已有用户，选择没有删除的系统用户
    // 删除状态，不删除
    cq.eq("deleteFlag", Globals.Delete_Normal);
    // 系统用户
    cq.eq("userType", Globals.USER_TYPE_SYSTEM);
    // update-end--Author:zhoujf Date:20180803 for：角色增加已有用户，选择没有删除的系统用户
    cq.add();
    this.systemService.getDataGridReturn(cq, true);
    TagUtil.datagrid(response, dataGrid);
}


public UserService getUserService(){
    return userService;
}


@RequestMapping(params = "exportXlsByT")
public String exportXlsByT(TSRole tsRole,HttpServletRequest request,HttpServletResponse response,DataGrid dataGrid,ModelMap modelMap){
    modelMap.put(NormalExcelConstants.FILE_NAME, "用户表");
    modelMap.put(NormalExcelConstants.CLASS, TSRole.class);
    modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("用户表列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
    modelMap.put(NormalExcelConstants.DATA_LIST, new ArrayList());
    return NormalExcelConstants.JEECG_EXCEL_VIEW;
}


@SuppressWarnings("unchecked")
@RequestMapping(params = "importExcel", method = RequestMethod.POST)
@ResponseBody
public AjaxJson importExcel(HttpServletRequest request,HttpServletResponse response){
    AjaxJson j = new AjaxJson();
    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
    for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
        // 获取上传文件对象
        MultipartFile file = entity.getValue();
        ImportParams params = new ImportParams();
        params.setTitleRows(2);
        params.setHeadRows(1);
        params.setNeedSave(true);
        try {
            List<TSRole> tsRoles = ExcelImportUtil.importExcel(file.getInputStream(), TSRole.class, params);
            for (TSRole tsRole : tsRoles) {
                String roleCode = tsRole.getRoleCode();
                List<TSRole> roles = systemService.findByProperty(TSRole.class, "roleCode", roleCode);
                if (roles.size() != 0) {
                    TSRole role = roles.get(0);
                    MyBeanUtils.copyBeanNotNull2Bean(tsRole, role);
                    systemService.saveOrUpdate(role);
                } else {
                    systemService.save(tsRole);
                }
            }
            j.setMsg("文件导入成功！");
        } catch (Exception e) {
            j.setMsg("文件导入失败！");
            logger.error(ExceptionUtil.getExceptionMessage(e));
        } finally {
            try {
                file.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    return j;
}


@RequestMapping(params = "addorupdate")
public ModelAndView addorupdate(TSRole role,HttpServletRequest req){
    if (role.getId() != null) {
        role = systemService.getEntity(TSRole.class, role.getId());
        req.setAttribute("role", role);
    }
    return new ModelAndView("system/role/role");
}


@RequestMapping(params = "setAuthority")
@ResponseBody
public List<ComboTree> setAuthority(TSRole role,HttpServletRequest request,ComboTree comboTree){
    CriteriaQuery cq = new CriteriaQuery(TSFunction.class);
    if (comboTree.getId() != null) {
        cq.eq("TSFunction.id", comboTree.getId());
    }
    if (comboTree.getId() == null) {
        cq.isNull("TSFunction");
    }
    cq.notEq("functionLevel", Short.parseShort("-1"));
    cq.add();
    List<TSFunction> functionList = systemService.getListByCriteriaQuery(cq, false);
    Collections.sort(functionList, new NumberComparator());
    List<ComboTree> comboTrees = new ArrayList<ComboTree>();
    String roleId = request.getParameter("roleId");
    // 已有权限菜单
    List<TSFunction> loginActionlist = new ArrayList<TSFunction>();
    role = this.systemService.get(TSRole.class, roleId);
    if (role != null) {
        List<TSRoleFunction> roleFunctionList = systemService.findByProperty(TSRoleFunction.class, "TSRole.id", role.getId());
        if (roleFunctionList.size() > 0) {
            for (TSRoleFunction roleFunction : roleFunctionList) {
                TSFunction function = (TSFunction) roleFunction.getTSFunction();
                loginActionlist.add(function);
            }
        }
        roleFunctionList.clear();
    }
    ComboTreeModel comboTreeModel = new ComboTreeModel("id", "functionName", "TSFunctions");
    // author:zhoujf-----start-----date:20170210 ------- for: TASK #1667 【性能问题】角色管理，权限设置，点击展开慢
    // author:xugj-----start-----date:20160516 ------- for: TASK  #1071 【平台】优化角色权限这块功能
    comboTrees = comboTree(functionList, comboTreeModel, loginActionlist, true);
    MutiLangUtil.setMutiComboTree(comboTrees);
    // author:xugj-----start-----date:20160516 ------- for: TASK  #1071 【平台】优化角色权限这块功能
    // author:zhoujf-----end-----date:20170210 ------- for: TASK #1667 【性能问题】角色管理，权限设置，点击展开慢
    // update-begin--Author:scott  Date:20160530 for：清空降低缓存占用
    functionList.clear();
    functionList = null;
    loginActionlist.clear();
    loginActionlist = null;
    // update-end--Author:scott  Date:20160530 for：清空降低缓存占用
    // System.out.println(JSON.toJSONString(comboTrees,true));
    return comboTrees;
}


@RequestMapping(params = "updateAuthority")
@ResponseBody
public AjaxJson updateAuthority(HttpServletRequest request){
    AjaxJson j = new AjaxJson();
    try {
        String roleId = request.getParameter("roleId");
        String rolefunction = request.getParameter("rolefunctions");
        TSRole role = this.systemService.get(TSRole.class, roleId);
        List<TSRoleFunction> roleFunctionList = systemService.findByProperty(TSRoleFunction.class, "TSRole.id", role.getId());
        Map<String, TSRoleFunction> map = new HashMap<String, TSRoleFunction>();
        for (TSRoleFunction functionOfRole : roleFunctionList) {
            map.put(functionOfRole.getTSFunction().getId(), functionOfRole);
        }
        // update-begin--author:scott --- date:20170921 --- for: 2367 【网友问题】角色赋权bug -----
        Set<String> set = new HashSet<String>();
        if (StringUtil.isNotEmpty(rolefunction)) {
            String[] roleFunctions = rolefunction.split(",");
            for (String s : roleFunctions) {
                set.add(s);
            }
        }
        // update-end--author:scott --- date:20170921 ---- for:2367 【网友问题】角色赋权bug -----
        updateCompare(set, role, map);
        j.setMsg("权限更新成功");
    } catch (Exception e) {
        logger.error(ExceptionUtil.getExceptionMessage(e));
        j.setMsg("权限更新失败");
    }
    return j;
}


@RequestMapping(params = "updateOrgRole")
@ResponseBody
public AjaxJson updateOrgRole(HttpServletRequest request){
    AjaxJson j = new AjaxJson();
    try {
        String orgId = request.getParameter("orgId");
        String roleIds = request.getParameter("roleIds");
        List<String> roleIdList = extractIdListByComma(roleIds);
        systemService.executeSql("delete from t_s_role_org where org_id=?", orgId);
        if (!roleIdList.isEmpty()) {
            List<TSRoleOrg> roleOrgList = new ArrayList<TSRoleOrg>();
            TSDepart depart = new TSDepart();
            depart.setId(orgId);
            for (String roleId : roleIdList) {
                TSRole role = new TSRole();
                role.setId(roleId);
                TSRoleOrg roleOrg = new TSRoleOrg();
                roleOrg.setTsRole(role);
                roleOrg.setTsDepart(depart);
                roleOrgList.add(roleOrg);
            }
            systemService.batchSave(roleOrgList);
        }
        j.setMsg("角色更新成功");
    } catch (Exception e) {
        logger.error(ExceptionUtil.getExceptionMessage(e));
        j.setMsg("角色更新失败");
    }
    return j;
}


@RequestMapping(params = "setOperate")
@ResponseBody
public List<TreeGrid> setOperate(HttpServletRequest request,TreeGrid treegrid){
    String roleId = request.getParameter("roleId");
    CriteriaQuery cq = new CriteriaQuery(TSFunction.class);
    if (treegrid.getId() != null) {
        cq.eq("TSFunction.id", treegrid.getId());
    }
    if (treegrid.getId() == null) {
        cq.isNull("TSFunction");
    }
    cq.add();
    List<TSFunction> functionList = systemService.getListByCriteriaQuery(cq, false);
    List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
    Collections.sort(functionList, new SetListSort());
    TreeGridModel treeGridModel = new TreeGridModel();
    treeGridModel.setRoleid(roleId);
    treeGrids = systemService.treegrid(functionList, treeGridModel);
    return treeGrids;
}


@RequestMapping(params = "operationListForFunction")
public ModelAndView operationListForFunction(HttpServletRequest request,String functionId,String roleId){
    CriteriaQuery cq = new CriteriaQuery(TSOperation.class);
    cq.eq("TSFunction.id", functionId);
    // update-begin--Author:anchao  Date:20140822 for：[bugfree号]字段级权限（表单，列表）--------------------
    cq.eq("status", Short.valueOf("0"));
    // update-end--Author:anchao  Date:20140822 for：[bugfree号]字段级权限（表单，列表）--------------------
    cq.add();
    List<TSOperation> operationList = this.systemService.getListByCriteriaQuery(cq, false);
    Set<String> operationCodes = systemService.getOperationCodesByRoleIdAndFunctionId(roleId, functionId);
    request.setAttribute("operationList", operationList);
    request.setAttribute("operationcodes", operationCodes);
    request.setAttribute("functionId", functionId);
    return new ModelAndView("system/role/operationListForFunction");
}


@RequestMapping(params = "roleUserDatagrid")
public void roleUserDatagrid(TSUser user,HttpServletRequest request,HttpServletResponse response,DataGrid dataGrid){
    CriteriaQuery cq = new CriteriaQuery(TSUser.class, dataGrid);
    // update-start--Author:zhangguoming  Date:20140828 for：bug修复：角色列表，查看用户列表报错
    // 查询条件组装器
    String roleId = request.getParameter("roleId");
    List<TSRoleUser> roleUser = systemService.findByProperty(TSRoleUser.class, "TSRole.id", roleId);
    /*
        // zhanggm：这个查询逻辑也可以使用这种 子查询的方式进行查询
        CriteriaQuery subCq = new CriteriaQuery(TSRoleUser.class);
        subCq.setProjection(Property.forName("TSUser.id"));
        subCq.eq("TSRole.id", roleId);
        subCq.add();
        cq.add(Property.forName("id").in(subCq.getDetachedCriteria()));
        cq.add();
        */
    // update-end--Author:zhangguoming  Date:20140828 for：bug修复：角色列表，查看用户列表报错
    Criterion cc = null;
    if (roleUser.size() > 0) {
        for (int i = 0; i < roleUser.size(); i++) {
            if (i == 0) {
                cc = Restrictions.eq("id", roleUser.get(i).getTSUser().getId());
            } else {
                cc = cq.getor(cc, Restrictions.eq("id", roleUser.get(i).getTSUser().getId()));
            }
        }
    } else {
        cc = Restrictions.eq("id", "-1");
    }
    cq.add(cc);
    cq.eq("deleteFlag", Globals.Delete_Normal);
    cq.add();
    org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, user);
    this.systemService.getDataGridReturn(cq, true);
    TagUtil.datagrid(response, dataGrid);
}


@RequestMapping(params = "goAddUserToRole")
public ModelAndView goAddUserToOrg(HttpServletRequest req){
    return new ModelAndView("system/role/noCurRoleUserList");
}


@RequestMapping(params = "fun")
public ModelAndView fun(HttpServletRequest request){
    String roleId = request.getParameter("roleId");
    request.setAttribute("roleId", roleId);
    return new ModelAndView("system/role/roleSet");
}


}