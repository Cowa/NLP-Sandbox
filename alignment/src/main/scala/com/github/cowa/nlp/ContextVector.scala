package com.github.cowa.nlp

import com.github.cowa.models.Term

case class ContextVector(word: String, context: List[String])
case class ContextVectorFrequency(word: String, context: List[(String, Int)])

object ContextVector {
  def build(terms: List[Term], size: Int): List[ContextVector] = {
    terms.zipWithIndex.map { case (e, i) =>
      ContextVector(e.lemme, (terms.slice(i - size / 2, i) ++ terms.slice(i + 1, i + size / 2 + 1)).map(_.lemme))
    }
  }

  def addFrequencies(vectors: List[ContextVector]): List[ContextVectorFrequency] = {
    vectors.groupBy(_.word).map { case (k, v) =>
      ContextVectorFrequency(k, v.flatMap(_.context).groupBy(x => x).map { case (kv, vv) => (kv, vv.length) }.toList)
    }.toList
  }

  def toMap(vectors: List[ContextVectorFrequency]): Map[String, List[(String, Int)]] =
    vectors.groupBy(_.word).map { case (k, v) => (k, v.flatMap(_.context)) }
}
