package com.github.cowa

import java.io.File
import java.util.regex.{Matcher, Pattern}
import scala.io.Source

object Alignment {
  def main(args: Array[String]) {
    val textTarget = new File("corpus/txt_target").listFiles.filter(_.getName.endsWith(".txt"))
    val textSources = new File("corpus/txt_source").listFiles.filter(_.getName.endsWith(".txt"))
    val termerSource = new File("corpus/termer_source/corpus.lem")
    val termerTarget = new File("corpus/termer_target/corpus.lem")

    rawTermerFileToHandyStruct(termerSource)
  }

  def rawTermerFileToHandyStruct(raw: File)/*: List[FileTermer]*/ = {
    splitRawTermerFileContentByFile(Source.fromFile(raw).getLines.toList)
  }

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

case class Term(word: String, lemme: String, tag: String)
case class FileTermer(fileName: String, terms: List[Term])
