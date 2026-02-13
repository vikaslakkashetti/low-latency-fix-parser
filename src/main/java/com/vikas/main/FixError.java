package com.vikas.main;

public enum FixError {

    NO_ERROR(0),

    MALFORMED_TAG(1),
    MALFORMED_FIELD(2),
    INVALID_NUMERIC(3),

    INVALID_HEADER_ORDER(10),

    BODY_LENGTH_MISMATCH(20),
    INVALID_CHECKSUM(21);

    private final int code;

    FixError(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}