package com.github.cowa.domains

import com.github.cowa.models._

object Alignment {
  private def isTransfugee(word0: String, word1: String) = word0 == word1
  private def isCognate(word0: String, word1: String) =
    (word0.length > 4 && word1.length > 4) && word0.substring(0, 4) == word1.substring(0, 4)

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

    println(transfugees)
    println(cognates)
  }
}
