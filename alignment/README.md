```scala
val textTarget = new File("corpus/txt_target").listFiles.filter(_.getName.endsWith(".txt"))
val textSources = new File("corpus/txt_source").listFiles.filter(_.getName.endsWith(".txt"))
```
