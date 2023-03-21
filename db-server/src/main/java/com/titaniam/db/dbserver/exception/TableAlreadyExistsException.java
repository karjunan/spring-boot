package com.titaniam.db.dbserver.exception;

public class TableAlreadyExistsException extends RuntimeException {

    public TableAlreadyExistsException(String data) {
        super(data);
    }
}
