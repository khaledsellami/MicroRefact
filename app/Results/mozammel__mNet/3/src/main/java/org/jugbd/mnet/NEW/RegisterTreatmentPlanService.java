package org.jugbd.mnet.NEW;
 import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.jugbd.mnet.dao.RegisterDao;
import org.jugbd.mnet.domain.Register;
@Service
public class RegisterTreatmentPlanService {

@Autowired
 private RegisterDao registerdao;


public TreatmentPlan setRegister(Long id,Register register){
return registerdao.setRegister(id,register);
}


public Register getRegister(Long id){
return registerdao.getRegister(id);
}


}