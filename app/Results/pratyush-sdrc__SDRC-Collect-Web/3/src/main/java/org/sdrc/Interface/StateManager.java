package org.sdrc.Interface;
public interface StateManager {

   public List<Error> getError();
   public Object isEmpty(Object Object);
   public void setError(List<Error> errModels);
   public Object getValue(String key);
}