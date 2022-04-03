package com.ec.survey.model.administration;
 import com.ec.survey.tools.ConversionTools;
import java.util.Date;
public class ContributionSearchResult {

 private  String surveyUID;

 private  String surveyAlias;

 private  String surveyTitle;

 private  String draftId;

 private  String uniqueCode;

 private  boolean isDraft;

 private  String invitationId;

 private  String updateDate;

public ContributionSearchResult(String surveyUID, String surveyAlias, String surveyTitle, String draftId, String uniqueCode, boolean isDraft, String invitationId, Date updateDate) {
    this.surveyUID = surveyUID;
    this.surveyAlias = surveyAlias;
    this.surveyTitle = surveyTitle;
    this.draftId = draftId;
    this.uniqueCode = uniqueCode;
    this.isDraft = isDraft;
    this.invitationId = invitationId;
    this.setUpdateDate(updateDate);
}
public String getSurveyTitle(){
    return surveyTitle;
}


public void setUpdateDate(Date updateDate){
    this.updateDate = ConversionTools.getFullString(updateDate);
}


public void setSurveyUID(String surveyUID){
    this.surveyUID = surveyUID;
}


public void setDraftId(String draftId){
    this.draftId = draftId;
}


public boolean isDraft(){
    return isDraft;
}


public String getDraftId(){
    return draftId;
}


public void setInvitationId(String invitationId){
    this.invitationId = invitationId;
}


public void setUniqueCode(String uniqueCode){
    this.uniqueCode = uniqueCode;
}


public void setSurveyTitle(String surveyTitle){
    this.surveyTitle = surveyTitle;
}


public void setSurveyAlias(String surveyAlias){
    this.surveyAlias = surveyAlias;
}


public String getInvitationId(){
    return invitationId;
}


public String getUpdateDate(){
    return updateDate;
}


public String getSurveyAlias(){
    return surveyAlias;
}


public void setDraft(boolean isDraft){
    this.isDraft = isDraft;
}


public String getSurveyUID(){
    return surveyUID;
}


public String getUniqueCode(){
    return uniqueCode;
}


}