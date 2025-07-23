package com.soumya.urlshortener.exception;

public class ResourceNotFoundException extends RuntimeException {

    private String resourec;
    private String field;
    private String value;

    public ResourceNotFoundException(String resourec, String field, String value) {
        super(String.format("%s Not Found With %s : %s",resourec,field,value));
        this.resourec = resourec;
        this.field = field;
        this.value = value;
    }
}
