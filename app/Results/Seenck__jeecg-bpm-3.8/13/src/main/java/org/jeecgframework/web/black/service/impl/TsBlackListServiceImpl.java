package org.jeecgframework.web.black.service.impl;
 import org.jeecgframework.web.black.service.TsBlackListServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.black.entity.TsBlackListEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.io.Serializable;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import DTO.CgformEnhanceJavaInter;
@Service("tsBlackListService")
@Transactional
public class TsBlackListServiceImpl extends CommonServiceImplimplements TsBlackListServiceI{


public void doDelBus(TsBlackListEntity t){
// -----------------sql增强 start----------------------------
// -----------------sql增强 end------------------------------
// -----------------java增强 start---------------------------
// -----------------java增强 end-----------------------------
}


public void executeJavaExtend(String cgJavaType,String cgJavaValue,Map<String,Object> data){
    if (StringUtil.isNotEmpty(cgJavaValue)) {
        Object obj = null;
        try {
            if ("class".equals(cgJavaType)) {
                // 因新增时已经校验了实例化是否可以成功，所以这块就不需要再做一次判断
                obj = MyClassLoader.getClassByScn(cgJavaValue).newInstance();
            } else if ("spring".equals(cgJavaType)) {
                obj = ApplicationContextUtil.getContext().getBean(cgJavaValue);
            }
            if (obj instanceof CgformEnhanceJavaInter) {
                CgformEnhanceJavaInter javaInter = (CgformEnhanceJavaInter) obj;
                javaInter.execute("ts_black_list", data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("执行JAVA增强出现异常！");
        }
    }
}


public Map<String,Object> populationMap(TsBlackListEntity t){
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("id", t.getId());
    map.put("create_name", t.getCreateName());
    map.put("create_by", t.getCreateBy());
    map.put("create_date", t.getCreateDate());
    map.put("update_name", t.getUpdateName());
    map.put("update_by", t.getUpdateBy());
    map.put("update_date", t.getUpdateDate());
    map.put("sys_org_code", t.getSysOrgCode());
    map.put("sys_company_code", t.getSysCompanyCode());
    map.put("bpm_status", t.getBpmStatus());
    map.put("ip", t.getIp());
    return map;
}


public Serializable save(TsBlackListEntity entity){
    Serializable t = super.save(entity);
    // 执行新增操作增强业务
    this.doAddBus(entity);
    return t;
}


public void doAddBus(TsBlackListEntity t){
// -----------------sql增强 start----------------------------
// -----------------sql增强 end------------------------------
// -----------------java增强 start---------------------------
// -----------------java增强 end-----------------------------
}


public String replaceVal(String sql,TsBlackListEntity t){
    sql = sql.replace("#{id}", String.valueOf(t.getId()));
    sql = sql.replace("#{create_name}", String.valueOf(t.getCreateName()));
    sql = sql.replace("#{create_by}", String.valueOf(t.getCreateBy()));
    sql = sql.replace("#{create_date}", String.valueOf(t.getCreateDate()));
    sql = sql.replace("#{update_name}", String.valueOf(t.getUpdateName()));
    sql = sql.replace("#{update_by}", String.valueOf(t.getUpdateBy()));
    sql = sql.replace("#{update_date}", String.valueOf(t.getUpdateDate()));
    sql = sql.replace("#{sys_org_code}", String.valueOf(t.getSysOrgCode()));
    sql = sql.replace("#{sys_company_code}", String.valueOf(t.getSysCompanyCode()));
    sql = sql.replace("#{bpm_status}", String.valueOf(t.getBpmStatus()));
    sql = sql.replace("#{ip}", String.valueOf(t.getIp()));
    sql = sql.replace("#{UUID}", UUID.randomUUID().toString());
    return sql;
}


public void delete(TsBlackListEntity entity){
    super.delete(entity);
    // 执行删除操作增强业务
    this.doDelBus(entity);
}


public void doUpdateBus(TsBlackListEntity t){
// -----------------sql增强 start----------------------------
// -----------------sql增强 end------------------------------
// -----------------java增强 start---------------------------
// -----------------java增强 end-----------------------------
}


public void saveOrUpdate(TsBlackListEntity entity){
    super.saveOrUpdate(entity);
    // 执行更新操作增强业务
    this.doUpdateBus(entity);
}


}