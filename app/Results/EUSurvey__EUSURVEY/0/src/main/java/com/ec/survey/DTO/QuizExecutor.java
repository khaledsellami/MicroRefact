package com.ec.survey.DTO;
 import java.io.InputStream;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.ec.survey.model.AnswerSet;
import com.ec.survey.service.MailService;
import com.ec.survey.service.PDFService;
import com.ec.survey.Interface.MailService;
import com.ec.survey.Interface.AnswerSet;
public class QuizExecutor implements Runnable{

 private  PDFService pdfService;

 private  MailService mailService;

 public  ServletContext servletContext;

 private  AnswerSet answerSet;

 private  String email;

 private  String from;

 private  String host;

 private  Logger logger;

 private RestTemplate restTemplate = new RestTemplate();

  String url = "http://6";


public void init(AnswerSet answerSet){
    this.answerSet = answerSet;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/init"))

.queryParam("answerSet",answerSet)
;
restTemplate.put(builder.toUriString(),null);
}


}