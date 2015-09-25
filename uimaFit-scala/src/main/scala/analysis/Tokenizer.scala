package analysis

import java.text.BreakIterator

import types.Token

import org.apache.uima.jcas.JCas

import com.github.jenshaase.uimascala.core._
import com.github.jenshaase.uimascala.core.SCasAnnotator_ImplBase

class Tokenizer extends SCasAnnotator_ImplBase {
  def process(jcas: JCas) = {
    val bi = BreakIterator.getWordInstance
    bi.setText(jcas.getDocumentText)

    var last = bi.first
    var cur = bi.next
    while (cur != BreakIterator.DONE) {
      if (jcas.getDocumentText.substring(last, cur).trim != "") {
        jcas.annotate[Token](last, cur)
      }

      last = cur
      cur = bi.next
    }
  }

}
