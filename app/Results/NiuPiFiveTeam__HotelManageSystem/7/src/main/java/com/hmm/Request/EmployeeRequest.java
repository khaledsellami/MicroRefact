package com.hmm.Request;
import com.hmm.DTO.Employee;
public interface EmployeeRequest {

   public Employee getEmploy(Integer emp_idSDYK);
   public void setEmploy(Employee employ,Integer emp_idSDYK);
}