package com.sinch.rcssdk.rcs.message.component.richcard;

public class FileInfo {
    private String mime_type;
    private int file_size;
    private String file_name;
    private String file_uri;

    public FileInfo() {

    }

    public FileInfo(String mime_type, int file_size, String file_name, String file_uri) {
        this.mime_type = mime_type;
        this.file_size = file_size;
        this.file_name = file_name;
        this.file_uri = file_uri;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public int getFile_size() {
        return file_size;
    }

    public void setFile_size(int file_size) {
        this.file_size = file_size;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_uri() {
        return file_uri;
    }

    public void setFile_uri(String file_uri) {
        this.file_uri = file_uri;
    }

}
