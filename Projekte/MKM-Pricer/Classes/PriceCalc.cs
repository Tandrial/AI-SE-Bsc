using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Windows.Forms;
using Facet.Combinatorics;

namespace MKM_Pricer {
    static class PriceCalc {
        internal static void PriceCalculation(DeckList deck) {
            FindOneSellerForEachCard(deck);
            ReduceSellerCount(deck);
            SplitCardOffers(deck);
            CreateSellerList(deck);
        }

        private static void FindOneSellerForEachCard(DeckList deck) {
            foreach (Card card in deck.cards) {
                bool added = false;
                foreach (Offer offer in card.filteredoffers) {
                    if (offer.Count >= card.Count) {
                        deck.boughtOffers.Add(new BoughtOffer(offer.Seller, offer.SellerId, offer.Location, card,
                                                              card.Count,
                                                              offer.Price));
                        added = true;
                        break;
                    }
                }
                if (!added) deck.boughtOffers.Add(new BoughtOffer("BOGUS", 00000, "Deutschland", card, card.Count, 9998f));
            }
        }

        private static void ReduceSellerCount(DeckList deck) {
            for (int i = 0; i < deck.boughtOffers.Count; i++)
                for (int j = 0; j < deck.boughtOffers.Count; j++)
                    if (i != j && deck.boughtOffers[i].Seller != deck.boughtOffers[j].Seller)
                        foreach (Offer offer in deck.boughtOffers[i].Karte.filteredoffers) {
                            if (offer.Seller == deck.boughtOffers[j].Seller &&
                                offer.Count >= deck.boughtOffers[i].Count &&
                                offer.Price < deck.boughtOffers[i].Price + Config.GetPorto(deck.boughtOffers[i].Count,
                                                                                           GetLandNumber(deck.boughtOffers[i].Location),
                                                                                           false)
                                )

                                deck.boughtOffers[i] = new BoughtOffer(offer.Seller, offer.SellerId, offer.Location,
                                                                       deck.boughtOffers[i].Karte,
                                                                       deck.boughtOffers[i].Karte.Count, offer.Price);
                        }
        }

        private static void CreateSellerList(DeckList deck) {
            foreach (BoughtOffer t in deck.boughtOffers) {
                bool added = false;
                foreach (SellerList t1 in deck.sellerList.Where(t1 => t1.Seller == t.Seller)) {
                    t1.AddOffer(t);
                    added = true;
                    break;
                }
                if (!added)
                    deck.sellerList.Add(new SellerList(t.Seller, t.SellerId.ToString(), t));
            }
        }

        private static void SplitCardOffers(DeckList deck) {
            for (int j = deck.boughtOffers.Count - 1; j >= 0; j--) {
                float price1 = deck.boughtOffers[j].Karte.filteredoffers[0].Price +
                               Config.GetPorto(deck.boughtOffers[j].Count,
                                               GetLandNumber(deck.boughtOffers[j].Location),
                                               false);
                float price2 = deck.boughtOffers[j].Price;
                if (deck.boughtOffers[j].Karte.filteredoffers[0].Price + Config.GetPorto(deck.boughtOffers[j].Count,
                                                                                           GetLandNumber(deck.boughtOffers[j].Location),
                                                                                           false) < deck.boughtOffers[j].Price) {
                    string lowestercombi = string.Empty;
                    float lowestprice = 9999f;
                    for (int i = 1; i < deck.boughtOffers[j].Karte.Count + 1; i++) {
                        string combi = SplitIntoN(deck.boughtOffers[j].Karte, i);
                        float price = Convert.ToSingle(combi.Substring(combi.IndexOf(";") + 1));

                        if (price < lowestprice) {
                            lowestprice = price;
                            lowestercombi = combi.Remove(combi.IndexOf(";"));
                        }
                    }
                    BoughtOffer helper = deck.boughtOffers[j];

                    deck.boughtOffers.RemoveAt(j);

                    lowestercombi = lowestercombi.Remove(lowestercombi.Length - 1);
                    string[] combination = lowestercombi.Split(',');
                    int cardslefttobuy = helper.Karte.Count;
                    foreach (string word in combination) {
                        int index = Convert.ToInt16(word);
                        if (cardslefttobuy < helper.Karte.filteredoffers[Convert.ToInt16(word)].Count) {
                            deck.boughtOffers.Add(new BoughtOffer(helper.Karte.filteredoffers[index].Seller,
                                                                  helper.Karte.filteredoffers[index].SellerId,
                                                                  helper.Karte.filteredoffers[index].Location,
                                                                  helper.Karte,
                                                                  cardslefttobuy,
                                                                  helper.Karte.filteredoffers[index].Price));
                        } else {
                            deck.boughtOffers.Add(new BoughtOffer(helper.Karte.filteredoffers[index].Seller,
                                                                  helper.Karte.filteredoffers[index].SellerId,
                                                                  helper.Karte.filteredoffers[index].Location,
                                                                  helper.Karte,
                                                                  helper.Karte.filteredoffers[index].Count,
                                                                  helper.Karte.filteredoffers[index].Price));
                            cardslefttobuy -= helper.Karte.filteredoffers[index].Count;
                        }
                    }
                }
            }
        }

        private static string SplitIntoN(Card card, int n) {
            var lowestCombi = new int[n];
            float lowestPrice = 9999f;
            var result = String.Empty;

            var inputSet = new int[card.filteredoffers.Count];
            for (var i = 0; i < card.filteredoffers.Count; i++)
                inputSet[i] = i;

            var combinations = new Combinations<int>(inputSet, n);

            foreach (IList<int> c in combinations) {
                float price = 0f;

                int anzahl = c.Sum(t => card.filteredoffers[t].Count);

                int counthelp = card.Count;
                int count = 0;

                while (counthelp > 0) {
                    int help = card.filteredoffers[c[count]].Count;
                    if (help <= counthelp) {
                        counthelp -= help;
                        price += help * card.filteredoffers[c[count]].Price;
                    } else {
                        price += counthelp * card.filteredoffers[c[count]].Price;
                        break;
                    }
                    count++;
                    if (count >= c.Count) break;
                }

                if (anzahl >= card.Count && price < lowestPrice) {
                    for (int i = 0; i < c.Count; i++) lowestCombi[i] = c[i];
                    lowestPrice = price;
                }

                if (lowestPrice < 9999f) break;
            }

            for (int i = 0; i < n; i++) result += lowestCombi[i].ToString() + ",";
            result.Trim();

            var h = Regex.Split(result, ",");
            if (h[h.Length - 1] == String.Empty) h[h.Length - 1] = null;
            var porto = h.Sum(s => Config.GetPorto(card.filteredoffers[Convert.ToInt16(s)].Count,
                                                   GetLandNumber(card.filteredoffers[Convert.ToInt16(s)].Location),
                                                   (card.filteredoffers[Convert.ToInt16(s)].Count * card.filteredoffers[Convert.ToInt16(s)].Price > 25) ? true : false));

            return String.Format("{0};{1}", result, (lowestPrice + porto).ToString());
        }


        static public LandToNumber GetLandNumber(string land) {
            return (LandToNumber)Enum.Parse(typeof(LandToNumber), land, true);
        }

        internal static void PrintShoppingList(DeckList deck, List<BoughtOffer> auswahl, ListBox listbox) {
            float totalPrice = 0f;
            float totalPorto = 0f;

            listbox.Items.Add("");
            foreach (SellerList seller in deck.sellerList) {
                listbox.Items.Add("//" + seller.Seller);
                float priceperBuyer = 0f;
                foreach (BoughtOffer t in seller.boughtOffers) {
                    float pricePerCardSet = 0f;
                    listbox.Items.Add(String.Format("{0} {1} from {2} each costs {3}",
                                                    t.Count,
                                                    t.Karte.Name.Replace(" ", "+"),
                                                    t.Seller,
                                                    t.Price));
                    pricePerCardSet += t.Count * t.Price;
                    priceperBuyer += pricePerCardSet;
                }
                totalPrice += priceperBuyer;
                var buyerLocation = GetLandNumber(seller.boughtOffers[0].Location);
                var count = seller.boughtOffers.Sum(s => s.Count);
                var portoPerSeller = Config.GetPorto(count, buyerLocation, (priceperBuyer > 25) ? true : false);

                totalPorto += portoPerSeller;
                listbox.Items.Add(String.Format("Porto: {0:0.00} €", portoPerSeller));
            }
            deck.TotalCost = totalPrice;
            deck.TotalPorto = totalPorto;
            listbox.Items[0] = String.Format(Config.ShoppingKosten,
                                             totalPrice + totalPorto,
                                             totalPrice,
                                             totalPorto);
        }
    }

}