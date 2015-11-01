package com.github.cowa.nlp

import scala.math._

case class ContextVector(word: String, context: List[String])
case class ContextVectorFrequency(word: String, context: Map[String, Int])

object ContextVector {
  def build(terms: List[String], size: Int): List[ContextVector] = {
    terms.zipWithIndex.map { case (e, i) =>
      ContextVector(e, terms.slice(i - size / 2, i) ++ terms.slice(i + 1, i + size / 2 + 1))
    }
  }

  def addFrequencies(vectors: List[ContextVector]): List[ContextVectorFrequency] = {
    vectors.groupBy(_.word).map { case (k, v) =>
      ContextVectorFrequency(k, v.flatMap(_.context).groupBy(identity).mapValues(_.size))
    }.toList
  }

  def toMap(vectors: List[ContextVectorFrequency]): Map[String, List[(String, Int)]] =
    vectors.groupBy(_.word).mapValues(_.flatMap(_.context))

  def normalize(mapVector: Map[String, List[(String, Int)]]): Map[String, List[(String, Double)]] = {
    /* TF-IDF : 42% max */
    val df = mapVector.values.flatten.groupBy(_._1).mapValues(_.size)
    val size = mapVector.size

    mapVector.mapValues(l => l.map { case (w, freq) => (w, (freq.toDouble / l.size) * log(size / df(w))) })

    /* TF : 19% max
    mapVector.mapValues(l => l.map { case (w, freq) => (w, freq.toDouble / l.size) })*/

    /* None: 48% max
    mapVector.mapValues(l => l.map { case (w, freq) => (w, freq.toDouble) })*/
  }
}
