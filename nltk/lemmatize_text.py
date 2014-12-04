#!/usr/bin/env python
# coding: utf-8
# author: Brice Thomas

import os
import re
import nltk
import glob
import codecs
import pprint

"""
 Only for \w+ followers
 Please, join us.
"""

# Word Net lemmatizer
wnl = nltk.WordNetLemmatizer()

# Get text files
files = glob.glob("txt/*.txt")

filesContent = []
wordAndLemma = dict()

# Get all text files' content
for file in files:
    fh = codecs.open(file, "r", "utf-8")
    filesContent.append(fh.read())
    fh.close()

# Extract words from text files' content
for fileContent in filesContent:
    regexp = re.compile("\w+", re.U)

    for word in regexp.findall(fileContent):
        wordAndLemma[word] = wnl.lemmatize(word)

pp = pprint.PrettyPrinter(indent=2)
pp.pprint(wordAndLemma)

print "Vocabulary size : %d" % len(wordAndLemma)
print "Lema size : %d" % len(set(wordAndLemma.values()))
