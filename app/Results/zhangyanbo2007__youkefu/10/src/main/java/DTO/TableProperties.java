package DTO;
 import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.ukefu.util.UKTools;
public class TableProperties implements Cloneable{

 private  long serialVersionUID;

 private  String id;

 private  String tablename;

 private  String dbtableid;

 private  String name;

 private  String code;

 private  String fieldname;

 private  int datatypecode;

 private  String datatypename;

 private  String indexdatatype;

 private  String groupid;

 private  String userid;

 private  boolean pk;

 private  boolean modits;

 private  String orgi;

 private  String viewtype;

 private  int sortindex;

 private  boolean token;

 private  String tokentype;

 private  boolean inx;

 private  boolean title;

 private  boolean systemfield;

 private  int length;

 private  boolean fieldstatus;

 private  boolean secfield;

 private  String secdistype;

 private  boolean phonenumber;

 private  String phonetype;

 private  String phonememo;

 private  String styletype;

 private  boolean seldata;

 private  String seldatatype;

 private  String seldatacode;

 private  String seldatakey;

 private  String seldatavalue;

 private  String reftbid;

 private  String reftbname;

 private  String reftpid;

 private  String reftpname;

 private  String reftype;

 private  String reftptitlefield;

 private  boolean defaultsort;

 private  boolean descorder;

 private  String defaultvalue;

 private  String defaultvaluetitle;

 private  String defaultfieldvalue;

 private  boolean multpartfile;

 private  String uploadtype;

 private  String cascadetype;

 private  boolean impfield;

 private  boolean reffk;

 private  boolean privatefield;

 private  boolean sysfield;

 private  String plugin;

 private RestTemplate restTemplate = new RestTemplate();

  String url = "http://3";

public TableProperties() {
}public TableProperties(String fieldname, String datatypename, int datatypecode, String tablename) {
    this(fieldname, datatypename, datatypecode, tablename, null, null, false);
}public TableProperties(String fieldname, String datatypename, int datatypecode, String tablename, String orgi, String tableid, boolean fieldstatus) {
    this(fieldname, fieldname, datatypename, datatypecode, tablename, orgi, tableid, fieldstatus);
}public TableProperties(String fieldname, String datatypename, int datatypecode, String tablename, String orgi, String tableid, boolean fieldstatus, boolean token, String tokentype) {
    this(fieldname, fieldname, datatypename, datatypecode, tablename, orgi, tableid, fieldstatus);
    this.token = token;
    this.tokentype = tokentype;
}public TableProperties(String title, String fieldname, String datatypename, int datatypecode, String tablename, String orgi, String tableid, boolean fieldstatus) {
    if (fieldname != null) {
        fieldname = fieldname.toLowerCase();
    }
    if (tablename != null) {
        tablename = tablename.toLowerCase();
    }
    this.fieldname = fieldname;
    this.name = title;
    this.datatypecode = datatypecode;
    this.datatypename = datatypename;
    this.tablename = tablename;
    this.dbtableid = tableid;
    this.fieldstatus = fieldstatus;
    this.orgi = orgi;
}
public String getSeldatatype(){
    return seldatatype != null ? seldatatype : "";
}


public String getDatatypename(){
    return datatypename;
}


public String getTokentype(){
    return tokentype;
}


public String getCode(){
    return code;
}


public String getDefaultvaluetitle(){
    return defaultvaluetitle;
}


public String getDefaultvalue(){
    return defaultvalue;
}


public String getCascadetype(){
    return cascadetype;
}


public String getFieldname(){
    return fieldname != null ? fieldname.toLowerCase() : null;
}


public String getTablename(){
    return tablename;
}


public String getUserid(){
    return userid;
}


public String getUploadtype(){
    return uploadtype;
}


public String getReftbid(){
    return reftbid;
}


public String getReftpname(){
    return reftpname;
}


public String getPlugin(){
    return plugin;
}


public int getSortindex(){
    return sortindex;
}


public String getName(){
    return name;
}


public String getIndexdatatype(){
    return indexdatatype;
}


public String getSeldatakey(){
    return seldatakey;
}


public String getReftbname(){
    return reftbname;
}


public String getReftype(){
    return reftype;
}


public String getDefaultfieldvalue(){
    return defaultfieldvalue;
}


@Transient
public String getKey(){
    return UKTools.genIDByKey(this.id);
}


public String getStyletype(){
    return styletype;
}


public int getDatatypecode(){
    return datatypecode;
}


public String getSeldatacode(){
    return seldatacode;
}


public String getSecdistype(){
    return secdistype;
}


public String getDbtableid(){
    return dbtableid;
}


@Id
@Column(length = 32)
@GeneratedValue(generator = "system-uuid")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
public String getId(){
    return id;
}


public String getPhonememo(){
    return phonememo;
}


public String getPhonetype(){
    return phonetype;
}


public String getViewtype(){
    return viewtype;
}


public String getSeldatavalue(){
    return seldatavalue;
}


public String getGroupid(){
    return groupid;
}


public String getReftpid(){
    return reftpid;
}


public String getOrgi(){
    return orgi;
}


public String getReftptitlefield(){
    return reftptitlefield;
}


public int getLength(){
    return length;
}


public boolean isPhonenumber(){
    return phonenumber;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/isPhonenumber"))

;
boolean aux = restTemplate.getForObject(builder.toUriString(),boolean.class);
return aux;
}


public boolean isSecfield(){
    return secfield;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/isSecfield"))

;
boolean aux = restTemplate.getForObject(builder.toUriString(),boolean.class);
return aux;
}


public void setOrgi(String orgi){
    this.orgi = orgi;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setOrgi"))

.queryParam("orgi",orgi)
;
restTemplate.put(builder.toUriString(),null);
}


public void setDatatypecode(int datatypecode){
    this.datatypecode = datatypecode;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setDatatypecode"))

.queryParam("datatypecode",datatypecode)
;
restTemplate.put(builder.toUriString(),null);
}


public void setLength(int length){
    this.length = length;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setLength"))

.queryParam("length",length)
;
restTemplate.put(builder.toUriString(),null);
}


public void setDatatypename(String datatypename){
    this.datatypename = datatypename;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setDatatypename"))

.queryParam("datatypename",datatypename)
;
restTemplate.put(builder.toUriString(),null);
}


public void setName(String name){
    this.name = name;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setName"))

.queryParam("name",name)
;
restTemplate.put(builder.toUriString(),null);
}


public void setSysfield(boolean sysfield){
    this.sysfield = sysfield;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setSysfield"))

.queryParam("sysfield",sysfield)
;
restTemplate.put(builder.toUriString(),null);
}


}