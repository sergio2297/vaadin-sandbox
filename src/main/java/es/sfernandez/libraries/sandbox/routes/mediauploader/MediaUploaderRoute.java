package es.sfernandez.libraries.sandbox.routes.mediauploader;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import es.sfernandez.libraries.library4vaadin.components.media.uploader.mediaviews.AudioFileView;
import es.sfernandez.libraries.library4vaadin.components.media.uploader.mediaviews.ImageFileView;
import es.sfernandez.libraries.library4vaadin.components.media.uploader.mediaviews.VideoFileView;

@Route("media-uploader")
public class MediaUploaderRoute
        extends VerticalLayout {

    //---- Constants and Definitions ----

    //---- Attributes ----
    private final MediaUploaderCC components = new MediaUploaderCC();

    //---- Constructor ----
    public MediaUploaderRoute() {
        buildView();
        initComponents();
        addListeners();
    }

    private void buildView() {
        setSizeFull();

        add(components);
    }

    private void initComponents() {
        components.getMediaUploader().addAcceptedMediaFileView(new ImageFileView(), new VideoFileView(), new AudioFileView());
    }

    private void addListeners() {
    }

    //---- Methods ----
}
