package es.sfernandez.libraries.sandbox.routes.library4vaadin.interactivelist;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import es.sfernandez.libraries.library4vaadin.components.VaadinComponent;
import es.sfernandez.libraries.library4vaadin.components.inputs.interactivelist.container.InteractiveListContainer;
import es.sfernandez.libraries.library4vaadin.components.inputs.interactivelist.InteractiveList;
import es.sfernandez.libraries.library4vaadin.components.inputs.interactivelist.InteractiveListNewItemRequester;
import es.sfernandez.libraries.library4vaadin.components.inputs.interactivelist.items.InteractiveListItemWrapper;
import org.jetbrains.annotations.NotNull;

@Route("l4vaadin/interactive-list")
public class InteractiveListRoute
        extends VerticalLayout {

    //---- Constants y Definitions ----
    private class TextFieldItem
            extends TextField
            implements VaadinComponent {

        @NotNull
        @Override
        public Component asVaadinComponent() {
            return this;
        }
    }

    //---- Attributes ----
    private final InteractiveListCC components = new InteractiveListCC();

    private final InteractiveList<TextFieldItem> itemList =
            new InteractiveList<>(
                    InteractiveListNewItemRequester.Companion.fromButton(components.getBtnAddItem()),
                    () -> new InteractiveListItemWrapper<>(new TextFieldItem()),
                    InteractiveListContainer.Companion.fromLayout(components.getLytItems()),
                    3, 3
            );

    //---- Constructor ----
    public InteractiveListRoute() {
        buildView();
        initComponents();
        addListeners();
    }

    private void buildView() {
        setSizeFull();
        add(components);
    }

    private void initComponents() {

    }

    private void addListeners() {

    }

    //---- Methods ----

}
