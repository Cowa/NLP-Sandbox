import math.random

case class MarkovChain(states: List[State]) {
  var currentState: State = states(0)

  def generate(length: Int): String = {
    currentState = initialState
    "a"
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
