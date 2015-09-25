package analysis

import org.apache.uima.jcas.JCas

import types.{NamedEntity, Token}

import com.github.jenshaase.uimascala.core._

/**
 * Dumbest named entity recognition
 */
class NamedEntityAnnotator extends SCasAnnotator_ImplBase {
  def process(jcas: JCas) = {
    jcas
      .select[Token]
      .filter(_.getCoveredText.matches("[A-Z].*"))
      .foreach(e => jcas.annotate[NamedEntity](e.getBegin, e.getEnd))
  }
}
