using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MKM_Pricer {
    class BoughtOffer {
        public string Seller { get; private set; }
        public int SellerId { get; private set; }
        public string Location { get; private set; }
        public Card Karte { get; private set; }
        public int Count { get; private set; }
        public float Price { get; private set; }

        internal BoughtOffer(string seller, int sellerID, string location,
            Card card, int count, float price) {
            this.Seller = seller;
            this.SellerId = sellerID;
            this.Location = location;
            this.Karte = card;
            this.Count = count;
            this.Price = price;
        }
    }
}
