using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Text.RegularExpressions;
using System.Windows.Forms;
using System.Drawing;

namespace MKM_Pricer {
    static class HtmlToOffer {
        static public void CreateOffer(DeckList deckList, ProgressBar pbFortschritt) {
            foreach (Card card in deckList.cards)
                CreateOffer(card, pbFortschritt);
        }

        static public void CreateOffer(Card card, ProgressBar pbFortschritt) {
            card.offers.Clear();
            card.filteredoffers.Clear();

            string cardSource = HtmlToOffer.DownloadURL(Config.BaseUrl + getCardUrl(card));

            SearchOffers(cardSource, card);
            pbFortschritt.PerformStep();
            pbFortschritt.Refresh();
            SetProgressBarText(pbFortschritt);
        }


        static public void SetProgressBarText(ProgressBar Target) {
            Font TextFont = new Font("Arial", (float)8.25, FontStyle.Regular);

            var Text = String.Format("{0}%", (int)(((double)(Target.Value - Target.Minimum) / (double)(Target.Maximum - Target.Minimum)) * 100));

            using (Graphics gr = Target.CreateGraphics()) {
                gr.DrawString(Text, TextFont, new SolidBrush(Color.Black),
                              new PointF(Target.Width / 2 - (gr.MeasureString(Text, TextFont).Width / 2.0F),
                                         Target.Height / 2 - (gr.MeasureString(Text, TextFont).Height / 2.0F)));
            }
        }

        static public string getCardUrl(Card card) {
            string cardName = card.GetCardBaseName();
            string cardEdition = Config.FindEdition(card.Edition.longName.Replace(" ", "_")).longName;

            string key = String.Format("{0}_{1}", cardName, cardEdition.ToLower());

            int index = ~CardBase.CardListSort.BinarySearch(key);
            if (index != CardBase.CardListSort.Count && CardBase.CardListSort[index].ToLower().StartsWith(key))
                return CardBase.CardListSort[index];
            return String.Empty;
        }

        static public void SearchOffers(string htmlCode, Card karte) {
            var offers = new List<string>();
            int id = 0;

            var r = new Regex(@"idInfoUser=.*?Class=.horListItem.><a", RegexOptions.IgnoreCase | RegexOptions.Multiline | RegexOptions.Singleline);
            var m = r.Match(htmlCode);

            while (m.Success) {
                offers.Add(m.ToString());
                m = m.NextMatch();
            }

            foreach (string line in offers) {
                string seller = Regex.Replace(Regex.Match(line, @">.*?<", RegexOptions.Singleline).ToString(), @"(<)|(>)", "").Trim();

                string idHelp = Regex.Match(line, @"[0-9].*?""", RegexOptions.Singleline).ToString();
                int sellerID = Convert.ToInt32(idHelp.Remove(idHelp.Length - 1));

                string location = Regex.Match(line, @"Artikelstandort: .+?'", RegexOptions.Singleline).ToString();
                location = location.Replace("'", "").Remove(0, location.IndexOf(" "));
                location = location.Remove(0, location.IndexOf(" ") + 1);

                var regexEvalAnzahl = new Regex(@"[(][0-9]+[)]", RegexOptions.Singleline);
                Match matchEvalAnzahl = regexEvalAnzahl.Match(line);

                int evelGrade = Convert.ToInt32(Regex.Replace(matchEvalAnzahl.ToString(), @"[(]|[)]", ""));
                matchEvalAnzahl.NextMatch();

                int count = Convert.ToInt32(Regex.Replace(Regex.Match(line, @"[>][0-9]+[<]", RegexOptions.Singleline).ToString(), @"[>]|[<]", ""));

                float price = (float)Convert.ToDecimal(Regex.Replace(Regex.Match(line, @"\d+,\d+ &#x20AC", RegexOptions.Singleline).ToString(), " &#x20AC", ""));

                string quality = Regex.Match(line, @"cardstateicon.*?'[)]").ToString().Trim();
                quality = quality.Remove(0, quality.IndexOf("'") + 1).Replace("')", "");

                string language = Regex.Match(line, @"centered.*?nmouseout").ToString();
                language = language.Remove(0, language.IndexOf("'") + 1).Split('\'')[0];

                bool foil = (Regex.Match(line, @"foil.png", RegexOptions.Singleline).ToString() != "");

                karte.AddAngebot(seller, sellerID, count, foil, price, quality, language, evelGrade, location, id++);
            }
        }

        static public string DownloadURL(string _URL) {
            string pageContent = null;
            try {
                System.Net.HttpWebRequest httpWebRequest = (System.Net.HttpWebRequest)System.Net.HttpWebRequest.Create(_URL);

                // You can also specify additional header values like the user agent or the referer: (Optional)
                httpWebRequest.UserAgent = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)";
                httpWebRequest.Referer = "http://www.google.com/";

                // set timeout for 10 seconds (Optional)
                httpWebRequest.Timeout = 10000;
                // Request response:
                System.Net.WebResponse webResponse = httpWebRequest.GetResponse();
                // Open data stream:
                System.IO.Stream webStream = webResponse.GetResponseStream();
                // Create reader object:
                System.IO.StreamReader streamReader = new System.IO.StreamReader(webStream);

                // Read the entire stream content:
                pageContent = streamReader.ReadToEnd();

                // Cleanup
                streamReader.Close();
                webStream.Close();
                webResponse.Close();
            }
            catch (Exception e) {
                // Error
                MessageBox.Show("Exception caught in process: {0}", e.ToString());
                return null;
            }

            return pageContent;
        }

        static public void LoadFromWeb(string file) {
            var sw = new StreamWriter(file);
            string cards = DownloadURL(Config.DataBaseUrl + file);
            sw.WriteLine(cards);
            sw.Close();
        }
    }
}
