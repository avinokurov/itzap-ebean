package com.itzap.ebeans;


import com.itzap.common.exception.IZapException;

public interface EbeanErrorHandler<R> {
    IZapException handle(R sql, IZapException exception);
    IZapException handle(R sql, Exception ex);
}
