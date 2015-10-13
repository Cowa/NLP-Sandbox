package com.github.cowa.nlp

import scala.io.Source

trait CsvDictionary {
  def load(path: String): List[String] = Source.fromFile(path).getLines().toList

  def toData(lines: List[String], srcIndex: Int, trgIndex: Int): List[Entry] = {
    lines.map { l =>
      val cols = l.split(";")
      Entry(cols(srcIndex), cols(trgIndex))
    }
  }
}
