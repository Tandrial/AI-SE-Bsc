using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MKM_Pricer {
    class SellerList {
        public string Seller { get; private set; }
        public string SellerId { get; private set; }

        public List<BoughtOffer> boughtOffers;

        internal SellerList(string seller, string sellerId, BoughtOffer boughtOffer) {
            this.Seller = seller;
            this.SellerId = sellerId;
            boughtOffers = new List<BoughtOffer> { boughtOffer };
        }

        internal void AddOffer(BoughtOffer boughtOffer) {
            boughtOffers.Add(boughtOffer);
        }
    }
}
