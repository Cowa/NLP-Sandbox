package com.github.cowa.staf

// For more informations on Staf :
// https://github.com/Cowa/SimpleTextAnnotationFormat
case class Staf(`type`: String, info: Option[String], line: Int, column: Int, endLine: Int, endColumn: Int)
