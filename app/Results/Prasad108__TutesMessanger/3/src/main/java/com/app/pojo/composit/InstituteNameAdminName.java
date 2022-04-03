package com.app.pojo.composit;
 import java.util.Date;
public class InstituteNameAdminName {

 private  String instName;

 private  String instAddress;

 private  String instContactNo;

 private  String instEmail;

 private  Date instSubscriptionTill;

 private  String adminName;

 private  String adminSurname;

 private  String adminEmail;

 private  String adminContactNo;

public InstituteNameAdminName(String instName, String instAddress, String instContactNo, String instEmail, Date instSubscriptionTill, String adminName, String adminSurname, String adminEmail, String adminContactNo) {
    super();
    this.instName = instName;
    this.instAddress = instAddress;
    this.instContactNo = instContactNo;
    this.instEmail = instEmail;
    this.instSubscriptionTill = instSubscriptionTill;
    this.adminName = adminName;
    this.adminSurname = adminSurname;
    this.adminEmail = adminEmail;
    this.adminContactNo = adminContactNo;
}public InstituteNameAdminName() {
}
public String getInstName(){
    return instName;
}


public void setInstAddress(String instAddress){
    this.instAddress = instAddress;
}


public void setAdminContactNo(String adminContactNo){
    this.adminContactNo = adminContactNo;
}


public String getInstContactNo(){
    return instContactNo;
}


public void setInstContactNo(String instContactNo){
    this.instContactNo = instContactNo;
}


public Date getInstSubscriptionTill(){
    return instSubscriptionTill;
}


public void setInstEmail(String instEmail){
    this.instEmail = instEmail;
}


public void setInstSubscriptionTill(Date instSubscriptionTill){
    this.instSubscriptionTill = instSubscriptionTill;
}


public String getInstAddress(){
    return instAddress;
}


public void setAdminSurname(String adminSurname){
    this.adminSurname = adminSurname;
}


public String getAdminEmail(){
    return adminEmail;
}


public void setInstName(String instName){
    this.instName = instName;
}


public void setAdminName(String adminName){
    this.adminName = adminName;
}


public String getAdminSurname(){
    return adminSurname;
}


public String getAdminContactNo(){
    return adminContactNo;
}


public void setAdminEmail(String adminEmail){
    this.adminEmail = adminEmail;
}


public String getAdminName(){
    return adminName;
}


public String getInstEmail(){
    return instEmail;
}


}