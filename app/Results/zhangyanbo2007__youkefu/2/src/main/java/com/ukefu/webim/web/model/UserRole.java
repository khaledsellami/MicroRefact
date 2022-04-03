package com.ukefu.webim.web.model;
 import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "uk_userrole")
@org.hibernate.annotations.Proxy(lazy = false)
public class UserRole {

 private  long serialVersionUID;

 private  String id;

 private  User user;

 private  Role role;

 private  String creater;

 private  String orgi;

 private  Date createtime;


public Date getCreatetime(){
    return createtime;
}


@OneToOne(optional = true)
public User getUser(){
    return user;
}


public void setRole(Role role){
    this.role = role;
}


@OneToOne(optional = true)
public Role getRole(){
    return role;
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


public String getCreater(){
    return creater;
}


public void setCreatetime(Date createtime){
    this.createtime = createtime;
}


public void setUser(User user){
    this.user = user;
}


}