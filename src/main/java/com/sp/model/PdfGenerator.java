package com.sp.model;

import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfGenerator {
	 public static void generateBillPdf(String fileName, String billDetails) {
	        try {
	            Document document = new Document();
	            PdfWriter.getInstance(document, new FileOutputStream(fileName));
	            document.open();
	            document.add(new Paragraph("Bill Details:"));
	            document.add(new Paragraph(billDetails));
	            document.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
