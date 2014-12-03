object main {
  def main(args: Array[String]) {
    val alphabet = Alphabet(List(
                    Element("a", 0.33),
                    Element("b", 0.33),
                    Element("c", 0.33))
                   )

    val generator = TextGenerator(alphabet)
    println(generator.generate(5))
    
    // Markov states
    lazy val state0: State = State(
                        0.5,
                        Map(
                          state1 -> 0.25,
                          state2 -> 0.25
                        )
                      )
     lazy val state1: State = State(
                        0.5,
                        Map(
                          state0 -> 0.5,
                          state2 -> 0.25
                        )
                      )
    lazy val state2: State = State(
                        0.5,
                        Map(
                          state0 -> 0.5,
                          state1 -> 0.25
                        )
                      )   


    val markov = Markov(alphabet, List(state0, state1, state2))
  }
}

