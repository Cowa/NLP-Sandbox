import math.random

case class MarkovChain(chains: List[(State, List[(State, Double)])]) {

  /** Generate a text with the given length
    */
  def generate(length: Int, acc: String = ""): String = {
    def rec(length: Int, state: State, acc: String = ""): String = length match {
      case 0 => acc
      case _ => rec(length - 1, next(state), acc + generateOne(state))
    }
    rec(length, initial())
  }

  /** Generate a single element
    */
  def generateOne(state: State, prob: Double = random, acc: Double = 0.0): String = {
    def rec(elements: List[Element], prob: Double, acc: Double): String = elements match {
      case h :: t if (acc + h.prob >= prob) => h.content
      case h :: t => rec(t, prob, acc + h.prob)
      case Nil => ""
    }
    rec(state.alphabet.elements, prob, acc)
  }

  /** Return the initial state based on the entrace probabilities
    */
  def initial(acc: Double = 0.0, prob: Double = random): State = {
    def rec(chains: List[(State, List[(State, Double)])], acc: Double, prob: Double): State = chains match {
      case (s, l) :: t if (acc + s.prob >= prob) => s
      case (s, l) :: t => rec(t, acc + s.prob, prob)
    }
    rec(chains, acc, prob)
  }

  /** Return the next state of the given state
    */
  def next(state: State, prob: Double = random, acc: Double = 0.0): State = {
    def rec0(l: List[(State, List[(State, Double)])]): State = l match {
      case (s, l) :: t if (s == state) => {
        def rec1(l: List[(State, Double)], prob: Double, acc: Double): State = l match {
          case (s, p) :: t if (acc + p >= prob) => s
          case (s, p) :: t => rec1(t, prob, acc + p)
        }
        rec1(l, prob, acc)
      }
      case (s, l) :: t => rec0(t)
    }
    rec0(chains)
  }
}

case class State(alphabet: Alphabet, prob: Double)
