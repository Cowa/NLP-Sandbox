package com.github.cowa

import java.io.File
import com.github.cowa.nlp._
import com.github.cowa.helpers._

object Main {
  lazy val simpleDictionary = SimpleDictionary.get("src/main/resources/dictionary-fr-en.csv")
  lazy val cognateDictionary = CognateDictionary.get("src/main/resources/cognates.csv")
  lazy val specializedDictionary = SpecializedDictionary.get("src/main/resources/ts.xml")

  lazy val sourceTermsFile = new File("corpus/termer_source/corpus.lem")
  lazy val targetTermsFile = new File("corpus/termer_target/corpus.lem")

  def main(args: Array[String]): Unit = {
    contextVectorTranslator()
  }

  // Translate with context vector
  def contextVectorTranslator() {
    val sources = TermsExtractor.rawTermerFileToHandyStruct(sourceTermsFile)
    val targets = TermsExtractor.rawTermerFileToHandyStruct(targetTermsFile)

    val srcTrms = sources.map(Preprocessing(_))
    val trgTrms = targets.map(Preprocessing(_))

    val (srcContextVector, trgContextVector) = Timer.executionTime {
      (srcTrms.map(ContextVector.build(_, 7)), trgTrms.map(ContextVector.build(_, 7)))
    }
  }

  // Generate cognates
  def cognates() {
    // From unstructured to structured data
    println("Structuring raw data...")

    val sources = TermsExtractor.rawTermerFileToHandyStruct(sourceTermsFile)
    val targets = TermsExtractor.rawTermerFileToHandyStruct(targetTermsFile)

    println("Done.\n")

    // And here goes pre-processing!
    println("Pre-processing...")

    val sourcesTerms = Preprocessing(sources)
    val targetsTerms = Preprocessing(targets)

    println(s"Source terms number: ${sourcesTerms.length}")
    println(s"Target terms number: ${targetsTerms.length}")

    println("Done.\n")

    println("Starting cognates alignment...")

    // Starting alignment process...
    val aligned = Timer.executionTime {
      AlignmentCognate(sourcesTerms, targetsTerms)
    }.filter(x => simpleDictionary.contains(x.w0))

    println(s"Cognates number: ${aligned.length}")

    // Write results to CSV ;)
    AlignedWriter.writeToCsv(aligned.sortBy(_.w0), "result-cognate.csv")
  }
}
