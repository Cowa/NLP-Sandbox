package me.verticale

import math.random

/** A text generator generate text with the given alphabet
  *
  * @param alphabet The alphabet to use
  */
case class TextGenerator(alphabet: Alphabet) {

  /** Generate a text with the given length
    */
  def generate(length: Int, acc: String = ""): String = length match {
    case 0 => acc
    case _ => generate(length - 1, acc + generateOne())
  }

  /** Generate a single element
    */
  def generateOne(acc: Double = 0.0, prob: Double = random): String = {
    def rec(elements: List[(Element, Double)], prob: Double, acc: Double): String = elements match {
      case (e, p) :: t if (acc + p >= prob) => e.content
      case (e, p) :: t => rec(t, prob, acc + p)
      case List() => ""
    }
    rec(alphabet.elements, prob, acc)
  }
}
