package com.fosun.fc.projects.creepers.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.fosun.fc.projects.creepers.Interface.ICreepersHlDetailService;
public class ICreepersHlDetailServiceImpl implements ICreepersHlDetailService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://15";


public Map<String,Object> findByRptNoForMap(String rptNo){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/findByRptNoForMap"))
    .queryParam("rptNo",rptNo)
;  Map<String,Object> aux = restTemplate.getForObject(builder.toUriString(), Map<String,Object>.class);

 return aux;
}


}