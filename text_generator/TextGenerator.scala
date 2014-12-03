import math.random

case class TextGenerator(alphabet: Alphabet) {

  /** Generate a text with the given length */
  def generate(length: Int, acc: String = ""): String = length match {
    case 0 => acc
    case _ => generate(length - 1, acc + generateChar)
  }

  /** Generate a single element */
  def generateChar: String = {
    var acc = 0.0
    val probability = random

    for (element <- alphabet.elements) {
      acc += element.probability

      if (acc >= probability)
        return element.content
    }
    ""
  }
}

case class MarkovChain(states: List[State]) {
  var currentState: State = states(0)

  def generate(length: Int): String = {
    currentState = initialState
    ""
  }

  /** Return the initial state based on the entrace probabilities */
  def initialState: State = {
    var acc = 0.0
    val probability = random

    for (state <- states) {
      acc += state.entrance
      
      if (acc >= probability)
        return state
    }
    states(0)
  }
}

case class State(alphabet: Alphabet, entrance: Double)

