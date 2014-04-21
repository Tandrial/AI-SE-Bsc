using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MKM_Pricer {
    class Offer {
        public string Seller { get; private set; }
        public int SellerId { get; private set; }
        public string Location { get; private set; }
        public int EvalGrade { get; private set; }
        public string Quality { get; private set; }
        public string Language { get; private set; }
        public bool Foil { get; private set; }
        public int Count { get; private set; }
        public float Price { get; private set; }
        public int Id { get; private set; }

        internal Offer(string seller, int sellerID, int count, bool foil, float price,
            string quality, string language, int evalGrade, string location, int id) {
            this.Seller = seller;
            this.SellerId = sellerID;
            this.Count = count;
            this.Foil = foil;
            this.Price = price;
            this.Quality = quality;
            this.Language = language;
            this.EvalGrade = evalGrade;
            this.Location = location;
            this.Id = id;
        }
    }
}
