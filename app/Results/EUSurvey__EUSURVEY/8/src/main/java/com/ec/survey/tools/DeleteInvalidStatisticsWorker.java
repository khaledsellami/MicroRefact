package com.ec.survey.tools;
 import com.ec.survey.service.AnswerService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.ec.survey.Interface.AnswerService;
@Service("deleteInvalidStatisticsWorker")
@Scope("singleton")
public class DeleteInvalidStatisticsWorker implements Runnable{

 protected  Logger logger;

@Resource(name = "answerService")
 private  AnswerService answerService;


@Override
public void run(){
    try {
        logger.info("DeleteInvalidStatisticsWorker started");
        answerService.deleteInvalidStatistics();
    } catch (Exception e) {
        logger.error(e.getLocalizedMessage(), e);
    }
    logger.info("DeleteInvalidStatisticsWorker completed");
}


}