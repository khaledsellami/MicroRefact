package DTO;
 import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.adempiere.exceptions.AdempiereException;
import org.slf4j.Logger;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import de.metas.i18n.ITranslatableString;
import de.metas.i18n.TranslatableStrings;
import de.metas.logging.LogManager;
import de.metas.ui.web.cache.ETag;
import de.metas.ui.web.cache.ETagAware;
import de.metas.ui.web.document.filter.DocumentFilterDescriptor;
import de.metas.ui.web.view.IViewRow;
import de.metas.ui.web.view.ViewCloseAction;
import de.metas.ui.web.view.ViewProfileId;
import de.metas.ui.web.view.descriptor.annotation.ViewColumnHelper;
import de.metas.ui.web.view.descriptor.annotation.ViewColumnHelper.ClassViewColumnOverrides;
import de.metas.ui.web.view.json.JSONViewDataType;
import de.metas.ui.web.window.datatypes.WindowId;
import de.metas.ui.web.window.descriptor.DetailId;
import de.metas.ui.web.window.descriptor.DocumentLayoutElementDescriptor;
import de.metas.ui.web.window.descriptor.DocumentLayoutElementFieldDescriptor;
import de.metas.ui.web.window.descriptor.factory.standard.LayoutFactory;
import de.metas.ui.web.window.model.DocumentQueryOrderBy;
import de.metas.ui.web.window.model.DocumentQueryOrderByList;
import de.metas.util.Check;
import de.metas.util.GuavaCollectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
public class Builder {

 private  WindowId windowId;

 private  DetailId detailId;

 private  ITranslatableString caption;

 private  ITranslatableString description;

 private  ITranslatableString emptyResultText;

 private  ITranslatableString emptyResultHint;

 private  Collection<DocumentFilterDescriptor> filters;

 private  DocumentQueryOrderByList defaultOrderBys;

 private  boolean hasAttributesSupport;

 private  IncludedViewLayout includedViewLayout;

 private  LinkedHashSet<ViewCloseAction> allowedViewCloseActions;

 private  ImmutableSet<ViewCloseAction> DEFAULT_allowedViewCloseActions;

 private  boolean hasTreeSupport;

 private  boolean treeCollapsible;

 private  int treeExpandedDepth;

 private  boolean allowOpeningRowDetails;

 private  List<DocumentLayoutElementDescriptor.Builder> elementBuilders;

 private  String idFieldName;

 private  String focusOnFieldName;

 private RestTemplate restTemplate = new RestTemplate();

  String url = "http://1";


public Set<String> getFieldNames(){
    return elementBuilders.stream().flatMap(element -> element.getFieldNames().stream()).collect(GuavaCollectors.toImmutableSet());
}


public ImmutableList<DocumentFilterDescriptor> getFilters(){
    if (filters == null || filters.isEmpty()) {
        return ImmutableList.of();
    } else {
        return filters.stream().sorted(Comparator.comparing(DocumentFilterDescriptor::getSortNo)).collect(ImmutableList.toImmutableList());
    }
}


public ImmutableSet<ViewCloseAction> getAllowedViewCloseActions(){
    return allowedViewCloseActions != null ? ImmutableSet.copyOf(allowedViewCloseActions) : DEFAULT_allowedViewCloseActions;
}


public DocumentQueryOrderByList getDefaultOrderBys(){
    return defaultOrderBys != null ? defaultOrderBys : DocumentQueryOrderByList.EMPTY;
}


public List<DocumentLayoutElementDescriptor.Builder> getElements(){
    return elementBuilders;
}


public String getIdFieldName(){
    return idFieldName;
}


public Builder clearElements(){
    elementBuilders.clear();
    return this;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/clearElements"))

Builder aux = restTemplate.getForObject(builder.toUriString(),Builder.class);
return aux;
}


public Builder addElementsFromViewRowClassAndFieldNames(Class<T> viewRowClass,JSONViewDataType viewDataType,ClassViewColumnOverrides columns){
    final List<DocumentLayoutElementDescriptor.Builder> elements = ViewColumnHelper.createLayoutElementsForClassAndFieldNames(viewRowClass, viewDataType, columns);
    // shall never happen
    Check.assumeNotEmpty(elements, "elements is not empty");
    addElements(elements);
    return this;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/addElementsFromViewRowClassAndFieldNames"))

.queryParam("viewRowClass",viewRowClass);
.queryParam("viewDataType",viewDataType);
.queryParam("columns",columns);
Builder aux = restTemplate.getForObject(builder.toUriString(),Builder.class);
return aux;
}


public SqlViewBinding build(){
    return new SqlViewBinding(this);
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/build"))

SqlViewBinding aux = restTemplate.getForObject(builder.toUriString(),SqlViewBinding.class);
return aux;
}


public Builder setParameters(Map<String,Object> parameters){
    parameters.forEach(this::setParameter);
    return this;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setParameters"))

.queryParam("parameters",parameters);
Builder aux = restTemplate.getForObject(builder.toUriString(),Builder.class);
return aux;
}


public Builder setParameter(String name,Object value){
    if (value == null) {
        if (parameters != null) {
            parameters.remove(name);
        }
    } else {
        if (parameters == null) {
            parameters = new LinkedHashMap<>();
        }
        parameters.put(name, value);
    }
    return this;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setParameter"))

.queryParam("name",name);
.queryParam("value",value);
Builder aux = restTemplate.getForObject(builder.toUriString(),Builder.class);
return aux;
}


}