package com.qidian.hcm.NEWInstance;
 import org.springframework.web.bind.annotation.*;
@RestController
@CrossOrigin
public class SalaryThresholdRepositoryController {

 private SalaryThresholdRepository salarythresholdrepository;


@GetMapping
("/findById")
public Object findById(@RequestParam(name = "Object") Object Object){
  return salarythresholdrepository.findById(Object);
}


}