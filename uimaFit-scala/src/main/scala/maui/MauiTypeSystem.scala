package maui

import com.github.jenshaase.uimascala.core.description._

@TypeSystemDescription
object MauiTypeSystem {
  val Token = Annotation {}
  val NamedEntity = Annotation {}

  val Topic = Annotation {
    val id = Feature[String]
    val title = Feature[String]
    val correct = Feature[Boolean]
    val probability = Feature[Double]
  }
}
