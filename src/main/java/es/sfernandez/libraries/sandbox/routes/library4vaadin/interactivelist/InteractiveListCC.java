package es.sfernandez.libraries.sandbox.routes.library4vaadin.interactivelist;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class InteractiveListCC
        extends VerticalLayout {

    //---- Attributes ----
    private final VerticalLayout lytItems = new VerticalLayout();
    private final Button btnAddItem = new Button(VaadinIcon.PLUS_CIRCLE_O.create());

    //---- Constructor ----
    public InteractiveListCC() {
        buildView();
    }

    private void buildView() {
        add(lytItems, btnAddItem);
    }

    //---- Methods ----
    public VerticalLayout getLytItems() {
        return lytItems;
    }

    public Button getBtnAddItem() {
        return btnAddItem;
    }
}
