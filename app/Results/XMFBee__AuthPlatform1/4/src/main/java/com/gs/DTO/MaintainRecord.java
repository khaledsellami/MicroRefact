package com.gs.DTO;
 import java.util.Date;
import com.gs.Interface.Checkin;
public class MaintainRecord {

 private  String recordId;

 private  String checkinId;

 private  Date startTime;

 private  Date endTime;

 private  Date actualEndTime;

 private  Date recordCreatedTime;

 private  Date pickupTime;

 private  String recordDes;

 private  String recordStatus;

 private  String currentStatus;

 private  String ifConfirm;

 private  Checkin checkin;

 private  int count;

 private  Double total;

 private  Date todayTime;

 private  Double discountMoney;

 private RestTemplate restTemplate = new RestTemplate();

  String url = "http://1";


public String getCheckinId(){
    return checkinId;
}


public String getIfConfirm(){
    return ifConfirm;
}


public Date getRecordCreatedTime(){
    return recordCreatedTime;
}


public Date getEndTime(){
    return endTime;
}


public String getRecordStatus(){
    return recordStatus;
}


public String getCurrentStatus(){
    return currentStatus;
}


public Date getStartTime(){
    return startTime;
}


public int getCount(){
    return count;
}


public Date getTodayTime(){
    return todayTime;
}


public Date getPickupTime(){
    return pickupTime;
}


public Date getActualEndTime(){
    return actualEndTime;
}


public String getRecordDes(){
    return recordDes;
}


public String getRecordId(){
    return recordId;
}


public Double getTotal(){
    return total;
}


public Checkin getCheckin(){
    return checkin;
}


public Double getDiscountMoney(){
    return discountMoney;
}


public void setCheckinId(String checkinId){
    this.checkinId = checkinId;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setCheckinId"))

.queryParam("checkinId",checkinId)
;
restTemplate.put(builder.toUriString(),null);
}


public void setCurrentStatus(String currentStatus){
    this.currentStatus = currentStatus;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setCurrentStatus"))

.queryParam("currentStatus",currentStatus)
;
restTemplate.put(builder.toUriString(),null);
}


public void setRecordDes(String recordDes){
    this.recordDes = recordDes;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setRecordDes"))

.queryParam("recordDes",recordDes)
;
restTemplate.put(builder.toUriString(),null);
}


public void setIfConfirm(String ifConfirm){
    this.ifConfirm = ifConfirm;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setIfConfirm"))

.queryParam("ifConfirm",ifConfirm)
;
restTemplate.put(builder.toUriString(),null);
}


}