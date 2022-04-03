package com.sobey.cmop.mvc.DTO;
 import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
public class Apply {

 private  Integer id;

 private  User user;

 private  String title;

 private  String serviceTag;

 private  Integer serviceType;

 private  Integer priority;

 private  String description;

 private  String serviceStart;

 private  String serviceEnd;

 private  Date createTime;

 private  Integer status;

 private  AuditFlow auditFlow;

 private  Integer redmineIssueId;

 private  Set<ComputeItem> computeItems;

 private  Set<StorageItem> storageItems;

 private  Set<NetworkEipItem> networkEipItems;

 private  Set<NetworkElbItem> networkElbItems;

 private  Set<NetworkDnsItem> networkDnsItems;

 private  Set<MonitorCompute> monitorComputes;

 private  Set<MonitorElb> monitorElbs;

 private  Set<Audit> audits;

 private  Set<MonitorMail> monitorMails;

 private  Set<MonitorPhone> monitorPhones;

 private  Set<MdnItem> mdnItems;

 private  Set<CpItem> cpItems;

 private RestTemplate restTemplate = new RestTemplate();

  String url = "http://1";

// Constructors
/**
 * default constructor
 */
public Apply() {
}/**
 * minimal constructor
 */
public Apply(User user, String title, String serviceTag, Integer serviceType, Integer priority, String description, String serviceStart, String serviceEnd, Date createTime, Integer status) {
    this.user = user;
    this.title = title;
    this.serviceTag = serviceTag;
    this.serviceType = serviceType;
    this.priority = priority;
    this.description = description;
    this.serviceStart = serviceStart;
    this.serviceEnd = serviceEnd;
    this.createTime = createTime;
    this.status = status;
}/**
 * full constructor
 */
public Apply(User user, String title, String serviceTag, Integer serviceType, Integer priority, String description, String serviceStart, String serviceEnd, Date createTime, Integer status, Integer redmineIssueId, AuditFlow auditFlow, Set<StorageItem> storageItems, Set<NetworkEipItem> networkEipItems, Set<ComputeItem> computeItems, Set<NetworkElbItem> networkElbItems, Set<NetworkDnsItem> networkDnsItems, Set<MonitorCompute> monitorComputes, Set<MonitorElb> monitorElbs, Set<Audit> audits, Set<MonitorMail> monitorMails, Set<MonitorPhone> monitorPhones, Set<MdnItem> mdnItems, Set<CpItem> cpItems) {
    this.user = user;
    this.title = title;
    this.serviceTag = serviceTag;
    this.serviceType = serviceType;
    this.priority = priority;
    this.description = description;
    this.serviceStart = serviceStart;
    this.serviceEnd = serviceEnd;
    this.createTime = createTime;
    this.status = status;
    this.auditFlow = auditFlow;
    this.redmineIssueId = redmineIssueId;
    this.storageItems = storageItems;
    this.networkEipItems = networkEipItems;
    this.computeItems = computeItems;
    this.networkElbItems = networkElbItems;
    this.networkDnsItems = networkDnsItems;
    this.monitorComputes = monitorComputes;
    this.monitorElbs = monitorElbs;
    this.audits = audits;
    this.monitorMails = monitorMails;
    this.monitorPhones = monitorPhones;
    this.mdnItems = mdnItems;
    this.cpItems = cpItems;
}
@Column(name = "create_time", nullable = false, length = 19)
public Date getCreateTime(){
    return this.createTime;
}


@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
public Set<MonitorElb> getMonitorElbs(){
    return this.monitorElbs;
}


@Column(name = "status", nullable = false)
public Integer getStatus(){
    return this.status;
}


@Column(name = "title", nullable = false, length = 20)
public String getTitle(){
    return this.title;
}


@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "audit_flow_id")
public AuditFlow getAuditFlow(){
    return this.auditFlow;
}


@Column(name = "service_start", nullable = false, length = 10)
public String getServiceStart(){
    return this.serviceStart;
}


@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
public Set<NetworkElbItem> getNetworkElbItems(){
    return this.networkElbItems;
}


@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
public Set<MdnItem> getMdnItems(){
    return mdnItems;
}


@Column(name = "service_end", nullable = false, length = 10)
public String getServiceEnd(){
    return this.serviceEnd;
}


@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
public Set<CpItem> getCpItems(){
    return cpItems;
}


@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
public Set<MonitorPhone> getMonitorPhones(){
    return monitorPhones;
}


public Integer getServiceType(){
    return serviceType;
}


@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
public Set<StorageItem> getStorageItems(){
    return this.storageItems;
}


@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = false)
public User getUser(){
    return this.user;
}


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id", unique = true, nullable = false)
public Integer getId(){
    return this.id;
}


@Column(name = "description", nullable = false, length = 2000)
public String getDescription(){
    return this.description;
}


@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
@OrderBy("createTime ASC")
public Set<Audit> getAudits(){
    return this.audits;
}


@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
public Set<MonitorCompute> getMonitorComputes(){
    return this.monitorComputes;
}


@Column(name = "redmine_issue_id")
public Integer getRedmineIssueId(){
    return this.redmineIssueId;
}


@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
public Set<NetworkEipItem> getNetworkEipItems(){
    return this.networkEipItems;
}


@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
public Set<NetworkDnsItem> getNetworkDnsItems(){
    return this.networkDnsItems;
}


@Column(name = "service_tag", nullable = false, length = 45)
public String getServiceTag(){
    return this.serviceTag;
}


@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
public Set<ComputeItem> getComputeItems(){
    return this.computeItems;
}


@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
public Set<MonitorMail> getMonitorMails(){
    return monitorMails;
}


public Integer getPriority(){
    return priority;
}


public void setStatus(Integer status){
    this.status = status;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setStatus"))

.queryParam("status",status)
;
restTemplate.put(builder.toUriString(),null);
}


}