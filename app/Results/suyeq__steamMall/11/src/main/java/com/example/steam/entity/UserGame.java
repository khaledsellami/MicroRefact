package com.example.steam.entity;
 import org.springframework.stereotype.Component;
import java.util.Date;
@Component
public class UserGame {

 private  Long id;

 private  String email;

 private  Long gameId;

 private  int playTime;

 private  Date lastPlay;

public UserGame() {
}public UserGame(Long id, String email, Long gameId) {
    this.id = id;
    this.email = email;
    this.gameId = gameId;
    this.playTime = 0;
    this.lastPlay = new Date();
}
public int getPlayTime(){
    return playTime;
}


public void setEmail(String email){
    this.email = email;
}


public void setGameId(Long gameId){
    this.gameId = gameId;
}


public Long getGameId(){
    return gameId;
}


public void setId(Long id){
    this.id = id;
}


public String getEmail(){
    return email;
}


public Long getId(){
    return id;
}


public Date getLastPlay(){
    return lastPlay;
}


public void setPlayTime(int playTime){
    this.playTime = playTime;
}


public void setLastPlay(Date lastPlay){
    this.lastPlay = lastPlay;
}


}