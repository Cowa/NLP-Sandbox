package com.github.cowa.pdfprocessor

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFTextStripper

/** Process stuff on PDF, more or less */
object PdfProcessor {

  /** This. is. main !
    *
    * @param args Classical command line arguments
    *             Only the first one is used: path to the PDF file
    */
  def main(args: Array[String]) {
    // Load the PDF passed in first argument
    val pdf = PDDocument.load(new File(args(0)))

    val stripper = new PDFTextStripper
    val extracted = stripper.getText(pdf)
    pdf.close()

    extractParagraphs(extracted)
  }

  /** Attempt to extract paragraphs of a PDF extracted text
    *
    * @param text Text extracted from a PDF (using PDFBox)
    *
    * @return A string array containing each paragraph (or almost)
    */
  def extractParagraphs(text: String): Array[String] = {
    val date = """(\d\d\d\d)-(\d\d)-(\d\d)""".r

    text match {
      case date(year, month, day) => println(s"$year was a good year for PLs.")
      case _ => println("God no")
    }

    Array("oh", "god", "it's", "a", "chair!")
  }
}
