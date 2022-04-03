package com.app.service;
 import java.util.List;
import com.app.pojo.Subject;
import com.app.pojo.SubjectDivComposit;
public interface SubjectDivCompositService {


public void deleteByDivId(int subId,int divId)
;

public List<SubjectDivComposit> getall()
;

public SubjectDivComposit edit(int id)
;

public SubjectDivComposit find(int id)
;

public void create(SubjectDivComposit subDivComp)
;

public void update(SubjectDivComposit subDivComp)
;

public String findSubjectName(int subDivCompId)
;

public void delet(int id)
;

public List<Subject> findByDivId(int divId)
;

}