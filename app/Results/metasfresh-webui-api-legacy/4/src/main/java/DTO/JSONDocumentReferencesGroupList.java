package DTO;
 import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import de.metas.ui.web.menu.MenuNode;
import de.metas.ui.web.menu.MenuTree;
import de.metas.ui.web.window.datatypes.json.JSONDocumentReferencesGroup.JSONDocumentReferencesGroupBuilder;
import de.metas.ui.web.window.model.DocumentReference;
import de.metas.util.lang.UIDStringUtil;
public class JSONDocumentReferencesGroupList {

 public  JSONDocumentReferencesGroupList EMPTY;

 private  List<JSONDocumentReferencesGroup> groups;

 private  List<JSONDocumentReference> references;

 private RestTemplate restTemplate = new RestTemplate();

  String url = "http://16";


public JSONDocumentReferencesGroupList of(Collection<DocumentReference> documentReferences,MenuTree menuTree,String othersMenuCaption,JSONOptions jsonOpts){
    if (documentReferences.isEmpty()) {
        return EMPTY;
    }
    final Map<String, JSONDocumentReferencesGroupBuilder> groupsBuilders = new HashMap<>();
    final String othersGroupId = "_others_" + UIDStringUtil.createRandomUUID();
    for (final DocumentReference documentReference : documentReferences) {
        final JSONDocumentReference jsonDocumentReference = JSONDocumentReference.of(documentReference, jsonOpts);
        if (jsonDocumentReference == null) {
            continue;
        }
        final MenuNode topLevelMenuGroup = menuTree.getTopLevelMenuGroupOrNull(documentReference.getWindowId());
        final String topLevelMenuGroupId = topLevelMenuGroup != null ? topLevelMenuGroup.getId() : othersGroupId;
        final JSONDocumentReferencesGroupBuilder groupBuilder = groupsBuilders.computeIfAbsent(topLevelMenuGroupId, k -> {
            final boolean isMiscGroup = topLevelMenuGroup == null;
            final String caption = topLevelMenuGroup != null ? topLevelMenuGroup.getCaption() : othersMenuCaption;
            return JSONDocumentReferencesGroup.builder().caption(caption).isMiscGroup(isMiscGroup);
        });
        groupBuilder.reference(jsonDocumentReference);
    }
    // Sort by Caption, but keep the "misc group" last
    Comparator<JSONDocumentReferencesGroup> sorting = Comparator.<JSONDocumentReferencesGroup>comparingInt(group -> group.isMiscGroup() ? 1 : 0).thenComparing(JSONDocumentReferencesGroup::getCaption);
    final List<JSONDocumentReferencesGroup> groups = groupsBuilders.values().stream().map(groupBuilder -> groupBuilder.build()).filter(group -> !group.isEmpty()).sorted(sorting).collect(ImmutableList.toImmutableList());
    return new JSONDocumentReferencesGroupList(groups);
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/of"))

.queryParam("documentReferences",documentReferences);
.queryParam("menuTree",menuTree);
.queryParam("othersMenuCaption",othersMenuCaption);
.queryParam("jsonOpts",jsonOpts);
JSONDocumentReferencesGroupList aux = restTemplate.getForObject(builder.toUriString(),JSONDocumentReferencesGroupList.class);
return aux;
}


}