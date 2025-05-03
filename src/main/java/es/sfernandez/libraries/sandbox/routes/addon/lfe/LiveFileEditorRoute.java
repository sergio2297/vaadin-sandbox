package es.sfernandez.libraries.sandbox.routes.addon.lfe;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.jetbrains.annotations.NotNull;
import org.vaadin.addons.sfernandez.lfe.LfeState;
import org.vaadin.addons.sfernandez.lfe.LiveFileEditor;
import org.vaadin.addons.sfernandez.lfe.events.*;
import org.vaadin.addons.sfernandez.lfe.parameters.FileInfo;
import org.vaadin.addons.sfernandez.lfe.parameters.FileType;
import org.vaadin.addons.sfernandez.lfe.setup.LfeAutosaveSetup;
import org.vaadin.addons.sfernandez.lfe.setup.LfeSetup;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;


@Route("live-file-editor")
public class LiveFileEditorRoute
        extends VerticalLayout {

    //---- Attributes ----
    private final LiveFileEditorCC components = new LiveFileEditorCC();
    private final LiveFileEditor lfe = new LiveFileEditor(components.getEditor());

    //---- Constructor ----
    public LiveFileEditorRoute() {
        buildView();
        initComponents();
        addListeners();
    }

    private void buildView() {
        setSizeFull();

        add(components);
    }

    private void initComponents() {
        initMenu();
        initEditor();
        initLogs();
    }

    private void initMenu() {
        initSetupMenu();
        initOperationsMenu();
        initOtherMenu();
    }

    private void initSetupMenu() {
        components.getMiRememberLastDirectory().setCheckable(true);
        components.getMiRememberLastDirectory().setChecked(true);

        components.getMiAllowAllFileTypes().setCheckable(true);
        components.getMiAllowAllFileTypes().setChecked(true);

        components.getMiAllowTextFilesType().setCheckable(true);
        components.getMiAllowImageFilesType().setCheckable(true);
        components.getMiAllowCustomFilesType().setCheckable(true);

        components.getMiAlterPollInterval().setCheckable(true);
        components.getMiAlterPollInterval().setChecked(true);

        components.getMiAutosaveFrequency500ms().setCheckable(true);
        components.getMiAutosaveFrequency5s().setCheckable(true);
        components.getMiAutosaveFrequency1min().setCheckable(true);
    }

    private void initOperationsMenu() {
        components.getMiEnableAutosave().setCheckable(true);
    }

    private void initOtherMenu() {
        components.getMiDisableUiPollInterval().setCheckable(true);

        components.getMiShowEditor().setCheckable(true);
        components.getMiShowEditor().setChecked(true);
        components.getMiEnableEditor().setCheckable(true);
        components.getMiEnableEditor().setChecked(true);
        components.getMiReadOnlyEditor().setCheckable(true);
        components.getMiReadOnlyEditor().setChecked(false);
    }

    private void initEditor() {

    }

    private void initLogs() {
        components.getEventsFilter().setItems(
                List.of(
                        LfeAutosaveWorkingStateChangeEvent.class, LfeCloseFileEvent.class, LfeCreateFileEvent.class,
                        LfeOpenFileEvent.class, LfeSaveFileEvent.class, LfeStateChangeEvent.class, LfeWorkingStateChangeEvent.class
                )
        );
        components.getEventsFilter().setItemLabelGenerator(Class::getSimpleName);
        components.getEventsFilter().select(LfeStateChangeEvent.class);

        components.getOperationsLog().setReadOnly(true);
        components.getListenersLog().setReadOnly(true);
    }

    private void addListeners() {
        addListenersLfe();

        addListenersMenu();
        addListenersEditor();
        addListenersLogs();
    }

    private void addListenersLfe() {
        lfe.observer().addAutosaveWorkingStateChangeListener(event -> {
            components.getMiStartAutosave().setEnabled(!event.isWorking());
            components.getMiStopAutosave().setEnabled(event.isWorking());

            logEventListened(event);
        });

        lfe.observer().addCloseFileListener(event -> {
            if(!event.failed())
                components.getEditor().clear();

            logEventListened(event);
        });
        lfe.observer().addCreateFileListener(this::logEventListened);
        lfe.observer().addOpenFileListener(event -> {
            if(!event.failed() && event.fileInfo().isPresent())
                components.getEditor().setValue(event.fileInfo().get().content());

            logEventListened(event);
        });
        lfe.observer().addSaveFileListener(this::logEventListened);

        lfe.observer().addStateChangeListener(event -> {
            logState(event.state());
            logEventListened(event);
        });

        lfe.observer().addWorkingStateChangeListener(event -> {
            components.getMiStart().setEnabled(!event.isWorking());
            components.getMiStop().setEnabled(event.isWorking());

            logEventListened(event);
        });
    }

    private void addListenersMenu() {
        addListenersSetupMenu();
        addListenersOperationsMenu();
        addListenersOtherMenu();
    }

    private void addListenersSetupMenu() {
        components.getMiRememberLastDirectory().addClickListener(click -> setupLfe());
        components.getMiAllowAllFileTypes().addClickListener(click -> setupLfe());
        components.getMiAllowTextFilesType().addClickListener(click -> setupLfe());
        components.getMiAllowImageFilesType().addClickListener(click -> setupLfe());
        components.getMiAllowCustomFilesType().addClickListener(click -> setupLfe());

        components.getMiAlterPollInterval().addClickListener(click -> setupLfeAutosave());
        components.getMiAutosaveFrequency500ms().addClickListener(click -> onCheckAutosaveFrequencyMenu(click.getSource()));
        components.getMiAutosaveFrequency5s().addClickListener(click -> onCheckAutosaveFrequencyMenu(click.getSource()));
        components.getMiAutosaveFrequency1min().addClickListener(click -> onCheckAutosaveFrequencyMenu(click.getSource()));
    }

    private void addListenersOperationsMenu() {
        components.getMiIsWorking().addClickListener(click ->
                executeOperation(() -> "Is working: " + lfe.isWorking())
        );

        components.getMiStart().addClickListener(click ->
                executeOperation(() -> {
                    components.getEditorParent().add(components.getEditor());
                    return "Start";
                })
        );

        components.getMiStop().addClickListener(click ->
                executeOperation(() -> {
                    components.getEditorParent().remove(components.getEditor());
                    return "Stop";
                })
        );

        components.getMiCreateFile().addClickListener(click ->
                executeOperation(
                        lfe::createFile, fileInfo -> "Create: " + fileInfo.map(FileInfo::name).orElse("-")
                )
        );

        components.getMiOpenFile().addClickListener(click ->
                executeOperation(
                        lfe::openFile, fileInfo -> "Open: " + fileInfo.map(FileInfo::name).orElse("-")
                )
        );

        components.getMiCloseFile().addClickListener(click ->
                executeOperation(
                        lfe::closeFile, fileInfo -> "Close: " + fileInfo.map(FileInfo::name).orElse("-")
                )
        );

        components.getMiSaveFile().addClickListener(click ->
                executeOperation(
                        () -> lfe.saveFile(components.getEditor().getValue()),
                        data -> "Save: " + data.map(this::formatFileContent).orElse("-")
                )
        );

        components.getMiGetState().addClickListener(click ->
                executeOperation(() -> "State: " + lfe.state())
        );

        components.getMiEnableAutosave().addClickListener(click ->
                executeOperation(() -> {
                    lfe.autosave().setEnabled(components.getMiEnableAutosave().isChecked());
                    return "Enable (autosave): " + lfe.autosave().isEnabled();
                })
        );

        components.getMiIsEnabledAutosave().addClickListener(click ->
                executeOperation(() -> "Is enabled (autosave): " + lfe.autosave().isEnabled())
        );

        components.getMiStartAutosave().addClickListener(click ->
                executeOperation(() -> {
                    lfe.autosave().start();
                    return "Autosave started";
                })
        );

        components.getMiStopAutosave().addClickListener(click ->
                executeOperation(() -> {
                    lfe.autosave().stop();
                    return "Autosave stopped";
                })
        );
    }

    private void addListenersOtherMenu() {

        components.getMiShowEditor().addClickListener(click ->
                components.getEditor().setVisible(components.getMiShowEditor().isChecked())
        );

        components.getMiEnableEditor().addClickListener(click ->
                components.getEditor().setEnabled(components.getMiEnableEditor().isChecked())
        );

        components.getMiReadOnlyEditor().addClickListener(click ->
                components.getEditor().setReadOnly(components.getMiReadOnlyEditor().isChecked())
        );
    }

    private void addListenersEditor() {

    }

    private void addListenersLogs() {
        components.getBtnClearLogs().addClickListener(click -> clearLogs());
    }

    //---- Methods ----
    private void clearLogs() {
        components.getOperationsLog().clear();
        components.getListenersLog().clear();
    }

    private void logState(LfeState state) {
        components.getState().getElement().setProperty("innerHTML",
                """
                <h3>State</h3>
                <ul>
                    <li><b>Is working</b>: {$isWorking}</li>
                    <li><b>Is working (autosave)</b>: {$isWorkingAutosave}</li>
                    <li><b>File</b>: {$file}</li>
                    <li><b>Last save time</b>: {$lastSaveTime}</li>
                    <li><b>Last saved data</b>: {$lastSavedData}</li>
                </ul>
                """
                        .replace("{$isWorking}", String.valueOf(state.editorIsWorking()))
                        .replace("{$isWorkingAutosave}", String.valueOf(state.autosaveIsWorking()))
                        .replace("{$file}", state.openedFile().map(this::formatFileInfo).orElse(" - "))
                        .replace("{$lastSaveTime}", state.lastSaveTime().map(this::formatDateTime).orElse(" - "))
                        .replace("{$lastSavedData}", state.lastSaveData().map(this::formatFileContent).orElse(" - "))
        );
    }

    private String formatFileInfo(@NotNull FileInfo fileInfo) {
        return """
                <ul>
                    <li><b>Name</b>: {$name}</li>
                    <li><b>Size</b>: {$size}</li>
                    <li><b>Type</b>: {$type}</li>
                    <li><b>Content</b>: {$content}</li>
                </ul>
                """
                .replace("{$name}", fileInfo.name())
                .replace("{$size}", fileInfo.size().toBytes() + " bytes")
                .replace("{$type}", fileInfo.type())
                .replace("{$content}", formatFileContent(fileInfo.content()));
    }

    private String formatDateTime(@NotNull LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private String formatFileContent(@NotNull String fileContent) {
        StringBuilder string = new StringBuilder();
        string.append("<span style=\"font-size: var(--lumo-font-size-xxs)\">");

        if(fileContent.length() <= 40)
            string.append(fileContent);
        else
            string.append(fileContent, 0, 40).append("...");

        return string.append("</span>").toString();
    }

    private void executeOperation(Supplier<String> operation) {
        try {
            logOperation(operation.get());
        } catch (Exception e) {
            logOperation(e.getMessage());
        }
    }

    private <T> void executeOperation(Supplier<CompletableFuture<T>> operation, Function<T, String> mapper) {
        try {
            operation.get()
                    .thenApply(mapper)
                    .exceptionally(Throwable::getMessage)
                    .thenAccept(this::logOperation);
        } catch (Exception e) {
            logOperation(e.getMessage());
        }
    }

    private void logOperation(String msg) {
        components.getOperationsLog().setValue(
                components.getOperationsLog().getValue() + "- " + msg + "\n"
        );
    }

    private void logEventListened(Object event) {
        if(isEventExcluded(event))
            return;

        components.getListenersLog().setValue(
                components.getListenersLog().getValue() + "- " + event + "\n"
        );
    }

    private boolean isEventExcluded(Object event) {
        return components.getEventsFilter().getSelectedItems().contains(event.getClass());
    }

    private void onCheckAutosaveFrequencyMenu(MenuItem checkedMenu) {
        uncheckAllFrequencyMenus();
        checkedMenu.setChecked(true);

        setupLfeAutosave();
    }

    private void setupLfe() {
        LfeSetup setup = new LfeSetup();

        setup.setRememberLastDirectory(components.getMiRememberLastDirectory().isChecked());
        if(components.getMiAllowAllFileTypes().isChecked())
            setup.allowAllFileTypes();
        else
            setup.setAllowedFileTypes(readAllowedFileTypesConfigured());

        lfe.setup(setup);
    }

    private FileType[] readAllowedFileTypesConfigured() {
        List<FileType> allowedFileTypes = new ArrayList<>();

        if(components.getMiAllowTextFilesType().isChecked())
            allowedFileTypes.add(new FileType("Text", "text/plain", ".txt"));
        if(components.getMiAllowImageFilesType().isChecked())
            allowedFileTypes.add(new FileType("Images", "image/png", ".png", ".gif", ".jpg"));
        if(components.getMiAllowCustomFilesType().isChecked())
            allowedFileTypes.add(new FileType("Custom", "text/plain", ".cust"));

        return allowedFileTypes.toArray(FileType[]::new);
    }

    private void setupLfeAutosave() {
        LfeAutosaveSetup setup = new LfeAutosaveSetup.Builder()
                .allowToAlterUiPollInterval(components.getMiAlterPollInterval().isChecked())
                .frequency(readAutosaveFrequencyConfigured())
                .dataToSaveSupplier(components.getEditor()::getValue)
                .build();

        lfe.autosave().setup(setup);
    }

    private void uncheckAllFrequencyMenus() {
        components.getMiAutosaveFrequency500ms().setChecked(false);
        components.getMiAutosaveFrequency5s().setChecked(false);
        components.getMiAutosaveFrequency1min().setChecked(false);
    }

    private Duration readAutosaveFrequencyConfigured() {
        if(components.getMiAutosaveFrequency500ms().isChecked())
            return Duration.ofMillis(500);
        if(components.getMiAutosaveFrequency5s().isChecked())
            return Duration.ofSeconds(5);
        if(components.getMiAutosaveFrequency1min().isChecked())
            return Duration.ofMinutes(1);

        return null;
    }
}
