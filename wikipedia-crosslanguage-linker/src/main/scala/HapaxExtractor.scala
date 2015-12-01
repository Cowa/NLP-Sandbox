import java.io.File
import scala.io.Source

case class DocumentHapax(document: String, hapax: Vector[String])

object HapaxExtractor {

  /**
   * Extract hapax from an iterator of tokens
   *
   * @param document Document identifier
   * @param tokens Tokens
   *
   * @return DocumentHapax
   */
  def apply(document: String, tokens: Iterator[String]): DocumentHapax = {
    val hapax = tokens.map(_.toLowerCase).toVector
      .groupBy(identity).filter(x => x._2.size == 1)
      .filterKeys(_.length > 3)
      .keys

    DocumentHapax(document, hapax.toVector)
  }

  /**
   * Extract hapax from a file with one token by line (used for FR corpus)
   *
   * @param document File
   *
   * @return DocumentHapax
   */
  def extractFr(document: File): DocumentHapax = {
    val file = Source.fromFile(document)
    val extracted = apply(document.getName, file.getLines())
    file.close()

    extracted
  }

  /**
   * Extract hapax from a file with one single line and tokens separated with whitespaces (used for EN and DE corpus)
   *
   * @param document File
   *
   * @return DocumentHapax
   */
  def extractEnDe(document: File): DocumentHapax = {
    val file = Source.fromFile(document)
    val extracted = apply(document.getName, file.mkString.filter(_ >= ' ').trim.split(" ").toIterator)
    file.close()

    extracted
  }

  /**
   * Read an hapax file and structure it
   *
   * @param hapaxPath Path to hapax file
   *
   * @return Vector of DocumentHapax
   */
  def hapaxFileToStructure(hapaxPath: String): Vector[DocumentHapax] = {
    val lines = Source.fromFile(hapaxPath).getLines().toVector
    lines.map { l =>
      val splitted = l.split(" ")

      DocumentHapax(splitted(0), splitted.drop(1).toVector)
    }
  }
}
