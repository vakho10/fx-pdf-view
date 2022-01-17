package io.github.vakho10.pdf;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PDFView extends ImageView {

    private static final String DEFAULT_STYLE_CLASS = "pdf-view";
    private static final int DEFAULT_PAGE_INDEX = 0;

    private PDFRenderer renderer;

    private ObjectProperty<PDDocument> document = new ObjectPropertyBase<>() {

        @Override
        public Object getBean() {
            return PDFView.this;
        }

        @Override
        public String getName() {
            return "document";
        }
    };

    private IntegerProperty page = new SimpleIntegerProperty(DEFAULT_PAGE_INDEX);
    private IntegerProperty numberOfPages = new SimpleIntegerProperty();

    {
        document.addListener(this::onDocumentChange);
        page.addListener(this::onPageChange);
    }

    public PDFView() {
        super();
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    public PDFView(PDDocument document) {
        this();
        setDocument(document);
    }

    public PDDocument getDocument() {
        return document.get();
    }

    public ObjectProperty<PDDocument> documentProperty() {
        return document;
    }

    public void setDocument(PDDocument document) {
        this.document.set(document);
    }

    public int getPage() {
        return page.get();
    }

    public IntegerProperty pageProperty() {
        return page;
    }

    public void setPage(int page) {
        this.page.set(page);
    }

    public int getNumberOfPages() {
        return numberOfPages.get();
    }

    public IntegerProperty numberOfPagesProperty() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages.set(numberOfPages);
    }

    public void onDocumentChange(ObservableValue<? extends PDDocument> observable, PDDocument oldValue, PDDocument newValue) {
        // Reset the page index
        setPage(DEFAULT_PAGE_INDEX);

        // Set number of pages
        setNumberOfPages(newValue.getNumberOfPages());

        // Recreate the renderer and updage image
        renderer = new PDFRenderer(newValue);
        updateImage();
    }

    private void onPageChange(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        updateImage();
    }

    private void updateImage() {
        try {
            BufferedImage bufferedImage = renderer.renderImage(getPage());
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                ImageIO.write(bufferedImage, "JPG", byteArrayOutputStream);
                try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray())) {
                    setImage(new Image(byteArrayInputStream));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
