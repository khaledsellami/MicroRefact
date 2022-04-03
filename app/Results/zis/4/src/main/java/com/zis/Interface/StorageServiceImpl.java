package com.zis.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.zis.Interface.StorageService;
public class StorageServiceImpl implements StorageService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://1";


public StorageProduct findBySkuIdAndRepoId(Integer skuId,Integer repoId){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/findBySkuIdAndRepoId"))
    .queryParam("skuId",skuId)
    .queryParam("repoId",repoId)
;  StorageProduct aux = restTemplate.getForObject(builder.toUriString(), StorageProduct.class);

 return aux;
}


}