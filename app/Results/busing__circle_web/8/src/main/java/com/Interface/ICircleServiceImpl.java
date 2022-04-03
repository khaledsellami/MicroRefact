package com.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.Interface.ICircleService;
public class ICircleServiceImpl implements ICircleService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://4";


public List<Circle> queryCircleListCircleId(String circlrIds){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/queryCircleListCircleId"))
    .queryParam("circlrIds",circlrIds)
;  List<Circle> aux = restTemplate.getForObject(builder.toUriString(), List<Circle>.class);

 return aux;
}


public int queryCircleMemberByCircleIdAndUserId(String circleId,String userId){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/queryCircleMemberByCircleIdAndUserId"))
    .queryParam("circleId",circleId)
    .queryParam("userId",userId)
;  int aux = restTemplate.getForObject(builder.toUriString(), int.class);

 return aux;
}


}