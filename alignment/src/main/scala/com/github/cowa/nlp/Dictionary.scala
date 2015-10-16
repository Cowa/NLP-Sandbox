package com.github.cowa.nlp

import scala.io.Source

case class Entry(source: String, target: String)

trait Dictionary {
  def load(path: String): List[String] = Source.fromFile(path).getLines().toList
}

trait CsvDictionary extends Dictionary {
  def toData(lines: List[String], srcIndex: Int, trgIndex: Int): List[Entry] = {
    lines.map { l =>
      val cols = l.split(";")
      Entry(cols(srcIndex), cols(trgIndex))
    }
  }
}
