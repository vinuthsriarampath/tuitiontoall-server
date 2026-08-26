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

package edu.vinu.domain.openPdf.font;

import org.openpdf.text.Font;

import static edu.vinu.domain.openPdf.colours.ColourPalette.*;

public class Fonts {
    public static Font titleFont() {
        return new Font(Font.HELVETICA, 24, Font.BOLD, SKY_400);
    }

    public static Font sectionFont() {
        return new Font(Font.HELVETICA, 11, Font.BOLD, SKY_300);
    }

    public static Font labelFont() {
        return new Font(Font.HELVETICA, 8, Font.BOLD, SKY_300);
    }

    public static Font valueFont() {
        return new Font(Font.HELVETICA, 10, Font.NORMAL, SKY_50);
    }
}
