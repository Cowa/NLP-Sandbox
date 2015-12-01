import java.io.File
import java.io.PrintWriter

object Main extends App {
  def listFiles(path: String) = new File(path).listFiles
  def write(fileName: String, data: String) = new PrintWriter(fileName) { write(data); close() }

  /// French
  /*val documentsFr = listFiles("data-bucc/fr")
  val hapaxesFr = documentsFr.map(d => HapaxExtractor.extractFr(d))

  val indexFr = hapaxesFr.map { hapaxes =>
    hapaxes.document + " " + hapaxes.hapax.mkString(" ")
  }

  write("fr-hapax.txt", indexFr.mkString("\n"))*/

  /// English
  /*val documentsEn = listFiles("data-bucc/en").take(114000)

  val hapaxesEn = documentsEn.map(d => HapaxExtractor.extractEnDe(d))

  val indexEn = hapaxesEn.map { hapaxes =>
    hapaxes.document + " " + hapaxes.hapax.mkString(" ")
  }

  write("en-hapax.txt", indexEn.mkString("\n"))*/

  /// De
  /*val documentsDe = listFiles("data-bucc/de")

  val hapaxesDe = documentsDe.map(d => HapaxExtractor.extractEnDe(d))

  val indexDe = hapaxesDe.map { hapaxes =>
    hapaxes.document + " " + hapaxes.hapax.mkString(" ")
  }

  write("de-hapax.txt", indexDe.mkString("\n"))*/

  println(InvertedIndex("fr-hapax.txt").mkString("\n"))
}