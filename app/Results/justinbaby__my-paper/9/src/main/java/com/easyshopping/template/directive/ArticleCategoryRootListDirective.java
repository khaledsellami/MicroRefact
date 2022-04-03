package com.easyshopping.template.directive;
 import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.easyshopping.entity.ArticleCategory;
import com.easyshopping.service.ArticleCategoryService;
import org.springframework.stereotype.Component;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
@Component("articleCategoryRootListDirective")
public class ArticleCategoryRootListDirective extends BaseDirective{

 private  String VARIABLE_NAME;

@Resource(name = "articleCategoryServiceImpl")
 private  ArticleCategoryService articleCategoryService;


@SuppressWarnings({ "unchecked", "rawtypes" })
public void execute(Environment env,Map params,TemplateModel[] loopVars,TemplateDirectiveBody body){
    List<ArticleCategory> articleCategories;
    boolean useCache = useCache(env, params);
    String cacheRegion = getCacheRegion(env, params);
    Integer count = getCount(params);
    if (useCache) {
        articleCategories = articleCategoryService.findRoots(count, cacheRegion);
    } else {
        articleCategories = articleCategoryService.findRoots(count);
    }
    setLocalVariable(VARIABLE_NAME, articleCategories, env, body);
}


}