package com.github.cowa.nlp

object CognateDictionary extends CsvDictionary {
  def get(path: String): Map[String, List[String]] =
    toData(load(path), 0, 1).groupBy(_.source).map { case (k, v) => (k, v.map(_.target)) }
}
