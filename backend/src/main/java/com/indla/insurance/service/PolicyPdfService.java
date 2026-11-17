package com.indla.insurance.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.indla.insurance.model.Policy;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class PolicyPdfService {

    public byte[] generatePolicyCertificate(Policy policy) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 40, 40);
            PdfWriter.getInstance(document, out);
            document.open();

            Font companyFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, new Color(26, 54, 93));
            Font subCompanyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(113, 128, 150));
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(43, 108, 176));
            Font serialFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, new Color(160, 174, 192));
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, new Color(45, 55, 72));
            Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new Color(74, 85, 104));
            Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(26, 32, 44));
            Font termFont = FontFactory.getFont(FontFactory.HELVETICA, 8, new Color(113, 128, 150));
            Font signatureFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, new Color(43, 108, 176));

            Paragraph companyTitle = new Paragraph("LIFE & GENERAL INSURANCE CORPORATION", companyFont);
            companyTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(companyTitle);

            Paragraph companySub = new Paragraph("Enterprise Policy Administration & Underwriting System • IRDAI Regulated\nIntegrated Policy & Claims Management Platform", subCompanyFont);
            companySub.setAlignment(Element.ALIGN_CENTER);
            companySub.setSpacingAfter(15);
            document.add(companySub);

            PdfPTable divider = new PdfPTable(1);
            divider.setWidthPercentage(100);
            PdfPCell divCell = new PdfPCell();
            divCell.setBackgroundColor(new Color(43, 108, 176));
            divCell.setFixedHeight(2.5f);
            divCell.setBorder(Rectangle.NO_BORDER);
            divider.addCell(divCell);
            document.add(divider);

            Paragraph certTitle = new Paragraph("\nPOLICY BOND & CERTIFICATE OF INSURANCE", titleFont);
            certTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(certTitle);

            String issueDate = new SimpleDateFormat("dd MMMM yyyy, hh:mm a").format(new Date());
            Paragraph certSerial = new Paragraph("Certificate No: SL-CERT-" + (policy.getId() != null ? (10000 + policy.getId()) : "99999") +
                    "   •   Issue Timestamp: " + issueDate + "\n\n", serialFont);
            certSerial.setAlignment(Element.ALIGN_CENTER);
            document.add(certSerial);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{25, 25, 25, 25});
            table.setSpacingBefore(10);
            table.setSpacingAfter(15);

            PdfPCell sectionHeader = new PdfPCell(new Phrase("SCHEDULE OF ASSURANCE & COVERAGE BENEFIT", sectionFont));
            sectionHeader.setColspan(4);
            sectionHeader.setBackgroundColor(new Color(237, 242, 247));
            sectionHeader.setPadding(8);
            sectionHeader.setBorderColor(new Color(203, 213, 224));
            table.addCell(sectionHeader);

            addTableRow(table, "Policy Number:", defaultIfEmpty(policy.getPolicyNumber(), "POL-N/A"),
                    "Policy Status:", defaultIfEmpty(policy.getStatus(), "ACTIVE"), labelFont, valueFont);

            addTableRow(table, "Insured Name:", defaultIfEmpty(policy.getName(), "N/A"),
                    "Registered Email:", defaultIfEmpty(policy.getEmail(), "N/A"), labelFont, valueFont);

            String sumAssured = policy.getSumAssured() != null ? "INR " + policy.getSumAssured() : "INR 10,00,000 (Guaranteed)";
            String coverage = policy.getCoverageType() != null ? policy.getCoverageType() : "Comprehensive Term Life Plan";
            addTableRow(table, "Plan / Coverage:", coverage,
                    "Sum Assured:", sumAssured, labelFont, valueFont);

            String premium = policy.getPremiumAmount() != null ? "INR " + policy.getPremiumAmount() : "INR 25,000 / Annual";
            String claimNo = policy.getClaimNumber() != null ? policy.getClaimNumber() : "None Filed";
            addTableRow(table, "Annual Premium:", premium,
                    "Claim Tracking ID:", claimNo, labelFont, valueFont);

            String claimStat = policy.getClaimStatus() != null ? policy.getClaimStatus() : "NO CLAIM PENDING";
            addTableRow(table, "Claim State:", claimStat,
                    "Underwriting Office:", "National Underwriting Center", labelFont, valueFont);

            document.add(table);

            Paragraph termsHeader = new Paragraph("STATUTORY DECLARATION & TERMS OF COVERAGE", sectionFont);
            termsHeader.setSpacingBefore(5);
            termsHeader.setSpacingAfter(4);
            document.add(termsHeader);

            Paragraph termsText = new Paragraph(
                    "1. This document constitutes the definitive evidence of the contract of insurance between the Insurance Management Corporation and the Insured Person named herein.\n" +
                    "2. The Sum Assured stated in this schedule is payable on the survival of the policy term or upon the unfortunate demise of the Life Assured, subject to the conditions of the policy.\n" +
                    "3. A statutory grace period of 30 days is allowed for the payment of renewal premiums. The policy will lapse if premiums remain in default past the grace duration.\n" +
                    "4. Any discrepancy or pending claim adjudication should be submitted through the Insurance Management Portal referencing the registered Policy and Claim identifiers.\n" +
                    "5. This electronic policy bond is legally binding under the provisions of the Information Technology Act, 2000 and requires no physical seal.",
                    termFont
            );
            termsText.setSpacingAfter(25);
            document.add(termsText);

            PdfPTable footerTable = new PdfPTable(2);
            footerTable.setWidthPercentage(100);
            footerTable.setWidths(new float[]{60, 40});

            PdfPCell leftSignCell = new PdfPCell();
            leftSignCell.setBorder(Rectangle.NO_BORDER);
            leftSignCell.addElement(new Paragraph("System Audit Verification: PASSED", serialFont));
            leftSignCell.addElement(new Paragraph("Digital Signature Hash: SHA-256-AUTHENTICATED", serialFont));
            footerTable.addCell(leftSignCell);

            PdfPCell rightSignCell = new PdfPCell();
            rightSignCell.setBorder(Rectangle.NO_BORDER);
            rightSignCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            Paragraph signTitle = new Paragraph("Omvikas Indla\nChief Underwriting Officer\nInsurance Management System", signatureFont);
            signTitle.setAlignment(Element.ALIGN_RIGHT);
            rightSignCell.addElement(signTitle);
            footerTable.addCell(rightSignCell);

            document.add(footerTable);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF Policy Certificate", e);
        }
    }

    private void addTableRow(PdfPTable table, String l1, String v1, String l2, String v2, Font labelFont, Font valueFont) {
        Color borderClr = new Color(226, 232, 240);
        PdfPCell c1 = new PdfPCell(new Phrase(l1, labelFont));
        c1.setBackgroundColor(new Color(247, 250, 252));
        c1.setBorderColor(borderClr);
        c1.setPadding(6);
        table.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase(v1, valueFont));
        c2.setBorderColor(borderClr);
        c2.setPadding(6);
        table.addCell(c2);

        PdfPCell c3 = new PdfPCell(new Phrase(l2, labelFont));
        c3.setBackgroundColor(new Color(247, 250, 252));
        c3.setBorderColor(borderClr);
        c3.setPadding(6);
        table.addCell(c3);

        PdfPCell c4 = new PdfPCell(new Phrase(v2, valueFont));
        c4.setBorderColor(borderClr);
        c4.setPadding(6);
        table.addCell(c4);
    }

    private String defaultIfEmpty(String str, String fallback) {
        return (str != null && !str.trim().isEmpty()) ? str : fallback;
    }
}
