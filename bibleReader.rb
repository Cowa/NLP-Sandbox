require 'stemmify'
require 'tokenizer'

# Returns file's content as a string
def pleaseReadFile(pathToFile)
    contentFile = ''

    File.open(pathToFile, 'r:ascii-8bit') do |file|
        file.each_line do |line|
            contentFile += line
        end
    end

    return contentFile
end

# Returns array of tokens (as string) of the given string
def pleaseTokenize(toBeTokenized)
    token = Tokenizer::Tokenizer.new(:en).tokenize(toBeTokenized)
    return token
end

# Returns true if the given string is trash
def pleaseIsItTrash?(potentialTrash)
    if potentialTrash == '.' or potentialTrash == '..' or potentialTrash == '...' or potentialTrash == ',' or potentialTrash == '' or potentialTrash == ' ' or potentialTrash == "\n"
        return true
    else
        return false
    end
end

# Go through the given directory and compute some statistics
def pleaseCheckThisDirectory(directory)
    wordOccurrence = Hash.new(0)
    stemOccurrence = Hash.new(0)

    Dir.foreach(directory) do |file|
        puts 'Reading file ' + file
        next if pleaseIsItTrash?(file)

        pleaseTokenize(pleaseReadFile(directory + file)).each do |token|
            next if pleaseIsItTrash?(token)

            wordOccurrence[token]      += 1
            stemOccurrence[token.stem] += 1
        end
    end

    puts 'Number of token: ' + wordOccurrence.size().to_s
    puts 'Number of stem:  ' + stemOccurrence.size().to_s
end

=begin
                                     ....
                                    W$$$$$u
                                    $$$$F**+           .oW$$$eu
                                    ..ueeeWeeo..      e$$$$$$$$$
                                .eW$$$$$$$$$$$$$$$b- d$$$$$$$$$$W
                    ,,,,,,,uee$$$$$$$$$$$$$$$$$$$$$ H$$$$$$$$$$$~
                 :eoC$$$$$$$$$$$C""?$$$$$$$$$$$$$$$ T$$$$$$$$$$"
                  $$$*$$$$$$$$$$$$$e "$$$$$$$$$$$$$$i$$$$$$$$F"
                  ?f"!?$$$$$$$$$$$$$$ud$$$$$$$$$$$$$$$$$$$$*Co
                  $   o$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
          !!!!m.*eeeW$$$$$$$$$$$f?$$$$$$$$$$$$$$$$$$$$$$$$$$$$$U
          !!!!!! !$$$$$$$$$$$$$$  T$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
           *!!*.o$$$$$$$$$$$$$$$e,d$$$$$$$$$$$$$$$$$$$$$$$$$$$$$:
          "eee$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$C
         b ?$$$$$$$$$$$$$$**$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$!
         Tb "$$$$$$$$$$$$$$*uL"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$'
          $$o."?$$$$$$$$F" u$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
           $$$$en ```    .e$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$'
            $$$B*  =*"?.e$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$F
             $$$W"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"
              "$$$o#$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"
             R: ?$$$W$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" :!i.
              !!n.?$???""``.......,``````"""""""""""``   ...+!!!
               !* ,+::!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*`
               "!?!!!!!!!!!!!!!!!!!!~ !!!!!!!!!!!!!!!!!!!~`
               +!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!!!?!`
             .!!!!!!!!!!!!!!!!!!!!!' !!!!!!!!!!!!!!!, !!!!
            :!!!!!!!!!!!!!!!!!!!!!!' !!!!!!!!!!!!!!!!! `!!:
         .+!!!!!!!!!!!!!!!!!!!!!~~!! !!!!!!!!!!!!!!!!!! !!!.
        :!!!!!!!!!!!!!!!!!!!!!!!!!.`:!!!!!!!!!!!!!!!!!:: `!!+
        "~!!!!!!!!!!!!!!!!!!!!!!!!!!.~!!!!!!!!!!!!!!!!!!!!.`!!:
            ~~!!!!!!!!!!!!!!!!!!!!!!! ;!!!!~` ..eeeeeeo.`+!.!!!!.
          :..    `+~!!!!!!!!!!!!!!!!! :!;`.e$$$$$$$$$$$$$u .
          $$$$$$beeeu..  `````~+~~~~~" ` !$$$$$$$$$$$$$$$$ $b
          $$$$$$$$$$$$$$$$$$$$$UU$U$$$$$ ~$$$$$$$$$$$$$$$$ $$o
         !$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$. $$$$$$$$$$$$$$$~ $$$u
         !$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$! $$$$$$$$$$$$$$$ 8$$$$.
         !$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$X $$$$$$$$$$$$$$`u$$$$$W

                        Here the magic begins \o/
=end

pleaseCheckThisDirectory('bible/')
