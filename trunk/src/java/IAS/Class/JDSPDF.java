/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IAS.Class;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfContentByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;

public class JDSPDF implements IJDSPDF {

    public InputStream pdfTemplatesFile = null;
    public static int OUTER_PARAGRAPH_SPACE = 10;
    public static int LESS_OUTER_PARAGRAPH_SPACE = 2;
    public static int INNER_PARAGRAPH_SPACE = 10;
    public static int LEFT_INDENTATION_LESS = 15;
    public static int RIGHT_INDENTATION_LESS = 15;
    public static int LEFT_INDENTATION_MORE = 30;
    public static Font JDS_BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
    public static Font JDS_FONT_NORMAL_SMALL = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    public static Font JDS_FONT_NORMAL_SMALL_BOLD = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);

    public JDSPDF() {
        ServletContext context = ServletContextInfo.getServletContext();
        this.pdfTemplatesFile = context.getResourceAsStream("/WEB-INF/classes/pdf_templates.properties");
    }

    @Override
    public ByteArrayOutputStream getPDF() {
        throw (new UnsupportedOperationException("This method has to be overriden"));
    }

    public Document getPDFDocument() {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
        document.addAuthor("Indian Academy Of Sciences");
        document.addCreator("Indian Academy Of Sciences");
        document.addSubject("Indian Academy Of Sciences");
        document.addCreationDate();
        document.addTitle("Indian Academy Of Sciences");
        return document;
    }

    public Document getPDFDocumentLandscape() {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4.rotate());
        document.addAuthor("Indian Academy Of Sciences");
        document.addCreator("Indian Academy Of Sciences");
        document.addSubject("Indian Academy Of Sciences");
        document.addCreationDate();
        document.addTitle("Indian Academy Of Sciences");
        return document;
    }

    public void addFooter(Document document, PdfWriter pdfWriter) throws DocumentException{

        ColumnText ct = new ColumnText(pdfWriter.getDirectContent());
        float pageWidth = pdfWriter.getPageSize().getWidth();
        float pageHeight = pdfWriter.getPageSize().getHeight();

        float llx = (pageWidth - Utilities.millimetersToPoints(JDSConstants.width))/2;
        float lly = Utilities.millimetersToPoints(JDSConstants.heightFromBottomOfPage);
        float urx = llx + Utilities.millimetersToPoints(JDSConstants.width);
        float ury = lly + Utilities.millimetersToPoints(JDSConstants.height);

        PdfContentByte cb = pdfWriter.getDirectContent();
        cb.roundRectangle(llx - Utilities.millimetersToPoints(5), lly - Utilities.millimetersToPoints(3), Utilities.millimetersToPoints(JDSConstants.width + 5), Utilities.millimetersToPoints(JDSConstants.height + 5), 20f);
        cb.stroke();
        //cb.roundRectangle(llx - Utilities.millimetersToPoints(6), lly - Utilities.millimetersToPoints(4), Utilities.millimetersToPoints(JDSConstants.width + 7), Utilities.millimetersToPoints(JDSConstants.height + 7), 20f);
        //cb.stroke();

        // 1. Add the first line
        ct.setSimpleColumn(llx, lly, urx, ury);
        int status = ColumnText.START_COLUMN;

        /*
        Paragraph info = new Paragraph();
        info.add(new Chunk(JDSConstants.IAS_PAYMENTFOOT_HEADER, JDSPDF.JDS_FONT_NORMAL_SMALL));
        ct.addElement(info);
        */

        PdfPTable table0 = new PdfPTable(1);
        table0.setWidthPercentage(100f);
        table0.setHeaderRows(0);
        float[] widths0 = {100f};
        table0.setWidths(widths0);
        table0.getDefaultCell().setBorder(0);
        Phrase header = new Phrase();
        header.setFont(JDSPDF.JDS_FONT_NORMAL_SMALL_BOLD);
        header.add(JDSConstants.IAS_PAYMENTFOOT_HEADER);
        table0.addCell(header);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setHeaderRows(0);
        float[] widths = {40f, 10f, 40f};
        table.setWidths(widths);
        table.getDefaultCell().setBorder(0);

        // The center "-" is common to all
        Phrase common = new Phrase();
        common.setFont(JDSPDF.JDS_FONT_NORMAL_SMALL);
        common.add("-");

        // 2. Add line2 - details on account holder
        Phrase phrase1 = new Phrase();
        phrase1.setFont(JDSPDF.JDS_FONT_NORMAL_SMALL);
        phrase1.add(JDSConstants.IAS_PAYMENTFOOT_ACC);

        Phrase phrase2 = new Phrase();
        phrase2.setFont(JDSPDF.JDS_FONT_NORMAL_SMALL);
        phrase2.add(JDSConstants.IAS_PAYMENTFOOT_ACC_NAME);

        // 3. Add line2 - details of bank
        Phrase phrase3 = new Phrase();
        phrase3.setFont(JDSPDF.JDS_FONT_NORMAL_SMALL);
        phrase3.add(JDSConstants.IAS_PAYMENTFOOT_BANK);

        Phrase phrase4 = new Phrase();
        phrase4.setFont(JDSPDF.JDS_FONT_NORMAL_SMALL);
        phrase4.add(JDSConstants.IAS_PAYMENTFOOT_BANK_NAME);

        // 4. Add line2 - details of branch
        Phrase phrase5 = new Phrase();
        phrase5.setFont(JDSPDF.JDS_FONT_NORMAL_SMALL);
        phrase5.add(JDSConstants.IAS_PAYMENTFOOT_BRANCH);

        Phrase phrase6 = new Phrase();
        phrase6.setFont(JDSPDF.JDS_FONT_NORMAL_SMALL);
        phrase6.add(JDSConstants.IAS_PAYMENTFOOT_BRANCH_NAME);

        // 5. Add line2 - details of a/c no
        Phrase phrase7 = new Phrase();
        phrase7.setFont(JDSPDF.JDS_FONT_NORMAL_SMALL);
        phrase7.add(JDSConstants.IAS_PAYMENTFOOT_ACCNO);

        Phrase phrase8 = new Phrase();
        phrase8.setFont(JDSPDF.JDS_FONT_NORMAL_SMALL);
        phrase8.add(JDSConstants.IAS_PAYMENTFOOT_ACCNO_DTLS);

        // 6. Add line2 - details of IFS code
        Phrase phrase9 = new Phrase();
        phrase9.setFont(JDSPDF.JDS_FONT_NORMAL_SMALL);
        phrase9.add(JDSConstants.IAS_PAYMENTFOOT_IFSCOD);

        Phrase phrase10 = new Phrase();
        phrase10.setFont(JDSPDF.JDS_FONT_NORMAL_SMALL);
        phrase10.add(JDSConstants.IAS_PAYMENTFOOT_IFSCOD_DTLS);

        table.addCell(phrase1);
        table.addCell(common);
        table.addCell(phrase2);
        table.addCell(phrase3);
        table.addCell(common);
        table.addCell(phrase4);
        table.addCell(phrase5);
        table.addCell(common);
        table.addCell(phrase6);
        table.addCell(phrase7);
        table.addCell(common);
        table.addCell(phrase8);
        table.addCell(phrase9);
        table.addCell(common);
        table.addCell(phrase10);

        //document.add(table);
        ct.addElement(table0);
        ct.addElement(table);

        /*
        Phrase footerS = new Phrase();
        footerS.add(Chunk.NEWLINE);
        ct.addElement(footerS);
        */

        /*
        Paragraph paymentFooter = new Paragraph();
        paymentFooter.add(new Chunk(JDSConstants.IAS_PAYMENTFOOTER, JDSPDF.JDS_FONT_NORMAL_SMALL));
        ct.addElement(paymentFooter);
        */
        PdfPTable tableLast = new PdfPTable(1);
        tableLast.setWidthPercentage(100f);
        tableLast.setHeaderRows(0);
        float[] widthsLast = {100f};
        tableLast.setWidths(widthsLast);
        tableLast.getDefaultCell().setBorder(0);
        Phrase footer = new Phrase();
        footer.setFont(JDSPDF.JDS_FONT_NORMAL_SMALL_BOLD);
        footer.add(JDSConstants.IAS_PAYMENTFOOTER);
        tableLast.addCell(footer);

        ct.addElement(tableLast);

        int go = ct.go();
    }

    public Paragraph getLetterHead() throws BadElementException, java.net.MalformedURLException, IOException {
        Paragraph paragraph = new Paragraph();
        Paragraph paragraphDate = new Paragraph();

        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraphDate.setAlignment(Element.ALIGN_RIGHT);
        paragraphDate.setIndentationRight(JDSPDF.RIGHT_INDENTATION_LESS);

        ServletContext context = ServletContextInfo.getServletContext();
        String logo = context.getRealPath("/images/pdflogo.jpg");

        Image _logo = Image.getInstance(logo);
        _logo.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(_logo);

        Font _IASFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
        Chunk HeaderIAS = new Chunk(JDSConstants.IAS_LETTERHEAD);
        HeaderIAS.setFont(_IASFont);

        Chunk HeaderIASAddress = new Chunk(JDSConstants.IAS_LETTERHEAD_ADDRESS);
        Chunk HeaderIASTel = new Chunk(JDSConstants.IAS_LETTERHEAD_TELEPHONE);
        Chunk HeaderEmail_Web = new Chunk(JDSConstants.IAS_LETTERHEAD_EMAIL + " " + JDSConstants.IAS_LETTERHEAD_WEB);
        Chunk LetterDate = new Chunk("Date: " + util.getDateString());
        paragraphDate.add(LetterDate);

        paragraph.add(HeaderIAS);
        paragraph.add(Chunk.NEWLINE);
        paragraph.add(HeaderIASAddress);
        paragraph.add(Chunk.NEWLINE);
        paragraph.add(HeaderIASTel);
        paragraph.add(Chunk.NEWLINE);
        paragraph.add(HeaderEmail_Web);

        paragraphDate.setSpacingBefore(INNER_PARAGRAPH_SPACE);
        paragraph.add(paragraphDate);

        return paragraph;
    }

    public Paragraph getSalutation() {
        Paragraph paragraph = new Paragraph();
        paragraph.setSpacingBefore(40);
        paragraph.setIndentationLeft(LEFT_INDENTATION_LESS);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.add(new Chunk(JDSConstants.IAS_LETTERHEAD_SALUTATION));
        return paragraph;

    }

    public Paragraph getLetterFooter() {

        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        paragraph.setSpacingBefore(70);

        paragraph.add(new Chunk(JDSConstants.IAS_LETTERFOOT_CLOSING));
        paragraph.add(Chunk.NEWLINE);

        paragraph.add(new Chunk(JDSConstants.IAS_LETTERFOOT_SIGNATURE));
        paragraph.add(Chunk.NEWLINE);

        return paragraph;

    }

    public Paragraph getPaymentFooter() {
        Paragraph paragraph = new Paragraph();
        //paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        paragraph.setSpacingBefore(20);
        //paragraph.setSpacingAfter(20);
        PdfPTable table;
        table = new PdfPTable(1);
        table.setWidthPercentage(80);

        PdfPCell cell1 = new PdfPCell(new Paragraph(new Chunk(JDSConstants.IAS_PAYMENTFOOT_HEADER, JDSPDF.JDS_FONT_NORMAL_SMALL_BOLD)));
        cell1.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell1);

        String ArrString[] = new String[10];
        ArrString[0] = JDSConstants.IAS_PAYMENTFOOT_ACC;
        ArrString[1] = JDSConstants.IAS_PAYMENTFOOT_ACC_NAME;
        ArrString[2] = JDSConstants.IAS_PAYMENTFOOT_BANK;
        ArrString[3] = JDSConstants.IAS_PAYMENTFOOT_BANK_NAME;
        ArrString[4] = JDSConstants.IAS_PAYMENTFOOT_BRANCH;
        ArrString[5] = JDSConstants.IAS_PAYMENTFOOT_BRANCH_NAME;
        ArrString[6] = JDSConstants.IAS_PAYMENTFOOT_ACCNO;
        ArrString[7] = JDSConstants.IAS_PAYMENTFOOT_ACCNO_DTLS;
        ArrString[8] = JDSConstants.IAS_PAYMENTFOOT_IFSCOD;
        ArrString[9] = JDSConstants.IAS_PAYMENTFOOT_IFSCOD_DTLS;

        PdfPTable innerTable;
        innerTable = new PdfPTable(3);
        innerTable.setWidthPercentage(80);
        PdfPCell cellInnerTable1;
        PdfPCell cellInnerTable2;
        PdfPCell cellInnerTable3;

        for (int i = 0; i < ArrString.length; i++) {

            cellInnerTable1 = new PdfPCell(new Paragraph(new Chunk(ArrString[i], JDSPDF.JDS_FONT_NORMAL_SMALL)));
            cellInnerTable1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellInnerTable1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellInnerTable1.setBorder(Rectangle.NO_BORDER);
            innerTable.addCell(cellInnerTable1);
            cellInnerTable2 = new PdfPCell(new Paragraph(new Chunk("-", JDSPDF.JDS_FONT_NORMAL_SMALL)));
            cellInnerTable2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellInnerTable2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellInnerTable2.setBorder(Rectangle.NO_BORDER);
            innerTable.addCell(cellInnerTable2);
            cellInnerTable3 = new PdfPCell(new Paragraph(new Chunk(ArrString[i + 1], JDSPDF.JDS_FONT_NORMAL_SMALL)));
            cellInnerTable3.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellInnerTable3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellInnerTable3.setBorder(Rectangle.NO_BORDER);
            innerTable.addCell(cellInnerTable3);
            i++;
        }
        table.addCell(innerTable);

        cell1 = new PdfPCell(new Paragraph(new Chunk(JDSConstants.IAS_PAYMENTFOOTER, JDSPDF.JDS_FONT_NORMAL_SMALL_BOLD)));
        cell1.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell1);

        paragraph.add(table);
        return paragraph;

    }

    /**
     * Inner class to add a header and a footer.
     */
    static class HeaderFooter extends PdfPageEventHelper {

        Font.FontFamily fontType = Font.getFamily("HELVETICA");
        int fontSize = 9;
        float leading = 10.0f;

        public void setupFontAndLeading(PdfContentByte cb) {
            Font f = new Font(fontType, fontSize);
            BaseFont bf = f.getCalculatedBaseFont(false);
            cb.setFontAndSize(bf, fontSize);
            cb.setLeading(leading);
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            cb.roundRectangle(LEFT_INDENTATION_MORE * 2, 30, 480, 90, 10);
            
            this.setupFontAndLeading(cb);
            cb.beginText();
            cb.showTextAligned(Element.ALIGN_LEFT, 
                    JDSConstants.IAS_PAYMENTFOOT_HEADER, 
                    LEFT_INDENTATION_MORE * 3, 100, 0);
            cb.newlineText();
            cb.newlineShowText(JDSConstants.IAS_PAYMENTFOOT_ACC + ": " + JDSConstants.IAS_PAYMENTFOOT_ACC_NAME);
            cb.newlineShowText(JDSConstants.IAS_PAYMENTFOOT_BANK + ": " + JDSConstants.IAS_PAYMENTFOOT_BANK_NAME);
            cb.newlineShowText(JDSConstants.IAS_PAYMENTFOOT_BRANCH + ": " + JDSConstants.IAS_PAYMENTFOOT_BRANCH_NAME);            
            cb.newlineShowText(JDSConstants.IAS_PAYMENTFOOT_ACCNO + ": " + JDSConstants.IAS_PAYMENTFOOT_ACCNO_DTLS);
            cb.newlineShowText(JDSConstants.IAS_PAYMENTFOOT_IFSCOD + ": " + JDSConstants.IAS_PAYMENTFOOT_IFSCOD_DTLS);
            cb.endText();
            cb.stroke();
                        
        }
    }
}