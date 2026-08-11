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

package edu.vinu.domain.openPdf.table;

import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;

import static edu.vinu.domain.openPdf.colours.ColourPalette.*;
import static edu.vinu.domain.openPdf.colours.ColourPalette.SKY_800;
import static edu.vinu.domain.openPdf.font.Fonts.labelFont;
import static edu.vinu.domain.openPdf.font.Fonts.valueFont;

public class TableCell {
    public static PdfPCell createLabelCell(String text) {

        PdfPCell cell = new PdfPCell(new Phrase(text, labelFont()));

        cell.setBackgroundColor(SKY_950);
        cell.setBorderColor(SKY_800);
        cell.setPadding(8);

        return cell;
    }

    public static PdfPCell createValueCell(String text) {

        PdfPCell cell = new PdfPCell(new Phrase(text, valueFont()));

        cell.setBackgroundColor(SLATE_900);
        cell.setBorderColor(SKY_800);
        cell.setPadding(8);

        return cell;
    }
}
