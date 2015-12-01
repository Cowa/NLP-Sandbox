import java.io.File
import java.io.PrintWriter

case class DocumentLinker(documentSrc: String, documentTrg: String)

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

  println("Building inverted index...")
  val enInvertedIndex = Timer.executionTime(
    InvertedIndex("en-hapax.txt")
  )

  println("\nStructuring french hapaxes...")
  val frHapax = Timer.executionTime(
    HapaxExtractor.hapaxFileToStructure("fr-hapax.txt")
  )

  println("\nStarting alignment...\n")
  val alignments = Timer.executionTime(
    frHapax.take(1000).map { d =>
      println(s"Doing ${d.document}...")

      val docsSharingSomeHapax = d.hapax.flatMap(h => enInvertedIndex.get(h)).flatten

      val linkedWith = if (docsSharingSomeHapax.isEmpty) {
        "NONE"
      } else {
        docsSharingSomeHapax.groupBy(identity).mapValues(_.size).maxBy(_._2)._1
      }

      DocumentLinker(d.document, linkedWith)
    }
  )

  println(alignments.toList.mkString("\n"))
}
