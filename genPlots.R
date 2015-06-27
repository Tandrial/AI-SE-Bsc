args <- commandArgs(trailingOnly = TRUE)
setwd(args[1])

files <- list.files(path = "./Traces", pattern="*.csv", full.names=T, recursive=FALSE)
dataPoints <- NULL
for (f in files) {
  dataPoints <- rbind(dataPoints, read.csv(f, sep = ";"))
}
dataCount <- data.frame(table(dataPoints$Algo))
success <- subset(dataPoints, delayOkay == "true")

print("Saving SuccessBarPlot...")
png(filename = "./successBarPlot.png")
cf <- data.frame(table(success$Algo))
cf <- data.frame(cf[,], cf[2] / dataCount[2]*100)
names(cf)[]<-c("Algo","Count","Better")
succBarPlot <- barplot(cf$Better , names.arg = cf$Algo, ylim = c(0,100), 
                       main = sprintf("better than BruteForce \n [n = %i]",dataCount$Freq[1]))
text(succBarPlot, 5,labels = sprintf("%2.2f%%", cf$Better))
dev.off()

print("Saving Histograms...")
png(filename = "./BT_Histogram.png")
BT_succ <- subset(success, Algo =="BT")
hist(BT_succ$Steps, main = "Histogram Backtrack", xlab = "steps", ylab = "")
dev.off()

png(filename = "./HC_Histogram.png")
HC_succ <- subset(success, Algo =="HC")
hist(HC_succ$Steps, main = "Histogram Hillclimbing", xlab = "steps", ylab = "")
dev.off()

png(filename = "./SA_Histogram.png")
SA_succ <- subset(success, Algo =="SA")
hist(SA_succ$Steps, main = "Histogram Simulated Annealing", xlab = "steps", ylab = "")
dev.off()

png(filename = "./sEA_Histogram.png")
sEA_succ <- subset(success, Algo =="sEA")
hist(sEA_succ$Steps, main = "Histogram simple Evolutionary", xlab = "steps", ylab = "")
dev.off()