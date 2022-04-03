package com.sobey.cmop.mvc.DTO;
 import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;
public class MonitorCompute {

 private  Integer id;

 private  Apply apply;

 private  String identifier;

 private  String ipAddress;

 private  String cpuWarn;

 private  String cpuCritical;

 private  String memoryWarn;

 private  String memoryCritical;

 private  String diskWarn;

 private  String diskCritical;

 private  String pingLossWarn;

 private  String pingLossCritical;

 private  String pingDelayWarn;

 private  String pingDelayCritical;

 private  String maxProcessWarn;

 private  String maxProcessCritical;

 private  String port;

 private  String process;

 private  String mountPoint;

 private RestTemplate restTemplate = new RestTemplate();

  String url = "http://10";

// Constructors
/**
 * default constructor
 */
public MonitorCompute() {
}/**
 * minimal constructor
 */
public MonitorCompute(Apply apply, String identifier, String ipAddress) {
    this.apply = apply;
    this.identifier = identifier;
    this.ipAddress = ipAddress;
}/**
 * full constructor
 */
public MonitorCompute(Apply apply, String identifier, String ipAddress, String cpuWarn, String cpuCritical, String memoryWarn, String memoryCritical, String diskWarn, String diskCritical, String pingLossWarn, String pingLossCritical, String pingDelayWarn, String pingDelayCritical, String maxProcessWarn, String maxProcessCritical, String port, String process, String mountPoint) {
    this.apply = apply;
    this.identifier = identifier;
    this.ipAddress = ipAddress;
    this.cpuWarn = cpuWarn;
    this.cpuCritical = cpuCritical;
    this.memoryWarn = memoryWarn;
    this.memoryCritical = memoryCritical;
    this.diskWarn = diskWarn;
    this.diskCritical = diskCritical;
    this.pingLossWarn = pingLossWarn;
    this.pingLossCritical = pingLossCritical;
    this.pingDelayWarn = pingDelayWarn;
    this.pingDelayCritical = pingDelayCritical;
    this.maxProcessWarn = maxProcessWarn;
    this.maxProcessCritical = maxProcessCritical;
    this.port = port;
    this.process = process;
    this.mountPoint = mountPoint;
}
@Column(name = "ip_address", nullable = false, length = 45)
public String getIpAddress(){
    return ipAddress;
}


@Column(name = "memory_warn", length = 5)
public String getMemoryWarn(){
    return memoryWarn;
}


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id", unique = true, nullable = false)
public Integer getId(){
    return this.id;
}


@JsonBackReference
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "apply_id", nullable = false)
public Apply getApply(){
    return this.apply;
}


@Column(name = "cpu_critical", length = 5)
public String getCpuCritical(){
    return cpuCritical;
}


@Column(name = "identifier", nullable = false, length = 45)
public String getIdentifier(){
    return this.identifier;
}


@Column(name = "cpu_warn", length = 5)
public String getCpuWarn(){
    return cpuWarn;
}


@Column(name = "max_process_critical", length = 5)
public String getMaxProcessCritical(){
    return maxProcessCritical;
}


@Column(name = "disk_critical", length = 5)
public String getDiskCritical(){
    return diskCritical;
}


@Column(name = "process", length = 200)
public String getProcess(){
    return process;
}


@Column(name = "ping_delay_critical", length = 10)
public String getPingDelayCritical(){
    return pingDelayCritical;
}


@Column(name = "ping_loss_warn", length = 5)
public String getPingLossWarn(){
    return pingLossWarn;
}


@Column(name = "max_process_warn", length = 5)
public String getMaxProcessWarn(){
    return maxProcessWarn;
}


@Column(name = "ping_loss_critical", length = 5)
public String getPingLossCritical(){
    return pingLossCritical;
}


@Column(name = "memory_critical", length = 5)
public String getMemoryCritical(){
    return memoryCritical;
}


@Column(name = "disk_warn", length = 5)
public String getDiskWarn(){
    return diskWarn;
}


@Column(name = "port", length = 200)
public String getPort(){
    return port;
}


@Column(name = "mount_point", length = 200)
public String getMountPoint(){
    return mountPoint;
}


@Column(name = "ping_delay_warn", length = 10)
public String getPingDelayWarn(){
    return pingDelayWarn;
}


public void setId(Integer id){
    this.id = id;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setId"))

.queryParam("id",id)
;
restTemplate.put(builder.toUriString(),null);
}


public void setApply(Apply apply){
    this.apply = apply;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setApply"))

.queryParam("apply",apply)
;
restTemplate.put(builder.toUriString(),null);
}


public void setIdentifier(String identifier){
    this.identifier = identifier;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setIdentifier"))

.queryParam("identifier",identifier)
;
restTemplate.put(builder.toUriString(),null);
}


public void setIpAddress(String ipAddress){
    this.ipAddress = ipAddress;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setIpAddress"))

.queryParam("ipAddress",ipAddress)
;
restTemplate.put(builder.toUriString(),null);
}


public void setCpuWarn(String cpuWarn){
    this.cpuWarn = cpuWarn;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setCpuWarn"))

.queryParam("cpuWarn",cpuWarn)
;
restTemplate.put(builder.toUriString(),null);
}


public void setCpuCritical(String cpuCritical){
    this.cpuCritical = cpuCritical;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setCpuCritical"))

.queryParam("cpuCritical",cpuCritical)
;
restTemplate.put(builder.toUriString(),null);
}


public void setMemoryWarn(String memoryWarn){
    this.memoryWarn = memoryWarn;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setMemoryWarn"))

.queryParam("memoryWarn",memoryWarn)
;
restTemplate.put(builder.toUriString(),null);
}


public void setMemoryCritical(String memoryCritical){
    this.memoryCritical = memoryCritical;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setMemoryCritical"))

.queryParam("memoryCritical",memoryCritical)
;
restTemplate.put(builder.toUriString(),null);
}


public void setDiskWarn(String diskWarn){
    this.diskWarn = diskWarn;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setDiskWarn"))

.queryParam("diskWarn",diskWarn)
;
restTemplate.put(builder.toUriString(),null);
}


public void setDiskCritical(String diskCritical){
    this.diskCritical = diskCritical;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setDiskCritical"))

.queryParam("diskCritical",diskCritical)
;
restTemplate.put(builder.toUriString(),null);
}


public void setPingLossWarn(String pingLossWarn){
    this.pingLossWarn = pingLossWarn;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setPingLossWarn"))

.queryParam("pingLossWarn",pingLossWarn)
;
restTemplate.put(builder.toUriString(),null);
}


public void setPingLossCritical(String pingLossCritical){
    this.pingLossCritical = pingLossCritical;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setPingLossCritical"))

.queryParam("pingLossCritical",pingLossCritical)
;
restTemplate.put(builder.toUriString(),null);
}


public void setPingDelayWarn(String pingDelayWarn){
    this.pingDelayWarn = pingDelayWarn;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setPingDelayWarn"))

.queryParam("pingDelayWarn",pingDelayWarn)
;
restTemplate.put(builder.toUriString(),null);
}


public void setPingDelayCritical(String pingDelayCritical){
    this.pingDelayCritical = pingDelayCritical;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setPingDelayCritical"))

.queryParam("pingDelayCritical",pingDelayCritical)
;
restTemplate.put(builder.toUriString(),null);
}


public void setMaxProcessWarn(String maxProcessWarn){
    this.maxProcessWarn = maxProcessWarn;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setMaxProcessWarn"))

.queryParam("maxProcessWarn",maxProcessWarn)
;
restTemplate.put(builder.toUriString(),null);
}


public void setMaxProcessCritical(String maxProcessCritical){
    this.maxProcessCritical = maxProcessCritical;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setMaxProcessCritical"))

.queryParam("maxProcessCritical",maxProcessCritical)
;
restTemplate.put(builder.toUriString(),null);
}


public void setPort(String port){
    this.port = port;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setPort"))

.queryParam("port",port)
;
restTemplate.put(builder.toUriString(),null);
}


public void setProcess(String process){
    this.process = process;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setProcess"))

.queryParam("process",process)
;
restTemplate.put(builder.toUriString(),null);
}


public void setMountPoint(String mountPoint){
    this.mountPoint = mountPoint;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setMountPoint"))

.queryParam("mountPoint",mountPoint)
;
restTemplate.put(builder.toUriString(),null);
}


}