library("HMM")
library("Matrix")

HMMs_source <- function() {
  states <- c("s1", "s2")
  symbols <- c("a", "b", "c") 
  
  P1_source <- c(1, 0)
  A1_source <- matrix(c(0.1, 0.9, 0.6, 0.4), nrow = 2, ncol = 2, byrow = T)
  B1_source <- matrix(c(0.1, 0.3, 0.6, 0.4, 0.2, 0.4), nrow = 2, ncol = 3, byrow = T)
  
  lambda1_source <- initHMM(states, symbols, P1_source, A1_source, B1_source)
  
  P2_source <- c(1, 0)
  A2_source <- matrix(c(0.4, 0.6, 0.8, 0.2), nrow = 2, ncol = 2, byrow = T)
  B2_source <- matrix(c(0.5, 0.4, 0.1, 0.2, 0.1, 0.7), nrow = 2, ncol = 3, byrow = T)
  
  lambda2_source <- initHMM(states, symbols, P2_source, A2_source, B2_source)
  
  return (list(hmm1 = lambda1_source, hmm2 = lambda2_source))
}

HMM_simu <- function(hmm1, hmm2, T = 10) {
  simulation1 <- simHMM(hmm1, T)
  simulation2 <- simHMM(hmm2, T)

  return (list(sim1 = simulation1, sim2 = simulation2))
}

loglikelihood <- function(hmm, obs) {
  forw <- forward(hmm, obs)
  loglike <- forw[1, length(obs)]
  
  for (i in 2:length(hmm$States)) {
    t = forw[i, length(obs)]
    
    if (t > - Inf) {
      loglike <- logAdd(t, loglike)
    }
  }
  return (loglike)
}

logAdd <- function(a, b) {
  if (a <= b) {
    return (b + log1p(exp(a - b)))
  }
  return (a + log1p(exp(b - a)))
}

HMMs_new <- function() {
  states <- c("s1", "s2")
  symbols <- c("a", "b", "c")

  P1_source <- c(1, 0)
  A1_source <- matrix(c(0.8, 0.2, 0.5, 0.5), nrow = 2, ncol = 2, byrow = T)
  B1_source <- matrix(c(0.3, 0.4, 0.3, 0.1, 0.8, 0.1), nrow = 2, ncol = 3, byrow = T)

  lambda1_source <- initHMM(states, symbols, P1_source, A1_source, B1_source)

  return (list(hmm1 = lambda1_source, hmm2 = lambda1_source))
}

Check_likelihood_BW <- function (hmm, obs) {
  for (i in 1:10) {
    bw <- baumWelch(hmm, obs)
    hmm <- bw$hmm
    print(loglikelihood(hmm, obs))
  }
}

train_BW <- function(training, N = 10) {
  obs <- HMM_simu(training$hmm1, training$hmm2)

  bw1 <- baumWelch(training$hmm1, obs$sim1$observation, N)
  bw2 <- baumWelch(training$hmm2, obs$sim2$observation, N)

  return (list(hmm1 = bw1$hmm, hmm2 = bw2$hmm))
}

classify <- function (source1_2, lambda1_2, T = 10,N = 1) {
  
}
