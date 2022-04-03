package com.fosun.fc.projects.creepers.entity;
 import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fosun.fc.projects.creepers.Request.TCreepersAccountBakRequest;
import com.fosun.fc.projects.creepers.Request.Impl.TCreepersAccountBakRequestImpl;
import com.fosun.fc.projects.creepers.DTO.TCreepersAccountBak;
@Entity
@Table(name = "T_CREEPERS_OL_DETAIL")
@NamedQuery(name = "TCreepersOlDetail.findAll", query = "SELECT t FROM TCreepersOlDetail t")
public class TCreepersOlDetail {

 private  long serialVersionUID;

@Id
@SequenceGenerator(name = "T_CREEPERS_OL_DETAIL_ID_GENERATOR", sequenceName = "SEQ_CREEPERS_OL_DETAIL")
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_CREEPERS_OL_DETAIL_ID_GENERATOR")
 private  Long id;

 private  BigDecimal balance;

@Column(name = "CURRENCY_TYPE")
 private  String currencyType;

@Temporal(TemporalType.DATE)
@Column(name = "GRANT_DT")
 private  Date grantDt;

@Column(name = "GRANT_ORG")
 private  String grantOrg;

@Column(name = "HL_STATUS")
 private  String hlStatus;

@Column(name = "LOAN_AMOUNT")
 private  BigDecimal loanAmount;

@Temporal(TemporalType.DATE)
@Column(name = "LOAN_MATURITY_DT")
 private  Date loanMaturityDt;

@Column(name = "LOAN_TYPE")
 private  String loanType;

 private  String memo;

@Column(name = "OL_OVERDRAFT_SIXTY")
 private  BigDecimal olOverdraftSixty;

@Column(name = "OLL_OVERDRAFT_NINETY")
 private  BigDecimal ollOverdraftNinety;

@Column(name = "OVERDUE_AMOUNT")
 private  BigDecimal overdueAmount;

@Column(name = "RPT_NO")
 private  String rptNo;

@Temporal(TemporalType.DATE)
@Column(name = "STATISTICAL_DT")
 private  Date statisticalDt;

@Transient
 private  TCreepersAccountBak TCreepersAccountBak;

@Column(name = "id")
 private Long id;

@Transient
 private TCreepersAccountBakRequest tcreepersaccountbakrequest = new TCreepersAccountBakRequestImpl();;

public TCreepersOlDetail() {
}
public void setLoanAmount(BigDecimal loanAmount){
    this.loanAmount = loanAmount;
}


public void setLoanMaturityDt(Date loanMaturityDt){
    this.loanMaturityDt = loanMaturityDt;
}


public Long getId(){
    return this.id;
}


public void setCurrencyType(String currencyType){
    this.currencyType = currencyType;
}


public void setOlOverdraftSixty(BigDecimal olOverdraftSixty){
    this.olOverdraftSixty = olOverdraftSixty;
}


public void setTCreepersAccountBak(TCreepersAccountBak TCreepersAccountBak){
 tcreepersaccountbakrequest.setTCreepersAccountBak(TCreepersAccountBak,this.id);
}



public BigDecimal getOllOverdraftNinety(){
    return this.ollOverdraftNinety;
}


public Date getGrantDt(){
    return this.grantDt;
}


public BigDecimal getOlOverdraftSixty(){
    return this.olOverdraftSixty;
}


public void setGrantOrg(String grantOrg){
    this.grantOrg = grantOrg;
}


public void setId(Long id){
    this.id = id;
}


public BigDecimal getLoanAmount(){
    return this.loanAmount;
}


public void setBalance(BigDecimal balance){
    this.balance = balance;
}


public String getGrantOrg(){
    return this.grantOrg;
}


public Date getLoanMaturityDt(){
    return this.loanMaturityDt;
}


public String getHlStatus(){
    return this.hlStatus;
}


public void setOllOverdraftNinety(BigDecimal ollOverdraftNinety){
    this.ollOverdraftNinety = ollOverdraftNinety;
}


public void setOverdueAmount(BigDecimal overdueAmount){
    this.overdueAmount = overdueAmount;
}


public String getLoanType(){
    return this.loanType;
}


public void setLoanType(String loanType){
    this.loanType = loanType;
}


public void setStatisticalDt(Date statisticalDt){
    this.statisticalDt = statisticalDt;
}


public void setMemo(String memo){
    this.memo = memo;
}


public String getMemo(){
    return this.memo;
}


public BigDecimal getBalance(){
    return this.balance;
}


public void setRptNo(String rptNo){
    this.rptNo = rptNo;
}


public Date getStatisticalDt(){
    return this.statisticalDt;
}


public TCreepersAccountBak getTCreepersAccountBak(){
  this.TCreepersAccountBak = tcreepersaccountbakrequest.getTCreepersAccountBak(this.id);
return this.TCreepersAccountBak;
}


public void setHlStatus(String hlStatus){
    this.hlStatus = hlStatus;
}


public BigDecimal getOverdueAmount(){
    return this.overdueAmount;
}


public void setGrantDt(Date grantDt){
    this.grantDt = grantDt;
}


public String getRptNo(){
    return this.rptNo;
}


public String getCurrencyType(){
    return this.currencyType;
}


}