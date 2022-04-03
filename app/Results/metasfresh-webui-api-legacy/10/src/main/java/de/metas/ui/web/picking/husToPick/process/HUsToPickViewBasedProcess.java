package de.metas.ui.web.picking.husToPick.process;
 import de.metas.ui.web.handlingunits.WEBUI_HU_Constants.MSG_WEBUI_SELECT_ACTIVE_UNSELECTED_HU;
import java.util.Optional;
import java.util.stream.Stream;
import org.adempiere.exceptions.AdempiereException;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.common.collect.ImmutableMultimap;
import de.metas.handlingunits.HuId;
import de.metas.handlingunits.picking.PickFrom;
import de.metas.handlingunits.picking.PickingCandidateService;
import de.metas.handlingunits.picking.requests.PickRequest;
import de.metas.handlingunits.sourcehu.SourceHUsService;
import de.metas.i18n.IMsgBL;
import de.metas.i18n.ITranslatableString;
import de.metas.inoutcandidate.api.ShipmentScheduleId;
import de.metas.order.OrderLineId;
import de.metas.picking.api.PickingSlotId;
import de.metas.process.ProcessExecutionResult.ViewOpenTarget;
import de.metas.process.ProcessExecutionResult.WebuiViewToOpen;
import de.metas.process.ProcessPreconditionsResolution;
import de.metas.ui.web.handlingunits.HUEditorRow;
import de.metas.ui.web.handlingunits.HUEditorView;
import de.metas.ui.web.picking.packageable.PackageableRow;
import de.metas.ui.web.picking.packageable.PackageableView;
import de.metas.ui.web.picking.pickingslot.PickingSlotRow;
import de.metas.ui.web.picking.pickingslot.PickingSlotView;
import de.metas.ui.web.process.adprocess.ViewBasedProcessTemplate;
import de.metas.ui.web.view.IView;
import de.metas.ui.web.view.IViewsRepository;
import de.metas.ui.web.view.ViewId;
import de.metas.ui.web.window.datatypes.DocumentId;
import de.metas.util.Services;
import lombok.NonNull;
public class HUsToPickViewBasedProcess extends ViewBasedProcessTemplate{

@Autowired
 private  PickingCandidateService pickingCandidateService;

@Autowired
 private  IViewsRepository viewsRepo;

 private  SourceHUsService sourceHuService;


public void invalidatePickingSlotsView(){
    final PickingSlotView pickingSlotsView = getPickingSlotViewOrNull();
    if (pickingSlotsView == null) {
        return;
    }
    invalidateView(pickingSlotsView.getViewId());
}


@Override
public HUEditorRow getSingleSelectedRow(){
    return HUEditorRow.cast(super.getSingleSelectedRow());
}


public void invalidatePackablesView(){
    final PickingSlotView pickingSlotsView = getPickingSlotViewOrNull();
    if (pickingSlotsView == null) {
        return;
    }
    final ViewId packablesViewId = pickingSlotsView.getParentViewId();
    if (packablesViewId == null) {
        return;
    }
    invalidateView(packablesViewId);
}


@Override
public ProcessPreconditionsResolution checkPreconditionsApplicable(){
    final Optional<HUEditorRow> anyHU = retrieveEligibleHUEditorRows().findAny();
    if (!anyHU.isPresent()) {
        final ITranslatableString reason = Services.get(IMsgBL.class).getTranslatableMsgText(MSG_WEBUI_SELECT_ACTIVE_UNSELECTED_HU);
        return ProcessPreconditionsResolution.reject(reason);
    }
    return ProcessPreconditionsResolution.accept();
}


public PackageableRow getSingleSelectedPackageableRowOrNull(){
    // returnNullIfNotFound=true
    return getSingleSelectedPackageableRow(true);
}


public void addHUIdToCurrentPickingSlot(HuId huId){
    final PickingSlotView pickingSlotsView = getPickingSlotView();
    final PickingSlotRow pickingSlotRow = getPickingSlotRow();
    final PickingSlotId pickingSlotId = pickingSlotRow.getPickingSlotId();
    final ShipmentScheduleId shipmentScheduleId = pickingSlotsView.getCurrentShipmentScheduleId();
    pickingCandidateService.pickHU(PickRequest.builder().shipmentScheduleId(shipmentScheduleId).pickFrom(PickFrom.ofHuId(huId)).pickingSlotId(pickingSlotId).build());
}


public PickingSlotView getPickingSlotView(){
    PickingSlotView pickingSlotsView = getPickingSlotViewOrNull();
    if (pickingSlotsView == null) {
        throw new AdempiereException("PickingSlots view is not available");
    }
    return pickingSlotsView;
}


public Stream<HUEditorRow> retrieveEligibleHUEditorRows(){
    return getView().streamByIds(getSelectedRowIds()).filter(this::isEligible);
}


public void invalidateAndGoBackToPickingSlotsView(){
    // https://github.com/metasfresh/metasfresh-webui-frontend/issues/1447
    // commenting this out because we now close the current view; currently this is a must,
    // because currently the frontend then load *this* view's data into the pickingSlotView
    // invalidateView();
    invalidatePickingSlotsView();
    invalidatePackablesView();
    // After this process finished successfully go back to the picking slots view
    getResult().setWebuiViewToOpen(WebuiViewToOpen.builder().viewId(getPickingSlotView().getViewId().getViewId()).target(ViewOpenTarget.IncludedView).build());
}


public boolean isEligible(HUEditorRow huRow){
    if (!huRow.isTopLevel()) {
        return false;
    }
    if (!huRow.isHUStatusActive()) {
        return false;
    }
    // TODO: add clarity
    if (!huRowReservationMatchesPackageableRow(huRow)) {
        return false;
    }
    // may not yet be a source-HU
    if (sourceHuService.isSourceHu(huRow.getHuId())) {
        return false;
    }
    if (pickingCandidateService.isHuIdPicked(huRow.getHuId())) {
        return false;
    }
    return true;
}


public PickingSlotView getPickingSlotViewOrNull(){
    final ViewId parentViewId = getView().getParentViewId();
    if (parentViewId == null) {
        return null;
    }
    final IView parentView = viewsRepo.getView(parentViewId);
    return PickingSlotView.cast(parentView);
}


public boolean huRowReservationMatchesPackageableRow(HUEditorRow huRow){
    final Optional<OrderLineId> orderLineId = Optional.ofNullable(getSingleSelectedPackageableRowOrNull()).flatMap(PackageableRow::getSalesOrderLineId);
    final ImmutableMultimap<OrderLineId, HUEditorRow> // 
    includedOrderLineReservations = huRow.getIncludedOrderLineReservations();
    if (orderLineId.isPresent()) {
        final int numberOfOrderLineIds = includedOrderLineReservations.keySet().size();
        final boolean reservedForMoreThanOneOrderLine = numberOfOrderLineIds > 1;
        if (reservedForMoreThanOneOrderLine) {
            return false;
        } else if (numberOfOrderLineIds == 1) {
            final boolean reservedForDifferentOrderLine = !includedOrderLineReservations.containsKey(orderLineId.get());
            if (reservedForDifferentOrderLine) {
                return false;
            }
        }
    } else {
        final boolean rowHasHuWithReservation = !includedOrderLineReservations.isEmpty();
        if (rowHasHuWithReservation) {
            return false;
        }
    }
    return true;
}


public PickingSlotRow getPickingSlotRow(){
    final HUEditorView huView = getView();
    final DocumentId pickingSlotRowId = huView.getParentRowId();
    final PickingSlotView pickingSlotView = getPickingSlotView();
    return pickingSlotView.getById(pickingSlotRowId);
}


@Override
public HUEditorView getView(){
    return HUEditorView.cast(super.getView());
}


public PackageableRow getSingleSelectedPackageableRow(boolean returnNullIfNotFound){
    final PickingSlotView pickingSlotView = getPickingSlotView();
    final ViewId packageablesViewId = pickingSlotView.getParentViewId();
    if (packageablesViewId == null) {
        if (returnNullIfNotFound) {
            return null;
        }
        throw new AdempiereException("Packageables view is not available");
    }
    final DocumentId packageableRowId = pickingSlotView.getParentRowId();
    if (packageableRowId == null) {
        if (returnNullIfNotFound) {
            return null;
        }
        throw new AdempiereException("There is no single packageable row selected");
    }
    final PackageableView packageableView = PackageableView.cast(viewsRepo.getView(packageablesViewId));
    return packageableView.getById(packageableRowId);
}


}