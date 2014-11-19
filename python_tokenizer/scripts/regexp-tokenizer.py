#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
Ce script permet de découper en mots le fichier texte passé en paramètre sur la
base d'une expression rationnelle capturant les différents mots.

Auteur : Florian Boudin 

Exemple d'utilisation :
    python regexp-tokenizer.py data/txt/fich1.txt
"""

import sys, codecs, re

# L'expression rationnelle à utiliser
regexp = re.compile(r"""(?x)
		TODO !!!
		""", re.U)

# Lecture du fichier passé en paramètre
fh = codecs.open(sys.argv[1], "r", "utf-8")
content = fh.read()
fh.close()

# Extraction des mots
for word in regexp.finditer(content):
	print word.group(0)

