package ru.selsup.dev;

import lombok.Data;

@Data
public class Body {
    private Document product_document;
    private Document_format document_format = Document_format.MANUAL;
    private String type = "LP_INTRODUCE_GOODS";
    private String signature;


}
