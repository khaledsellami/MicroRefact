package com.fosun.fc.projects.creepers.service.impl;
 import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fosun.fc.modules.mapper.BeanMapper;
import com.fosun.fc.projects.creepers.constant.BaseConstant;
import com.fosun.fc.projects.creepers.dao.CreepersCourtDishonestDao;
import com.fosun.fc.projects.creepers.downloader.HttpRequestDownloader;
import com.fosun.fc.projects.creepers.dto.CreepersCourtDishonestDTO;
import com.fosun.fc.projects.creepers.dto.CreepersJobDTO;
import com.fosun.fc.projects.creepers.dto.CreepersParamDTO;
import com.fosun.fc.projects.creepers.entity.TCreepersCourtDishonest;
import com.fosun.fc.projects.creepers.pageprocessor.CreditChina.SupremeCourtDishonestyBlackListProcessor;
import com.fosun.fc.projects.creepers.pipeline.CreditChina.SupremeCourtDishonestyBlackListPipeline;
import com.fosun.fc.projects.creepers.service.ICreepersCourtDishonestyService;
import com.fosun.fc.projects.creepers.service.ICreepersJobService;
import com.fosun.fc.projects.creepers.utils.CommonMethodUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import com.fosun.fc.projects.creepers.Interface.SupremeCourtDishonestyBlackListProcessor;
import com.fosun.fc.projects.creepers.Interface.ICreepersJobService;
import com.fosun.fc.projects.creepers.DTO.CreepersJobDTO;
import com.fosun.fc.projects.creepers.DTO.CreepersParamDTO;
@Service
@Transactional
public class CreepersCourtDishonestyServiceImpl implements ICreepersCourtDishonestyService{

 private  Logger logger;

@Autowired
 private  SupremeCourtDishonestyBlackListProcessor supremeCourtDishonestyBlackListProcessor;

@Autowired
 private  SupremeCourtDishonestyBlackListPipeline supremeCourtDishonestyBlackListPipeline;

@Autowired
 private  CreepersCourtDishonestDao creepersCourtDishonestDao;

@Autowired
 private  ICreepersJobService creepersJobServiceImpl;

 private  String URL_SYMBOL;


@Override
public void queryByName(String name){
    creepersCourtDishonestDao.deleteByName(name);
}


@Override
public List<TCreepersCourtDishonest> findListByName(String name){
    return creepersCourtDishonestDao.findByName(name);
}


@Override
public List<TCreepersCourtDishonest> findListByNameAndCode(String name,String code){
    return creepersCourtDishonestDao.findListByNameAndCode(name, code);
}


@SuppressWarnings("unchecked")
@Override
public Page<CreepersCourtDishonestDTO> findList(Map<String,Object> searchParams,int pageNumber,int pageSize,String sortType){
    PageRequest pageable = buildPageRequest(pageNumber, pageSize, sortType);
    Specification<TCreepersCourtDishonest> spec = (Specification<TCreepersCourtDishonest>) buildSpecification(searchParams);
    Page<TCreepersCourtDishonest> page = creepersCourtDishonestDao.findAll(spec, pageable);
    List<TCreepersCourtDishonest> list = page.getContent();
    List<CreepersCourtDishonestDTO> dtoList = new ArrayList<CreepersCourtDishonestDTO>();
    dtoList = BeanMapper.mapList(list, CreepersCourtDishonestDTO.class);
    Page<CreepersCourtDishonestDTO> result = new PageImpl<CreepersCourtDishonestDTO>(new ArrayList<CreepersCourtDishonestDTO>(dtoList), pageable, page.getTotalElements());
    return result;
}


public NameValuePair[] jsonToNameValuePair(String jsonString){
    JSONArray jsonArray = JSON.parseArray(jsonString);
    NameValuePair[] nameValuePairs = new NameValuePair[jsonArray.size()];
    for (int i = 0; i < jsonArray.size(); i++) {
        String name = jsonArray.getJSONObject(i).getString("name");
        String value = jsonArray.getJSONObject(i).getString("value");
        nameValuePairs[i] = new BasicNameValuePair(name, value);
    }
    return nameValuePairs;
}


@Override
public void processByJob(String jobName){
    logger.info("=============>CreepersCourtDishonestyServiceImpl.processByName start!");
    // 查询任务
    CreepersJobDTO job = creepersJobServiceImpl.findJob(jobName);
    // 初始化Param DTO
    CreepersParamDTO param = new CreepersParamDTO();
    Request request;
    String indexUrl;
    int threadNum;
    if (StringUtils.isBlank(job.getMemo())) {
        indexUrl = job.getIndexUrl();
        indexUrl = indexUrl.substring(0, indexUrl.indexOf(URL_SYMBOL)) + URL_SYMBOL + new Date().getTime();
        param.putNameValuePair("keyword", "");
        param.putNameValuePair("page", "1");
        param.putTargetUrlList(indexUrl);
        param.setTaskType(BaseConstant.TaskListType.COURT_DISHONESTY_LIST.getValue());
        // 初始化Request
        request = CommonMethodUtils.buildDefaultRequest(param, indexUrl);
        threadNum = job.getThreadNum();
        request.putExtra("threadNum", threadNum);
        request.putExtra("pageNo", 1);
    } else {
        request = JSON.parseObject(job.getMemo(), Request.class);
        indexUrl = request.getUrl();
        indexUrl = indexUrl.substring(0, indexUrl.indexOf(URL_SYMBOL)) + URL_SYMBOL + new Date().getTime();
        request.setUrl(indexUrl);
        JSONObject jsonObject = JSON.parseObject(job.getMemo());
        JSONObject extras = jsonObject.getJSONObject("extras");
        String nameValuePair = extras.getString("nameValuePair");
        request.putExtra(BaseConstant.POST_NAME_VALUE_PAIR, jsonToNameValuePair(nameValuePair));
        param.putTargetUrlList(request.getUrl());
        param.setTaskType(BaseConstant.TaskListType.COURT_DISHONESTY_LIST.getValue());
        threadNum = (int) request.getExtra("threadNum");
        creepersJobServiceImpl.updateResumeRequestByJobName(jobName, "");
        ;
    }
    // 启动爬虫
    logger.info("=============>启动爬虫!");
    Spider.create(supremeCourtDishonestyBlackListProcessor).addPipeline(supremeCourtDishonestyBlackListPipeline).setDownloader(new HttpRequestDownloader().setParam(param)).thread(threadNum).addRequest(request).run();
    logger.info("=============>CreepersCourtDishonestyServiceImpl.processByName start!");
}


@Override
public void saveEntity(List<TCreepersCourtDishonest> entityList){
    for (TCreepersCourtDishonest entity : entityList) {
        creepersCourtDishonestDao.saveAndFlush(entity);
    }
}


@Override
public void saveOrUpdate(TCreepersCourtDishonest entity){
    TCreepersCourtDishonest oldEntity = creepersCourtDishonestDao.findTopByName(entity.getName());
    if (oldEntity != null) {
        entity.setId(oldEntity.getId());
        entity.setUpdatedDt(new Date());
        entity.setVersion(oldEntity.getVersion());
    }
    creepersCourtDishonestDao.saveAndFlush(entity);
}


}