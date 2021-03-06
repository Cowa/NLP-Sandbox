package com.github.cowa.nlp

import com.github.cowa.models.{FileTermer, Term}

object Preprocessing {
  val tagsWeWant = List("ADJ", "JJ", "SBC", "NN"/*, "VCJ" , "VBN"*/)
  val tagsWeIgnore = List("ADJ1", "ADJ2", "NNP")

  def apply(s: List[FileTermer]): List[Term] = {
    s.flatMap(_.terms).filter(isInteresting).groupBy(_.lemme).map(_._2.head).toList
  }

  def apply(s: FileTermer): List[Term] = {
    s.terms.filter(isInteresting)
  }

  def isInteresting(t: Term): Boolean = {
    t.lemme.length >= 4 && tagsWeWant.exists(x =>
      t.tag.startsWith(x) && !tagsWeIgnore.exists(y => t.tag.startsWith(y))
    )
  }
}
