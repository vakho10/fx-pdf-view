package io.github.vakho10.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PageDrawerParameters;

import java.io.IOException;

public class PDFRenderer extends org.apache.pdfbox.rendering.PDFRenderer {

    PDFRenderer(PDDocument document) {
        super(document);
    }

    @Override
    protected PageDrawer createPageDrawer(PageDrawerParameters parameters) throws IOException {
        return new PageDrawer(parameters);
    }
}
