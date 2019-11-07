package com.itzap.ebeans.execptions;

import com.itzap.common.ErrorCode;

public enum EBeanErrors implements ErrorCode {
    SQL_SERVER_TRANS_ERROR("itzap.ebean.server-trans-error", "SQL transaction error"),
    SQL_SERVER_INIT("itzap.ebean.init", "SQL init/statement error"),
    SQL_SERVER_JSON_MAPPING("itzap.ebean.json-mapping", "SQL JSON mapping error")
    ;


    private final String errorCode;
    private final String message;

    EBeanErrors(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
