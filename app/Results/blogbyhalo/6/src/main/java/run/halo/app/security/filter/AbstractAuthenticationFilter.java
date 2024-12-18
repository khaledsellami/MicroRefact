package run.halo.app.security.filter;
 import run.halo.app.model.support.HaloConst.ONE_TIME_TOKEN_HEADER_NAME;
import run.halo.app.model.support.HaloConst.ONE_TIME_TOKEN_QUERY_NAME;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;
import run.halo.app.cache.AbstractStringCacheStore;
import run.halo.app.config.properties.HaloProperties;
import run.halo.app.exception.AbstractHaloException;
import run.halo.app.exception.BadRequestException;
import run.halo.app.exception.ForbiddenException;
import run.halo.app.exception.NotInstallException;
import run.halo.app.model.enums.Mode;
import run.halo.app.model.properties.PrimaryProperties;
import run.halo.app.security.context.SecurityContextHolder;
import run.halo.app.security.handler.AuthenticationFailureHandler;
import run.halo.app.security.handler.DefaultAuthenticationFailureHandler;
import run.halo.app.security.service.OneTimeTokenService;
import run.halo.app.service.OptionService;
import run.halo.app.Interface.HaloProperties;
import run.halo.app.Interface.OptionService;
import run.halo.app.Interface.AbstractStringCacheStore;
import run.halo.app.Interface.OneTimeTokenService;
import run.halo.app.Interface.AuthenticationFailureHandler;
import run.halo.app.DTO.AuthenticationFailureHandler;
import run.halo.app.DTO.AuthenticationFailureHandler;
@Slf4j
public class AbstractAuthenticationFilter extends OncePerRequestFilter{

 protected  AntPathMatcher antPathMatcher;

 protected  HaloProperties haloProperties;

 protected  OptionService optionService;

 protected  AbstractStringCacheStore cacheStore;

 private  UrlPathHelper urlPathHelper;

 private  OneTimeTokenService oneTimeTokenService;

 private  AuthenticationFailureHandler failureHandler;

 private  Set<String> excludeUrlPatterns;

 private  Set<String> urlPatterns;

AbstractAuthenticationFilter(HaloProperties haloProperties, OptionService optionService, AbstractStringCacheStore cacheStore, OneTimeTokenService oneTimeTokenService) {
    this.haloProperties = haloProperties;
    this.optionService = optionService;
    this.cacheStore = cacheStore;
    this.oneTimeTokenService = oneTimeTokenService;
    antPathMatcher = new AntPathMatcher();
}
@Override
public void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain){
    // Check whether the blog is installed or not
    Boolean isInstalled = optionService.getByPropertyOrDefault(PrimaryProperties.IS_INSTALLED, Boolean.class, false);
    if (!isInstalled && !Mode.TEST.equals(haloProperties.getMode())) {
        // If not installed
        getFailureHandler().onFailure(request, response, new NotInstallException("当前博客还没有初始化"));
        return;
    }
    try {
        // Check the one-time-token
        if (isSufficientOneTimeToken(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        // Do authenticate
        doAuthenticate(request, response, filterChain);
    } catch (AbstractHaloException e) {
        getFailureHandler().onFailure(request, response, e);
    } finally {
        SecurityContextHolder.clearContext();
    }
}


public void setUrlPatterns(Collection<String> urlPatterns){
    Assert.notNull(urlPatterns, "UrlPatterns must not be null");
    this.urlPatterns = new LinkedHashSet<>(urlPatterns);
}


@NonNull
public Set<String> getExcludeUrlPatterns(){
    return excludeUrlPatterns;
}


public void setExcludeUrlPatterns(Collection<String> excludeUrlPatterns){
    Assert.notNull(excludeUrlPatterns, "Exclude url patterns must not be null");
    this.excludeUrlPatterns = new HashSet<>(excludeUrlPatterns);
}


@Override
public boolean shouldNotFilter(HttpServletRequest request){
    Assert.notNull(request, "Http servlet request must not be null");
    // check white list
    boolean result = excludeUrlPatterns.stream().anyMatch(p -> antPathMatcher.match(p, urlPathHelper.getRequestUri(request)));
    return result || urlPatterns.stream().noneMatch(p -> antPathMatcher.match(p, urlPathHelper.getRequestUri(request)));
}


public void setFailureHandler(AuthenticationFailureHandler failureHandler){
    Assert.notNull(failureHandler, "Authentication failure handler must not be null");
    this.failureHandler = failureHandler;
}


@NonNull
public AuthenticationFailureHandler getFailureHandler(){
    if (failureHandler == null) {
        synchronized (this) {
            if (failureHandler == null) {
                // Create default authentication failure handler
                DefaultAuthenticationFailureHandler failureHandler = new DefaultAuthenticationFailureHandler();
                failureHandler.setProductionEnv(haloProperties.getMode().isProductionEnv());
                this.failureHandler = failureHandler;
            }
        }
    }
    return failureHandler;
}


public void addUrlPatterns(String urlPatterns){
    Assert.notNull(urlPatterns, "UrlPatterns must not be null");
    Collections.addAll(this.urlPatterns, urlPatterns);
}


public void addExcludeUrlPatterns(String excludeUrlPatterns){
    Assert.notNull(excludeUrlPatterns, "Exclude url patterns must not be null");
    Collections.addAll(this.excludeUrlPatterns, excludeUrlPatterns);
}


public Collection<String> getUrlPatterns(){
    return this.urlPatterns;
}


public void doAuthenticate(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain)


public boolean isSufficientOneTimeToken(HttpServletRequest request){
    // Check the param
    final String oneTimeToken = getTokenFromRequest(request, ONE_TIME_TOKEN_QUERY_NAME, ONE_TIME_TOKEN_HEADER_NAME);
    if (StringUtils.isBlank(oneTimeToken)) {
        // If no one-time token is not provided, skip
        return false;
    }
    // Get allowed uri
    String allowedUri = oneTimeTokenService.get(oneTimeToken).orElseThrow(() -> new BadRequestException("The one-time token does not exist or has been expired").setErrorData(oneTimeToken));
    // Get request uri
    String requestUri = request.getRequestURI();
    if (!StringUtils.equals(requestUri, allowedUri)) {
        // If the request uri mismatches the allowed uri
        // TODO using ant path matcher could be better
        throw new ForbiddenException("The one-time token does not correspond the request uri").setErrorData(oneTimeToken);
    }
    // Revoke the token before return
    oneTimeTokenService.revoke(oneTimeToken);
    return true;
}


public String getTokenFromRequest(HttpServletRequest request,String tokenQueryName,String tokenHeaderName){
    Assert.notNull(request, "Http servlet request must not be null");
    Assert.hasText(tokenQueryName, "Token query name must not be blank");
    Assert.hasText(tokenHeaderName, "Token header name must not be blank");
    // Get from header
    String accessKey = request.getHeader(tokenHeaderName);
    // Get from param
    if (StringUtils.isBlank(accessKey)) {
        accessKey = request.getParameter(tokenQueryName);
        log.debug("Got access key from parameter: [{}: {}]", tokenQueryName, accessKey);
    } else {
        log.debug("Got access key from header: [{}: {}]", tokenHeaderName, accessKey);
    }
    return accessKey;
}


}