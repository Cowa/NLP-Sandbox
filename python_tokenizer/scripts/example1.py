#!/usr/bin/env python
# -*- coding: utf-8 -*-

# auteur : Florian Boudin


# Lecture du contenu du fichier
import codecs

fh = codecs.open("data/txt/fich1.txt", "r", "utf-8")
content = fh.read()
fh.close()

# Extract words
import re

regexp = re.compile("\w+", re.U)
for word in regexp.findall(content):
	print word
