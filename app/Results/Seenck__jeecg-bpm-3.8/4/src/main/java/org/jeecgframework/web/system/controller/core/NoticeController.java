package org.jeecgframework.web.system.controller.core;
 import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MutiLangUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSNotice;
import org.jeecgframework.web.system.pojo.base.TSNoticeAuthorityRole;
import org.jeecgframework.web.system.pojo.base.TSNoticeAuthorityUser;
import org.jeecgframework.web.system.pojo.base.TSNoticeReadUser;
import org.jeecgframework.web.system.pojo.base.TSRole;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.NoticeAuthorityRoleServiceI;
import org.jeecgframework.web.system.service.NoticeAuthorityUserServiceI;
import org.jeecgframework.web.system.service.NoticeService;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import Interface.SystemService;
import DTO.AjaxJson;
import DTO.CriteriaQuery;
import DTO.DataGrid;
@Controller
@RequestMapping("/noticeController")
public class NoticeController extends BaseController{

 private  SystemService systemService;

 private  ExecutorService executor;

@Autowired
 private  NoticeService noticeService;

@Autowired
 private  NoticeAuthorityRoleServiceI noticeAuthorityRoleService;

@Autowired
 private  NoticeAuthorityUserServiceI noticeAuthorityUserService;


@RequestMapping(params = "goUpdate")
public ModelAndView goUpdate(TSNotice tSNotice,HttpServletRequest req){
    if (StringUtil.isNotEmpty(tSNotice.getId())) {
        tSNotice = noticeService.getEntity(TSNotice.class, tSNotice.getId());
        if (tSNotice.getNoticeTerm() == null) {
            tSNotice.setNoticeTerm(new Date());
        }
        req.setAttribute("tSNoticePage", tSNotice);
        if (tSNotice.getNoticeLevel().equals("2")) {
            TSNoticeAuthorityRole role = new TSNoticeAuthorityRole();
            role.setNoticeId(tSNotice.getId());
            List<TSNoticeAuthorityRole> roles = systemService.findByExample(TSNoticeAuthorityRole.class.getName(), role);
            StringBuffer rolesid = new StringBuffer();
            StringBuffer rolesName = new StringBuffer();
            for (int i = 0; i < roles.size(); i++) {
                rolesid.append(roles.get(i).getRole().getId() + ",");
                rolesName.append(roles.get(i).getRole().getRoleName() + ",");
            }
            req.setAttribute("rolesid", rolesid);
            req.setAttribute("rolesName", rolesName);
        } else if (tSNotice.getNoticeLevel().equals("3")) {
            TSNoticeAuthorityUser user = new TSNoticeAuthorityUser();
            user.setNoticeId(tSNotice.getId());
            List<TSNoticeAuthorityUser> users = systemService.findByExample(TSNoticeAuthorityUser.class.getName(), user);
            StringBuffer usersid = new StringBuffer();
            StringBuffer usersName = new StringBuffer();
            for (int i = 0; i < users.size(); i++) {
                usersid.append(users.get(i).getUser().getId() + ",");
                usersName.append(users.get(i).getUser().getUserName() + ",");
            }
            req.setAttribute("usersid", usersid);
            req.setAttribute("usersName", usersName);
        }
    }
    return new ModelAndView("system/notice/tSNotice-update");
}


@RequestMapping(params = "noticeList")
public ModelAndView noticeList(HttpServletRequest request){
    return new ModelAndView("system/notice/noticeList");
}


@RequestMapping(params = "doAdd")
@ResponseBody
public AjaxJson doAdd(TSNotice tSNotice,HttpServletRequest request){
    String message = null;
    AjaxJson j = new AjaxJson();
    message = "通知公告添加成功";
    try {
        Serializable noticeSerializable = noticeService.save(tSNotice);
        if ("1".equals(tSNotice.getNoticeLevel())) {
            // 全员进行授权
            final String noticeId = noticeSerializable.toString();
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    List<TSUser> userList = systemService.findHql("from TSUser");
                    for (TSUser user : userList) {
                        String hql = "from TSNoticeReadUser where noticeId = ? and userId = ?";
                        List<TSNoticeReadUser> noticeReadList = systemService.findHql(hql, noticeId, user.getId());
                        if (noticeReadList == null || noticeReadList.isEmpty()) {
                            TSNoticeReadUser readUser = new TSNoticeReadUser();
                            readUser.setCreateTime(new Date());
                            readUser.setNoticeId(noticeId);
                            readUser.setUserId(user.getId());
                            systemService.save(readUser);
                        } else {
                            for (TSNoticeReadUser readUser : noticeReadList) {
                                if (readUser.getDelFlag() == 1) {
                                    readUser.setDelFlag(0);
                                    systemService.updateEntitie(readUser);
                                }
                            }
                        }
                    }
                    userList.clear();
                }
            });
        }
        if ("2".equals(tSNotice.getNoticeLevel())) {
            clearRole(tSNotice.getId(), request);
            String[] roleid = request.getParameter("roleid").split(",");
            for (int i = 0; i < roleid.length; i++) {
                TSNoticeAuthorityRole noticeAuthorityRole = new TSNoticeAuthorityRole();
                noticeAuthorityRole.setNoticeId(tSNotice.getId());
                TSRole role = new TSRole();
                role.setId(roleid[i]);
                noticeAuthorityRole.setRole(role);
                this.noticeAuthorityRoleService.saveTSNoticeAuthorityRole(noticeAuthorityRole);
            }
        } else if ("3".endsWith(tSNotice.getNoticeLevel())) {
            clearUser(tSNotice.getId(), request);
            String[] userid = request.getParameter("userid").split(",");
            for (int i = 0; i < userid.length; i++) {
                TSNoticeAuthorityUser noticeAuthorityUser = new TSNoticeAuthorityUser();
                noticeAuthorityUser.setNoticeId(tSNotice.getId());
                TSUser tsUser = new TSUser();
                tsUser.setId(userid[i]);
                noticeAuthorityUser.setUser(tsUser);
                this.noticeAuthorityUserService.saveNoticeAuthorityUser(noticeAuthorityUser);
            }
        }
        systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
    } catch (Exception e) {
        e.printStackTrace();
        message = "通知公告添加失败";
        throw new BusinessException(e.getMessage());
    }
    j.setMsg(message);
    return j;
}


@RequestMapping(params = "updateNoticeRead")
@ResponseBody
public AjaxJson updateNoticeRead(String noticeId,HttpServletRequest req){
    AjaxJson j = new AjaxJson();
    try {
    // TSUser user = ResourceUtil.getSessionUser();
    // TSNoticeReadUser readUser = new TSNoticeReadUser();
    // readUser.setNoticeId(noticeId);
    // readUser.setUserId(user.getId());
    // readUser.setCreateTime(new Date());
    // systemService.saveOrUpdate(readUser);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return j;
}


@RequestMapping(params = "datagrid2")
public void datagrid2(TSNotice tSNotice,HttpServletRequest request,HttpServletResponse response,DataGrid dataGrid){
    CriteriaQuery cq = new CriteriaQuery(TSNotice.class, dataGrid);
    // 查询条件组装器
    org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tSNotice, request.getParameterMap());
    try {
    // 自定义追加查询条件
    } catch (Exception e) {
        throw new BusinessException(e.getMessage());
    }
    cq.add();
    this.noticeService.getDataGridReturn(cq, true);
    TagUtil.datagrid(response, dataGrid);
}


@RequestMapping(params = "doBatchDel")
@ResponseBody
public AjaxJson doBatchDel(String ids,HttpServletRequest request){
    String message = null;
    AjaxJson j = new AjaxJson();
    message = "通知公告删除成功";
    try {
        for (String id : ids.split(",")) {
            TSNotice tSNotice = systemService.getEntity(TSNotice.class, id);
            noticeService.delete(tSNotice);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        }
    } catch (Exception e) {
        e.printStackTrace();
        message = "通知公告删除失败";
        throw new BusinessException(e.getMessage());
    }
    j.setMsg(message);
    return j;
}


@RequestMapping(params = "goAdd")
public ModelAndView goAdd(TSNotice tSNotice,HttpServletRequest req){
    if (StringUtil.isNotEmpty(tSNotice.getId())) {
        tSNotice = noticeService.getEntity(TSNotice.class, tSNotice.getId());
        req.setAttribute("tSNoticePage", tSNotice);
    }
    return new ModelAndView("system/notice/tSNotice-add");
}


@Override
public void run(){
    List<TSUser> userList = systemService.findHql("from TSUser");
    for (TSUser user : userList) {
        String hql = "from TSNoticeReadUser where noticeId = ? and userId = ?";
        List<TSNoticeReadUser> noticeReadList = systemService.findHql(hql, noticeId, user.getId());
        if (noticeReadList == null || noticeReadList.isEmpty()) {
            TSNoticeReadUser readUser = new TSNoticeReadUser();
            readUser.setCreateTime(new Date());
            readUser.setNoticeId(noticeId);
            readUser.setUserId(user.getId());
            systemService.save(readUser);
        } else {
            for (TSNoticeReadUser readUser : noticeReadList) {
                if (readUser.getDelFlag() == 1) {
                    readUser.setDelFlag(0);
                    systemService.updateEntitie(readUser);
                }
            }
        }
    }
    userList.clear();
}


@Autowired
public void setSystemService(SystemService systemService){
    this.systemService = systemService;
}


@RequestMapping(params = "tSNotice")
public ModelAndView tSNotice(HttpServletRequest request){
    return new ModelAndView("system/notice/tSNoticeList");
}


@RequestMapping(params = "doDel")
@ResponseBody
public AjaxJson doDel(TSNotice tSNotice,HttpServletRequest request){
    String message = null;
    AjaxJson j = new AjaxJson();
    tSNotice = systemService.getEntity(TSNotice.class, tSNotice.getId());
    message = "通知公告删除成功";
    try {
        if ("2".equals(tSNotice.getNoticeLevel())) {
            String sql = "delete from t_s_notice_authority_role where notice_id = ?";
            systemService.executeSql(sql, tSNotice.getId());
        } else if ("3".equals(tSNotice.getNoticeLevel())) {
            String sql = "delete from t_s_notice_authority_user where notice_id = ?";
            systemService.executeSql(sql, tSNotice.getId());
        }
        String sql = "delete from t_s_notice_read_user where notice_id = ?";
        systemService.executeSql(sql, tSNotice.getId());
        noticeService.delete(tSNotice);
        systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
    } catch (Exception e) {
        e.printStackTrace();
        message = "通知公告删除失败";
        throw new BusinessException(e.getMessage());
    }
    j.setMsg(message);
    return j;
}


@RequestMapping(params = "goNotice")
public ModelAndView noticeInfo(TSNotice notice,HttpServletRequest request){
    if (StringUtil.isNotEmpty(notice.getId())) {
        notice = this.systemService.getEntity(TSNotice.class, notice.getId());
        request.setAttribute("notice", notice);
        TSUser user = ResourceUtil.getSessionUser();
        String hql = "from TSNoticeReadUser where noticeId = ? and userId = ?";
        List<TSNoticeReadUser> noticeReadList = systemService.findHql(hql, notice.getId(), user.getId());
        if (noticeReadList != null && !noticeReadList.isEmpty()) {
            TSNoticeReadUser readUser = noticeReadList.get(0);
            if (readUser.getIsRead() == 0) {
                readUser.setIsRead(1);
                systemService.saveOrUpdate(readUser);
            }
        }
    }
    return new ModelAndView("system/notice/noticeinfo");
}


public void clearUser(String id,HttpServletRequest request){
    TSNoticeAuthorityUser user = new TSNoticeAuthorityUser();
    user.setNoticeId(id);
    List<TSNoticeAuthorityUser> users = systemService.findByExample(TSNoticeAuthorityUser.class.getName(), user);
    for (int i = 0; i < users.size(); i++) {
        this.noticeAuthorityUserService.doDelNoticeAuthorityUser(users.get(i));
    }
}


@RequestMapping(params = "getNoticeList")
@ResponseBody
public AjaxJson getNoticeList(Integer isRead,HttpServletRequest req){
    AjaxJson j = new AjaxJson();
    try {
        TSUser user = ResourceUtil.getSessionUser();
        // update-begin-author:taoYan date:20180802 for:TASK #3040 【sql注入问题】公告功能未修改
        String sql = "SELECT notice.*,noticeRead.is_read as is_read FROM t_s_notice notice " + "LEFT JOIN t_s_notice_read_user noticeRead ON  notice.id = noticeRead.notice_id " + "WHERE noticeRead.del_flag = 0 and noticeRead.user_id = ? ";
        sql += " and noticeRead.is_read = ? ";
        sql += " ORDER BY noticeRead.create_time DESC ";
        if (isRead == null || !(isRead == 1 || isRead == 0)) {
            isRead = 0;
        }
        List<Map<String, Object>> noticeList = systemService.findForJdbcParam(sql, 1, 10, user.getId(), isRead.intValue());
        // update-end-author:taoYan date:20180802 for:TASK #3040 【sql注入问题】公告功能未修改
        // 将List转换成JSON存储
        JSONArray result = new JSONArray();
        if (noticeList != null && noticeList.size() > 0) {
            for (int i = 0; i < noticeList.size(); i++) {
                JSONObject jsonParts = new JSONObject();
                jsonParts.put("id", noticeList.get(i).get("id"));
                jsonParts.put("noticeTitle", noticeList.get(i).get("notice_title"));
                result.add(jsonParts);
            }
        }
        Map<String, Object> attrs = new HashMap<String, Object>();
        attrs.put("noticeList", result);
        String tip = MutiLangUtil.getLang("notice.tip");
        attrs.put("tip", tip);
        String seeAll = MutiLangUtil.getLang("notice.seeAll");
        attrs.put("seeAll", seeAll);
        j.setAttributes(attrs);
        // update-begin-author:taoYan date:20180802 for:TASK #3040 【sql注入问题】公告功能未修改
        // 获取通知公告总数
        String sql2 = "SELECT count(notice.id) FROM t_s_notice notice " + "LEFT JOIN t_s_notice_read_user noticeRead ON  notice.id = noticeRead.notice_id " + "WHERE noticeRead.del_flag = 0 and noticeRead.user_id = ? " + "and noticeRead.is_read = 0";
        List<Map<String, Object>> resultList2 = systemService.findForJdbc(sql2, user.getId());
        // update-end-author:taoYan date:20180802 for:TASK #3040 【sql注入问题】公告功能未修改
        Object count = resultList2.get(0).get("count");
        j.setObj(count);
    } catch (Exception e) {
        j.setSuccess(false);
        e.printStackTrace();
    }
    return j;
}


@RequestMapping(params = "doUpdate")
@ResponseBody
public AjaxJson doUpdate(TSNotice tSNotice,HttpServletRequest request){
    String message = null;
    AjaxJson j = new AjaxJson();
    message = "通知公告更新成功";
    TSNotice t = noticeService.get(TSNotice.class, tSNotice.getId());
    try {
        if ("1".equals(tSNotice.getNoticeLevel()) && !t.getNoticeLevel().equals(tSNotice.getNoticeLevel())) {
            clearRole(tSNotice.getId(), request);
            clearUser(tSNotice.getId(), request);
            // 授权级别发生变更，之前为全员授权处理方案，此时应删除之前的数据信息
            final String noticeId = tSNotice.getId();
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    List<TSUser> userList = systemService.findHql("from TSUser");
                    for (TSUser user : userList) {
                        String hql = "from TSNoticeReadUser where noticeId = ? and userId = ?";
                        List<TSNoticeReadUser> noticeReadList = systemService.findHql(hql, noticeId, user.getId());
                        if (noticeReadList == null || noticeReadList.isEmpty()) {
                            TSNoticeReadUser readUser = new TSNoticeReadUser();
                            readUser.setCreateTime(new Date());
                            readUser.setNoticeId(noticeId);
                            readUser.setUserId(user.getId());
                            systemService.save(readUser);
                        } else {
                            for (TSNoticeReadUser readUser : noticeReadList) {
                                if (readUser.getDelFlag() == 1) {
                                    readUser.setDelFlag(0);
                                    systemService.updateEntitie(readUser);
                                }
                            }
                        }
                    }
                    userList.clear();
                }
            });
        } else if (!"1".equals(tSNotice.getNoticeLevel()) && "1".equals(t.getNoticeLevel())) {
            String sql = "delete from t_s_notice_read_user where notice_id = ?";
            systemService.executeSql(sql, t.getId());
        }
        MyBeanUtils.copyBeanNotNull2Bean(tSNotice, t);
        noticeService.saveOrUpdate(t);
        if ("2".endsWith(tSNotice.getNoticeLevel())) {
            clearRole(tSNotice.getId(), request);
            clearUser(tSNotice.getId(), request);
            String[] roleid = request.getParameter("roleid").split(",");
            for (int i = 0; i < roleid.length; i++) {
                TSNoticeAuthorityRole noticeAuthorityRole = new TSNoticeAuthorityRole();
                noticeAuthorityRole.setNoticeId(tSNotice.getId());
                TSRole role = new TSRole();
                role.setId(roleid[i]);
                noticeAuthorityRole.setRole(role);
                this.noticeAuthorityRoleService.saveTSNoticeAuthorityRole(noticeAuthorityRole);
            }
        } else if ("3".equals(tSNotice.getNoticeLevel())) {
            clearRole(tSNotice.getId(), request);
            clearUser(tSNotice.getId(), request);
            String[] userid = request.getParameter("userid").split(",");
            for (int i = 0; i < userid.length; i++) {
                TSNoticeAuthorityUser noticeAuthorityUser = new TSNoticeAuthorityUser();
                noticeAuthorityUser.setNoticeId(tSNotice.getId());
                TSUser tsUser = new TSUser();
                tsUser.setId(userid[i]);
                noticeAuthorityUser.setUser(tsUser);
                this.noticeAuthorityUserService.saveNoticeAuthorityUser(noticeAuthorityUser);
            }
        }
        systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
    } catch (Exception e) {
        e.printStackTrace();
        message = "通知公告更新失败";
        throw new BusinessException(e.getMessage());
    }
    j.setMsg(message);
    return j;
}


@RequestMapping(params = "datagrid")
public void datagrid(TSNotice notice,HttpServletRequest request,HttpServletResponse response,DataGrid dataGrid){
    // CriteriaQuery cq = new CriteriaQuery(TSNotice.class, dataGrid);
    // //查询条件组装器
    // org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, notice, request.getParameterMap());
    // this.noticeService.getDataGridReturn(cq, true);
    // update-begin-author:taoYan date:20180802 for:TASK #3040 【sql注入问题】公告功能未修改
    TSUser user = ResourceUtil.getSessionUser();
    String sql = "SELECT notice.*,noticeRead.is_read as is_read FROM t_s_notice notice " + " LEFT JOIN t_s_notice_read_user noticeRead ON  notice.id = noticeRead.notice_id " + " WHERE noticeRead.del_flag = 0 and noticeRead.user_id = ? " + " ORDER BY noticeRead.is_read asc,noticeRead.create_time DESC ";
    List<Map<String, Object>> resultList = systemService.findForJdbcParam(sql, dataGrid.getPage(), dataGrid.getRows(), user.getId());
    // update-end-author:taoYan date:20180802 for:TASK #3040 【sql注入问题】公告功能未修改
    // 将List转换成JSON存储
    List<Map<String, Object>> noticeList = new ArrayList<Map<String, Object>>();
    if (resultList != null && resultList.size() > 0) {
        for (int i = 0; i < resultList.size(); i++) {
            Map<String, Object> obj = resultList.get(i);
            Map<String, Object> n = new HashMap<String, Object>();
            n.put("id", String.valueOf(obj.get("id")));
            n.put("noticeTitle", String.valueOf(obj.get("notice_title")));
            n.put("noticeContent", String.valueOf(obj.get("notice_content")));
            n.put("createTime", String.valueOf(obj.get("create_time")));
            n.put("isRead", String.valueOf(obj.get("is_read")));
            noticeList.add(n);
        }
    }
    dataGrid.setResults(noticeList);
    // update-begin-author:taoYan date:20180802 for:TASK #3040 【sql注入问题】公告功能未修改
    String getCountSql = "SELECT count(notice.id) as count FROM t_s_notice notice LEFT JOIN t_s_notice_read_user noticeRead ON  notice.id = noticeRead.notice_id " + "WHERE noticeRead.del_flag = 0 and noticeRead.user_id = ? and noticeRead.is_read = 0";
    List<Map<String, Object>> resultList2 = systemService.findForJdbc(getCountSql, user.getId());
    // update-end-author:taoYan date:20180802 for:TASK #3040 【sql注入问题】公告功能未修改
    Object count = resultList2.get(0).get("count");
    dataGrid.setTotal(Integer.valueOf(count.toString()));
    TagUtil.datagrid(response, dataGrid);
}


public void clearRole(String id,HttpServletRequest request){
    TSNoticeAuthorityRole role = new TSNoticeAuthorityRole();
    role.setNoticeId(id);
    List<TSNoticeAuthorityRole> roles = systemService.findByExample(TSNoticeAuthorityRole.class.getName(), role);
    for (int i = 0; i < roles.size(); i++) {
        this.noticeAuthorityRoleService.doDelTSNoticeAuthorityRole(roles.get(i));
    }
}


}