package es.sfernandez.libraries.sandbox.routes.mediauploader;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import es.sfernandez.libraries.library4vaadin.components.media.uploader.MediaFileUploaderComponent;

class MediaUploaderCC
        extends HorizontalLayout {

    //---- Attributes ----
    private final MediaFileUploaderComponent mediaUploader = new MediaFileUploaderComponent();

    //---- Constructor ----
    public MediaUploaderCC() {
        buildView();
    }

    private void buildView() {
        setSizeFull();

        mediaUploader.setMaxWidth("20rem");
        mediaUploader.setMaxHeight("20rem");

        add(mediaUploader);
    }

    //---- Methods ----
    public MediaFileUploaderComponent getMediaUploader() {
        return mediaUploader;
    }
}
