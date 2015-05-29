network MergeAndSplit {
        n0.ports++ <--> DatarateChannel <--> b0.ports++;
        b0.ports++ <--> DatarateChannel <--> b1.ports++;
        n1.ports++ <--> DatarateChannel <--> b0.ports++;
        b1.ports++ <--> DatarateChannel <--> n3.ports++;
        b1.ports++ <--> DatarateChannel <--> n2.ports++;
}