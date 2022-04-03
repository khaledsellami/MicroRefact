package de.metas.ui.web.menu;
 import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.adempiere.exceptions.AdempiereException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.metas.logging.LogManager;
import de.metas.security.IUserRolePermissionsDAO;
import de.metas.security.UserRolePermissionsKey;
import de.metas.ui.web.session.UserSession;
import de.metas.user.UserId;
import de.metas.user.api.IUserMenuFavoritesDAO;
import de.metas.util.Check;
import de.metas.util.Services;
@Repository
public class MenuTreeRepository implements MenuNodeFavoriteProvider{

 private  Logger logger;

@Autowired
 private  UserSession userSession;

 private  LoadingCache<MenuTreeKey,MenuTree> menuTrees;

 private  LoadingCache<UserId,UserMenuFavorites> userMenuFavoritesByUserId;

 private  UserRolePermissionsKey userRolePermissionsKey;

 private  String adLanguage;

 private  UserId adUserId;

 private  Set<Integer> menuIds;

 private  UserId adUserId;

 private  Set<Integer> menuIds;


public UserMenuFavorites retrieveFavoriteMenuIds(UserId adUserId){
    final List<Integer> adMenuIds = Services.get(IUserMenuFavoritesDAO.class).retrieveMenuIdsForUser(adUserId);
    return UserMenuFavorites.builder().adUserId(adUserId).addMenuIds(adMenuIds).build();
}


public UserRolePermissionsKey getUserRolePermissionsKey(){
    return userRolePermissionsKey;
}


public UserMenuFavorites getUserMenuFavorites(){
    final UserId adUserId = userSession.getLoggedUserId();
    try {
        return userMenuFavoritesByUserId.get(adUserId);
    } catch (final ExecutionException ex) {
        throw AdempiereException.wrapIfNeeded(ex);
    }
}


public void setFavorite(int adMenuId,boolean favorite){
    if (favorite) {
        menuIds.add(adMenuId);
    } else {
        menuIds.remove(adMenuId);
    }
}


public String getAD_Language(){
    return adLanguage;
}


public MenuTree getMenuTree(UserRolePermissionsKey userRolePermissionsKey,String adLanguage){
    try {
        final MenuTreeKey key = new MenuTreeKey(userRolePermissionsKey, adLanguage);
        MenuTree menuTree = menuTrees.get(key);
        // 
        // If menuTree's version is not the current one, try re-acquiring it.
        int retry = 3;
        final long currentVersion = Services.get(IUserRolePermissionsDAO.class).getCacheVersion();
        while (menuTree.getVersion() != currentVersion) {
            menuTrees.invalidate(key);
            menuTree = menuTrees.get(key);
            retry--;
            if (retry <= 0) {
                break;
            }
        }
        if (menuTree.getVersion() != currentVersion) {
            logger.warn("Could not acquire menu tree version {}. Returning what we got... \nmenuTree: {}\nkey={}", currentVersion, menuTree, key);
        }
        return menuTree;
    } catch (final ExecutionException e) {
        throw AdempiereException.wrapIfNeeded(e);
    }
}


public void cacheReset(){
    menuTrees.invalidateAll();
    menuTrees.cleanUp();
    userMenuFavoritesByUserId.invalidateAll();
    userMenuFavoritesByUserId.cleanUp();
}


@Override
public UserMenuFavorites load(UserId adUserId){
    return retrieveFavoriteMenuIds(adUserId);
}


public UserId getAdUserId(){
    return adUserId;
}


public MenuTreeRepository.UserMenuFavorites build(){
    return new UserMenuFavorites(this);
}


@Override
public int hashCode(){
    return Objects.hash(userRolePermissionsKey, adLanguage);
}


@Override
public boolean equals(Object obj){
    if (this == obj) {
        return true;
    }
    if (obj instanceof MenuTreeKey) {
        final MenuTreeKey other = (MenuTreeKey) obj;
        return Objects.equals(userRolePermissionsKey, other.userRolePermissionsKey) && Objects.equals(adLanguage, other.adLanguage);
    } else {
        return false;
    }
}


public Builder builder(){
    return new Builder();
}


public Builder adUserId(UserId adUserId){
    this.adUserId = adUserId;
    return this;
}


public MenuTree getUserSessionMenuTree(){
    final UserRolePermissionsKey userRolePermissionsKey = userSession.getUserRolePermissionsKey();
    final String adLanguage = userSession.getAD_Language();
    return getMenuTree(userRolePermissionsKey, adLanguage);
}


@Override
public String toString(){
    return MoreObjects.toStringHelper(this).add("adLanguage", adLanguage).addValue(userRolePermissionsKey).toString();
}


public Builder addMenuIds(List<Integer> adMenuIds){
    if (adMenuIds.isEmpty()) {
        return this;
    }
    menuIds.addAll(adMenuIds);
    return this;
}


public boolean isFavorite(MenuNode menuNode){
    return menuIds.contains(menuNode.getAD_Menu_ID());
}


}