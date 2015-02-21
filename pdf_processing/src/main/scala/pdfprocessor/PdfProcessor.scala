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

    extractParagraphs(extracted)
  }

  /** Attempt to extract paragraphs of a PDF extracted text
    *
    * @param text Text extracted from a PDF (using PDFBox)
    *
    * @return A string array containing each paragraph (or almost)
    */
  def extractParagraphs(text: String): List[String] = {
    var paragraph = ""
    var maxLineLength = 0
    var paragraphs = List[String]()

    text.split("\n").zipWithIndex foreach { case(line, i) =>
      line.endsWith(".") match {
        // It ends with a point
        case true => line.length match {
          case x if (x >= maxLineLength) => {
            maxLineLength = x
            paragraph = paragraph + "\n" + line
            //paragraphs = paragraph :: paragraphs
          }
          case _ => {
            paragraphs = (paragraph + "\n" + line) :: paragraphs
            paragraph = ""
          }
        }
        // It doesn't end with a point
        case false => line.length match {
          case x if (x >= maxLineLength) => {
            maxLineLength = x
            paragraph = paragraph + "\n" + line
          }
          case _ => paragraph = paragraph + "\n" + line
        }
      }
    }

    println(paragraphs)
    println(paragraphs.length)

    paragraphs
  }
}
