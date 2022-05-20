import java.util.List;

public class CsvDocument {

    private String fileName;
    private List<String> headerNames;
    private List<String[]> content;

    public CsvDocument(){}

    public CsvDocument(String fileName, List<String> headerNames, List<String[]> content) {
        this.fileName = fileName;
        this.headerNames = headerNames;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getHeaderNames() {
        return headerNames;
    }

    public void setHeaderNames(List<String> headerNames) {
        this.headerNames = headerNames;
    }

    public List<String[]> getContent() {
        return content;
    }

    public void setContent(List<String[]> content) {
        this.content = content;
    }
}
