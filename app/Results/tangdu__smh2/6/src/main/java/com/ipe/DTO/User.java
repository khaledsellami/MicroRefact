package com.ipe.DTO;
 import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ipe.common.entity.IDEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence;
import java.sql.Timestamp;
import java.util.Collection;
public class User extends IDEntity{

 private  String userAccount;

 private  String userPwd;

 private  String enabled;

 private  String remark;

 private  String userName;

 private  String isAdmin;

 private  Timestamp createdDate;

 private  Timestamp updatedDate;

 private  String resourceId;

 private  Collection<UserRole> userRoles;


@Column(name = "user_account", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
@JSONField(serialize = false)
public String getUserAccount(){
    return userAccount;
}


@Column(name = "updated_date", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@LastModifiedDate
public Timestamp getUpdatedDate(){
    return updatedDate;
}


@JSONField(serialize = false)
@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
public Collection<UserRole> getUserRoles(){
    return userRoles;
}


@Column(name = "enabled_", nullable = true, insertable = true, updatable = true, length = 2, precision = 0)
public String getEnabled(){
    return enabled;
}


@Column(name = "remark_", nullable = true, insertable = true, updatable = true, length = 1000, precision = 0)
public String getRemark(){
    return remark;
}


@Column(name = "user_name", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
public String getUserName(){
    return userName;
}


@Column(name = "is_admin", nullable = true, insertable = true, updatable = true, length = 2, precision = 0)
@JSONField(serialize = false)
public String getAdmin(){
    return isAdmin;
}


@Column(name = "user_pwd", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
@JSONField(serialize = false)
public String getUserPwd(){
    return userPwd;
}


@Column(name = "created_date", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@CreatedDate
public Timestamp getCreatedDate(){
    return createdDate;
}


}