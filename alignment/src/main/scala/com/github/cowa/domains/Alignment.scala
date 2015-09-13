package com.github.cowa.domains

import com.github.cowa.models._

object Alignment {
  private def isTransfugee(w0: String, w1: String) = w0 == w1
  private def isCognate(w0: String, w1: String) =
    (w0.length > 4 && w1.length > 4) && !isTransfugee(w0, w1) && w0.substring(0, 4) == w1.substring(0, 4)

  def findCognatesAndTransfugees(sources: List[Term], targets: List[Term]) = {
    val targetsSet = targets.toSet
    val sourcesSet = sources.toSet

    val transfugees = sourcesSet.flatMap(
      x => targetsSet.filter(
        y => isTransfugee(x.lemme, y.lemme)).map(y => Comparable(x.lemme, y.lemme, "transfugee"))
    )

    val cognates = sourcesSet.flatMap(
      x => targetsSet.filter(
        y => isCognate(x.lemme, y.lemme)).map(y => Comparable(x.lemme, y.lemme, "cognate"))
    )

    println(transfugees.size)
    println(cognates.size)
  }
}
