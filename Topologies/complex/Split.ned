network Split {
        n0.ports++ <--> DatarateChannel <--> b0.ports++;
        b0.ports++ <--> DatarateChannel <--> b1.ports++;
        b1.ports++ <--> DatarateChannel <--> b2.ports++;
        b2.ports++ <--> DatarateChannel <--> n1.ports++;
        b0.ports++ <--> DatarateChannel <--> b3.ports++;
        b3.ports++ <--> DatarateChannel <--> b4.ports++;
        b4.ports++ <--> DatarateChannel <--> n2.ports++;
}