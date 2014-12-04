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

# Stemmer
porter = nltk.PorterStemmer()
lancaster = nltk.LancasterStemmer()

# Get text files
files = glob.glob("txt/*.txt")

filesContent = []
wordAndStemP = dict()
wordAndStemL = dict()

# Get all text files' content
for file in files:
    fh = codecs.open(file, "r", "utf-8")
    filesContent.append(fh.read())
    fh.close()

# Tokenize & stemmatize
for fileContent in filesContent:
    regexp = re.compile("\w+", re.U)

    for word in regexp.findall(fileContent):
        wordAndStemP[word] = porter.stem(word)
        wordAndStemL[word] = lancaster.stem(word)

# Result
pp = pprint.PrettyPrinter(indent=2)
pp.pprint(wordAndStemP)
pp.pprint(wordAndStemL)

print "Vocabulary size : %d" % len(wordAndStemP)
print "Stem size Porter : %d" % len(set(wordAndStemP.values()))
print "Stem size Lancaster : %d" % len(set(wordAndStemL.values()))
