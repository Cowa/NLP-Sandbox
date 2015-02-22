package com.github.cowa.pdfprocessor

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFTextStripper

/** Process stuff on PDF, more or less */
object PdfProcessor {

  /** This. is. main !
    *
    * @param args Classical command line arguments :
    *               - #1 path to the PDF file
    */
  def main(args: Array[String]) {
    val pdf = PDDocument.load(new File(args(0)))

    val stripper = new PDFTextStripper
    val extracted = stripper.getText(pdf)
    pdf.close()

    val paragraphs = extractParagraphs(extracted)

    println(paragraphs.mkString("\n\n\n"))
    println(s"\nSo yeah... there are less than ${paragraphs.length} paragraphs (trash inside).\n")
  }

  /** [BASELINE] Oh dog, it's a baseline !
    *
    * Attempt to extract paragraphs of a PDF extracted text
    *
    * @param text Text extracted from a PDF (using PDFBox)
    *
    * @return A string list containing each paragraph (or almost)
    */
  def extractParagraphs(text: String): List[String] = {
    var paragraph = ""
    var maxLineLength = 0
    var paragraphs = List[String]()

    text.split("\n").zipWithIndex foreach { case(line, i) =>
      line.length match {
        case x if (x >= maxLineLength) => {
          maxLineLength = x
          paragraph = paragraph + "\n" + line
        }
        case x => line.endsWith(".") match {
          case true => {
            paragraphs =  paragraphs :+ (paragraph + "\n" + line)
            paragraph = ""
          }
          case false => {
            paragraph = paragraph + "\n" + line
          }
        }
      }
    }

    paragraphs
  }
}
