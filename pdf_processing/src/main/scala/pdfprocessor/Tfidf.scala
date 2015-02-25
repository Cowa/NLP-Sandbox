package com.github.cowa.pdfprocessor

import scala.collection.mutable.{HashMap}

object Tfidf {

  /** Compute tokens frequencies
    *
    * @param tokens Tokens
    *
    * @return Tokens frequencies in a HashMap: (token -> frequency)
    */
  def tokensFrequencies(tokens: List[String]): HashMap[String, Int] = {
    var frequencies = HashMap[String, Int]()

    for (token <- tokens) { frequencies.contains(token) match {
      case true => frequencies(token) = frequencies(token) + 1
      case false => frequencies(token) = 1
      }
    }

    frequencies
  }
}
