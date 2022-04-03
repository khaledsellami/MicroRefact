package com.ec.survey.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.ec.survey.Interface.ArchiveService;
public class ArchiveServiceImpl implements ArchiveService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://5";


public void add(Archive archive){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/add"))
    .queryParam("archive",archive)
;
  restTemplate.put(builder.toUriString(), null);
}


}