package com.zis.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.zis.Interface.BookAmountService;
public class BookAmountServiceImpl implements BookAmountService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://4";


}