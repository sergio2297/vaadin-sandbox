package es.sfernandez.libraries.sandbox.routes.addon.lfe;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.theme.lumo.LumoUtility;

class LiveFileEditorCC
        extends HorizontalLayout {

    //---- Attributes ----
    private MenuItem miRememberLastDirectory, miAllowAllFileTypes,
            miAllowTextFilesType, miAllowImageFilesType, miAllowCustomFilesType,
            miAlterPollInterval,
            miAutosaveFrequency500ms, miAutosaveFrequency5s, miAutosaveFrequency1min;
    private MenuItem miIsWorking, miStart, miStop,
            miCreateFile, miOpenFile, miCloseFile, miSaveFile,
            miGetState,
            miEnableAutosave, miIsEnabledAutosave, miIsWorkingAutosave, miStartAutosave, miStopAutosave;
    private MenuItem miDisableUiPollInterval, miGetUiPollInterval, miShowEditor, miEnableEditor, miReadOnlyEditor;

    private final Div editorParent = new Div();
    private final TextArea editor = new TextArea("Editor");

    private final MultiSelectComboBox<Class<?>> eventsFilter = new MultiSelectComboBox<>("Excluded events");
    private final Button btnClearLogs = new Button("Clear logs", VaadinIcon.CLOSE.create());
    private final Span state = new Span();
    private final TextArea operationsLog = new TextArea("Operations"),
        listenersLog = new TextArea("Listeners");

    //---- Constructor ----
    public LiveFileEditorCC() {
        buildView();
    }

    private void buildView() {
        setSizeFull();

        Component lytEditor = buildLytEditor();
        Component lytLogs = buildLytLogs();

        add(lytEditor, lytLogs);
        setFlexGrow(2, lytEditor);
        setFlexGrow(1, lytLogs);
    }

    private Component buildLytEditor() {
        VerticalLayout lyt = new VerticalLayout();
        lyt.setSizeFull();

        editorParent.setSizeFull();
        editor.setSizeFull();
        editorParent.add(editor);

        lyt.add(buildMenu());
        lyt.addAndExpand(editorParent);

        return lyt;
    }

    private MenuBar buildMenu() {
        MenuBar menu = new MenuBar();

        addSetupSubMenu(menu);
        addOperationsSubMenu(menu);
        addOtherSubMenu(menu);

        return menu;
    }

    private void addSetupSubMenu(MenuBar menu) {
        MenuItem setup = menu.addItem("Setup");
        miRememberLastDirectory = setup.getSubMenu().addItem("Remember last directory");

        MenuItem allowedFileTypes = setup.getSubMenu().addItem("Allowed file types");
        miAllowAllFileTypes = allowedFileTypes.getSubMenu().addItem("All");
        miAllowTextFilesType = allowedFileTypes.getSubMenu().addItem("Text Files");
        miAllowImageFilesType = allowedFileTypes.getSubMenu().addItem("Image Files");
        miAllowCustomFilesType = allowedFileTypes.getSubMenu().addItem("Custom Files (.cust)");

        MenuItem autosave = setup.getSubMenu().addItem("Autosave");
        miAlterPollInterval = autosave.getSubMenu().addItem("Alter UI poll interval");
        MenuItem autosaveFrequency = autosave.getSubMenu().addItem("Frequency");
        miAutosaveFrequency500ms = autosaveFrequency.getSubMenu().addItem("500 ms");
        miAutosaveFrequency5s = autosaveFrequency.getSubMenu().addItem("5 s");
        miAutosaveFrequency1min = autosaveFrequency.getSubMenu().addItem("1 min");
    }

    private void addOperationsSubMenu(MenuBar menu) {
        MenuItem operations = menu.addItem("Operations");

        miIsWorking = operations.getSubMenu().addItem("Is working");
        miStart = operations.getSubMenu().addItem("Start (Attach TextArea used as editor)");
        miStop = operations.getSubMenu().addItem("Stop (Detach TextArea used as editor)");

        operations.getSubMenu().addSeparator();

        miCreateFile = operations.getSubMenu().addItem("Create file");
        miOpenFile = operations.getSubMenu().addItem("Open file");
        miCloseFile = operations.getSubMenu().addItem("Close file");
        miSaveFile = operations.getSubMenu().addItem("Save file");

        operations.getSubMenu().addSeparator();

        miGetState = operations.getSubMenu().addItem("Get state");

        operations.getSubMenu().addSeparator();

        MenuItem autosave = operations.getSubMenu().addItem("Autosave");
        miEnableAutosave = autosave.getSubMenu().addItem("Enable");
        miIsEnabledAutosave = autosave.getSubMenu().addItem("Is enable");
        miIsWorkingAutosave = autosave.getSubMenu().addItem("Is working");
        miStartAutosave = autosave.getSubMenu().addItem("Start");
        miStopAutosave = autosave.getSubMenu().addItem("Stop");
    }

    private void addOtherSubMenu(MenuBar menu) {
        MenuItem other = menu.addItem("Other");

        miDisableUiPollInterval = other.getSubMenu().addItem("Disable UI poll interval (500ms)");
        miGetUiPollInterval = other.getSubMenu().addItem("Get UI poll interval");

        other.getSubMenu().addSeparator();

        MenuItem editor = other.getSubMenu().addItem("Editor (TextArea)");
        miShowEditor = editor.getSubMenu().addItem("Visible");
        miEnableEditor = editor.getSubMenu().addItem("Enable");
        miReadOnlyEditor = editor.getSubMenu().addItem("Read only");
    }

    private Component buildLytLogs() {
        VerticalLayout lyt = new VerticalLayout();
        lyt.setSizeFull();
        lyt.addClassNames(LumoUtility.Gap.XSMALL);

        state.setWidthFull();
        operationsLog.setSizeFull();
        listenersLog.setSizeFull();

        HorizontalLayout lytControls = new HorizontalLayout();
        lytControls.setWidthFull();
        lytControls.setJustifyContentMode(JustifyContentMode.BETWEEN);
        lytControls.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        eventsFilter.setWidth("19rem");
        lytControls.add(eventsFilter, btnClearLogs);

        lyt.add(lytControls, state, operationsLog, listenersLog);

        return lyt;
    }

    //---- Methods ----
    public MenuItem getMiRememberLastDirectory() {
        return miRememberLastDirectory;
    }

    public MenuItem getMiAllowAllFileTypes() {
        return miAllowAllFileTypes;
    }

    public MenuItem getMiAllowTextFilesType() {
        return miAllowTextFilesType;
    }

    public MenuItem getMiAllowImageFilesType() {
        return miAllowImageFilesType;
    }

    public MenuItem getMiAllowCustomFilesType() {
        return miAllowCustomFilesType;
    }

    public MenuItem getMiAlterPollInterval() {
        return miAlterPollInterval;
    }

    public MenuItem getMiAutosaveFrequency500ms() {
        return miAutosaveFrequency500ms;
    }

    public MenuItem getMiAutosaveFrequency5s() {
        return miAutosaveFrequency5s;
    }

    public MenuItem getMiAutosaveFrequency1min() {
        return miAutosaveFrequency1min;
    }

    public MenuItem getMiIsWorking() {
        return miIsWorking;
    }

    public MenuItem getMiStart() {
        return miStart;
    }

    public MenuItem getMiStop() {
        return miStop;
    }

    public MenuItem getMiCreateFile() {
        return miCreateFile;
    }

    public MenuItem getMiOpenFile() {
        return miOpenFile;
    }

    public MenuItem getMiCloseFile() {
        return miCloseFile;
    }

    public MenuItem getMiSaveFile() {
        return miSaveFile;
    }

    public MenuItem getMiGetState() {
        return miGetState;
    }

    public MenuItem getMiEnableAutosave() {
        return miEnableAutosave;
    }

    public MenuItem getMiIsEnabledAutosave() {
        return miIsEnabledAutosave;
    }

    public MenuItem getMiIsWorkingAutosave() {
        return miIsWorkingAutosave;
    }

    public MenuItem getMiStartAutosave() {
        return miStartAutosave;
    }

    public MenuItem getMiStopAutosave() {
        return miStopAutosave;
    }

    public MenuItem getMiDisableUiPollInterval() {
        return miDisableUiPollInterval;
    }

    public MenuItem getMiGetUiPollInterval() {
        return miGetUiPollInterval;
    }

    public MenuItem getMiShowEditor() {
        return miShowEditor;
    }

    public MenuItem getMiEnableEditor() {
        return miEnableEditor;
    }

    public MenuItem getMiReadOnlyEditor() {
        return miReadOnlyEditor;
    }

    public Div getEditorParent() {
        return editorParent;
    }

    public TextArea getEditor() {
        return editor;
    }

    public MultiSelectComboBox<Class<?>> getEventsFilter() {
        return eventsFilter;
    }

    public Button getBtnClearLogs() {
        return btnClearLogs;
    }

    public Span getState() {
        return state;
    }

    public TextArea getOperationsLog() {
        return operationsLog;
    }

    public TextArea getListenersLog() {
        return listenersLog;
    }
}
