package com.github.cowa.domains

import java.io.File
import scala.io.Source

import com.github.cowa.models._

object RawTermerDataTransducer {

  /**
   * Transform a raw terms file to a pretty and handy structure
   *
   * @param raw File containing the raw terms
   *
   * @return List of sexy structure
   */
  def rawTermerFileToHandyStruct(raw: File): List[FileTermer] = {
    val fileContentByFile = splitRawTermerFileContentByFile(Source.fromFile(raw).getLines().toList)
    val termRegex = """([^\s/]+?)/([^\s/]+?)/([^\s/]+)""".r

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
