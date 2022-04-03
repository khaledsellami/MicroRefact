package com.byr.warehouse.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.byr.warehouse.Interface.ExcelService;
public class ExcelServiceImpl implements ExcelService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://6";


public void ExportEcel(List<T> excellist,String title,String sheetName,String filename,Class calzz){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/ExportEcel"))
    .queryParam("excellist",excellist)
    .queryParam("title",title)
    .queryParam("sheetName",sheetName)
    .queryParam("filename",filename)
    .queryParam("calzz",calzz)
;
  restTemplate.put(builder.toUriString(), null);
}


}