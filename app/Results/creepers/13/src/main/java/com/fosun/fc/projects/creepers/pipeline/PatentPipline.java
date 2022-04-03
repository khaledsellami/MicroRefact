package com.fosun.fc.projects.creepers.pipeline;
 import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fosun.fc.projects.creepers.constant.BaseConstant;
import com.fosun.fc.projects.creepers.dto.CreepersParamDTO;
import com.fosun.fc.projects.creepers.entity.TCreepersPatent;
import com.fosun.fc.projects.creepers.service.ICreepersPatentService;
import com.fosun.fc.projects.creepers.utils.CommonMethodUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
@Component("patentPipline")
public class PatentPipline extends BasePipeline{

 private  Logger logger;

@Autowired
 private  ICreepersPatentService creepersPatentServiceIpml;

public PatentPipline() {
    setPath("/data/webmagic/");
}public PatentPipline(String path) {
    setPath(path);
}
@SuppressWarnings("unchecked")
@Override
public void process(ResultItems resultItems,Task task){
    logger.info("step: ======>> entry PatentPipline");
    List<Map<String, String>> resultList = resultItems.get("resultList");
    CreepersParamDTO param = resultItems.get(BaseConstant.PARAM_DTO_KEY);
    param.setErrorPath(getClass().toString());
    try {
        if (!CommonMethodUtils.isEmpty(resultList)) {
            logger.info("step:  ======>>  CommonMethodUtils.mapList. START!");
            List<TCreepersPatent> entityList = CommonMethodUtils.mapList(resultList, new TCreepersPatent());
            logger.info("step:  ======>>  CommonMethodUtils.mapList. SUCCEED!");
            logger.info("step:  ======>>  CommonMethodUtils.setByDT. START!");
            Map<String, String> map = new HashMap<String, String>();
            for (TCreepersPatent tCreepersCourtAnnounce : entityList) {
                CommonMethodUtils.setByDT(tCreepersCourtAnnounce);
                String merName = tCreepersCourtAnnounce.getMerName();
                if (!map.containsKey(merName)) {
                    map.put(merName, merName);
                    creepersPatentServiceIpml.deleteByMerName(merName);
                }
            }
            logger.info("step:  ======>>  CommonMethodUtils.setByDT. SUCCEED!");
            logger.info("step:  ======>>  creepersPatentDao save. START!");
            creepersPatentServiceIpml.saveEntity(entityList);
            logger.info("step:  ======>>  creepersPatentDao save. SUCCEED!");
            param.setTaskStatus(BaseConstant.TaskListStatus.SUCCEED.getValue());
            logger.info("step:  ======>>  creepersListServiceImpl update status to succeed START!");
            creepersListServiceImpl.updateList(param);
            logger.info("step:  ======>>  creepersListServiceImpl update status to succeed SUCCEED!");
        } else {
            logger.info("step:  ======>>  input resultList is empty! Then update status to false!");
            param.setErrorInfo("input resultList is empty! Then update status to false!");
            logger.info("step:  ======>>  creepersListServiceImpl START!");
            creepersExceptionHandleServiceImpl.handleExceptionAndPrintLog(param);
            logger.info("step:  ======>>  creepersListServiceImpl SUCCEED!");
        }
    } catch (Exception e) {
        e.printStackTrace();
        logger.info("step:  ======>>  creepersPatentDao save FALSE!!!");
        logger.error("write DB error", e.getCause().getClass() + e.getCause().getMessage());
        param.setErrorInfo("write DB error" + e.getCause().getClass() + e.getCause().getMessage());
        creepersExceptionHandleServiceImpl.handleExceptionAndPrintLog(param);
    }
    logger.info("step: ======>> exit PatentPipline");
}


}