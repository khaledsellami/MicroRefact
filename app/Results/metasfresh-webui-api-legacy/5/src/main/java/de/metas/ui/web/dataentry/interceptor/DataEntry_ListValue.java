package de.metas.ui.web.dataentry.interceptor;
 import org.adempiere.ad.modelvalidator.annotations.Interceptor;
import org.adempiere.ad.modelvalidator.annotations.ModelChange;
import org.compiere.model.ModelValidator;
import org.springframework.stereotype.Component;
import de.metas.dataentry.model.I_DataEntry_ListValue;
import lombok.NonNull;
@Component
@Interceptor(I_DataEntry_ListValue.class)
public class DataEntry_ListValue {

 private  DataEntryInterceptorUtil dataEntryInterceptorUtil;


@ModelChange(timings = { ModelValidator.TYPE_AFTER_NEW, ModelValidator.TYPE_AFTER_CHANGE, ModelValidator.TYPE_BEFORE_DELETE })
public void invalidateDocumentDescriptorCache(I_DataEntry_ListValue dataEntryListValueRecord){
    dataEntryInterceptorUtil.resetCacheFor(dataEntryListValueRecord);
}


}