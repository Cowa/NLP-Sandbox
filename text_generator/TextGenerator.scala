import math.random

case class TextGenerator(alphabet: Alphabet) {

  /** Generate a text with the given length */
  def generate(length: Int, acc: String = ""): String = length match {
    case 0 => acc
    case _ => generate(length - 1, acc + generateChar())
  }

  /** Generate a single element */
  def generateChar(acc: Double = 0.0, prob: Double = random): String = {
    def rec(elements: List[Element], prob: Double, acc: Double): String = elements match {
      case h :: t if (acc + h.prob >= prob) => h.content
      case h :: t => rec(t, prob, acc + h.prob)
      case Nil => ""
    }
    rec(alphabet.elements, prob, acc)
  }
}
