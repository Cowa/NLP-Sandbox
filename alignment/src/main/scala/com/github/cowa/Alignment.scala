package com.github.cowa

import java.io.File
import scala.io.Source

case class Term(word: String, lemme: String, tag: String)
case class FileTermer(fileName: String, terms: List[Term])

object Alignment {
  def main(args: Array[String]) {
    val termsSource = new File("corpus/termer_source/corpus.lem")
    val termsTarget = new File("corpus/termer_target/corpus.lem")
    //val textTarget = new File("corpus/txt_target").listFiles.filter(_.getName.endsWith(".txt"))
    //val textSources = new File("corpus/txt_source").listFiles.filter(_.getName.endsWith(".txt"))

    // And here goes pre-processing!
    // From unstructured to structured data
    rawTermerFileToHandyStruct(termsSource)
    rawTermerFileToHandyStruct(termsTarget)
  }

  /**
   * Transform a raw terms file to a pretty and handy structure
   *
   * @param raw File containing the raw terms
   *
   * @return List of sexy structure
   */
  def rawTermerFileToHandyStruct(raw: File): List[FileTermer] = {
    val fileContentByFile = splitRawTermerFileContentByFile(Source.fromFile(raw).getLines().toList)
    val termRegex = """([^\s]+?)/([^\s]+?)/([^\s]+)""".r

    fileContentByFile.keys.toList.map { fileName =>
      val matches = termRegex findAllIn fileContentByFile(fileName)
      val terms = matches.map(_ => Term(matches group 1, matches group 3, matches group 2)).toList

      FileTermer(fileName, terms)
    }
  }

  /**
   * Split raw terms file content by file
   * @todo Arghh ugly! Refactor this shit in a more functional way
   *
   * @param content Raw terms content line by line
   *
   * @return A map, where key is the file name, and the value is the text content
   */
  def splitRawTermerFileContentByFile(content: List[String]): Map[String, String] = {
    val fileReferenceRegex = """^__FILE=(.*?.txt).*$""".r

    var fileContentByFile = scala.collection.mutable.Map[String,String]()
    var currentFileContent = ""
    var currentFileName = ""

    content.foreach { l =>
      if (l.startsWith("__FILE")) {

        l match {
          case fileReferenceRegex(fileName) => currentFileName = fileName
        }
      }
      else if (l.startsWith("__END")) {
        fileContentByFile += currentFileName -> currentFileContent
        currentFileContent = ""
      }
      else {
        currentFileContent += l
      }
    }
    fileContentByFile.toMap
  }
}
