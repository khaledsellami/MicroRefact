package com.ukefu.webim.web.model;
 import javax.persistence.Transient;
import Interface.User;
public class ESBean {

 private  User user;


@Transient
public User getUser(){
    return user;
}


public void setUser(User user){
    this.user = user;
}


public boolean isDatastatus()


}