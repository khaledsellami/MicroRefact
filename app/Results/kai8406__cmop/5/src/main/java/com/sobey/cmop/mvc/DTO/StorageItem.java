package com.sobey.cmop.mvc.DTO;
 import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import com.google.common.collect.Lists;
public class StorageItem {

 private  Integer id;

 private  Apply apply;

 private  String identifier;

 private  Integer space;

 private  Integer storageType;

 private  String controllerAlias;

 private  String volume;

 private  String mountPoint;

 private  List<ComputeItem> computeItemList;

// Constructors
/**
 * default constructor
 */
public StorageItem() {
}/**
 * minimal constructor
 */
public StorageItem(Apply apply, String identifier, Integer space, Integer storageType) {
    this.apply = apply;
    this.identifier = identifier;
    this.space = space;
    this.storageType = storageType;
}/**
 * full constructor
 */
public StorageItem(Apply apply, String identifier, Integer space, Integer storageType, String controllerAlias, String volume, String mountPoint) {
    this.apply = apply;
    this.identifier = identifier;
    this.space = space;
    this.storageType = storageType;
    this.controllerAlias = controllerAlias;
    this.volume = volume;
    this.mountPoint = mountPoint;
}
@ManyToMany
@JoinTable(name = "compute_storage_item", joinColumns = { @JoinColumn(name = "storage_item_id") }, inverseJoinColumns = { @JoinColumn(name = "compute_item_id") })
// Fecth策略定义
@Fetch(FetchMode.SUBSELECT)
// 集合按id排序.
@OrderBy("id")
// 集合中对象id的缓存.
@NotFound(action = NotFoundAction.IGNORE)
public List<ComputeItem> getComputeItemList(){
    return computeItemList;
}


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id", unique = true, nullable = false)
public Integer getId(){
    return this.id;
}


@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "apply_id", nullable = false)
public Apply getApply(){
    return this.apply;
}


@Column(name = "volume", length = 45)
public String getVolume(){
    return this.volume;
}


@Transient
public String getMountComputes(){
    return extractToString(computeItemList);
}


@Column(name = "identifier", nullable = false, length = 45)
public String getIdentifier(){
    return this.identifier;
}


@Column(name = "space", nullable = false)
public Integer getSpace(){
    return this.space;
}


@Column(name = "mount_point", length = 45)
public String getMountPoint(){
    return mountPoint;
}


@Column(name = "storage_type", nullable = false)
public Integer getStorageType(){
    return storageType;
}


@Column(name = "controller_alias", length = 45)
public String getControllerAlias(){
    return this.controllerAlias;
}


}