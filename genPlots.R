# args[1] needs to contain the path of the current folder
args <- commandArgs(trailingOnly = TRUE)
setwd(args[1])

# read all the data in ./Traces
files <- list.files(path = "./Traces", pattern="*.csv", full.names=T, recursive=FALSE)
dataPoints <- NULL
for (f in files) {
  dataPoints <- rbind(dataPoints, read.csv(f, sep = ";"))
}
# Count runs for each Algo
dataCount <- data.frame(table(dataPoints$Algo))
# Only care about successful runs
success <- subset(dataPoints, delayOkay == "true")

#BarPlot with the % of runs that were successful
print("Saving SuccessBarPlot...")
png(filename = "./pics/successBarPlot.png")
cf <- data.frame(table(success$Algo))
cf <- data.frame(cf[,], cf[2] / dataCount[2] * 100)
names(cf)[]<-c("Algo","Count","Better")
succBarPlot <- barplot(cf$Better, names.arg = cf$Algo, ylim = c(0,100), 
                       main = sprintf("besser als BruteForce \n [Stichprobenanzahl = %i]",dataCount$Freq[1]))
text(succBarPlot, 5, labels = sprintf("%2.0f%%", cf$Better))
dev.off()

for(portCount in 2:20) {
  for (depthCount in 2:portCount) {
    for (flowCount in 3:6){
      for (prioCount in 2:4) {
        name <- sprintf("./Traces/%dports_%ddepth_%dflows_%dprios_*.csv",portCount, depthCount, flowCount, prioCount)
        files <- Sys.glob(name)
        if (length(files) != 0) {
          dataPoints <- NULL
          for (f in files) {
            dataPoints <- rbind(dataPoints, read.csv(f, sep = ";"))
         }
         # Only care about successful runs
          dataPoints <- subset(dataPoints, delayOkay == "true")
          BT_succ <- subset(dataPoints, dataPoints$Algo =="BT")
          HC_succ <- subset(dataPoints, dataPoints$Algo =="HC")
          SA_succ <- subset(dataPoints, dataPoints$Algo =="SA")
          sEA_succ <- subset(dataPoints, dataPoints$Algo =="sEA")
          file <- sprintf("./pics/%dports_%ddepth_%dflows_%dprios.png",portCount, depthCount, flowCount, prioCount)
          print(file)
          png(filename = file)
          boxplot(BT_succ$Steps, HC_succ$Steps, SA_succ$Steps, sEA_succ$Steps, 
                  names = c("BT", "HC", "SA", "sEA") , ylab = "Schritte", ylim = c(0,50))
          dev.off()
        }
      }
    }
  }
}
