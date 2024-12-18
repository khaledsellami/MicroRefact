package com.wxcrm.member;
 import javax.persistence.GenerationType.IDENTITY;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import com.wxcrm.util.DateUtil;
import com.wxcrm.weixin.pojo.WeixinUser;
@Entity
@Table(name = "lz_wei_member", uniqueConstraints = @UniqueConstraint(columnNames = "WMB_CARD_ID"))
public class LzWeiMember {

 private  Integer wmbId;

 private  Integer wmbWecId;

 private  Integer wmbWcsId;

 private  String wmbCardId;

 private  String wmbOpenid;

 private  String wmbType;

 private  String wmbName;

 private  String wmbMobule;

 private  String wmbStatus;

 private  String wmbDesc;

 private  Integer wmbRegistor;

 private  String wmbRegistdate;

 private  Integer wmbWecId_Q;

 private  String currentPage;

 private  String pageSize;

 private  String wmbOpenid_Q;

 private  String wmbType_Q;

 private  String wmbName_Q;

 private  String wmbMobule_Q;

 private  String[] wmbIds;

// Constructors
/**
 * default constructor
 */
public LzWeiMember() {
}/**
 * minimal constructor
 */
public LzWeiMember(Integer wmbWecId, String wmbCardId, String wmbOpenid) {
    this.wmbWecId = wmbWecId;
    this.wmbCardId = wmbCardId;
    this.wmbOpenid = wmbOpenid;
}/**
 * full constructor
 */
public LzWeiMember(Integer wmbWecId, String wmbCardId, String wmbOpenid, String wmbType, String wmbName, String wmbMobule, String wmbStatus, String wmbDesc, Integer wmbRegistor, String wmbRegistdate) {
    this.wmbWecId = wmbWecId;
    this.wmbCardId = wmbCardId;
    this.wmbOpenid = wmbOpenid;
    this.wmbType = wmbType;
    this.wmbName = wmbName;
    this.wmbMobule = wmbMobule;
    this.wmbStatus = wmbStatus;
    this.wmbDesc = wmbDesc;
    this.wmbRegistor = wmbRegistor;
    this.wmbRegistdate = wmbRegistdate;
}public LzWeiMember(String wmbOpenid, Integer wmbWecId, Integer wmbWcsId) {
    this.wmbWecId = wmbWecId;
    this.wmbOpenid = wmbOpenid;
    // 
    this.wmbType = "1";
    // 
    this.wmbStatus = "1000";
    this.wmbDesc = "ϵͳĬ�����";
    this.wmbRegistor = 1;
    this.wmbRegistdate = DateUtil.parseString(new Date(), "yyyy-MM-dd HH:mm:ss");
    this.wmbWcsId = wmbWcsId;
}public LzWeiMember(String wmbOpenid, Integer wmbWecId, Integer wmbWcsId, WeixinUser user) {
    this.wmbWecId = wmbWecId;
    this.wmbOpenid = wmbOpenid;
    // 
    this.wmbType = "1";
    // 
    this.wmbStatus = "1000";
    this.wmbDesc = "ϵͳĬ�����";
    this.wmbRegistor = 1;
    this.wmbRegistdate = DateUtil.parseString(new Date(), "yyyy-MM-dd HH:mm:ss");
    this.wmbWcsId = wmbWcsId;
    this.wmbName = user.getNickname();
}
public void setWmbWcsId(Integer wmbWcsId){
    this.wmbWcsId = wmbWcsId;
}


public void setWmbCardId(String wmbCardId){
    this.wmbCardId = wmbCardId;
}


@Transient
public String getWmbOpenid_Q(){
    return wmbOpenid_Q;
}


@Transient
public Integer getWmbWecId_Q(){
    return wmbWecId_Q;
}


public void setWmbMobule_Q(String wmbMobule_Q){
    this.wmbMobule_Q = wmbMobule_Q;
}


public void setWmbIds(String[] wmbIds){
    this.wmbIds = wmbIds;
}


@Column(name = "WMB_REGISTDATE", length = 19)
public String getWmbRegistdate(){
    return this.wmbRegistdate;
}


@Column(name = "WMB_MOBULE", length = 20)
public String getWmbMobule(){
    return this.wmbMobule;
}


@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "WMB_ID", unique = true, nullable = false)
public Integer getWmbId(){
    return this.wmbId;
}


@Column(name = "WMB_TYPE", length = 20)
public String getWmbType(){
    return this.wmbType;
}


@Transient
public String[] getWmbIds(){
    return wmbIds;
}


@Transient
public String getCurrentPage(){
    return currentPage;
}


@Column(name = "WMB_STATUS", length = 20)
public String getWmbStatus(){
    return this.wmbStatus;
}


public void setWmbStatus(String wmbStatus){
    this.wmbStatus = wmbStatus;
}


@Column(name = "WMB_WEC_ID")
public Integer getWmbWecId(){
    return this.wmbWecId;
}


public void setWmbMobule(String wmbMobule){
    this.wmbMobule = wmbMobule;
}


public void setWmbWecId(Integer wmbWecId){
    this.wmbWecId = wmbWecId;
}


@Column(name = "WMB_REGISTOR")
public Integer getWmbRegistor(){
    return this.wmbRegistor;
}


@Column(name = "WMB_NAME", length = 80)
public String getWmbName(){
    return this.wmbName;
}


@Column(name = "WMB_DESC", length = 200)
public String getWmbDesc(){
    return this.wmbDesc;
}


@Column(name = "WMB_CARD_ID", unique = true, length = 80)
public String getWmbCardId(){
    return this.wmbCardId;
}


public void setWmbRegistdate(String wmbRegistdate){
    this.wmbRegistdate = wmbRegistdate;
}


public void setCurrentPage(String currentPage){
    this.currentPage = currentPage;
}


@Column(name = "WEB_WCS_ID")
public Integer getWmbWcsId(){
    return wmbWcsId;
}


public void setWmbId(Integer wmbId){
    this.wmbId = wmbId;
}


@Transient
public String getWmbName_Q(){
    return wmbName_Q;
}


public void setWmbName_Q(String wmbName_Q){
    this.wmbName_Q = wmbName_Q;
}


public void setWmbDesc(String wmbDesc){
    this.wmbDesc = wmbDesc;
}


@Transient
public String getPageSize(){
    return pageSize;
}


@Column(name = "WMB_OPENID", nullable = false, length = 100)
public String getWmbOpenid(){
    return this.wmbOpenid;
}


public void setWmbOpenid(String wmbOpenid){
    this.wmbOpenid = wmbOpenid;
}


public void setWmbOpenid_Q(String wmbOpenid_Q){
    this.wmbOpenid_Q = wmbOpenid_Q;
}


public void setWmbWecId_Q(Integer wmbWecId_Q){
    this.wmbWecId_Q = wmbWecId_Q;
}


public void setWmbType(String wmbType){
    this.wmbType = wmbType;
}


@Transient
public String getWmbType_Q(){
    return wmbType_Q;
}


public void setWmbRegistor(Integer wmbRegistor){
    this.wmbRegistor = wmbRegistor;
}


public void setWmbName(String wmbName){
    this.wmbName = wmbName;
}


public void setPageSize(String pageSize){
    this.pageSize = pageSize;
}


public void setWmbType_Q(String wmbType_Q){
    this.wmbType_Q = wmbType_Q;
}


@Transient
public String getWmbMobule_Q(){
    return wmbMobule_Q;
}


}