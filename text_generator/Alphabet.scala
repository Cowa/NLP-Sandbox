/** An alphabet is composed of several elements */
case class Alphabet(elements: List[Element])

/** An element has a content and a probability to be generated */
case class Element(content: String, prob: Double)
