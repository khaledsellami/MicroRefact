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


public T queryForObject(String sql,Class<T> clazz){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/queryForObject"))
    .queryParam("sql",sql)
    .queryParam("clazz",clazz)
;  T aux = restTemplate.getForObject(builder.toUriString(), T.class);

 return aux;
}


public int update(String sql,Map<String,?> paramMap){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/update"))
    .queryParam("sql",sql)
    .queryParam("paramMap",paramMap)
;  int aux = restTemplate.getForObject(builder.toUriString(), int.class);

 return aux;
}


public int getLastId(String tableName,String idColume){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/getLastId"))
    .queryParam("tableName",tableName)
    .queryParam("idColume",idColume)
;  int aux = restTemplate.getForObject(builder.toUriString(), int.class);

 return aux;
}


}