using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Windows.Forms;
using System.Xml;

namespace MKM_Pricer {
    public class DeckList {
        public string Name { get; set; }
        public int CardCount { get; private set; }
        public float TotalCost { get; set; }
        public float TotalPorto { get; set; }

        internal List<Card> cards;
        internal List<BoughtOffer> boughtOffers;
        internal List<SellerList> sellerList;

        internal DeckList() {
            cards = new List<Card>();
            boughtOffers = new List<BoughtOffer>();
            sellerList = new List<SellerList>();
        }

        internal void CompressDeckList() {
            for (int i = 0; i < this.cards.Count; i++) {
                for (int j = this.cards.Count - 1; j >= 0; j--) {
                    if ((this.cards[i].Name != this.cards[j].Name) || (this.cards[i].Edition != this.cards[j].Edition) ||
                        (j == i)) continue;
                    cards[i].Count = this.cards[i].Count + this.cards[j].Count;
                    cards[i].InitialCount = cards[i].Count;
                    cards.RemoveAt(j);
                }
            }
        }

        internal void ImportKorrigieren(List<string> deckListe) {
            for (int i = deckListe.Count - 1; i >= 0; i--) {
                string line = deckListe[i];

                if (line.StartsWith("//") || line == "")
                    deckListe.RemoveAt(i);
                else {
                    line = line.Replace("SB:", "");
                    deckListe[i] = line.Trim();
                }

                if (line.IndexOf("(") >= 0)
                    if (line.IndexOf(" (") < 0) {
                        deckListe[i] = line.Replace("(", " (");
                    }

                if (Card.IsStandardLand(line, false) && (line.IndexOf("(") < 0))
                    deckListe[i] = Card.GetCorrectedLand(line);
            }
            CompressDeckList(ref deckListe);
            KartenErstellen(deckListe);
        }

        private static void CompressDeckList(ref List<string> deckListe) {
            for (int i = 0; i < deckListe.Count; i++) {
                for (int j = deckListe.Count - 1; j >= 0; j--) {
                    string line1 = deckListe[i].Trim();
                    string line2 = deckListe[j].Trim();
                    if (Card.GetCardName(line1, true).ToLower() == (Card.GetCardName(line2, true).ToLower()) && j != i &&
                        (Card.GetEdition(line1) == Card.GetEdition(line2))) {
                        deckListe[i] = String.Format("{0} [{1}] {2}", (Card.GetCardCount(line1) + Card.GetCardCount(line2)),
                                                 Card.GetEdition(line1), Card.GetCardName(line1, true));
                        deckListe.RemoveAt(j);
                    }
                }
            }
        }

        private void KartenErstellen(List<string> deckListe) {
            foreach (string line in deckListe) {
                this.AddCard(Card.GetCardName(line, true), Card.GetCardCount(line), Card.GetEdition(line));
            }
        }

        internal void AddCard(string cardName, int cardCount, Edition edition) {
            var karte = new Card(cardName, cardCount, edition);
            cards.Add(karte);
        }

        internal void RemoveCard(string cardName) {
            cards.RemoveAll(s => s.Name == cardName);
        }

        internal void CalculateCardCount() {
            CardCount = cards.Sum(karte => karte.Count);
        }

        internal void DeckListOutput(DataGridView dgvDecklist) {
            CalculateCardCount();
            if (CardCount > 0) {
                dgvDecklist.DataSource = null;
                dgvDecklist.DataSource = this.cards;
                dgvDecklist.CurrentCell = dgvDecklist.Rows[0].Cells[0];
                dgvDecklist.Update();
            }
        }

        internal void ChangeAmount(string cardName, int Amount) {
            foreach (Card karte in cards.Where(karte => karte.Name == cardName)) {
                karte.Count += Amount;
                if (karte.Count < 0)
                    karte.Count = karte.InitialCount;
                else if (karte.Count > karte.InitialCount)
                    karte.InitialCount++;
            }
        }

        public void LoadFromXmlFile(string fileName) {
            using (XmlReader reader = XmlReader.Create(fileName)) {
                string id = "";
                int count = 0;
                string set = "";
                while (reader.Read()) {
                    switch (reader.Name) {
                        case "item":
                            id = reader["id"];
                            break;

                        case "card":
                            count = Convert.ToInt32(reader["count"]);
                            set = reader["set"];
                            break;
                    }

                    if (id != "" && count != 0 && set != "") {
                        if (Card.IsStandardLand(id, true)) id = Card.GetCorrectedLand(id);
                        this.AddCard(id, count, Config.FindEdition(set));
                        id = "";
                        count = 0;
                        set = "";
                    }
                }
            }
        }

        public void LoadFromMWSFile(string fileName) {
            var streamReader = new StreamReader(fileName);
            string data = streamReader.ReadToEnd();
            streamReader.Close();

            if (!string.IsNullOrEmpty(data)) {
                var importKarten = new List<string>();
                importKarten.AddRange(data.Split(new string[] { "\r\n" }, StringSplitOptions.RemoveEmptyEntries));
                ImportKorrigieren(importKarten);
                importKarten.Clear();
            }
        }

        public void FixCardEditions() {
            foreach (Card card in cards) {
                bool isValid = false;
                foreach (Edition line in Config.CompleteEditionList) {
                    if (card.Edition != null)
                        if (line.id == card.Edition.id)
                            isValid = true;
                }
                if (!isValid) {
                    string name = card.GetCardBaseName();
                    card.Edition = CardBase.FindlatestEdition(name, true);
                }
            }
        }
    }
}
