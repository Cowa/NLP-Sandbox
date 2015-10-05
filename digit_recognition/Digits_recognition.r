library("nnet")
# remember to change your working directory :  setwd("...path...")
setwd("/Users/bthomas/Projects/Cowa/NLP-Sandbox/digit_recognition")

#load data sets
digits <- read.delim("Digits_Train_7x20.data", header=F)
# 1300 row + all column
digits <- digits[1:1300,]
# Class (1st column)
digitCl = digits[,1]
digitsTest <- read.delim("Digits_Test_7x20.data", header=F)
digitClTest = digitsTest[,1]
#full version:
 #digitFt = digits[,3:141]
#or reduced features version:
f <- 0:19 * 7 +3
digitFt = digits[,f]
digitFtTest = digitsTest[,f]

#reduce number of classes:
select <- ((digitCl == 1) | (digitCl == 2)| (digitCl == 3)| (digitCl == 4)| (digitCl == 5))
selectTest <- ((digitClTest == 1) | (digitClTest == 2)| (digitClTest == 3)| (digitClTest == 4)| (digitClTest == 5))
digitCl <- digitCl[select]
digitFt <- digitFt[select,]
digitClTest <- digitClTest[selectTest]
digitFtTest <- digitFtTest[selectTest,]

#build the target vectors [0,..,0,1,0,..0]
digitTarg <- class.ind(digitCl)
digitTargTest <- class.ind(digitClTest)

#subset of training :
#select <- sample(dim(digitTarg)[1],20)
#digitTarg <- digitTarg[select,]
#digitFt <- digitFt[select,]


#training
# size -> nb neur dans couche caché
# maxit -> nb d'itér max (early stopping)
# decay -> sur les poids
# rang -> valeur des poids à l'init
#nndigit <- nnet(digitFt,digitTarg, size=20, maxit=500, decay=1e-4,rang = 1)

#save the weights and continue the training from this point:
#w <- nndigit$wts
#nndigit <- nnet(digitFt,digitTarg, size=20, maxit=500, decay=1e-4,rang = 1,Wts=w) -> Wts: save weights

#usefull functions to compute results:
# computes confusion matrix
test.cl <- function(true, pred) {
  true <- max.col(true)
  res <- max.col(pred)
  return (table(true, res))
}

# computes recognition rates
test.reco <- function(true, pred) {
  true <- max.col(true)
  res <- max.col(pred)
  return (as.numeric(sum(true == res)))
}

#compute the MSE
test.mse <- function(true, pred) {
  diff <- true - pred
  sqred <- diff * diff
  return (sum(sqred) / length(sqred))
}


#print(test.cl(digitTarg, predict(nndigit,digitFt)))
#cat("Reco rate (train) = ",test.reco(digitTarg, predict(nndigit,digitFt)),"/",dim(digitTarg)[1],"\n")
#cat("MSE  (train)      = ",test.mse(digitTarg, predict(nndigit,digitFt)),"\n")
#cat("Reco rate (Test)  = ",test.reco(digitTargTest, predict(nndigit,digitFtTest)),"/",dim(digitTargTest)[1],"\n")


###### Proposal for the first question :

learnVal <- function (dataFt, dataTarg,trainId, valId, nbN, nbLoop, nbIteX){
  dataTrainFt <- dataFt[trainId,]
  dataTrainTarg <- dataTarg[trainId,]
  
  dataValFt <- dataFt[valId,]
  dataValTarg <- dataTarg[valId,]
  
  #init a new random MLP
  new_nn <- nnet(dataTrainFt, dataTrainTarg, size=nbN, maxit=0, decay=1e-4, rang=1)
  best_nn = new_nn
  curr_w <- new_nn$wts
  # compute initial rates / mse and save them
  currTrRate <- test.reco(dataTrainTarg, predict(new_nn, dataTrainFt)) / dim(dataTrainFt)[1]
  currTrRateVal <- test.reco(dataValTarg, predict(new_nn, dataValFt)) / dim(dataValFt)[1]
  currTrMSE <- test.mse(dataTrainTarg, predict(new_nn, dataTrainFt))
  currTrMSEVal <- test.mse(dataValTarg, predict(new_nn, dataValFt))
  
  scoresT <- c(currTrRate/length(trainId))
  scoresV <- c(currTrRateVal/length(valId))
  mseT <- c(currTrMSE)
  mseV <- c(currTrMSEVal)
  iterations <- c(0)
  bestRate <- 0  
  bestIt <- 0
  cat("Starting Reco rate = ",currTrRate,"\n")
  for(i in 1:nbLoop){
    cat(i," / ", nbLoop, "\n")
    #continue the training
    new_nn <- nnet(dataTrainFt, dataTrainTarg, size=nbN, maxit=nbIteX, decay=1e-4, rang=1, Wts=curr_w)
    curr_w <- new_nn$wts
    #compute the rates/MSE
    currTrRate <- test.reco(dataTrainTarg, predict(new_nn, dataTrainFt))
    currTrRateVal <- test.reco(dataValTarg, predict(new_nn, dataValFt))
    currTrMSE <- test.mse(dataTrainTarg, predict(new_nn, dataTrainFt))
    currTrMSEVal <- test.mse(dataValTarg, predict(new_nn, dataValFt))
    
    #save values to plot
    scoresT <- c(scoresT,currTrRate / dim(dataTrainFt)[1])
    scoresV <- c(scoresV,currTrRateVal / dim(dataValFt)[1])
    mseT <- c(mseT,currTrMSE)
    mseV <- c(mseV,currTrMSEVal)
    iterations <- c(iterations, i * nbIteX)
    #save if best
    if(currTrRateVal > bestRate){
      bestRate <- currTrRateVal
      best_nn <- new_nn
      bestIt <- nbIteX * i
    }
  }
  return (list(nn = best_nn, nbIt=bestIt, scoreTrain = scoresT, scoreVal = scoresV, it=iterations, mseT=mseT,mseV=mseV))
}

plumn = 0
nbIteFor <- 2
for (i in 0:nbIteFor) {
  select = sample(645, 400)
  res <- learnVal(digitFt, digitTarg, select, -select, 20, 20, 50)
  nndigit <- res$nn

  plumn <- plumn + max(res$scoreVal)

  #par(fg = "black")
  #plot(res$it, res$scoreT, type = "l")
  #par(fg = "red")
  #lines(res$it, res$scoreVal, type = "l")

  #par(fg = "black")
  #plot(res$it, res$mseT, type = "l")
  #par(fg = "red")
  #lines(res$it, res$mseV, type = "l")  
}

azerty <- plumn / (nbIteFor + 1)
print(azerty)
