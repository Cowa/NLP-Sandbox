package com.github.cowa.models

case class Term(word: String, lemme: String, tag: String)
case class FileTermer(fileName: String, terms: List[Term])
case class Aligned(w0: String, w1: String, kind: String)
