package com.uec.imonitor.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.uec.imonitor.Interface.ITenantRequestService;
public class ITenantRequestServiceImpl implements ITenantRequestService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://2";


public List<TenantRequestEntity> findByTenantId(Integer tenantId){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/findByTenantId"))
    .queryParam("tenantId",tenantId)
;  List<TenantRequestEntity> aux = restTemplate.getForObject(builder.toUriString(), List<TenantRequestEntity>.class);

 return aux;
}


}