network Linear {   
        n0.ports++ <--> DatarateChannel <--> b0.ports++;
        b0.ports++ <--> DatarateChannel <--> b1.ports++;
        b1.ports++ <--> DatarateChannel <--> b2.ports++;
        b2.ports++ <--> DatarateChannel <--> n1.ports++;
}