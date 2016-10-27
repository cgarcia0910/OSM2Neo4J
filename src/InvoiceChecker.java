//package com.skatestown.invoice;

import java.io.InputStream;

/**
 * SkatesTown invoice checker
 */
public interface InvoiceChecker {
    /**
     * Check invoice totals.
     *
     * @param       invoiceXML Invoice XML document
     * @exception   Exception  Any exception returned during checking
     */
    void checkInvoice(InputStream invoiceXML) throws Exception;
}