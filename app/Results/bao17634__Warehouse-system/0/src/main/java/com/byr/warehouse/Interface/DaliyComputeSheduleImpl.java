package com.byr.warehouse.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.byr.warehouse.Interface.DaliyComputeShedule;
public class DaliyComputeSheduleImpl implements DaliyComputeShedule{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://1";


public void sendMail(){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/sendMail"))
;
  restTemplate.put(builder.toUriString(), null);
}


}