package com.github.cowa.helpers

import com.github.cowa.models._

object Alignment {
  def isTransfugee(w0: String, w1: String) = w0 == w1

  def isCognate(w0: String, w1: String) =
    (w0.length > 4 && w1.length > 4) && !isTransfugee(w0, w1) && w0.substring(0, 4) == w1.substring(0, 4) && Levenshtein.distance(w0, w1) < 3

  def isEquivalentTag(t0: String, t1: String): Boolean = {
    t0 match {
      case x if x.startsWith("SBC") && t1.startsWith("NN") => true
      case x if x.startsWith("ADJ") && t1.startsWith("JJ") => true
      case _ => false
    }
  }

  def findCognatesAndTransfugees(sources: List[Term], targets: List[Term]): Map[String, List[Aligned]] = {
    sources.flatMap(
      x => targets.filter(
        y => isEquivalentTag(x.tag, y.tag) && isTransfugee(x.lemme, y.lemme)).map(y => Aligned(x.lemme, y.lemme, "transfugee"))
    ).union(sources.flatMap(
      x => targets.filter(
        y => isEquivalentTag(x.tag, y.tag) && isCognate(x.lemme, y.lemme)).map(y => Aligned(x.lemme, y.lemme, "cognate"))
    )).groupBy(_.kind)
  }
}
