package com.sobey.cmop.mvc.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.sobey.cmop.mvc.Interface.AuditService;
public class AuditServiceImpl implements AuditService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://1";


}