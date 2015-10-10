package co.certicamara.portalfunctionary.api.representations;

public enum SupportedFileTypes {

    ODT("odt"), PDF("pdf"), SERIALIZED("ser"), pdf("pdf"), docx("docx"), doc("doc"), 
    jpg("jpg"), png ("png"), TIF ("tif"), tif ("tif");

    private final String extension;

    private SupportedFileTypes(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}