/*
 * Copyright (c) 2026 vinuth sri arampath
 *
 * This code is the intellectual property of vinuth sri arampath and is protected under copyright law.
 * Unauthorized copying, modification, distribution, or use of this code, in whole or in part,
 * without prior written permission is strictly prohibited.
 *
 * Portions of this code may be generated with AI and modified by vinuth sri arampath
 * All rights reserved.
 *
 *
 */

package edu.vinu.domain.openPdf.service.impl;

import edu.vinu.domain.openPdf.service.InvoicePdfGeneratorService;
import edu.vinu.domain.payment.entity.Payment;
import edu.vinu.domain.student_batch_enrollment.entity.StudentBatchEnrollment;
import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static edu.vinu.domain.openPdf.font.Fonts.*;
import static edu.vinu.domain.openPdf.table.TableCell.createLabelCell;
import static edu.vinu.domain.openPdf.table.TableCell.createValueCell;

@Component
public class InvoicePdfGeneratorServiceImpl implements InvoicePdfGeneratorService {
    @Override
    public byte[] generate(StudentBatchEnrollment enrollment, Payment payment) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4);

            PdfWriter.getInstance(document, outputStream);

            document.open();

//          Invoice Header
            Paragraph title = new Paragraph("TuitionToAll", titleFont());
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

//          Subtitle
            Paragraph subtitle = new Paragraph("Course Registration Invoice", sectionFont());
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);

            document.add(new Paragraph(" "));

//          Invoice Information Section
            Paragraph invoiceInfo = new Paragraph("Invoice Information", sectionFont());
            invoiceInfo.setAlignment(Element.ALIGN_LEFT);
            document.add(invoiceInfo);

            document.add(new Paragraph(" "));

            PdfPTable invoiceInfoTable = new PdfPTable(2);

            invoiceInfoTable.setWidthPercentage(100);

            invoiceInfoTable.addCell(createLabelCell("Transaction Reference"));
            invoiceInfoTable.addCell(createValueCell(payment.getTransactionRef()));
            invoiceInfoTable.addCell(createLabelCell("Date"));
            invoiceInfoTable.addCell(createValueCell(payment.getCreatedDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH))));
            invoiceInfoTable.addCell(createLabelCell("Time"));
            invoiceInfoTable.addCell(createValueCell(payment.getCreatedDate().format(DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH))));

            document.add(invoiceInfoTable);

            document.add(new Paragraph(" "));

//          Student Information Section
            Paragraph studentInfo = new Paragraph("Student Information", sectionFont());
            studentInfo.setAlignment(Element.ALIGN_LEFT);
            document.add(studentInfo);

            document.add(new Paragraph(" "));

            PdfPTable studentInfoTable = new PdfPTable(2);
            studentInfoTable.setWidthPercentage(100);

            studentInfoTable.addCell(createLabelCell("Name"));
            studentInfoTable.addCell(createValueCell(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName()));
            studentInfoTable.addCell(createLabelCell("Email"));
            studentInfoTable.addCell(createValueCell(enrollment.getStudent().getUser().getEmail()));
            studentInfoTable.addCell(createLabelCell("Contact"));
            studentInfoTable.addCell(createValueCell(enrollment.getStudent().getUser().getContact()));

            document.add(studentInfoTable);

            document.add(new Paragraph(" "));

//          Course Information Section

            Paragraph courseBatchInfo = new Paragraph("Course & Batch Information", sectionFont());
            courseBatchInfo.setAlignment(Element.ALIGN_LEFT);
            document.add(courseBatchInfo);

            document.add(new Paragraph(" "));

            PdfPTable courseBatchTable = new PdfPTable(2);
            courseBatchTable.setWidthPercentage(100);

            courseBatchTable.addCell(createLabelCell("Course Title"));
            courseBatchTable.addCell(createValueCell(enrollment.getBatch().getCourse().getTitle()));
            courseBatchTable.addCell(createLabelCell("Batch Name"));
            courseBatchTable.addCell(createValueCell(enrollment.getBatch().getName()));
            courseBatchTable.addCell(createLabelCell("Start Date"));
            courseBatchTable.addCell(createValueCell(enrollment.getBatch().getStart_date().format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH))));
            courseBatchTable.addCell(createLabelCell("End Time"));
            courseBatchTable.addCell(createValueCell(enrollment.getBatch().getStart_time().format(DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH))));

            document.add(courseBatchTable);

            document.add(new Paragraph(" "));

//          Payment Information

            Paragraph paymentInfo = new Paragraph("Payment", sectionFont());
            paymentInfo.setAlignment(Element.ALIGN_LEFT);
            document.add(paymentInfo);

            document.add(new Paragraph(" "));

            PdfPTable paymentInfoTable = new PdfPTable(2);
            paymentInfoTable.setWidthPercentage(100);

            paymentInfoTable.addCell(createLabelCell("Amount"));
            paymentInfoTable.addCell(createValueCell("LKR : "+payment.getAmount().toString()));
            paymentInfoTable.addCell(createLabelCell("Method"));
            paymentInfoTable.addCell(createValueCell(payment.getPaymentMethod().toString()));
            paymentInfoTable.addCell(createLabelCell("Status"));
            paymentInfoTable.addCell(createValueCell(payment.getStatus().toString()));

            document.add(paymentInfoTable);

            document.add(new Paragraph(" "));

            Paragraph thankYou = new Paragraph("Thank you for your payment.", labelFont());
            thankYou.setAlignment(Element.ALIGN_CENTER);
            document.add(thankYou);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }
    }
}
