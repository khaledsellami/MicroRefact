package org.jeecgframework.web.system.controller.core;
 import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MutiLangUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.vo.easyui.ComboTreeModel;
import org.jeecgframework.tag.vo.easyui.TreeGridModel;
import org.jeecgframework.web.system.pojo.base.TSCategoryEntity;
import org.jeecgframework.web.system.pojo.base.TSIcon;
import org.jeecgframework.web.system.service.CategoryServiceI;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;
@Controller
@RequestMapping("/categoryController")
public class CategoryController extends BaseController{

 private  Logger logger;

 private  String CATEGORY_LIST;

 private  String CATEGORY_ADD_OR_UPDATE;

@Autowired
 private  CategoryServiceI categoryService;

@Autowired
 private  SystemService systemService;


@RequestMapping(params = "addorupdate")
public String addorupdate(ModelMap map,TSCategoryEntity category){
    if (StringUtil.isNotEmpty(category.getCode())) {
        category = categoryService.findUniqueByProperty(TSCategoryEntity.class, "code", category.getCode());
        map.put("categoryPage", category);
    }
    map.put("iconlist", systemService.findByProperty(TSIcon.class, "iconType", (short) 1));
    if (category.getParent() != null && StringUtil.isNotEmpty(category.getParent().getCode())) {
        TSCategoryEntity parent = categoryService.findUniqueByProperty(TSCategoryEntity.class, "code", category.getParent().getCode());
        category.setParent(parent);
        map.put("categoryPage", category);
    }
    return CATEGORY_ADD_OR_UPDATE;
}


@RequestMapping(params = "combotree")
@ResponseBody
public List<ComboTree> combotree(String selfCode,ComboTree comboTree){
    CriteriaQuery cq = new CriteriaQuery(TSCategoryEntity.class);
    if (StringUtils.isNotEmpty(comboTree.getId())) {
        cq.createAlias("parent", "parent");
        cq.eq("parent.code", comboTree.getId());
    } else if (StringUtils.isNotEmpty(selfCode)) {
        cq.eq("code", selfCode);
    } else {
        cq.isNull("parent");
    }
    cq.add();
    List<TSCategoryEntity> categoryList = systemService.getListByCriteriaQuery(cq, false);
    List<ComboTree> comboTrees = new ArrayList<ComboTree>();
    ComboTreeModel comboTreeModel = new ComboTreeModel("code", "name", "list");
    comboTrees = systemService.ComboTree(categoryList, comboTreeModel, null, false);
    MutiLangUtil.setMutiTree(comboTrees);
    return comboTrees;
}


@RequestMapping(params = "save")
@ResponseBody
public AjaxJson save(TSCategoryEntity category,HttpServletRequest request){
    AjaxJson j = new AjaxJson();
    if (StringUtil.isNotEmpty(category.getId())) {
        j.setMsg("分类管理更新成功");
        TSCategoryEntity t = categoryService.get(TSCategoryEntity.class, category.getId());
        // update-start--Author:luobaoli  Date:20150606 for：修改分类时将空值转为NULL值
        category.getParent().setCode(t.getParent() == null || "".equals(t.getParent().getCode()) ? null : t.getParent().getCode());
        // update-end--Author:luobaoli  Date:20150606 for：修改分类时将空值转为NULL值
        try {
            MyBeanUtils.copyBeanNotNull2Bean(category, t);
            categoryService.saveOrUpdate(t);
            systemService.addLog(j.getMsg(), Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e.fillInStackTrace());
            j.setMsg("分类管理更新失败");
        }
    } else {
        j.setMsg("分类管理添加成功");
        categoryService.saveCategory(category);
        systemService.addLog(j.getMsg(), Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
    }
    return j;
}


@RequestMapping(params = "tree")
@ResponseBody
public List<ComboTree> tree(String selfCode,ComboTree comboTree,boolean isNew){
    CriteriaQuery cq = new CriteriaQuery(TSCategoryEntity.class);
    if (StringUtils.isNotEmpty(comboTree.getId())) {
        cq.createAlias("parent", "parent");
        cq.eq("parent.code", comboTree.getId());
    } else if (StringUtils.isNotEmpty(selfCode)) {
        cq.eq("code", selfCode);
    } else {
        cq.isNull("parent");
    }
    cq.add();
    List<TSCategoryEntity> categoryList = systemService.getListByCriteriaQuery(cq, false);
    List<ComboTree> comboTrees = new ArrayList<ComboTree>();
    for (int i = 0; i < categoryList.size(); i++) {
        comboTrees.add(categoryConvertToTree(categoryList.get(i)));
    }
    return comboTrees;
}


@RequestMapping(params = "del")
@ResponseBody
public AjaxJson del(TSCategoryEntity tSCategory,HttpServletRequest request){
    AjaxJson j = new AjaxJson();
    tSCategory = systemService.getEntity(TSCategoryEntity.class, tSCategory.getId());
    j.setMsg("分类管理删除成功");
    categoryService.delete(tSCategory);
    systemService.addLog(j.getMsg(), Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
    return j;
}


@SuppressWarnings("unchecked")
@RequestMapping(params = "datagrid")
@ResponseBody
public List<TreeGrid> datagrid(TSCategoryEntity category,HttpServletRequest request,HttpServletResponse response,DataGrid dataGrid){
    CriteriaQuery cq = new CriteriaQuery(TSCategoryEntity.class, dataGrid);
    if (category.getId() == null || StringUtils.isEmpty(category.getId())) {
        cq.isNull("parent");
    } else {
        cq.eq("parent.code", category.getId());
        category.setId(null);
    }
    // 查询条件组装器
    org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, category, request.getParameterMap());
    List<TSCategoryEntity> list = this.categoryService.getListByCriteriaQuery(cq, false);
    List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
    TreeGridModel treeGridModel = new TreeGridModel();
    treeGridModel.setIdField("code");
    treeGridModel.setSrc("id");
    treeGridModel.setTextField("name");
    treeGridModel.setIcon("icon_iconPath");
    treeGridModel.setParentText("parent_name");
    treeGridModel.setParentId("parent_code");
    treeGridModel.setChildList("list");
    treeGrids = systemService.treegrid(list, treeGridModel);
    return treeGrids;
}


@RequestMapping(params = "category")
public String category(HttpServletRequest request){
    return CATEGORY_LIST;
}


public ComboTree categoryConvertToTree(TSCategoryEntity entity){
    ComboTree tree = new ComboTree();
    tree.setId(entity.getCode());
    tree.setText(entity.getName());
    // update-begin--Author:zhoujf  Date:20180727 for：树控件报错问题--------------------
    if (entity.getIcon() != null) {
        tree.setIconCls(entity.getIcon().getIconClas());
    }
    // update-end--Author:zhoujf  Date:20150821 for：树控件报错问题--------------------
    if (entity.getList() != null && entity.getList().size() > 0) {
        List<ComboTree> comboTrees = new ArrayList<ComboTree>();
        for (int i = 0; i < entity.getList().size(); i++) {
            comboTrees.add(categoryConvertToTree(entity.getList().get(i)));
        }
        tree.setChildren(comboTrees);
    }
    return tree;
}


}