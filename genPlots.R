setwd("~/sync/Studium/FS_6/Bachelor-Projekt/UBSOpti/Traces")
files <- list.files(pattern="*.csv", full.names=T, recursive=FALSE)
lapply(files, function(x) {
  x
})


test <- read.csv("./8_10ports_50StreamConfigCount_10368maxSteps_2MaxPrio_100modiBTHCSAsEA.csv", header=TRUE, sep=";")
View(test)
# All failed tries
failed <- subset(test, delayOkay == "false")
cf <- data.frame(table(a$Algo))
names(cf)[1]<-"Algo"
names(cf)[2]<-"Count"
barplot(cf$Count, names.arg = cf$Algo, main = "# of Try worse than BruteForce (50%)")


success <- subset(test, delayOkay == "true" & test$Steps > 1)

BT_succ <- subset(success, Algo =="BT")
HC_succ <- subset(success, Algo =="HC")
SA_succ <- subset(success, Algo =="SA")
sEA_succ <- subset(success, Algo =="sEA")


boxplot(data.frame(BT_succ)$Steps, data.frame(HC_succ)$Steps, data.frame(SA_succ)$Steps, data.frame(sEA_succ)$Steps,
        names = c("BT", "HC", "SA", "sEA"), outline = FALSE)

boxplot(data.frame(BT_succ)$Steps, data.frame(HC_succ)$Steps, data.frame(SA_succ)$Steps,
        names = c("BT", "HC", "SA"), outline = FALSE)

hist(data.frame(BT_succ)$Steps, main = "Histogram Backtrack", xlab = "steps")
hist(data.frame(HC_succ)$Steps, main = "Histogram Hillclimbing", xlab = "steps")
hist(data.frame(SA_succ)$Steps, main = "Histogram Simulated Annealing", xlab = "steps")
hist(data.frame(sEA_succ)$Steps, main = "Histogram simple Evolutionary", xlab = "steps")
