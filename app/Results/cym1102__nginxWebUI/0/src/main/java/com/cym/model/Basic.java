package com.cym.model;
 import cn.craccd.sqlHelper.bean.BaseModel;
import cn.craccd.sqlHelper.config.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel("基础参数")
@Table
public class Basic extends BaseModel{

@ApiModelProperty("参数名")
 private String name;

@ApiModelProperty("参数值")
 private String value;

@ApiModelProperty(hidden = true)
 private Long seq;

public Basic() {
}public Basic(String name, String value, Long seq) {
    this.name = name;
    this.value = value;
    this.seq = seq;
}
public void setName(String name){
    this.name = name;
}


public Long getSeq(){
    return seq;
}


public String getValue(){
    return value;
}


public String getName(){
    return name;
}


public void setValue(String value){
    this.value = value;
}


public void setSeq(Long seq){
    this.seq = seq;
}


}