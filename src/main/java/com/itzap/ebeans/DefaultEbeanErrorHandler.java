package com.itzap.ebeans;

import com.itzap.common.exception.IZapException;
import com.itzap.ebeans.execptions.EBeanErrors;

import java.util.Objects;

public class DefaultEbeanErrorHandler<R> implements EbeanErrorHandler<R> {
    @Override
    public IZapException handle(R sql, IZapException exception) {
        return new IZapException(String.format("SQL request failed. Statement=%s", Objects.toString(sql)),
                exception.getErrorCode(), exception);
    }

    @Override
    public IZapException handle(R sql, Exception ex) {
        return new IZapException(String.format("SQL request failed. Statement=%s", Objects.toString(sql)),
                EBeanErrors.SQL_SERVER_INIT, ex);
    }
}
