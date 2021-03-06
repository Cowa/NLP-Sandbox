package me.verticale

object main {
  def main(args: Array[String]) {
    println("\nClassic text generator\n")
    textGenerator()

    println("\nHere comes Markov\n")
    markovChains()

    println("\nCounter\n")
    sequenceCounter()
  }

  def textGenerator() {

    /** Simple alphabet with probabilities
      * For "non-biaisée", use a probability of 1/n (n => #elements)
      */
    val alphabet = Alphabet(List(
      (Element("a"), 0.25),
      (Element("b"), 0.25),
      (Element("c"), 0.5 )))

    /** Basic text generator based on alphabet */
    val generator = TextGenerator(alphabet)
    val generatedText = generator.generate(50)

    println(generatedText)

    /** Generate music \o/
      */
    val alphabet2 = Alphabet(List(
      (Element("Do "),  0.1428571428571429),
      (Element("Re "),  0.1428571428571429),
      (Element("Mi "),  0.1428571428571429),
      (Element("Fa "),  0.1428571428571429),
      (Element("Sol "), 0.1428571428571429),
      (Element("La "),  0.1428571428571429),
      (Element("Si "),  0.1428571428571429)))

    /** Basic text generator based on alphabet2 */
    val generator2 = TextGenerator(alphabet2)
    val generatedText2 = generator2.generate(50)

    println(generatedText2)
  }

  def markovChains() {
    // States
    val states = Array(Element("a"), Element("b"), Element("c"))

    // Transitions
    val transitions = Array(
      Array(0.1, 0.7, 0.2),
      Array(0.2, 0.2, 0.6),
      Array(0.4, 0.1, 0.5)
    )

    val markov = MarkovChains(0, states, transitions)
    val generatedMarkov = markov.generate(50)

    println(generatedMarkov)
  }

  def sequenceCounter() {
    var toFind = Array(Element("a"), Element("b"), Element("b"), Element("a"))
    val inside = Array(
      Element("a"), Element("b"), Element("r"), Element("a"), Element("c"), Element("a"), Element("d"), Element("a"),
      Element("b"), Element("r"), Element("a"), Element("d"), Element("a"), Element("b"), Element("r"), Element("a")
    )

    println("ABBA in ABRACADABRADABRA")
    println(SequenceCounter().count(toFind, inside))

    val alphabet = Alphabet(List(
      (Element("a"), 0.25),
      (Element("b"), 0.25),
      (Element("c"), 0.5 )))

    /** Basic text generator based on alphabet */
    val generator = TextGenerator(alphabet)

    // Average on 10 generated text of 3 to 50 length text
    for (i <- 3 to 50 by 2) {
      var sum = 0

      for (j <- 0 to 10) {
        val generated = generator.generate(i)
        sum += SequenceCounter().count(toFind, generated.toArray)
      }
      println(sum / 10)
    }
  }
}
