network Chain {    
        n0.ports++ <--> DatarateChannel <--> b0.ports++;
        n1.ports++ <--> DatarateChannel <--> b0.ports++;
        n2.ports++ <--> DatarateChannel <--> b1.ports++;
        b0.ports++ <--> DatarateChannel <--> b1.ports++;
        n3.ports++ <--> DatarateChannel <--> b2.ports++;
        b1.ports++ <--> DatarateChannel <--> b2.ports++;
        n4.ports++ <--> DatarateChannel <--> b3.ports++;
        n5.ports++ <--> DatarateChannel <--> b4.ports++;
        b3.ports++ <--> DatarateChannel <--> b4.ports++;
        b2.ports++ <--> DatarateChannel <--> b3.ports++;
        b4.ports++ <--> DatarateChannel <--> n6.ports++;
}