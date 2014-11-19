#!/usr/bin/env python
# coding: utf-8
# author: Brice Thomas

import os
import re
import glob
import codecs

"""
 Only for \w+ followers
 Please, join us.
"""

# Get text files
files = glob.glob("txt/*.txt")

filesContent = []

# Get all text files' content
for file in files:
	fh = codecs.open(file, "r", "utf-8")
	filesContent.append(fh.read())
	fh.close()

# Extract words from text files' content
cursor = 0

for fileContent in filesContent:
	# Get the right number of the file (ex: fich11.txt => 11)
	fileNumber = re.findall("\d+", files[cursor])
	annotatedFile = open("txt/annotated_automatic/fich" + fileNumber[0] + ".txt", "wb")

	regexp = re.compile("\w+", re.U)

	for word in regexp.findall(fileContent):
		annotatedFile.write(word + " ")

	cursor = cursor + 1

annotatedFile.close()

# Quick & Dirty
# Launch evaluations
idFiles = [1, 2, 3, 11, 13, 14, 15, 16, 18, 21, 22, 24, 26, 28, 29, 30, 31, 32, 36, 39]

for id in idFiles:
	print "Evalution for file " + str(id)
	os.system("python scripts/eval.py txt/annotated_handmade/fich" + str(id) + ".txt txt/annotated_automatic/fich" + str(id) + ".txt")
	print ""

