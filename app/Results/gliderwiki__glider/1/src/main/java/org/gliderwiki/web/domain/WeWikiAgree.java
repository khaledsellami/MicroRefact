package org.gliderwiki.web.domain;
 import java.util.Date;
import org.directwebremoting.annotations.DataTransferObject;
import org.gliderwiki.framework.orm.sql.annotation.Column;
import org.gliderwiki.framework.orm.sql.annotation.Table;
import org.gliderwiki.web.vo.BaseObjectBean;
@Table("WE_WIKI_AGREE")
@DataTransferObject
public class WeWikiAgree extends BaseObjectBean{

 private  long serialVersionUID;

@Column(name = "we_user_idx")
 private  Integer we_user_idx;

@Column(name = "we_wiki_idx")
 private  Integer we_wiki_idx;

@Column(name = "we_view_date")
 private  Date we_view_Date;


public Integer getWe_wiki_idx(){
    return we_wiki_idx;
}


public Integer getWe_user_idx(){
    return we_user_idx;
}


public void setWe_wiki_idx(Integer we_wiki_idx){
    this.we_wiki_idx = we_wiki_idx;
}


public void setWe_user_idx(Integer we_user_idx){
    this.we_user_idx = we_user_idx;
}


public Date getWe_view_Date(){
    return we_view_Date;
}


public void setWe_view_Date(Date we_view_Date){
    this.we_view_Date = we_view_Date;
}


}