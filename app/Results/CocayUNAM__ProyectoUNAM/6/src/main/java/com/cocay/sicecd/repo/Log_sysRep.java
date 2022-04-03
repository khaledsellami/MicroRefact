package com.cocay.sicecd.repo;
 import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.cocay.sicecd.model.Curso;
import com.cocay.sicecd.model.Log_sys;
@Repository
public interface Log_sysRep extends PagingAndSortingRepository<Log_sys, Integer>{


@Query(value = "SELECT * FROM Log_sys", nativeQuery = true)
public List<Log_sys> findEv()
;

public List<Log_sys> findByIp(String name)
;

}