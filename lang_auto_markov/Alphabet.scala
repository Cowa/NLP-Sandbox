package me.verticale

/** An alphabet is composed of several elements associated with a probability
  */
case class Alphabet(elements: List[(Element, Double)])

/** An element
  */
case class Element(val content: String) {
  override def toString(): String = content
}
