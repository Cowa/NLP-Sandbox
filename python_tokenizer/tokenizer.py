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
for fileContent in filesContent:
	regexp = re.compile("\w+", re.U)
	for word in regexp.findall(fileContent):
		print word

