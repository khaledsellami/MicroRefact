package com.byr.warehouse.sheduler;
 import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import java.text.SimpleDateFormat;
import java.util.Date;
public class MySchedulerJob2 implements Job{

@Autowired
 private DaliyComputeShedule daliyComputeShedule;


public SimpleDateFormat dateFormat(){
    return new SimpleDateFormat("HH:mm:ss");
}


@Override
public void execute(JobExecutionContext context){
    daliyComputeShedule.sendMail();
    System.out.println("邮件已发送" + dateFormat().format(new Date()));
}


}