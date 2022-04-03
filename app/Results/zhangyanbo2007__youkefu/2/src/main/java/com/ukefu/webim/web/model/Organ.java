package com.ukefu.webim.web.model;
 import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "uk_organ")
@org.hibernate.annotations.Proxy(lazy = false)
public class Organ {

 private  long serialVersionUID;

 private  String id;

 private  String name;

 private  String code;

 private  String parent;

 private  Date createtime;

 private  String creater;

 private  boolean skill;

 private  String area;

 private  String username;

 private  Date updatetime;

 private  String orgi;

 private  String orgid;


public void setName(String name){
    this.name = name;
}


public void setOrgid(String orgid){
    this.orgid = orgid;
}


public String getParent(){
    return parent;
}


public String getName(){
    return name;
}


public void setUsername(String username){
    this.username = username;
}


public String getOrgid(){
    return orgid;
}


public void setCode(String code){
    this.code = code;
}


public Date getUpdatetime(){
    return updatetime;
}


public void setArea(String area){
    this.area = area;
}


public void setUpdatetime(Date updatetime){
    this.updatetime = updatetime;
}


public boolean isSkill(){
    return skill;
}


public void setOrgi(String orgi){
    this.orgi = orgi;
}


@Id
@Column(length = 32)
@GeneratedValue(generator = "system-uuid")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
public String getId(){
    return id;
}


public String getUsername(){
    return username;
}


public Date getCreatetime(){
    return createtime;
}


public String getOrgi(){
    return orgi;
}


public void setId(String id){
    this.id = id;
}


public void setCreater(String creater){
    this.creater = creater;
}


public String getCreater(){
    return creater;
}


public void setCreatetime(Date createtime){
    this.createtime = createtime;
}


public void setParent(String parent){
    this.parent = parent;
}


public String getCode(){
    return code;
}


public void setSkill(boolean skill){
    this.skill = skill;
}


public String getArea(){
    return area;
}


}