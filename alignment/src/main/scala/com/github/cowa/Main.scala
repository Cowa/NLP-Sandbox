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
      (srcTrms.map(x => ContextVector.build(x.map(_.lemme), 7)), trgTrms.map(x => ContextVector.build(x.map(_.lemme), 7)))
    }

    // Add frequencies to context vectors
    println("\nAdding frequencies...")
    val (flatSrcContextVector, flatTrgContextVector) = Timer.executionTime {
      (ContextVector.addFrequencies(srcContextVector.flatten), ContextVector.addFrequencies(trgContextVector.flatten))
    }

    println("\nTurning to Map...")
    val (mapSrcContextVector, mapTrgContextVector) = Timer.executionTime {
      (ContextVector.toMap(flatSrcContextVector).filterKeys(specializedDictionary.contains), ContextVector.toMap(flatTrgContextVector))
    }

    val translatedReference = specializedDictionary.map { case (word, transla) =>
      val candidates = simpleDictionary.getOrElse(word, List()) ++ cognateDictionary.getOrElse(word, List())
      (word, candidates, specializedDictionary(word).intersect(candidates).headOption)
    }

    /*println("\nTranslating context vectors...")
    val translatedContextVector = Timer.executionTime {
      specializedDictionary.map { case (word, _) =>
        (word, mapSrcContextVector.getOrElse(word, List()).map { case (x, i) =>
          (x, (simpleDictionary.getOrElse(x, List()) ++ cognateDictionary.getOrElse(x, List())).groupBy(identity).mapValues(_.size))
        })
      }
    }*/

    // @todo Refactor, it's ugly
    // First let's find the correct translation for each reference with our dictionaries
    val translated = translatedReference.map { case (word, candidates, real) =>
      val proposed = {
        if (candidates.contains(word))
          Some((word, 1))

        else if (candidates.exists(Levenshtein(_, word) < 4))
          Some(candidates.find(Levenshtein(_, word) < 4).head, 1)

        else
          candidates.groupBy(identity).mapValues(_.size).toList.sortBy(_._2).headOption
      }

      (word, proposed, real)
    } flatMap {
      case (_, None, _) => None
      case (w, Some(x), Some(y)) => Some((w, x._1, y, x._1 == y))
      case _ => None
    }

    println(translated.mkString("\n"))

    val accuracy = translated.count(_._4).toFloat / specializedDictionary.keys.size
    println(s"\nAccuracy: ${accuracy * 100}%")
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
