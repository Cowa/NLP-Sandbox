package me.verticale.box

import opennlp.tools.sentdetect.SentenceModel
import opennlp.tools.sentdetect.SentenceDetectorME

import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel

import opennlp.tools.postag.POSTaggerME
import opennlp.tools.postag.POSModel

/** Sandbox of every components
  */
object main {
  lazy val sentenceDetector =
    new SentenceDetectorME(
      new SentenceModel(
        getClass.getResourceAsStream("/models/en-sent.bin")
      )
    )

  lazy val tokenizer =
    new TokenizerME(
      new TokenizerModel(
        getClass.getResourceAsStream("/models/en-token.bin")
      )
    )

  lazy val tagger =
    new POSTaggerME(
      new POSModel(
        getClass.getResourceAsStream("/models/en-pos-maxent.bin")
      )
    )

  def main(args: Array[String]) {
    val testingText = "Hello Mr. John. What do you need ? That's nice to see you again"

    val sentDetected = sentenceDetector.sentDetect(testingText)
    sentDetected.foreach(println)

    println()

    val tokens = tokenizer.tokenize(testingText)
    tokens.foreach(println)

    println()

    val postaggedSentences = tokens.map(tagger.tag(_))
    postaggedSentences.foreach(println)
  }
}
