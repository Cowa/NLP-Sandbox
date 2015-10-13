package com.github.cowa.nlp

import scala.xml._

case class LangElemXML(typ: String, term: String, lemm: String)
case class TradElemXML(valid: String, langElems: List[LangElemXML])

object SpecializedDictionary {
  def load(path: String): Elem = XML.loadFile("src/main/resources/ts.xml")

  def toData(xml: Elem): List[TradElemXML] = {
    (xml \\ "TRAD").map { e =>
      val langs = (e \\ "LANG").map { l =>
        LangElemXML((l \\ "@type").text, (l \\ "TERM").text, (l \\ "LEM").text)
      }

      TradElemXML((e \\ "@valid").text, langs.toList)
    }.toList
  }

  def get(path: String): Map[String, List[String]] = {
    toData(load(path)).filter(_.valid == "yes").map { entry =>
      entry.langElems.span(_.typ == "source") match {
        case (src, trg) => (src.head.lemm, trg.map(_.lemm))
      }
    }.toMap
  }
}
