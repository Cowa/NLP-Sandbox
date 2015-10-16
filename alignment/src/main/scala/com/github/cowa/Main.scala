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
    println("Loading and structuring...")
    val (sources, targets) = Timer.executionTime {
      (TermsExtractor.rawTermerFileToHandyStruct(sourceTermsFile), TermsExtractor.rawTermerFileToHandyStruct(targetTermsFile))
    }

    println("\nPre-processing structured data...")
    val (srcTrms, trgTrms) = Timer.executionTime {
      (sources.map(Preprocessing(_)), targets.map(Preprocessing(_)))
    }

    // Build simple context vector
    println("\nCreating context vectors...")
    val (srcContextVector, trgContextVector) = Timer.executionTime {
      (srcTrms.map(ContextVector.build(_, 7)), trgTrms.map(ContextVector.build(_, 7)))
    }

    // Add frequencies to context vectors
    println("\nAdding frequencies...")
    val (flatSrcContextVector, flatTrgContextVector) = Timer.executionTime {
      (ContextVector.addFrequencies(srcContextVector.flatten), ContextVector.addFrequencies(trgContextVector.flatten))
    }

    println("\nTurning to Map...")
    val (mapSrcContextVector, mapTrgContextVector) = Timer.executionTime {
      (ContextVector.toMap(flatSrcContextVector), ContextVector.toMap(flatTrgContextVector))
    }

    /* No needs

    val translatedReference = specializedDictionary.map { case (word, transla) =>
      val candidates = simpleDictionary.getOrElse(word, List()) ++ cognateDictionary.getOrElse(word, List())
      (word, candidates, specializedDictionary(word).intersect(candidates).nonEmpty)
    }

    val accuracy = translatedReference.count(_._3).toFloat / translatedReference.size

    println(s"Potential default maximum accuracy: ${accuracy * 100}%")
    */

    println("\nTranslating context vectors...")
    val translatedContextVector = Timer.executionTime {
      specializedDictionary.map { case (word, _) =>
        (word, mapSrcContextVector.getOrElse(word, List()).map { case (x, i) =>
          (x, (simpleDictionary.getOrElse(x, List()) ++ cognateDictionary.getOrElse(x, List())).groupBy(identity).mapValues(_.size))
        })
      }
    }

    //println(translatedContextVector.mkString("\n\n"))
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
