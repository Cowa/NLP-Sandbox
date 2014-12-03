object main {
  def main(args: Array[String]) {
    val alphabet = Alphabet(List(
                    Element("a", 0.33),
                    Element("b", 0.33),
                    Element("c", 0.33))
                   )

    val generator = TextGenerator(alphabet)
    println(generator.generate(50))
    
    val state0 = State(alphabet, 0.5)
    val state1 = State(alphabet, 0.25)
    val state2 = State(alphabet, 0.15)
    
    val markov = MarkovChain(List(state0, state1, state2))
    markov.generate(50)
  }
}

