package ink.champ.models;
 import ink.champ.service.AppService;
import javax.persistence;
import java.util.Set;
import ink.champ.Interface.User;
@Entity(name = "players")
public class Player {

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
 private  Long id;

@Column(length = 25)
 private  String name;

 private  boolean privat;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "user_id")
 private  User user;

@OrderBy("id ASC")
@OneToMany(targetEntity = TeamPlayer.class, mappedBy = "player", orphanRemoval = true, fetch = FetchType.LAZY)
 private  Set<TeamPlayer> teams;

@OneToMany(targetEntity = PlayerRole.class, mappedBy = "player", orphanRemoval = true, fetch = FetchType.LAZY)
 private  Set<PlayerRole> roles;

/**
 * Конструктор игрока
 */
public Player() {
}/**
 * Конструктор игрока
 * @param name Имя
 * @param privat Приватность
 * @param user Пользователь
 */
public Player(String name, boolean privat, User user) {
    this.name = name;
    this.privat = privat;
    this.user = user;
}
public void setName(String name){
    this.name = name;
}


public int getUserRole(User user){
    PlayerRole role = getPlayerRole(user);
    return role == null ? AppService.Role.NONE : role.getRole();
}


public String getName(){
    return name;
}


public PlayerRole getPlayerRole(User user){
    if (user != null) {
        for (PlayerRole role : roles) {
            if (role.getUser().getId().equals(user.getId()))
                return role;
        }
    }
    return null;
}


public User getUser(){
    return user;
}


public void setTeams(Set<TeamPlayer> teams){
    this.teams = teams;
}


public int getUserRequest(User user){
    PlayerRole role = getPlayerRole(user);
    return role == null ? AppService.Role.NONE : role.getRequest();
}


public Long getId(){
    return id;
}


public boolean isPrivate(){
    return privat;
}


public int getTeamsCount(){
    return teams.size();
}


public Set<TeamPlayer> getTeams(){
    return teams;
}


public void setId(Long id){
    this.id = id;
}


public void setUser(User user){
    this.user = user;
}


public void setRoles(Set<PlayerRole> roles){
    this.roles = roles;
}


public void setPrivate(boolean privat){
    this.privat = privat;
}


public Set<PlayerRole> getRoles(){
    return roles;
}


}