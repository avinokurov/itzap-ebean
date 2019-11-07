package com.itzap.ebeans;

public interface EbeanModelBuilderInterface<T extends EbeanModelInterface, B extends EbeanModelBuilderInterface> {
    B setId(Long id);
    B merge(T org);
    T build();
}
