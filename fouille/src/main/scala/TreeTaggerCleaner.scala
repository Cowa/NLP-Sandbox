import scala.io.Source

import java.io.PrintWriter

case class TreeTaggerEntry(word: String, tag: String, lemm: String)

object TreeTaggerCleaner {
  def main(args: Array[String]): Unit = {
    val lines = Source.fromFile("data/treeTagger2011.txt").getLines().toList
    val stopWords = Source.fromFile("data/fr-stopwords.txt").getLines().toList
    val ignoredTag = List("PUN", "SENT", "NUM")

    val entries = lines.map { l =>
      val tabs = l.split("\t")
      TreeTaggerEntry(tabs(0), tabs(1), tabs(2))
    }
      .filter(e => !ignoredTag.contains(e.tag))
      .filter(e => !stopWords.contains(e.lemm))

    // Write result to file
    val treeTaggerified = entries.map(e => s"${e.word}\t${e.tag}\t${e.lemm}").mkString("\n")
    write("data/treeTagger2011-filter.txt", treeTaggerified)
  }

  def write(fileName: String, data: String) =
    new PrintWriter(fileName) { write(data); close() }
}
