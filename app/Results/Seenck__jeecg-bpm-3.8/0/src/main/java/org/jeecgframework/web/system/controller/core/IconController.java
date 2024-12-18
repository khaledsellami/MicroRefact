package org.jeecgframework.web.system.controller.core;
 import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecgframework.core.util.MutiLangUtil;
import org.jeecgframework.web.system.pojo.base.TSFunction;
import org.jeecgframework.web.system.pojo.base.TSIcon;
import org.jeecgframework.web.system.pojo.base.TSOperation;
import org.jeecgframework.web.system.service.MutiLangServiceI;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.util.IconImageUtil;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.common.UploadFile;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import DTO.UploadFile;
@Controller
@RequestMapping("/iconController")
public class IconController extends BaseController{

 private  SystemService systemService;


@RequestMapping(params = "updateInfo")
@ResponseBody
public AjaxJson updateInfo(TSIcon icon,HttpServletRequest request){
    String message = null;
    AjaxJson j = new AjaxJson();
    try {
        TSIcon iconOld = systemService.getEntity(TSIcon.class, icon.getId());
        iconOld.setIconName(icon.getIconName());
        iconOld.setIconType(icon.getIconType());
        this.systemService.updateEntitie(iconOld);
        message = MutiLangUtil.paramUpdSuccess("common.icon");
        j.setMsg(message);
    } catch (Exception e) {
        message = MutiLangUtil.paramUpdFail("common.icon");
        j.setMsg(message + e.getMessage().toString());
    }
    return j;
}


@RequestMapping(params = "repair")
@ResponseBody
public AjaxJson repair(HttpServletRequest request){
    AjaxJson json = new AjaxJson();
    List<TSIcon> icons = systemService.loadAll(TSIcon.class);
    String rootpath = request.getSession().getServletContext().getRealPath("/");
    String csspath = request.getSession().getServletContext().getRealPath("/plug-in/accordion/css/icons.css");
    // 清空CSS文件内容
    clearFile(csspath);
    for (TSIcon c : icons) {
        File file = new File(rootpath + c.getIconPath());
        if (!file.exists()) {
            byte[] content = c.getIconContent();
            if (content != null) {
                BufferedImage imag = ImageIO.read(new ByteArrayInputStream(content));
                // 输出到 png 文件
                ImageIO.write(imag, "PNG", file);
            }
        }
        String css = "." + c.getIconClas() + "{background:url('../images/" + c.getIconClas() + "." + c.getExtend() + "') no-repeat}";
        write(request, css);
    }
    json.setMsg(MutiLangUtil.paramAddSuccess("common.icon.style"));
    json.setSuccess(true);
    return json;
}


@RequestMapping(params = "icon")
public ModelAndView icon(){
    return new ModelAndView("system/icon/iconList");
}


@SuppressWarnings("deprecation")
@RequestMapping(params = "update", method = RequestMethod.POST)
@ResponseBody
public AjaxJson update(HttpServletRequest request){
    String message = null;
    AjaxJson j = new AjaxJson();
    Short iconType = oConvertUtils.getShort(request.getParameter("iconType"));
    String iconName = java.net.URLDecoder.decode(oConvertUtils.getString(request.getParameter("iconName")));
    String id = request.getParameter("id");
    TSIcon icon = new TSIcon();
    if (StringUtil.isNotEmpty(id)) {
        icon = systemService.get(TSIcon.class, id);
        icon.setId(id);
    }
    icon.setIconName(iconName);
    icon.setIconType(iconType);
    systemService.saveOrUpdate(icon);
    // 图标的css样式
    String css = "." + icon.getIconClas() + "{background:url('../images/" + icon.getIconClas() + "." + icon.getExtend() + "') no-repeat}";
    write(request, css);
    message = "更新成功";
    j.setMsg(message);
    return j;
}


@RequestMapping(params = "del")
@ResponseBody
public AjaxJson del(TSIcon icon,HttpServletRequest request){
    String message = null;
    AjaxJson j = new AjaxJson();
    icon = systemService.getEntity(TSIcon.class, icon.getId());
    boolean isPermit = isPermitDel(icon);
    if (isPermit) {
        systemService.delete(icon);
        message = MutiLangUtil.paramDelSuccess("common.icon");
        systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        j.setMsg(message);
        return j;
    }
    message = MutiLangUtil.paramDelFail("common.icon,common.icon.isusing");
    j.setMsg(message);
    return j;
}


@RequestMapping(params = "saveOrUpdateIcon", method = RequestMethod.POST)
@ResponseBody
public AjaxJson saveOrUpdateIcon(HttpServletRequest request){
    String message = null;
    AjaxJson j = new AjaxJson();
    TSIcon icon = new TSIcon();
    Short iconType = oConvertUtils.getShort(request.getParameter("iconType"));
    String iconName = oConvertUtils.getString(request.getParameter("iconName"));
    String id = request.getParameter("id");
    icon.setId(id);
    icon.setIconName(iconName);
    icon.setIconType(iconType);
    // uploadFile.setBasePath("images/accordion");
    UploadFile uploadFile = new UploadFile(request, icon);
    uploadFile.setCusPath("plug-in/accordion/images");
    uploadFile.setExtend("extend");
    uploadFile.setTitleField("iconclas");
    uploadFile.setRealPath("iconPath");
    uploadFile.setObject(icon);
    uploadFile.setByteField("iconContent");
    uploadFile.setRename(false);
    systemService.uploadFile(uploadFile);
    // 图标的css样式
    String css = "." + icon.getIconClas() + "{background:url('../images/" + icon.getIconClas() + "." + icon.getExtend() + "') no-repeat}";
    write(request, css);
    message = MutiLangUtil.paramAddSuccess("common.icon");
    j.setMsg(message);
    return j;
}


@Autowired
public void setSystemService(SystemService systemService){
    this.systemService = systemService;
}


public boolean isPermitDel(TSIcon icon){
    List<TSFunction> functions = systemService.findByProperty(TSFunction.class, "TSIcon.id", icon.getId());
    if (functions == null || functions.isEmpty()) {
        return true;
    }
    return false;
}


public void clearFile(String path){
    try {
        FileOutputStream fos = new FileOutputStream(new File(path));
        fos.write("".getBytes());
        fos.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}


@RequestMapping(params = "addorupdate")
public ModelAndView addorupdate(TSIcon icon,HttpServletRequest req){
    if (StringUtil.isNotEmpty(icon.getId())) {
        icon = systemService.getEntity(TSIcon.class, icon.getId());
        req.setAttribute("icon", icon);
    }
    return new ModelAndView("system/icon/icons");
}


public void upEntity(TSIcon icon){
    List<TSFunction> functions = systemService.findByProperty(TSFunction.class, "TSIcon.id", icon.getId());
    if (functions.size() > 0) {
        for (TSFunction tsFunction : functions) {
            tsFunction.setTSIcon(null);
            systemService.saveOrUpdate(tsFunction);
        }
    }
    List<TSOperation> operations = systemService.findByProperty(TSOperation.class, "TSIcon.id", icon.getId());
    if (operations.size() > 0) {
        for (TSOperation tsOperation : operations) {
            tsOperation.setTSIcon(null);
            systemService.saveOrUpdate(tsOperation);
        }
    }
}


@SuppressWarnings("unchecked")
@RequestMapping(params = "datagrid")
public void datagrid(TSIcon icon,HttpServletRequest request,HttpServletResponse response,DataGrid dataGrid){
    CriteriaQuery cq = new CriteriaQuery(TSIcon.class, dataGrid);
    org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, icon);
    cq.add();
    this.systemService.getDataGridReturn(cq, true);
    // 先把数据库的byte存成图片到临时目录，再给每个TsIcon设置目录路径
    IconImageUtil.convertDataGrid(dataGrid, request);
    // update-begin--Author:zhoujf  Date:20150821 for：图标管理名称国际化问题--------------------
    List<TSIcon> list = dataGrid.getResults();
    for (TSIcon tsicon : list) {
        tsicon.setIconName(MutiLangUtil.doMutiLang(tsicon.getIconName(), ""));
    }
    // update-end--Author:zhoujf  Date:20150821 for：图标管理名称国际化问题--------------------
    TagUtil.datagrid(response, dataGrid);
}


public void write(HttpServletRequest request,String css){
    try {
        String path = request.getSession().getServletContext().getRealPath("/plug-in/accordion/css/icons.css");
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter out = new FileWriter(file, true);
        out.write("\r\n");
        out.write(css);
        out.close();
    } catch (Exception e) {
    }
}


}