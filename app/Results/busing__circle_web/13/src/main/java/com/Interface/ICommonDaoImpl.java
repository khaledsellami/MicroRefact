package com.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.Interface.ICommonDao;
public class ICommonDaoImpl implements ICommonDao{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://8";


public List<T> queryForList(String selectSql,Map<String,?> paramMap,Page page,Class<T> clazz){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/queryForList"))
    .queryParam("selectSql",selectSql)
    .queryParam("paramMap",paramMap)
    .queryParam("page",page)
    .queryParam("clazz",clazz)
;  List<T> aux = restTemplate.getForObject(builder.toUriString(), List<T>.class);

 return aux;
}


}