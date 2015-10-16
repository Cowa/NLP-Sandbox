package com.github.cowa.nlp

object SimpleDictionary extends CsvDictionary {
  def get(path: String): Map[String, List[String]] =
    toData(load(path), 0, 3).groupBy(_.source).map { case (k, v) => (k, v.map(_.target)) }
}
