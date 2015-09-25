import types._
import analysis._

import scalaz.stream.Process

import com.github.jenshaase.uimascala.core._
import com.github.jenshaase.uimascala.core.stream._

object Main {
  def main(args: Array[String]) {
    val p = Process("this is Richard Parker", "nice talk Patrick Palmer") |>
      casFromText |>
      annotate(new Tokenizer()) |>
      annotate(new NamedEntityAnnotator()) |>
      extractCas { cas =>
        cas.select[NamedEntity].map(_.getCoveredText).toList
      }

    println(p.toList)
  }
}
