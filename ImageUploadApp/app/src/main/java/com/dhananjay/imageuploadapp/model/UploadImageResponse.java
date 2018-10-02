package com.dhananjay.imageuploadapp.model;


public class UploadImageResponse {

    String name;
    String bucket;
    String generation;
    String metageneration;
    String contentType;
    String timeCreated;
    String updated;
    String storageClass;
    String size;
    String md5Hash;
    String contentEncoding;
    String contentDisposition;
    String crc32c;
    String etag;
    String downloadTokens;

    public String getImage() {
        return "https://firebasestorage.googleapis.com/v0/b/"
                + bucket
                + "/o"
                + "/"
                + name
                + "?alt=media"
                + "&token=" + downloadTokens;
    }
}
