using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Text.RegularExpressions;

namespace MKM_Pricer {
    class Card {
        public string Name { get; set; }

        public Edition Edition { get; set; }

        public string EditionShort { get { return Edition.shortName; } }

        public int Count { get; set; }
        public int InitialCount { get; set; }

        public List<Edition> AllEditions;
        public List<Offer> offers = new List<Offer>();
        public List<Offer> filteredoffers = new List<Offer>();

        public ToolStripItem[] editions;

        internal Card(string cardName, int cardCount, Edition edition) {
            this.Name = System.Globalization.CultureInfo.CurrentCulture.TextInfo.ToTitleCase(cardName.Trim());
            this.Count = cardCount;
            this.Edition = edition;
            this.InitialCount = cardCount;

            AllEditions = new List<Edition>();
            FindAllEditions(CardBase.CardListSort);
        }


        internal static int GetCardCount(string line) {
            string help = line.Remove(line.IndexOf(" "));
            help = Regex.Replace(help, @"\D", "");
            return Convert.ToInt16(help);
        }

        internal static string GetCardName(string line, bool ForDisplay) {
            if (line.Length == 0) return null;
            if (line.IndexOf(" ") > 0) line.Remove(0, line.IndexOf(" ")).Trim();
            var karte = new Card(line);

            karte.Name = Regex.Replace(Regex.Replace(karte.Name, @"\d", ""), @"[[].*[]]", "").Trim().Replace("(", "").Replace(")", "");

            string cardName = ForDisplay ? karte.Name : karte.GetCardBaseName();

            if (IsStandardLand(line, true))
                cardName = GetCorrectedLand(cardName);

            if (karte.Name.StartsWith("xx"))
                return cardName.Replace("xx", "x");
            return cardName.StartsWith("x")
                       ? cardName.Remove(0, 1)
                       : cardName;
        }

        internal static Edition GetEdition(string line) {
            string ed = Regex.Match(line, @"[[].*[]]").ToString().Replace("[", "").Replace("]", "");
            Edition edition;
            if (ed.Length != 0)
                edition = Config.FindEdition(ed);
            else
                edition = CardBase.FindlatestEdition(line, false);
            return edition;
        }

        internal static bool IsStandardLand(string line, bool fromFile) {
            string name = (fromFile ? line.ToLower() : Regex.Split(line.Trim(), " ")[1]).ToLower();

            return ((name == "mountain") || (name == "plains") || (name == "swamp") || (name == "island") ||
                    (name == "forest"));
        }

        internal static string GetCorrectedLand(string landName) {
            return landName + " (1)";
        }


        internal Card(string cardName) {
            this.Name = System.Globalization.CultureInfo.CurrentCulture.TextInfo.ToTitleCase(cardName.Trim());
        }

        internal void AddAngebot(string seller, int sellerID, int count, bool foil, float price, string quality, string language, int evalGrade, string location, int id) {
            var offer = new Offer(seller, sellerID, count, foil, price, quality, language, evalGrade, location, id);
            offers.Add(offer);
        }

        internal string GetCardBaseName() {
            return Name.Trim().ToLower().Replace("'", "").Replace(" ", "_").Replace(",", "").Replace("-", "_").Replace(")", "").Replace("(", "");
        }

        private void FindAllEditions(List<string> cardBase) {
            string name = GetCardBaseName();
            foreach (string s in cardBase) {
                if (s.ToLower().StartsWith(name)) {
                    Edition edition = Config.FindEditioninString(s.Substring(1));

                    AllEditions.Add(edition);
                }
            }
            CreateMenuItems();
        }
        private void CreateMenuItems() {
            int count = 0;
            bool check = false;
            editions = new ToolStripItem[AllEditions.Count];

            if (this.Edition == null) {
                this.Edition = CardBase.FindlatestEdition(this.Name, true);
            }

            foreach (Edition edition in this.AllEditions) {
                ToolStripMenuItem cMenuEdition = new ToolStripMenuItem();
                cMenuEdition.Name = "cMenuEdition" + count;

                cMenuEdition.Text = edition.longName.Replace("_", " ");
                if (edition.id == this.Edition.id) {
                    cMenuEdition.Checked = true;
                    check = true;
                }
                cMenuEdition.Click += new System.EventHandler(cMenuEditionChange_Click);
                editions[count++] = cMenuEdition;
            }
            if (!check) cMenuEditionChange_Click(editions[0], EventArgs.Empty);
        }

        private void cMenuEditionChange_Click(object sender, EventArgs e) {
            foreach (ToolStripMenuItem menuItem in editions)
                menuItem.Checked = false;

            Edition newEdition = Config.FindEdition(((ToolStripMenuItem)sender).Text.Replace(" ", "_"));
            ((ToolStripMenuItem)sender).Checked = true;

            this.Edition = newEdition;
            this.offers.Clear();
            this.filteredoffers.Clear();
        }
    }
}
