package com.github.cowa.helpers

import language.postfixOps

object CosineSimilarity {
  def apply(t1: Map[String, Double], t2: Map[String, Double]): Double = {
    val m = scala.collection.mutable.HashMap[String, (Double, Double)]() // word, t1 freq, t2 freq

    val sum1 = t1.foldLeft(0d) { case (sum, (word, freq)) =>
      m += word -> (freq, 0d)
      sum + freq
    }

    val sum2 = t2.foldLeft(0d) { case (sum, (word, freq)) =>
      m.get(word) match {
        case Some((freq1, _)) => m += word -> (freq1, freq)
        case None => m += word -> (0d, freq)
      }
      sum + freq
    }

    val (p1, p2, p3) = m.foldLeft((0d, 0d, 0d)) { case ((s1, s2, s3), e) =>
      val fs = e._2
      val f1 = fs._1 / sum1
      val f2 = fs._2 / sum2
      (s1 + f1 * f2, s2 + f1 * f1, s3 + f2 * f2)
    }

    p1 / (Math.sqrt(p2) * Math.sqrt(p3))
  }
}
