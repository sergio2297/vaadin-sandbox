package es.sfernandez.libraries.sandbox.routes.quicktests;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("quick-tests/disabled-return-channel")
public class DisabledReturnChannelRoute
        extends HorizontalLayout {

    //---- Attributes ----
    private final Button btnSwitchState = new Button("Switch state", VaadinIcon.EXCHANGE.create());
    private final TextField txt = new TextField("Input");
    private final Button btnExecJs = new Button("Execute JS");

    //---- Constructor ----
    public DisabledReturnChannelRoute() {
        buildView();
        initComponents();
        addListeners();
    }

    private void buildView() {
        setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        add(btnSwitchState, txt, btnExecJs);
    }

    private void initComponents() {

    }

    private void addListeners() {
        btnSwitchState.addClickListener(click -> txt.setEnabled(!txt.isEnabled()));
        btnExecJs.addClickListener(click -> doJsRequest());
    }

    private void doJsRequest() {
        txt.getElement().executeJs("""
                        {
                            const isNull = $0 === null;
                            alert(isNull);
                            return isNull;
                        }
                        """, blankToNull(txt.getValue()))
                .toCompletableFuture(Boolean.class)
                .thenAccept(isNull -> Notification.show(isNull ? "Is null" : "Is not null"));
    }

    //---- Methods ----
    private String blankToNull(String string) {
        if(string != null && string.isBlank())
            return null;

        return string;
    }

}
