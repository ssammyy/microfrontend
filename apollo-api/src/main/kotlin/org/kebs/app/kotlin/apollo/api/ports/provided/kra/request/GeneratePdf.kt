package org.kebs.app.kotlin.apollo.api.ports.provided.kra.request

import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import java.awt.Color
import java.io.IOException
import javax.servlet.http.HttpServletResponse

class PDFGeneratorVehicle {
    private val companyList: List<CompanyProfileEntity>? = null
    @Throws(DocumentException::class, IOException::class)
    fun generate(response: HttpServletResponse) {
        // Create the Object of Document
        val document = Document(PageSize.A4)
        // get the document and write the response to output stream
        PdfWriter.getInstance(document, response.getOutputStream())
        document.open()
        // Add Font
        val fontTiltle: Font = FontFactory.getFont(FontFactory.TIMES_ROMAN)
        fontTiltle.setSize(20F)
        // Create Object of Paragraph
        val paragraph = Paragraph("Company List", fontTiltle)
        paragraph.setAlignment(Paragraph.ALIGN_CENTER)
        // Add to the document
        document.add(paragraph)
        val table = PdfPTable(5)
        table.widthPercentage = 100f
        table.setWidths(intArrayOf(1, 1, 3, 2, 4))
        table.setSpacingBefore(5f)
        // Create Table Header
        val cell = PdfPCell()
        cell.backgroundColor = Color.MAGENTA
        cell.setPadding(5f)
        // Add Font
        val font: Font = FontFactory.getFont(FontFactory.TIMES_ROMAN)
        font.setColor(Color.WHITE)
        cell.phrase = Phrase("ID", font)
        table.addCell(cell)
        cell.phrase = Phrase("Name", font)
        table.addCell(cell)
        cell.phrase = Phrase("Entry Number", font)
        table.addCell(cell)
        cell.phrase = Phrase("KRA PIN", font)
        table.addCell(cell)
        cell.phrase = Phrase("Registration Number", font)
        table.addCell(cell)
        for (company in companyList!!) {
            table.addCell(java.lang.String.valueOf(company.id))
            table.addCell(company.name)
            table.addCell(company.entryNumber)
            table.addCell(company.kraPin)
            table.addCell(company.registrationNumber)
        }
        // Add table to document
        document.add(table)
        document.close()
    }

    fun setCompanyList(companyList: MutableIterable<CompanyProfileEntity>) {

    }
}

