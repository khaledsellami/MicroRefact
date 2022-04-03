package com.puffride.demo.rest;
 import org.springframework.web.bind.annotation;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.puffride.demo.entity.Rider;
import com.puffride.demo.dao.RiderDao;
import com.puffride.demo.utils.GlobalConstants;
@RestController
@RequestMapping(GlobalConstants.CONTEXT_PATH + "/rider")
public class RiderResource {

@Autowired
 private RiderDao dao;


@GetMapping("/{id}")
public Rider read(Long id){
    return dao.findOne(id);
}


@DeleteMapping
public boolean deleteAll(List<Rider> entityList){
    dao.deleteAll(entityList);
    return true;
}


@PostMapping
public Rider create(Rider entity){
    return dao.save(entity);
}


@PutMapping
public Rider update(Rider entity){
    return dao.save(entity);
}


@DeleteMapping("/{id}")
public boolean delete(Long id){
    dao.delete(id);
    return true;
}


@GetMapping
public List<Rider> readAll(){
    return dao.findAll();
}


}