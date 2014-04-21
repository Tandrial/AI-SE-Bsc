using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net;
using System.IO;
using System.Text.RegularExpressions;
using System.Windows.Forms;

namespace MKM_Pricer {
    public static class CardBase {
        static public List<String> CardList = new List<string>();
        static public List<String> CardListSort = new List<string>();
        static public DeckList Deck = new DeckList();

        static public bool CardIstValid(string line) {
            if (line.IndexOf("(") >= 0 && line.IndexOf(" (") < 0) line = line.Replace("(", " (");

            string key;
            if (line.IndexOf(" ") == -1) return false;
            string line1 = Regex.Split(line.Trim(), " ")[0];

            line1 = Regex.Replace(line1, @"\D", "");
            try { Int32.Parse(line1); } catch { return false; }

            string edition = Card.GetEdition(line).longName;
            if (edition == String.Empty) return false;

            string name = Card.GetCardName(line, false);

            if (Card.IsStandardLand(line, false))
                if (line.IndexOf("(") >= 0)
                    key = name.Replace("(", "").Replace(")", "") + edition.ToLower();
                else
                    key = String.Format("{0}_({1})", Card.GetCorrectedLand(name).Replace(" ", "_").Replace("(", "").Replace(")", ""), edition.ToLower());
            else
                key = String.Format("{0}_({1})", name, edition.ToLower());

            int index = ~CardListSort.BinarySearch(key);

            return index != CardListSort.Count && CardListSort[index].ToLower().StartsWith(key);
        }

        static public Edition FindlatestEdition(string line, bool fromFile) {
            string cardName = fromFile ? line : Card.GetCardName(line, false);
            Edition edition = new Edition();

            for (int i = 0; i < CardBase.CardList.Count; i++) {
                if (CardList[i].ToLower().StartsWith(cardName))
                    if (i >= 0) {
                        edition = Config.FindEdition(Regex.Match(CardList[i], "[(].+?[)]").ToString().Replace("(", "").Replace(")", ""));
                        break;
                    }
            }
            return edition;
        }

        static public void LoadFromTxt() {
            CardList.Clear();
            CardListSort.Clear();
            string line;
            var sr = new StreamReader("cardbase.txt");
            while ((line = sr.ReadLine()) != null) {
                if (line.Length > 0) {
                    CardList.Add(line);
                    CardListSort.Add(line);
                }
            }
            CardListSort.Sort();
        }

        static public void AddToListBox(ListBox listbox) {
            listbox.Items.Clear();
            foreach (string current in CardBase.CardListSort) {
                Edition edition = Config.FindEditioninString(current.Substring(1));
                string name = current.Substring(0, current.IndexOf(edition.longName, 1)).Replace("_", " ");

                if (edition.shortName.Length == 0)
                    listbox.Items.Add(string.Format("{0} - {1}", name, edition.longName));
                else {
                    listbox.Items.Add(string.Format("{0} [{1}]", name, edition.shortName));
                }
            }
        }

        static public void AddCustomToListBox(ListBox listbox, Edition[] editions) {
            listbox.Items.Clear();

            foreach (string current in CardBase.CardListSort) {
                Edition edition = Config.FindEditioninString(current.Substring(1));
                bool found = false;

                for (int i = 0; i < editions.Length; i++) {
                    if (editions[i].id == edition.id) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    string name = current.Substring(0, current.IndexOf(edition.longName, 1)).Replace("_", " ");

                    if (edition.shortName.Length == 0)
                        listbox.Items.Add(string.Format("{0} [{1}]", name, edition.longName));
                    else {
                        listbox.Items.Add(string.Format("{0} [{1}]", name, edition.shortName));
                    }
                }
            }
        }
    }
}
