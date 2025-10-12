package __Y2_S1_MTR_02.service;

import __Y2_S1_MTR_02.model.Booking;
import __Y2_S1_MTR_02.model.TrainSchedule;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public byte[] generateBookingTicket(Booking booking) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            // Create fonts
            PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont headerFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // Title
            Paragraph title = new Paragraph("SLRailE - Train Ticket")
                    .setFont(titleFont)
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Booking details table
            Table table = new Table(2).useAllAvailableWidth();
            table.setMarginBottom(20);

            // Add booking information
            addTableRow(table, "Booking ID:", booking.getId().toString(), headerFont, normalFont);
            addTableRow(table, "Passenger Email:", booking.getMemberEmail(), headerFont, normalFont);
            addTableRow(table, "Travel Date:", booking.getTravelDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), headerFont, normalFont);
            addTableRow(table, "Seat Numbers:", booking.getSeatNumbers(), headerFont, normalFont);
            addTableRow(table, "Total Amount:", "Rs. " + booking.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP), headerFont, normalFont);
            addTableRow(table, "Status:", booking.getStatus().toString(), headerFont, normalFont);

            // Add train schedule information if available
            if (booking.getTrainSchedule() != null) {
                TrainSchedule schedule = booking.getTrainSchedule();
                addTableRow(table, "Train Name:", schedule.getTrainName(), headerFont, normalFont);
                addTableRow(table, "From:", schedule.getRouteFrom(), headerFont, normalFont);
                addTableRow(table, "To:", schedule.getRouteTo(), headerFont, normalFont);
                addTableRow(table, "Departure Time:", schedule.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm")), headerFont, normalFont);
                addTableRow(table, "Arrival Time:", schedule.getArrivalTime().format(DateTimeFormatter.ofPattern("HH:mm")), headerFont, normalFont);
                addTableRow(table, "Platform:", schedule.getPlatform().toString(), headerFont, normalFont);
            }

            document.add(table);

            // Footer
            Paragraph footer = new Paragraph("Thank you for choosing SLRailE!")
                    .setFont(normalFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(30);
            document.add(footer);

            // Terms and conditions
            Paragraph terms = new Paragraph("Terms & Conditions:\n" +
                    "• This ticket is valid only for the specified date and train.\n" +
                    "• Please arrive at the station 15 minutes before departure.\n" +
                    "• Keep this ticket safe as it serves as proof of booking.\n" +
                    "• For any queries, contact SLRailE customer service.")
                    .setFont(normalFont)
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginTop(20);
            document.add(terms);

        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }

    private void addTableRow(Table table, String label, String value, PdfFont labelFont, PdfFont valueFont) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setFont(labelFont).setFontSize(10))
                .setPadding(5);
        
        Cell valueCell = new Cell()
                .add(new Paragraph(value != null ? value : "N/A").setFont(valueFont).setFontSize(10))
                .setPadding(5);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    public byte[] generateBookingHistoryPdf(java.util.List<Booking> bookings, String userEmail) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            // Create fonts
            PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont headerFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // Title
            Paragraph title = new Paragraph("SLRailE - Booking History")
                    .setFont(titleFont)
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(title);

            // User info
            Paragraph userInfo = new Paragraph("User: " + userEmail)
                    .setFont(normalFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(userInfo);

            // Generate date
            Paragraph generatedDate = new Paragraph("Generated on: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                    .setFont(normalFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(30);
            document.add(generatedDate);

            if (bookings.isEmpty()) {
                Paragraph noBookings = new Paragraph("No bookings found for this user.")
                        .setFont(normalFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER);
                document.add(noBookings);
            } else {
                // Create table for bookings
                Table table = new Table(6).useAllAvailableWidth();
                table.setMarginBottom(20);

                // Table headers
                String[] headers = {"Booking ID", "Train", "Route", "Date", "Seats", "Amount"};
                for (String header : headers) {
                    Cell headerCell = new Cell()
                            .add(new Paragraph(header).setFont(headerFont).setFontSize(10))
                            .setPadding(5)
                            .setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY);
                    table.addCell(headerCell);
                }

                // Add booking data
                for (Booking booking : bookings) {
                    table.addCell(new Cell().add(new Paragraph(booking.getId().toString()).setFont(normalFont).setFontSize(9)).setPadding(3));
                    
                    String trainName = booking.getTrainSchedule() != null ? booking.getTrainSchedule().getTrainName() : "N/A";
                    table.addCell(new Cell().add(new Paragraph(trainName).setFont(normalFont).setFontSize(9)).setPadding(3));
                    
                    String route = "N/A";
                    if (booking.getTrainSchedule() != null) {
                        route = booking.getTrainSchedule().getRouteFrom() + " → " + booking.getTrainSchedule().getRouteTo();
                    }
                    table.addCell(new Cell().add(new Paragraph(route).setFont(normalFont).setFontSize(9)).setPadding(3));
                    
                    table.addCell(new Cell().add(new Paragraph(booking.getTravelDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setFont(normalFont).setFontSize(9)).setPadding(3));
                    table.addCell(new Cell().add(new Paragraph(booking.getSeatNumbers()).setFont(normalFont).setFontSize(9)).setPadding(3));
                    table.addCell(new Cell().add(new Paragraph("Rs. " + booking.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP)).setFont(normalFont).setFontSize(9)).setPadding(3));
                }

                document.add(table);
            }

            // Footer
            Paragraph footer = new Paragraph("Generated by SLRailE System")
                    .setFont(normalFont)
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(30);
            document.add(footer);

        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }
}

