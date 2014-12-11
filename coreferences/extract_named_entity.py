#!/usr/bin/env python
# coding: utf-8
# author: Brice Thomas

import re, sys, glob, codecs

##
# Extract text content from the given folder
#
# @param folder Path to the folder containing .txt files (ex: "ne-src/")
#
# @return An array containing text of each files
#
def extractTextFromFolder(folder):
    files = glob.glob(folder + "*.txt")
    filesContent = []

    # Get all text files' content
    for file in files:
        fh = codecs.open(file, "r", "utf-8")
        filesContent.append(fh.read())
        fh.close()

    return filesContent

##
# Extract annotated named entity from the given text
#
# @param text A string containing the text
#
# @return A dictionnary (key -> type, values -> array of named entities)
#
def extractNamedEntity(text):
    matched = re.match(r"<(.*?)>(.*?)</(.*?)>", text)
    namedEntities = dict()

    if matched:
        print matched.group(1) + ": " + matched.group(2)
        namedEntities[matched.group(1)].append(matched.group(2))

##
# Main program
#
texts = extractTextFromFolder(sys.argv[1])

for text in texts:
    extractNamedEntity(text)
