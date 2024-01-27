package com.sp.service;

import org.springframework.stereotype.Service;

import com.sp.pdf.PdfGenerator;

@Service
public class PdfService {
	public String generateBillPdf(String orderId) {
        // Logic to generate PDF based on the order ID
        String fileName = "bill_" + orderId + ".pdf";
        PdfGenerator.generateBillPdf(fileName, "Bill details for order ID: " + orderId);
        return fileName;
    }
}
