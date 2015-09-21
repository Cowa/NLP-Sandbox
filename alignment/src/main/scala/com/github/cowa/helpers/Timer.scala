package com.github.cowa.helpers

object Timer {
  def executionTime[A](f: => A): A = {
    val t0 = System.nanoTime
    val result = f
    val t1 = System.nanoTime
    println(s"Done in: ${(t1 - t0) / 1e9}s")
    result
  }
}
