package com.poseidon.DTO;
 import java.time.OffsetDateTime;
import java.util.StringJoiner;
public class InvoiceVO {

 private  Long id;

 private  Long transactionId;

 private  String tagNo;

 private  Long customerId;

 private  String customerName;

 private  String description;

 private  String serialNo;

 private  int quantity;

 private  Double rate;

 private  Double amount;

 private  Boolean startsWith;

 private  Boolean includes;

 private  Boolean greater;

 private  Boolean lesser;

 private  OffsetDateTime createdDate;

 private  OffsetDateTime modifiedDate;

 private  String createdBy;

 private  String modifiedBy;


public int getQuantity(){
    return quantity;
}


public Long getId(){
    return id;
}


public String getDescription(){
    return description;
}


public Long getCustomerId(){
    return customerId;
}


public Double getRate(){
    return rate;
}


public void setLesser(Boolean lesser){
    this.lesser = lesser;
}


public void setSerialNo(String serialNo){
    this.serialNo = serialNo;
}


public Boolean getLesser(){
    return lesser;
}


public OffsetDateTime getCreatedDate(){
    return createdDate;
}


public void setGreater(Boolean greater){
    this.greater = greater;
}


public String getSerialNo(){
    return serialNo;
}


public Double getAmount(){
    return amount;
}


public Boolean getStartsWith(){
    return startsWith;
}


public String getCustomerName(){
    return customerName;
}


public String getTagNo(){
    return tagNo;
}


public void setRate(Double rate){
    this.rate = rate;
}


public String getModifiedBy(){
    return modifiedBy;
}


public OffsetDateTime getModifiedDate(){
    return modifiedDate;
}


public Boolean getGreater(){
    return greater;
}


public Long getTransactionId(){
    return transactionId;
}


public void setQuantity(int quantity){
    this.quantity = quantity;
}


public void setTransactionId(Long transactionId){
    this.transactionId = transactionId;
}


public Boolean getIncludes(){
    return includes;
}


public void setCustomerId(Long customerId){
    this.customerId = customerId;
}


public void setIncludes(Boolean includes){
    this.includes = includes;
}


public String getCreatedBy(){
    return createdBy;
}


}