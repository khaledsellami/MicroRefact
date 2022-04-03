package com.ipe.DTO;
 import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ipe.common.entity.IDEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
public class Resource extends IDEntity{

 private  String resourceName;

 private  String resourceType;

 private  String resourceUrl;

 private  Timestamp createdDate;

 private  Timestamp updatedDate;

 private  Integer sno;

 private  String remark;

 private  String enabled;

 private  Collection<Authority> authorities;

 private  Resource parent;

 private  Set<Resource> rows;

 private  boolean leaf;

 private  boolean expanded;

 private  Boolean checked;

 private RestTemplate restTemplate = new RestTemplate();

  String url = "http://1";


@Column(name = "updated_date", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
@Basic
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
public Timestamp getUpdatedDate(){
    return updatedDate;
}


@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "parent", fetch = FetchType.LAZY)
@OrderBy(value = "sno asc")
public Set<Resource> getRows(){
    return rows;
}


@OneToMany(mappedBy = "resource")
public Collection<Authority> getAuthorities(){
    return authorities;
}


@Column(name = "resource_type", nullable = true, insertable = true, updatable = true, length = 2, precision = 0)
@Basic
public String getResourceType(){
    return resourceType;
}


@Column(name = "remark_", nullable = true, insertable = true, updatable = true, length = 1000, precision = 0)
@Basic
public String getRemark(){
    return remark;
}


@Column(name = "enabled")
@Basic
public String getEnabled(){
    return enabled;
}


@Column(name = "created_date", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
@Basic
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
public Timestamp getCreatedDate(){
    return createdDate;
}


@Column(name = "resource_url", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
@Basic
public String getResourceUrl(){
    return resourceUrl;
}


@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "pid")
public Resource getParent(){
    return parent;
}


@Column(name = "resource_name", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
@Basic
public String getResourceName(){
    return resourceName;
}


@Column(name = "sno_", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
@Basic
public Integer getSno(){
    return sno;
}


@Transient
public Boolean getChecked(){
    return checked;
}


public void setParent(Resource parent){
    this.parent = parent;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setParent"))

.queryParam("parent",parent)
;
restTemplate.put(builder.toUriString(),null);
}


public void setSno(Integer sno){
    this.sno = sno;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setSno"))

.queryParam("sno",sno)
;
restTemplate.put(builder.toUriString(),null);
}


public void setCreatedDate(Timestamp createdDate){
    this.createdDate = createdDate;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setCreatedDate"))

.queryParam("createdDate",createdDate)
;
restTemplate.put(builder.toUriString(),null);
}


}