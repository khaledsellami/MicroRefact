package com.sda.inTeams.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.sda.inTeams.Interface.TeamService;
import com.sda.inTeams.DTO.*;
import java.util.*;
public class TeamServiceImpl implements TeamService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://4";


public List<Team> getAll(){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/getAll"))
;  List<Team> aux = restTemplate.getForObject(builder.toUriString(), List.class);

 return aux;
}


}