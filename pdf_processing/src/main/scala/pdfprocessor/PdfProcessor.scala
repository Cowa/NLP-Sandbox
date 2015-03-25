package com.github.cowa.pdfprocessor

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFTextStripper

import com.github.cowa.staf._

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

    println(extracted)

    val (paragraphs, trashes, stafs) = extractParagraphs(extracted)


    println(stafs.reverse.mkString("\n\n\n"))

    val revStafs = stafs.reverse
    val stafsParagraphs = revStafs.filter(_.`type` == "paragraph")
    val stafsTrashs = revStafs.filter(_.`type` == "trash")
/*
    for (i <- 0 until paragraphs.length) {
      println(stafsParagraphs(i))
      println(paragraphs(i))
      println("\n\n\n")
    }
*/
    for (i <- 0 until trashes.length) {
      println(stafsTrashs(i))
      println(trashes(i))
      println("\n\n\n")
    }

    //extractFigures(trashes)
  }

  /** Attempt to extract paragraphs of a PDF extracted text
    *
    * @param text Text extracted from a PDF (using PDFBox)
    *
    * @return A tuple :
    *           - string list containing each paragraph (or almost)
    *           - string list with 'kind-of' trash #happyface
    */
  def extractParagraphs(text: String): (List[String], List[String], List[Staf]) = {
    // So...
    //    Ugly !
    //        Sorry...
    var paragraph = ""
    var maxLineLength = 0
    var trash = ""
    var trashes = List[String]()
    var paragraphs = List[String]()
    var inTrash = false

    var stafs = List[Staf]()
    var startLine = 0

    var startLineTrash = 0

    text.split("\n").zipWithIndex foreach { case(line, i) =>
      line.length match {
        case x if (x >= maxLineLength) => {
          maxLineLength = x

          startLine = i

          paragraph = paragraph + "\n" + line
          if (inTrash) {
            if (trash != "") trashes = trashes :+ trash
            trash = ""
            inTrash = false
            stafs = stafTrash(startLineTrash, i) :: stafs
          }
        }
        // End of paragraph
        case x => line.trim.endsWith(".") || line.trim.endsWith(":") match {
          case true => {
            if (paragraph == "") {
              startLine = i
            }

            paragraphs = paragraphs :+ (paragraph + "\n" + line)
            paragraph = ""

            // endLine = i
            stafs = stafParagraph(startLine, i) :: stafs
            startLine = 0
            //endLine = 0

            if (inTrash) {
              if (trash != "") trashes = trashes :+ trash
              trash = ""
              inTrash = false
              stafs = stafTrash(startLineTrash, i) :: stafs
            }
          }
          case false if (x < maxLineLength / 3) => {
            inTrash = true
            startLineTrash = i

            trash = trash + "\n" + line
            if (paragraph != "") {
              stafs = stafParagraph(startLine, i) :: stafs
              startLine = 0
              paragraphs = paragraphs :+ paragraph
            }
            paragraph = ""
          }
          case false => {
            if (paragraph == "") {
              startLine = i
            }

            paragraph = paragraph + "\n" + line
            if (inTrash) {
              if (trash != "") trashes = trashes :+ trash
              trash = ""
              inTrash = false
              stafs = stafTrash(startLineTrash, i) :: stafs
            }
          }
        }
      }
    }

    (paragraphs, trashes, stafs)
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

  def stafParagraph(startLine: Int, endLine: Int): Staf = {
    Staf("paragraph", None, startLine, 0, endLine, 0)
  }

  def stafTrash(startLine: Int, endLine: Int): Staf = {
    Staf("trash", None, startLine, 0, endLine, 0)
  }
}
