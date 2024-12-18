package org.jeecgframework.web.system.controller.core;
 import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil;
import org.jeecgframework.core.util.MutiLangUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.NumberComparator;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.tag.vo.easyui.ComboTreeModel;
import org.jeecgframework.tag.vo.easyui.TreeGridModel;
import org.jeecgframework.web.system.pojo.base.TSDataRule;
import org.jeecgframework.web.system.pojo.base.TSFunction;
import org.jeecgframework.web.system.pojo.base.TSIcon;
import org.jeecgframework.web.system.pojo.base.TSOperation;
import org.jeecgframework.web.system.pojo.base.TSRoleFunction;
import org.jeecgframework.web.system.service.FunctionService;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import Interface.FunctionService;
import DTO.TSDataRule;
@Controller
@RequestMapping("/functionController")
public class FunctionController extends BaseController{

 private  Logger logger;

 private  UserService userService;

 private  SystemService systemService;

@Autowired
 private  FunctionService functionService;


@RequestMapping(params = "dataRule")
public ModelAndView operationData(HttpServletRequest request,String functionId){
    request.setAttribute("functionId", functionId);
    return new ModelAndView("system/dataRule/ruleDataList");
}


@RequestMapping(params = "searchApp")
public ModelAndView searchApp(TSFunction function,HttpServletRequest req){
    String name = req.getParameter("name");
    String menuListMap = "";
    // String menuListMap =
    // "<div class='appListContainer ui-sortable' customacceptdrop='0' index='0' _olddisplay='block' style='width: auto; height: auto; margin-left: 10px; margin-top: 10px; display: block;'>";
    CriteriaQuery cq = new CriteriaQuery(TSFunction.class);
    cq.notEq("functionLevel", Short.valueOf("0"));
    if (name == null || "".equals(name)) {
        cq.isNull("TSFunction");
    } else {
        String name1 = "%" + name + "%";
        cq.like("functionName", name1);
    }
    cq.add();
    List<TSFunction> functionList = systemService.getListByCriteriaQuery(cq, false);
    if (functionList.size() > 0 && functionList != null) {
        for (int i = 0; i < functionList.size(); i++) {
            // menuListMap = menuListMap +
            // "<div id='menuList_area'  class='menuList_area'>";
            String icon = "";
            if (!"".equals(functionList.get(i).getTSIconDesk()) && functionList.get(i).getTSIconDesk() != null) {
                icon = functionList.get(i).getTSIconDesk().getIconPath();
            } else {
                icon = "plug-in/sliding/icon/default.png";
            }
            menuListMap = menuListMap + "<div type='" + i + 1 + "' class='menuSearch_Info' id='" + functionList.get(i).getId() + "' title='" + functionList.get(i).getFunctionName() + "' url='" + functionList.get(i).getFunctionUrl() + "' icon='" + icon + "' style='float:left;left: 0px; top: 0px;margin-left: 30px;margin-top: 20px;'>" + "<div ><img alt='" + functionList.get(i).getFunctionName() + "' src='" + icon + "'></div>" + "<div class='appButton_appName_inner1' style='color:#333333;'>" + functionList.get(i).getFunctionName() + "</div>" + "<div class='appButton_appName_inner_right1'></div>" + // "</div>" +
            "</div>";
        }
    } else {
        menuListMap = menuListMap + "很遗憾，在系统中没有检索到与“" + name + "”相关的信息！";
    }
    // menuListMap = menuListMap + "</div>";
    // System.out.println("-------------------------------" + menuListMap);
    req.setAttribute("menuListMap", menuListMap);
    return new ModelAndView("system/function/menuAppList");
}


@RequestMapping(params = "addorupdateop")
public ModelAndView addorupdateop(TSOperation operation,HttpServletRequest req){
    List<TSIcon> iconlist = systemService.getList(TSIcon.class);
    req.setAttribute("iconlist", iconlist);
    if (operation.getId() != null) {
        operation = systemService.getEntity(TSOperation.class, operation.getId());
        req.setAttribute("operation", operation);
    }
    String functionId = oConvertUtils.getString(req.getParameter("functionId"));
    req.setAttribute("functionId", functionId);
    return new ModelAndView("system/operation/operation");
}


@Autowired
public void setUserService(UserService userService){
    this.userService = userService;
}


@RequestMapping(params = "del")
@ResponseBody
public AjaxJson del(TSFunction function,HttpServletRequest request){
    AjaxJson j = functionService.delFunction(function.getId());
    systemService.addLog(j.getMsg(), Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
    return j;
}


@Autowired
public void setSystemService(SystemService systemService){
    this.systemService = systemService;
}


@RequestMapping(params = "saveFunction")
@ResponseBody
public AjaxJson saveFunction(TSFunction function,HttpServletRequest request){
    String message = null;
    AjaxJson j = new AjaxJson();
    // ----------------------------------------------------------------
    function.setFunctionUrl(function.getFunctionUrl().trim());
    String functionOrder = function.getFunctionOrder();
    if (StringUtils.isEmpty(functionOrder)) {
        function.setFunctionOrder("0");
    }
    if (function.getTSFunction().getId().equals("")) {
        function.setTSFunction(null);
    } else {
        TSFunction parent = systemService.getEntity(TSFunction.class, function.getTSFunction().getId());
        function.setFunctionLevel(Short.valueOf(parent.getFunctionLevel() + 1 + ""));
    }
    if (StringUtil.isNotEmpty(function.getId())) {
        message = MutiLangUtil.paramUpdSuccess("common.menu");
        // update-begin--Author:scott Date:20170619 for：菜单编辑保存报错----------------------------
        TSFunction t = systemService.getEntity(TSFunction.class, function.getId());
        try {
            MyBeanUtils.copyBeanNotNull2Bean(function, t);
            // update-begin--Author:gj_shaojc Date:20180329 for：TASK #2595 【严重bug】三级菜单编辑，改成一级菜单不成功---------
            if (t.getFunctionLevel() == 0) {
                t.setTSFunction(null);
            }
            // update-end--Author:gj_shaojc Date:20180329 for：TASK #2595 【严重bug】三级菜单编辑，改成一级菜单不成功---------
            userService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // update-end--Author:scott Date:20170619 for：菜单编辑保存报错----------------------------
        systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        // update-begin--Author:anchao Date:20140914 for：Jeecg bug 20140914 菜单更新级别后显示混乱
        List<TSFunction> subFunction = systemService.findByProperty(TSFunction.class, "TSFunction.id", function.getId());
        updateSubFunction(subFunction, function);
        // update-end--Author:anchao Date:20140914 for：Jeecg bug 20140914 菜单更新级别后显示混乱
        // ----------------------------------------------------------------
        systemService.flushRoleFunciton(function.getId(), function);
    // ----------------------------------------------------------------
    } else {
        if (function.getFunctionLevel().equals(Globals.Function_Leave_ONE)) {
            List<TSFunction> functionList = systemService.findByProperty(TSFunction.class, "functionLevel", Globals.Function_Leave_ONE);
            // int ordre=functionList.size()+1;
            // function.setFunctionOrder(Globals.Function_Order_ONE+ordre);
            function.setFunctionOrder(function.getFunctionOrder());
        } else {
            List<TSFunction> functionList = systemService.findByProperty(TSFunction.class, "functionLevel", Globals.Function_Leave_TWO);
            // int ordre=functionList.size()+1;
            // function.setFunctionOrder(Globals.Function_Order_TWO+ordre);
            function.setFunctionOrder(function.getFunctionOrder());
        }
        message = MutiLangUtil.paramAddSuccess("common.menu");
        systemService.save(function);
        systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
    }
    j.setMsg(message);
    return j;
}


public UserService getUserService(){
    return userService;
}


@RequestMapping(params = "ruledategrid")
public void ruledategrid(HttpServletRequest request,HttpServletResponse response,DataGrid dataGrid){
    CriteriaQuery cq = new CriteriaQuery(TSDataRule.class, dataGrid);
    String functionId = oConvertUtils.getString(request.getParameter("functionId"));
    cq.eq("TSFunction.id", functionId);
    cq.add();
    this.systemService.getDataGridReturn(cq, true);
    TagUtil.datagrid(response, dataGrid);
}


@RequestMapping(params = "addorupdate")
public ModelAndView addorupdate(TSFunction function,HttpServletRequest req){
    String functionid = req.getParameter("id");
    List<TSFunction> fuinctionlist = systemService.getList(TSFunction.class);
    req.setAttribute("flist", fuinctionlist);
    // update-begin--Author:zhangguoming Date:20140509 for：添加云桌面图标管理
    // List<TSIcon> iconlist = systemService.getList(TSIcon.class);
    List<TSIcon> iconlist = systemService.findByQueryString("from TSIcon where iconType != 3");
    req.setAttribute("iconlist", iconlist);
    List<TSIcon> iconDeskList = systemService.findByQueryString("from TSIcon where iconType = 3");
    req.setAttribute("iconDeskList", iconDeskList);
    // update-end--Author:zhangguoming Date:20140509 for：添加云桌面图标管理
    if (functionid != null) {
        function = systemService.getEntity(TSFunction.class, functionid);
        req.setAttribute("function", function);
    }
    if (function.getTSFunction() != null && function.getTSFunction().getId() != null) {
        function.setFunctionLevel((short) 1);
        function.setTSFunction((TSFunction) systemService.getEntity(TSFunction.class, function.getTSFunction().getId()));
        req.setAttribute("function", function);
    }
    return new ModelAndView("system/function/function");
}


@RequestMapping(params = "functionList")
@ResponseBody
public void functionList(HttpServletRequest request,HttpServletResponse response,DataGrid dataGrid){
    CriteriaQuery cq = new CriteriaQuery(TSFunction.class, dataGrid);
    String id = oConvertUtils.getString(request.getParameter("id"));
    cq.isNull("TSFunction");
    if (id != null) {
        cq.eq("TSFunction.id", id);
    }
    cq.add();
    List<TSFunction> functionList = systemService.getListByCriteriaQuery(cq, false);
    List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
    this.systemService.getDataGridReturn(cq, true);
    TagUtil.datagrid(response, dataGrid);
}


@RequestMapping(params = "addorupdaterule")
public ModelAndView addorupdaterule(TSDataRule operation,HttpServletRequest req){
    List<TSIcon> iconlist = systemService.getList(TSIcon.class);
    req.setAttribute("iconlist", iconlist);
    if (operation.getId() != null) {
        operation = systemService.getEntity(TSDataRule.class, operation.getId());
        req.setAttribute("operation", operation);
    }
    String functionId = oConvertUtils.getString(req.getParameter("functionId"));
    req.setAttribute("functionId", functionId);
    return new ModelAndView("system/dataRule/ruleData");
}


public void updateSubFunction(List<TSFunction> subFunction,TSFunction parent){
    if (subFunction.size() == 0) {
        // 没有子菜单是结束
        return;
    } else {
        for (TSFunction tsFunction : subFunction) {
            tsFunction.setFunctionLevel(Short.valueOf(parent.getFunctionLevel() + 1 + ""));
            systemService.saveOrUpdate(tsFunction);
            List<TSFunction> subFunction1 = systemService.findByProperty(TSFunction.class, "TSFunction.id", tsFunction.getId());
            updateSubFunction(subFunction1, tsFunction);
        }
    }
}


@RequestMapping(params = "saveop")
@ResponseBody
public AjaxJson saveop(TSOperation operation,HttpServletRequest request){
    String message = null;
    String pid = request.getParameter("TSFunction.id");
    if (pid.equals("")) {
        operation.setTSFunction(null);
    }
    AjaxJson j = new AjaxJson();
    if (StringUtil.isNotEmpty(operation.getId())) {
        message = MutiLangUtil.paramUpdSuccess("common.operation");
        userService.saveOrUpdate(operation);
        systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
    } else {
        message = MutiLangUtil.paramAddSuccess("common.operation");
        userService.save(operation);
        systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
    }
    j.setMsg(message);
    return j;
}


@RequestMapping(params = "function")
public ModelAndView function(ModelMap model){
    return new ModelAndView("system/function/functionList");
}


@RequestMapping(params = "delrule")
@ResponseBody
public AjaxJson delrule(TSDataRule operation,HttpServletRequest request){
    String message = null;
    AjaxJson j = new AjaxJson();
    operation = systemService.getEntity(TSDataRule.class, operation.getId());
    message = MutiLangUtil.paramDelSuccess("common.operation");
    userService.delete(operation);
    systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
    j.setMsg(message);
    return j;
}


public int justHaveDataRule(TSDataRule dataRule){
    // update-begin-author:taoyan date:20180528 for:TASK #2745 【Sql 注入问题修改】
    String column = dataRule.getRuleColumn();
    if (oConvertUtils.isEmpty(column)) {
        return 0;
    }
    String sql = "SELECT count(*) FROM t_s_data_rule WHERE functionId = ? and rule_column = ? AND rule_conditions = ?";
    Long count = this.systemService.getCountForJdbcParam(sql, dataRule.getTSFunction().getId(), column, dataRule.getRuleConditions());
    return count.intValue();
// update-end-author:taoyan date:20180528 for:TASK #2745 【Sql 注入问题修改】
}


@RequestMapping(params = "opdategrid")
public void opdategrid(HttpServletRequest request,HttpServletResponse response,DataGrid dataGrid){
    CriteriaQuery cq = new CriteriaQuery(TSOperation.class, dataGrid);
    String functionId = oConvertUtils.getString(request.getParameter("functionId"));
    cq.eq("TSFunction.id", functionId);
    cq.add();
    this.systemService.getDataGridReturn(cq, true);
    TagUtil.datagrid(response, dataGrid);
}


@RequestMapping(params = "datagrid")
public void datagrid(HttpServletRequest request,HttpServletResponse response,DataGrid dataGrid){
    CriteriaQuery cq = new CriteriaQuery(TSFunction.class, dataGrid);
    this.systemService.getDataGridReturn(cq, true);
    TagUtil.datagrid(response, dataGrid);
}


@RequestMapping(params = "delop")
@ResponseBody
public AjaxJson delop(TSOperation operation,HttpServletRequest request){
    String message = null;
    AjaxJson j = new AjaxJson();
    operation = systemService.getEntity(TSOperation.class, operation.getId());
    message = MutiLangUtil.paramDelSuccess("common.operation");
    userService.delete(operation);
    // ---author:jg_xugj----start-----date:20151211--------for：#779 【菜单问题】当删了t_s_operation表中记录时， t_s_role_function 表中operation 字段应该同步更新。
    String operationId = operation.getId();
    String hql = "from TSRoleFunction rolefun where rolefun.operation like '%" + operationId + "%'";
    List<TSRoleFunction> roleFunctions = userService.findByQueryString(hql);
    for (TSRoleFunction roleFunction : roleFunctions) {
        String newOper = roleFunction.getOperation().replace(operationId + ",", "");
        if (roleFunction.getOperation().length() == newOper.length()) {
            newOper = roleFunction.getOperation().replace(operationId, "");
        }
        roleFunction.setOperation(newOper);
        userService.updateEntitie(roleFunction);
    }
    // ---author:jg_xugj----start-----date:20151211--------for：#779 【菜单问题】当删了t_s_operation表中记录时， t_s_role_function 表中operation 字段应该同步更新。
    systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
    j.setMsg(message);
    return j;
}


@RequestMapping(params = "operation")
public ModelAndView operation(HttpServletRequest request,String functionId){
    request.setAttribute("functionId", functionId);
    return new ModelAndView("system/operation/operationList");
}


@RequestMapping(params = "functionGrid")
@ResponseBody
public List<TreeGrid> functionGrid(HttpServletRequest request,TreeGrid treegrid,Integer type){
    CriteriaQuery cq = new CriteriaQuery(TSFunction.class);
    String selfId = request.getParameter("selfId");
    if (selfId != null) {
        cq.notEq("id", selfId);
    }
    if (treegrid.getId() != null) {
        cq.eq("TSFunction.id", treegrid.getId());
    }
    if (treegrid.getId() == null) {
        cq.isNull("TSFunction");
    }
    if (type != null) {
        cq.eq("functionType", type.shortValue());
    }
    cq.addOrder("functionOrder", SortDirection.asc);
    cq.add();
    // update--begin------author:scott--------------date:20151208-----------for:手工加载数据权限条件--------
    // 获取装载数据权限的条件HQL
    cq = HqlGenerateUtil.getDataAuthorConditionHql(cq, new TSFunction());
    cq.add();
    // update--end------author:scott--------------date:20151208-----------for:手工加载数据权限条件--------
    List<TSFunction> functionList = systemService.getListByCriteriaQuery(cq, false);
    // update-start-Author:zhangguoming  Date:20140914 for：菜单管理页面：菜单排序
    Collections.sort(functionList, new NumberComparator());
    // update-end-Author:zhangguoming  Date:20140914 for：菜单管理页面：菜单排序
    List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
    TreeGridModel treeGridModel = new TreeGridModel();
    treeGridModel.setIcon("TSIcon_iconPath");
    treeGridModel.setTextField("functionName");
    treeGridModel.setParentText("TSFunction_functionName");
    treeGridModel.setParentId("TSFunction_id");
    treeGridModel.setSrc("functionUrl");
    treeGridModel.setIdField("id");
    treeGridModel.setChildList("TSFunctions");
    // 添加排序字段
    treeGridModel.setOrder("functionOrder");
    // update-begin--Author:chenj  Date:20160722 for：添加菜单图标样式
    treeGridModel.setIconStyle("functionIconStyle");
    // update-end--Author:chenj  Date:20160722 for：添加菜单图标样式
    treeGridModel.setFunctionType("functionType");
    treeGrids = systemService.treegrid(functionList, treeGridModel);
    // update-begin--Author:Yandong  Date:20171226 for：TASK #2463 【bug】菜单维护，添加三级子菜单的时候，二级父级菜单的URL丢了--------------------
    // update-begin--Author:gj_shaojc Date:20180329 for：TASK #2599 【菜单问题】 类型为菜单类型的菜单，如果下级有权限类型的菜单，菜单地址不显示了---------
    // for (TreeGrid tg : treeGrids) {
    // if("closed".equals(tg.getState()))tg.setSrc("");
    // }
    // update-end--Author:gj_shaojc Date:20180329 for：TASK #2599 【菜单问题】 类型为菜单类型的菜单，如果下级有权限类型的菜单，菜单地址不显示了---------
    // update-end--Author:Yandong  Date:20171226 for：TASK #2463 【bug】菜单维护，添加三级子菜单的时候，二级父级菜单的URL丢了--------------------
    MutiLangUtil.setMutiTree(treeGrids);
    return treeGrids;
}


@RequestMapping(params = "setPFunction")
@ResponseBody
public List<ComboTree> setPFunction(HttpServletRequest request,ComboTree comboTree){
    CriteriaQuery cq = new CriteriaQuery(TSFunction.class);
    if (null != request.getParameter("selfId")) {
        cq.notEq("id", request.getParameter("selfId"));
    }
    if (comboTree.getId() != null) {
        cq.eq("TSFunction.id", comboTree.getId());
    }
    if (comboTree.getId() == null) {
        cq.isNull("TSFunction");
    }
    cq.add();
    List<TSFunction> functionList = systemService.getListByCriteriaQuery(cq, false);
    List<ComboTree> comboTrees = new ArrayList<ComboTree>();
    ComboTreeModel comboTreeModel = new ComboTreeModel("id", "functionName", "TSFunctions");
    comboTrees = systemService.ComboTree(functionList, comboTreeModel, null, false);
    MutiLangUtil.setMutiTree(comboTrees);
    return comboTrees;
}


@RequestMapping(params = "saverule")
@ResponseBody
public AjaxJson saverule(TSDataRule operation,HttpServletRequest request){
    String message = null;
    AjaxJson j = new AjaxJson();
    if (StringUtil.isNotEmpty(operation.getId())) {
        message = MutiLangUtil.paramUpdSuccess("common.operation");
        userService.saveOrUpdate(operation);
        systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
    } else {
        if (justHaveDataRule(operation) == 0) {
            message = MutiLangUtil.paramAddSuccess("common.operation");
            userService.save(operation);
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        } else {
            message = "操作 字段规则已存在";
        }
    }
    j.setMsg(message);
    return j;
}


}