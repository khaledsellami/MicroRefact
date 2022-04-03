package cn.com.cnc.fcc.DTO;
 import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence;
import javax.validation.constraints;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
public class QmsEnclosure implements Serializable{

 private  long serialVersionUID;

 private  Long id;

 private  Integer inspectionInfoId;

 private  String inspectionKbn;

 private  String enclosureAddress;

 private  String makeUser;

 private  ZonedDateTime makeTime;

 private  String modifyUser;

 private  ZonedDateTime modifyTime;

 private RestTemplate restTemplate = new RestTemplate();

  String url = "http://13";


public Long getId(){
    return id;
}


public String getMakeUser(){
    return makeUser;
}


public QmsEnclosure modifyUser(String modifyUser){
    this.modifyUser = modifyUser;
    return this;
}


public ZonedDateTime getMakeTime(){
    return makeTime;
}


public void setId(Long id){
    this.id = id;
}


public Integer getInspectionInfoId(){
    return inspectionInfoId;
}


public String getInspectionKbn(){
    return inspectionKbn;
}


public void setMakeUser(String makeUser){
    this.makeUser = makeUser;
}


public ZonedDateTime getModifyTime(){
    return modifyTime;
}


public String getModifyUser(){
    return modifyUser;
}


public void setModifyTime(ZonedDateTime modifyTime){
    this.modifyTime = modifyTime;
}


public QmsEnclosure makeTime(ZonedDateTime makeTime){
    this.makeTime = makeTime;
    return this;
}


public String getEnclosureAddress(){
    return enclosureAddress;
}


@Override
public String toString(){
    return "QmsEnclosure{" + "id=" + getId() + ", inspectionInfoId=" + getInspectionInfoId() + ", inspectionKbn='" + getInspectionKbn() + "'" + ", enclosureAddress='" + getEnclosureAddress() + "'" + ", makeUser='" + getMakeUser() + "'" + ", makeTime='" + getMakeTime() + "'" + ", modifyUser='" + getModifyUser() + "'" + ", modifyTime='" + getModifyTime() + "'" + "}";
}


public void setEnclosureAddress(String enclosureAddress){
    this.enclosureAddress = enclosureAddress;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setEnclosureAddress"))

.queryParam("enclosureAddress",enclosureAddress)
;
restTemplate.put(builder.toUriString(),null);
}


public void setInspectionInfoId(Integer inspectionInfoId){
    this.inspectionInfoId = inspectionInfoId;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setInspectionInfoId"))

.queryParam("inspectionInfoId",inspectionInfoId)
;
restTemplate.put(builder.toUriString(),null);
}


public void setInspectionKbn(String inspectionKbn){
    this.inspectionKbn = inspectionKbn;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setInspectionKbn"))

.queryParam("inspectionKbn",inspectionKbn)
;
restTemplate.put(builder.toUriString(),null);
}


public void setMakeTime(ZonedDateTime makeTime){
    this.makeTime = makeTime;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setMakeTime"))

.queryParam("makeTime",makeTime)
;
restTemplate.put(builder.toUriString(),null);
}


public void setModifyUser(String modifyUser){
    this.modifyUser = modifyUser;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setModifyUser"))

.queryParam("modifyUser",modifyUser)
;
restTemplate.put(builder.toUriString(),null);
}


}