package com.github.cowa.nlp

import scala.xml._

case class LangNodeXML(typ: String, term: String, lemm: String)
case class TradNodeXML(valid: String, langElems: List[LangNodeXML])

object SpecializedDictionary {
  def load(path: String): Elem = XML.loadFile("src/main/resources/ts.xml")

  def toData(xml: Elem): List[TradNodeXML] = {
    (xml \\ "TRAD").map { e =>
      val langs = (e \\ "LANG").map { l =>
        LangNodeXML((l \\ "@type").text, (l \\ "TERM").text, (l \\ "LEM").text)
      }

      TradNodeXML((e \\ "@valid").text, langs.toList)
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
