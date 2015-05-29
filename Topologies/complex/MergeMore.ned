network MergeMore {   
        n0.ports++ <--> DatarateChannel <--> b0.ports++;
        b0.ports++ <--> DatarateChannel <--> b1.ports++;
        b1.ports++ <--> DatarateChannel <--> b2.ports++;
        n1.ports++ <--> DatarateChannel <--> b0.ports++;
        b2.ports++ <--> DatarateChannel <--> n4.ports++;
        n2.ports++ <--> DatarateChannel <--> b0.ports++;
        n3.ports++ <--> DatarateChannel <--> b0.ports++;
}