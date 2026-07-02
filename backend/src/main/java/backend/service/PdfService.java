package backend.service;

import backend.entity.DeviceCheck;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

private static final String REPORT_FOLDER =
        "generated-reports";

// =========================================
// GENERATE HANDOVER PDF
// =========================================

public String generateHandoverPdf(
        DeviceCheck deviceCheck
) throws IOException {

    File folder =
            new File(REPORT_FOLDER);

    if (!folder.exists()) {
        folder.mkdirs();
    }

    String fileName;

    if ("BEFORE".equalsIgnoreCase(
            deviceCheck.getCheckType()
    )) {

        fileName =
                "ST-PINJAM-"
                        + deviceCheck.getInspectionCycleId()
                        + ".pdf";
    }

    else {

        fileName =
                "ST-KEMBALI-"
                        + deviceCheck.getInspectionCycleId()
                        + ".pdf";
    }

    String filePath =
            REPORT_FOLDER
                    + File.separator
                    + fileName;

    PDDocument document =
            new PDDocument();

    PDPage page =
            new PDPage();

    document.addPage(page);

    PDPageContentStream content =
            new PDPageContentStream(
                    document,
                    page
            );

    PDType1Font titleFont =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA_BOLD
            );

    PDType1Font normalFont =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA
            );

    float y = 750;

    content.beginText();
    content.setFont(titleFont, 16);
    content.newLineAtOffset(180, y);
    content.showText("CROWE INDONESIA");
    content.endText();

    y -= 30;

    content.beginText();
    content.setFont(titleFont, 14);
    content.newLineAtOffset(120, y);

    if ("BEFORE".equalsIgnoreCase(
            deviceCheck.getCheckType()
    )) {

        content.showText(
                "DOKUMEN SERAH TERIMA PEMINJAMAN LAPTOP"
        );
    }

    else {

        content.showText(
                "DOKUMEN SERAH TERIMA PENGEMBALIAN LAPTOP"
        );
    }

    content.endText();

    y -= 50;

    DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(
                    "dd MMMM yyyy HH:mm:ss"
            );

    String date =
            deviceCheck.getSubmittedAt() == null
                    ? "-"
                    : deviceCheck
                    .getSubmittedAt()
                    .format(formatter);

    y = writeLine(
            content,
            normalFont,
            y,
            "Tanggal : " + date
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "Nomor Dokumen : "
                    + deviceCheck.getInspectionCycleId()
    );

    y -= 20;

    y = writeLine(
            content,
            titleFont,
            y,
            "DATA LAPTOP"
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "Serial Number : "
                    + safe(
                    deviceCheck.getSerialNumber()
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "Model : "
                    + safe(
                    deviceCheck.getSystemName()
            )
                    + " "
                    + safe(
                    deviceCheck.getSystemModel()
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "CPU : "
                    + safe(
                    deviceCheck.getCpu()
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "GPU : "
                    + safe(
                    deviceCheck.getGpu()
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "RAM : "
                    + formatGB(
                    deviceCheck.getTotalRam()
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "Storage : "
                    + formatGB(
                    deviceCheck.getTotalStorage()
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "Available Storage : "
                    + formatGB(
                    deviceCheck.getAvailableStorage()
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "Battery Health : "
                    + formatBattery(
                    deviceCheck.getBatteryHealth()
            )
    );

    y -= 20;

    y = writeLine(
            content,
            titleFont,
            y,
            "DATA PENGGUNA"
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "Nama Peminjam : "
                    + safe(
                    deviceCheck.getBorrowerName()
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "Nama Inspector : "
                    + safe(
                    deviceCheck.getInspector()
            )
    );

    y -= 70;

    content.beginText();
    content.setFont(normalFont, 12);
    content.newLineAtOffset(80, y);
    content.showText("Peminjam");
    content.endText();

    content.beginText();
    content.setFont(normalFont, 12);
    content.newLineAtOffset(350, y);
    content.showText("Petugas IT");
    content.endText();

    y -= 70;

    content.beginText();
    content.setFont(normalFont, 12);
    content.newLineAtOffset(60, y);
    content.showText("___________________");
    content.endText();

    content.beginText();
    content.setFont(normalFont, 12);
    content.newLineAtOffset(330, y);
    content.showText("___________________");
    content.endText();

    y -= 20;

    content.beginText();
    content.setFont(normalFont, 12);
    content.newLineAtOffset(60, y);
    content.showText(
            "("
                    + safe(
                    deviceCheck.getBorrowerName()
            )
                    + ")"
    );
    content.endText();

    content.beginText();
    content.setFont(normalFont, 12);
    content.newLineAtOffset(330, y);
    content.showText(
            "("
                    + safe(
                    deviceCheck.getInspector()
            )
                    + ")"
    );
    content.endText();

    content.close();

    document.save(filePath);

    document.close();

    return filePath;
}

// =========================================
// GENERATE COMPARISON PDF
// =========================================

public String generateComparisonPdf(
        DeviceCheck before,
        DeviceCheck after
) throws IOException {

    File folder =
            new File(REPORT_FOLDER);

    if (!folder.exists()) {
        folder.mkdirs();
    }

    String fileName =
            "COMPARE-"
                    + before.getInspectionCycleId()
                    + ".pdf";

    String filePath =
            REPORT_FOLDER
                    + File.separator
                    + fileName;

    PDDocument document =
            new PDDocument();

    PDPage page =
            new PDPage();

    document.addPage(page);

    PDPageContentStream content =
            new PDPageContentStream(
                    document,
                    page
            );

    PDType1Font titleFont =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA_BOLD
            );

    PDType1Font normalFont =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA
            );

    float y = 750;

    content.beginText();
    content.setFont(titleFont, 16);
    content.newLineAtOffset(120, y);
    content.showText(
            "LAPORAN PERBANDINGAN KONDISI LAPTOP"
    );
    content.endText();

    y -= 40;

    DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(
                    "dd MMMM yyyy HH:mm:ss"
            );

    String beforeDate =
            before.getSubmittedAt() == null
                    ? "-"
                    : before
                    .getSubmittedAt()
                    .format(formatter);

    String afterDate =
            after.getSubmittedAt() == null
                    ? "-"
                    : after
                    .getSubmittedAt()
                    .format(formatter);

    y = writeLine(
            content,
            normalFont,
            y,
            "Borrower : "
                    + safe(
                    before.getBorrowerName()
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "Before Inspector : "
                    + safe(
                    before.getInspector()
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "After Inspector : "
                    + safe(
                    after.getInspector()
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "Before Scan : "
                    + beforeDate
    );

    y = writeLine(
            content,
            normalFont,
            y,
            "After Scan : "
                    + afterDate
    );

    y -= 20;

    y = writeLine(
            content,
            titleFont,
            y,
            "HASIL PERBANDINGAN"
    );

    y = writeLine(
            content,
            normalFont,
            y,
            compareLine(
                    "CPU",
                    before.getCpu(),
                    after.getCpu()
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            compareLine(
                    "GPU",
                    before.getGpu(),
                    after.getGpu()
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            compareLine(
                    "RAM",
                    formatGB(
                            before.getTotalRam()
                    ),
                    formatGB(
                            after.getTotalRam()
                    )
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            compareLine(
                    "Available Storage",
                    formatGB(
                            before.getAvailableStorage()
                    ),
                    formatGB(
                            after.getAvailableStorage()
                    )
            )
    );

    y = writeLine(
            content,
            normalFont,
            y,
            compareLine(
                    "Battery Health",
                    formatBattery(
                            before.getBatteryHealth()
                    ),
                    formatBattery(
                            after.getBatteryHealth()
                    )
            )
    );

    content.close();

    document.save(filePath);

    document.close();

    return filePath;
}

// =========================================
// HELPER
// =========================================

private float writeLine(
        PDPageContentStream content,
        PDType1Font font,
        float y,
        String text
) throws IOException {

    content.beginText();
    content.setFont(font, 12);
    content.newLineAtOffset(60, y);
    content.showText(text);
    content.endText();

    return y - 20;
}

private String safe(
        String value
) {

    return value == null
            || value.isBlank()
            ? "-"
            : value;
}

private String formatBattery(
        Double value
) {

    return value == null
            ? "-"
            : String.format(
            "%.0f%%",
            value
    );
}

private String formatGB(
        Long value
) {

    if (value == null) {
        return "-";
    }

    double gb =
            value
                    / 1024.0
                    / 1024.0
                    / 1024.0;

    return String.format(
            "%.2f GB",
            gb
    );
}

private String compareLine(
        String component,
        String before,
        String after
) {

    String status =
            safe(before)
                    .equals(
                            safe(after)
                    )
                    ? "Same"
                    : "Changed";

    return component
            + " : "
            + safe(before)
            + " -> "
            + safe(after)
            + " ("
            + status
            + ")";
}

}
