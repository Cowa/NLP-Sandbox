package me.verticale

import math.random

/** A text generator generate text with the given alphabet
  *
  * @param alphabet The alphabet to use
  */
case class TextGenerator(alphabet: Alphabet) {

  /** Generate a text with the given length
    */
  def generate(length: Int, acc: List[Element] = List()): List[Element] = length match {
    case 0 => acc
    case _ => generate(length - 1, generateOne() :: acc)
  }

  /** Generate a single element
    */
  def generateOne(acc: Double = 0.0, prob: Double = random): Element = {
    def rec(elements: List[(Element, Double)], prob: Double, acc: Double): Element = elements match {
      case (e, p) :: t if (acc + p >= prob) => e
      case (e, p) :: t => rec(t, prob, acc + p)
      case List() => Element("Nope")
    }
    rec(alphabet.elements, prob, acc)
  }
}
