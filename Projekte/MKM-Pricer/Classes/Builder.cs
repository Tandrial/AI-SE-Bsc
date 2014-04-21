using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Security.Cryptography;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using System.Windows.Forms;
using System.Xml.Serialization;

namespace MKM_Pricer {
    public static class Builder {
        /// <summary>
        /// Creates a new CardBase form MKM
        /// </summary>
        static public void BuildFromMKM(List<Edition> editionList) {
            var list = new List<string>();

            foreach (Edition line in editionList) {
                if (!line.longName.StartsWith("//")) {
                    Console.Write(line.longName + "..");
                    list.AddRange(GetCardsByEdition(line));
                } else {
                    list.Add(line.longName);
                }
                Console.WriteLine();
            }
            SaveToTxt(list, "cardbase.txt");
        }

        static public List<string> GetCardsByEdition(Edition edition) {
            List<string> result = new List<string>();
            int pageNo = 0;
            bool done = false;

            while (!done && pageNo <= 12) {
                //Thread.Sleep(1000);
                string URL = String.Format(Config.SearchUrl, edition.id, pageNo++);
               // result.Add(URL);
                string source = HtmlToOffer.DownloadURL(URL);

                if (!Regex.IsMatch(source, Config.SearchFinished))
                    ProcessSource(source, ref result);
                else
                    done = true;
                Console.Write(pageNo + "..");
            }
            return result;
        }

        static public List<Edition> BuildEditionList() {
            List<Edition> result = new List<Edition>();
            List<Edition> result1 = new List<Edition>();
            List<Edition> result2 = new List<Edition>();

            List<string> MKM = new List<string>();
            var r = new Regex(@"<option\b[^>]*>(.*?)</option>");
            string htmlCode = HtmlToOffer.DownloadURL("http://www.magickartenmarkt.de/?mainPage=browseCategory&idCategory=1");
            var m = r.Match(htmlCode);

            while (m.Success) {
                MKM.Add(m.ToString());
                m = m.NextMatch();
                if (m.ToString().IndexOf("Alle") != -1) break;
            }
            MKM.RemoveAt(0);

            List<string> INFO = new List<string>();
            r = new Regex(@"<li><a href=""/.*?/en.html"">.*?</a>"); // braucht source von http://magiccards.info/sitemap.htm
            //ergebnis: <li><a href="/dka/en.html">Dark Ascension</a>*/
            htmlCode = HtmlToOffer.DownloadURL("http://magiccards.info/sitemap.html");
            m = r.Match(htmlCode);
            while (m.Success) {
                INFO.Add(m.ToString());
                m = m.NextMatch();
            }

            foreach (string s in MKM) {
                int id = Int32.Parse(Regex.Match(s, @""".*?""").ToString().Replace(@"""", ""));
                string name = Regex.Match(s, @">.*?<").ToString().Replace(">", "").Replace("<", "");
                result1.Add(new Edition("", name, id));
            }

            result1.Sort((firstObj, secondObj) => firstObj.id.CompareTo(secondObj.id));
            result1.Reverse();

            foreach (string s in INFO) {
                string shortname = Regex.Match(s, @"/.*?/").ToString().Replace("/", "").Replace("/", "").ToUpper();
                string longname = Regex.Match(s, @""">.*?<").ToString().Replace(@""">", "").Replace("<", "");
                result2.Add(new Edition(shortname, longname, 0));
            }

            foreach (Edition edition in result1) {
                foreach (Edition edition1 in result2) {
                    if (edition.longName == edition1.longName)
                        result.Add(new Edition(edition1.shortName, edition1.longName, edition.id));
                }
            }

            Edition.SaveEditionsToXml(result);
            MessageBox.Show(result.Count.ToString());
            return result;
        }

        static public void SaveToTxt(List<String> list, string filename) {
            var sr = new StreamWriter(filename, false, Encoding.Default);
            foreach (string t in list)
                sr.WriteLine(t);
            sr.Close();
        }

        static public void ProcessSource(string source, ref List<String> list) {
            var r = new Regex(@"href=.+?.prod");
            var m = r.Match(source);
            m = m.NextMatch();
            m = m.NextMatch();

            while (m.Success) {
                string help = m.ToString().Remove(0, 6);
                if (!list.Contains(help))
                    list.Add(help);
                m = m.NextMatch();
                m = m.NextMatch();
            }
        }

        static public string getSHA1HashFromFile(string fileName) {
            using (FileStream fs = new FileStream(fileName, FileMode.Open))
            using (BufferedStream bs = new BufferedStream(fs)) {
                using (SHA1Managed sha1 = new SHA1Managed()) {
                    byte[] hash = sha1.ComputeHash(bs);
                    StringBuilder formatted = new StringBuilder(2 * hash.Length);
                    foreach (byte b in hash) {
                        formatted.AppendFormat("{0:X2}", b);
                    }
                    return formatted.ToString();
                }
            }
        }
    }
}
