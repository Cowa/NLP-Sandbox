package com.github.cowa.nlp

import scala.io.Source

case class Entry(source: String, target: String)

object Dictionary {
  def load(path: String): List[String] = Source.fromFile(path).getLines().toList

  def toData(lines: List[String]): List[Entry] = {
    lines.map { l =>
      val cols = l.split(";")
      Entry(cols(0), cols(3))
    }
  }

  def get(path: String): Map[String, List[String]] =
    toData(load(path)).groupBy(_.source).map { case (k, v) => (k, v.map(_.target)) }
}
