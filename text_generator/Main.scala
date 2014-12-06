object main {
  def main(args: Array[String]) {

    /** Simple alphabet with probability */
    val alphabet = Alphabet(List(
                    new Element("a", 0.50),
                    new Element("b", 0.25),
                    new Element("c", 0.25))
                   )

    /** Basic text generator based on alphabet */
    val generator = TextGenerator(alphabet)
    val generated = generator.generate(50)
    println(generated)
    println(generated.length)

    /** Markov states
      * How to read ?
      *
      * state0 generate text from the given alphabet
      *        has a 0.5 prob to be the initial state
      */
    val state0 = new State(alphabet, 0.5)
    val state1 = new State(alphabet, 0.25)
    val state2 = new State(alphabet, 0.25)

    /** State transitions
      * How to read ?
      *
      * state0 has a 0.5  prob to stay in the same state
      *        has a 0.25 prob to go to state1
      *        has a 0.25 prob to go to state2
      */
    val chains = List((
      state0, List(
        (state0, 0.5),
        (state1, 0.25),
        (state2, 0.25)
      )),
      (state1, List(
        (state0, 0.5),
        (state1, 0.5)
      )),
      (state2, List(
        (state1, 0.75),
        (state2, 0.25)
      ))
    )

    /** Generate text with Markov */
    val markov = MarkovChain(chains)
    val generatedMarkov = markov.generate(50)
    println(generatedMarkov)
    println(generatedMarkov.length)
  }
}
