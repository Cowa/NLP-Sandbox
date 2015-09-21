package com.github.cowa.helpers

import com.github.cowa.models.{FileTermer, Term}

object preprocessing {
  val tagsWeWant = List("ADJ", "JJ", "SBC", "NN")
  val tagsWeIgnore = List("ADJ1", "ADJ2", "NNP")

  def apply(s: List[FileTermer]): List[Term] = {
    s.flatMap(_.terms).filter(isItInteresting).groupBy(_.lemme).map(_._2.head).toList
  }

  def isItInteresting(t: Term): Boolean = {
    tagsWeWant.exists(
      x => t.tag.startsWith(x) && !tagsWeIgnore.exists(y => t.tag.startsWith(y))
    )
  }
}
