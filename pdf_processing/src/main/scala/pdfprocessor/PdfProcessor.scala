package com.github.cowa.pdfprocessor

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFTextStripper

object PdfProcessor {
  def main(args: Array[String]) {
    // Load the PDF passed in first argument
    val pdf = PDDocument.load(new File(args(0)))

    val stripper = new PDFTextStripper
    val extracted = stripper.getText(pdf)

    println(extracted)

    pdf.close()
  }
}
