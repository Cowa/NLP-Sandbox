package com.github.cowa.pdfprocessor

import java.io.File;

import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFTextStripper

/** Process stuff on PDF, more or less */
object PdfProcessor {

  lazy val tokenizer = new TokenizerME(new TokenizerModel(
    getClass.getResourceAsStream("/models/en-token.bin")
  ))

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

    val (paragraphs, trashes) = extractParagraphs(extracted)

    //println(trashes.mkString("\n\n\n"))

    extractFigures(trashes)
  }

  /** Attempt to extract paragraphs of a PDF extracted text
    *
    * @param text Text extracted from a PDF (using PDFBox)
    *
    * @return A tuple :
    *           - string list containing each paragraph (or almost)
    *           - string list with 'kind-of' trash #happyface
    */
  def extractParagraphs(text: String): (List[String], List[String]) = {
    var paragraph = ""
    var maxLineLength = 0
    var trash = ""
    var trashes = List[String]()
    var paragraphs = List[String]()
    var inTrash = false

    text.split("\n").zipWithIndex foreach { case(line, i) =>
      line.length match {
        case x if (x >= maxLineLength) => {
          maxLineLength = x
          paragraph = paragraph + "\n" + line
          if (inTrash) {
            if (trash != "") trashes = trashes :+ trash
            trash = ""
            inTrash = false
          }
        }
        case x => line.trim.endsWith(".") || line.trim.endsWith(":") match {
          case true => {
            paragraphs = paragraphs :+ (paragraph + "\n" + line)
            paragraph = ""
            if (inTrash) {
              if (trash != "") trashes = trashes :+ trash
              trash = ""
              inTrash = false
            }
          }
          case false if (x < maxLineLength / 1.75) => {
            inTrash = true
            trash = trash + "\n" + line
            if (paragraph != "") paragraphs = paragraphs :+ paragraph
            paragraph = ""
          }
          case false => {
            paragraph = paragraph + "\n" + line
            if (inTrash) {
              if (trash != "") trashes = trashes :+ trash
              trash = ""
              inTrash = false
            }
          }
        }
      }
    }

    (paragraphs, trashes)
  }

  /** Attempt to extract figures from extractParagraphs' trashes
    *
    * @param data Trashes from extractParagraphs
    *
    * @return A tuple:
    *           - List of string containing figures
    *           - List of string containing trashes (yeah trashes from trashes!)
    */
  def extractFigures(data: List[String]): (List[String], List[String]) = {
    var figures = List[String]()
    var trashes = List[String]()
    var figure = ""
    var trash = ""

    val isATitle = "([\\d][.].*)".r

    data.foreach { d => d.split("\n") foreach { line => line match {
      case isATitle(c) => {
        trashes = trashes :+ line
        if (figure != "") figures = figures :+ figure
        figure = ""
      }
      case _ => {
        figure = figure + "\n" + line
      }
    }}}

    println(figures.mkString("\n\n\n"))

    (figures, trashes)
  }
}
