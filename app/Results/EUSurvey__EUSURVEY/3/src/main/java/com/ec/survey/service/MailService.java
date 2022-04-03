package com.ec.survey.service;
 import com.ec.survey.exception.MessageException;
import com.ec.survey.model.MailTask;
import com.ec.survey.tools.InvitationMailCreator;
import com.ec.survey.tools.MailSender;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.util.List;
@Service("mailService")
public class MailService extends BasicService{


public void SendHtmlMail(String to,String from,String reply,String subject,String body,String info){
    SendHtmlMail(to, from, reply, subject, body, null, info);
}


@SuppressWarnings("unchecked")
public List<Integer> getParticipationGroupsWithRunningMail(String surveyUid){
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery("SELECT m.participationGroupId FROM MailTask m WHERE m.surveyUid = :uid AND m.state = :state").setString("uid", surveyUid).setString("state", MailTask.WAITING);
    return query.list();
}


public boolean isValidEmailAddress(String email){
    boolean result = true;
    try {
        InternetAddress emailAddr = new InternetAddress(email);
        emailAddr.validate();
    } catch (AddressException ex) {
        result = false;
    }
    return result;
}


@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
public List<MailTask> get(){
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery("FROM MailTask");
    return query.list();
}


public boolean isNotEmptyAndValidEmailAddress(String email){
    return email != null && email.trim().length() > 0 && isValidEmailAddress(email.trim());
}


@Transactional(readOnly = false)
public void start(MailTask task){
    task.setState(MailTask.WAITING);
    Session session = sessionFactory.getCurrentSession();
    session.saveOrUpdate(task);
    InvitationMailCreator creator = (InvitationMailCreator) context.getBean("invitationMailCreator");
    creator.init(task);
    getPool().execute(creator);
}


@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
public void save(MailTask task){
    Session session = sessionFactory.getCurrentSession();
    task = (MailTask) session.merge(task);
    session.setReadOnly(task, false);
    session.saveOrUpdate(task);
}


public MailTask getFirstFinishedMailTask(String surveyUid){
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery("FROM MailTask m WHERE m.surveyUid = :uid AND m.notified = false AND m.state != :state").setString("uid", surveyUid).setString("state", MailTask.WAITING);
    List<?> result = query.setMaxResults(1).list();
    if (!result.isEmpty())
        return (MailTask) result.get(0);
    return null;
}


}