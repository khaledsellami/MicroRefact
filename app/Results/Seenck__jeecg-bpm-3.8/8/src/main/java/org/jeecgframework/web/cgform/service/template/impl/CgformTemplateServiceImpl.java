package org.jeecgframework.web.cgform.service.template.impl;
 import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.cgform.entity.template.CgformTemplateEntity;
import org.jeecgframework.web.cgform.service.template.CgformTemplateServiceI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
@Service("cgformTemplateService")
@Transactional
public class CgformTemplateServiceImpl extends CommonServiceImplimplements CgformTemplateServiceI{


@Override
public List<CgformTemplateEntity> getTemplateListByType(String type){
    // update--begin--author:zhoujf date:20170320 for:TASK 1796【online】online改造模板，不区分一对多和单表的，固化模板文件名字，不需要复杂配置
    // update--begin--author:zhangjiaqiang date:20170305 for:TASK #1749 【新功能】自定义样式表加个字段 【是否激活】
    String hql = "";
    if ("1".equals(type)) {
        hql = "from CgformTemplateEntity where templateType in ('1','3') and status = 1";
        return findHql(hql);
    } else if ("2".equals(type)) {
        hql = "from CgformTemplateEntity where templateType in ('2','3') and status = 1";
        return findHql(hql);
    } else {
        return null;
    }
// update--end--author:zhangjiaqiang date:20170305 for:TASK #1749 【新功能】自定义样式表加个字段 【是否激活】
// update--end--author:zhoujf date:20170320 for:TASK 1796【online】online改造模板，不区分一对多和单表的，固化模板文件名字，不需要复杂配置
}


@Override
public CgformTemplateEntity findByCode(String code){
    return findUniqueByProperty(CgformTemplateEntity.class, "templateCode", code);
}


public Serializable save(T entity){
    Serializable t = super.save(entity);
    // 执行新增操作配置的sql增强
    this.doAddSql((CgformTemplateEntity) entity);
    return t;
}


public boolean doDelSql(CgformTemplateEntity t){
    return true;
}


public boolean doUpdateSql(CgformTemplateEntity t){
    return true;
}


public boolean doAddSql(CgformTemplateEntity t){
    return true;
}


public String replaceVal(String sql,CgformTemplateEntity t){
    sql = sql.replace("#{id}", String.valueOf(t.getId()));
    sql = sql.replace("#{create_name}", String.valueOf(t.getCreateName()));
    sql = sql.replace("#{create_by}", String.valueOf(t.getCreateBy()));
    sql = sql.replace("#{create_date}", String.valueOf(t.getCreateDate()));
    sql = sql.replace("#{update_name}", String.valueOf(t.getUpdateName()));
    sql = sql.replace("#{update_by}", String.valueOf(t.getUpdateBy()));
    sql = sql.replace("#{update_date}", String.valueOf(t.getUpdateDate()));
    sql = sql.replace("#{sys_org_code}", String.valueOf(t.getSysOrgCode()));
    sql = sql.replace("#{sys_company_code}", String.valueOf(t.getSysCompanyCode()));
    sql = sql.replace("#{template_name}", String.valueOf(t.getTemplateName()));
    sql = sql.replace("#{template_code}", String.valueOf(t.getTemplateCode()));
    sql = sql.replace("#{template_type}", String.valueOf(t.getTemplateType()));
    sql = sql.replace("#{template_share}", String.valueOf(t.getTemplateShare()));
    sql = sql.replace("#{template_pic}", String.valueOf(t.getTemplatePic()));
    sql = sql.replace("#{template_comment}", String.valueOf(t.getTemplateComment()));
    sql = sql.replace("#{UUID}", UUID.randomUUID().toString());
    return sql;
}


public void delete(T entity){
    super.delete(entity);
    // 执行删除操作配置的sql增强
    this.doDelSql((CgformTemplateEntity) entity);
}


public void saveOrUpdate(T entity){
    super.saveOrUpdate(entity);
    // 执行更新操作配置的sql增强
    this.doUpdateSql((CgformTemplateEntity) entity);
}


}