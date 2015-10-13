package com.github.cowa.nlp

import com.github.cowa.models.Term

case class ContextVector(word: String, context: List[String])

object ContextVector {
  def build(terms: List[Term], size: Int): List[ContextVector] = {
    terms.zipWithIndex.map { case (e, i) =>
      ContextVector(e.lemme, (terms.slice(i - size / 2, i) ++ terms.slice(i + 1, i + size / 2 + 1)).map(_.lemme))
    }
  }
}
