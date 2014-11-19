#!/usr/bin/env python
# coding: utf-8

import re
import glob
import codecs

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
	# Get the right number of the file (ex: fich11 => 11)
	fileNumber = re.findall('\d+', files[cursor])
	annotatedFile = open("txt/annotated_automatic/fich" + fileNumber[0] + ".txt", "wb")
	regexp = re.compile("\w+", re.U)

	for word in regexp.findall(fileContent):
		annotatedFile.write(word + " ")

	cursor = cursor + 1

annotatedFile.close()

