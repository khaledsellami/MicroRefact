package org.jeecgframework.web.superquery.entity;
 import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;
@Entity
@Table(name = "super_query_history", schema = "")
@SuppressWarnings("serial")
public class SuperQueryHistoryEntity {

 private  java.lang.String id;

 private  java.lang.String createName;

 private  java.lang.String createBy;

 private  java.util.Date createDate;

 private  java.lang.String updateName;

 private  java.lang.String updateBy;

 private  java.util.Date updateDate;

 private  java.lang.String sysOrgCode;

 private  java.lang.String sysCompanyCode;

@Excel(name = "用户id", width = 15)
 private  java.lang.String userId;

@Excel(name = "查询规则编码", width = 15)
 private  java.lang.String queryCode;

@Excel(name = "查询类型", width = 15, dicCode = "sel_type")
 private  java.lang.String queryType;

@Excel(name = "记录", width = 15)
 private  java.lang.String record;

@Excel(name = "名称", width = 15)
 private  java.lang.String historyName;


public void setSysCompanyCode(java.lang.String sysCompanyCode){
    this.sysCompanyCode = sysCompanyCode;
}


public void setRecord(java.lang.String record){
    this.record = record;
}


@Column(name = "SYS_ORG_CODE", nullable = true, length = 50)
public java.lang.String getSysOrgCode(){
    return this.sysOrgCode;
}


@Column(name = "CREATE_NAME", nullable = true, length = 50)
public java.lang.String getCreateName(){
    return this.createName;
}


@Column(name = "history_name", nullable = true, length = 255)
public java.lang.String getHistoryName(){
    return historyName;
}


public void setUpdateName(java.lang.String updateName){
    this.updateName = updateName;
}


public void setHistoryName(java.lang.String historyName){
    this.historyName = historyName;
}


@Id
@GeneratedValue(generator = "paymentableGenerator")
@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
@Column(name = "ID", nullable = false, length = 36)
public java.lang.String getId(){
    return this.id;
}


@Column(name = "CREATE_DATE", nullable = true, length = 20)
public java.util.Date getCreateDate(){
    return this.createDate;
}


public void setQueryCode(java.lang.String queryCode){
    this.queryCode = queryCode;
}


@Column(name = "QUERY_TYPE", nullable = true, length = 50)
public java.lang.String getQueryType(){
    return this.queryType;
}


public void setCreateName(java.lang.String createName){
    this.createName = createName;
}


@Column(name = "UPDATE_DATE", nullable = true, length = 50)
public java.util.Date getUpdateDate(){
    return this.updateDate;
}


public void setId(java.lang.String id){
    this.id = id;
}


@Column(name = "UPDATE_BY", nullable = true, length = 50)
public java.lang.String getUpdateBy(){
    return this.updateBy;
}


@Column(name = "record", nullable = true, length = 255)
public java.lang.String getRecord(){
    return record;
}


public void setUpdateDate(java.util.Date updateDate){
    this.updateDate = updateDate;
}


public void setSysOrgCode(java.lang.String sysOrgCode){
    this.sysOrgCode = sysOrgCode;
}


public void setUpdateBy(java.lang.String updateBy){
    this.updateBy = updateBy;
}


public void setCreateBy(java.lang.String createBy){
    this.createBy = createBy;
}


@Column(name = "QUERY_CODE", nullable = true, length = 50)
public java.lang.String getQueryCode(){
    return this.queryCode;
}


public void setQueryType(java.lang.String queryType){
    this.queryType = queryType;
}


@Column(name = "UPDATE_NAME", nullable = true, length = 50)
public java.lang.String getUpdateName(){
    return this.updateName;
}


@Column(name = "CREATE_BY", nullable = true, length = 50)
public java.lang.String getCreateBy(){
    return this.createBy;
}


public void setCreateDate(java.util.Date createDate){
    this.createDate = createDate;
}


@Column(name = "SYS_COMPANY_CODE", nullable = true, length = 50)
public java.lang.String getSysCompanyCode(){
    return this.sysCompanyCode;
}


@Column(name = "user_id", nullable = true, length = 50)
public java.lang.String getUserId(){
    return userId;
}


public void setUserId(java.lang.String userId){
    this.userId = userId;
}


}