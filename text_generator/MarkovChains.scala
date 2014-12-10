package me.verticale

import math.random

/** A simple Markov chains
  *
  * @param current Index of the current state (also used to set initial state)
  * @param states States
  * @param transitions Transitions with probabilities between states
  */
case class MarkovChains(var current: Int, states: Array[Element], transitions: Array[Array[Double]]) {

  /** Generate a text with the given length
    */
  def generate(length: Int, acc: String = ""): String = length match {
    case 0 => acc
    case _ => generate(length - 1, acc + generateOne())
  }

  /** Generate a single element
    */
  def generateOne(): String = {
    val result = states(current).content

    changeState()

    result
  }

  /** Change state
    */
  def changeState(): Unit = {
    var i = 0
    var acc  = 0.0
    val prob = random

    for (p <- transitions(current)) {
      if (acc + p >= prob)
        current = i
      else
        i += 1
    }
  }
}
