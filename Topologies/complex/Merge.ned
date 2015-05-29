network Merge {
        n0.ports++ <--> DatarateChannel <--> b0.ports++;
        b0.ports++ <--> DatarateChannel <--> b1.ports++;
        b1.ports++ <--> DatarateChannel <--> b2.ports++;
        n1.ports++ <--> DatarateChannel <--> b0.ports++;
        b2.ports++ <--> DatarateChannel <--> n2.ports++;
}