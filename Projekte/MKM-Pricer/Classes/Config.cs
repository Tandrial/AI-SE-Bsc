using System;
using System.Drawing;
using System.Linq;
using System.Collections.Generic;
using System.Reflection;
using System.Text.RegularExpressions;
using System.Windows.Forms;

namespace MKM_Pricer {
    public enum LandToNumber { Deutschland = 0, Österreicht = 2, Schweiz = 4, Frankreich = 6, Großbritannien = 8 }

    public static class Config {
        public static List<Edition> CompleteEditionList = new List<Edition>();
        //Legale Editionen der verschiedenen Formate
        public static Edition[] T2 = { new Edition("THS", "Theros", 1457), new Edition("M14", "Magic_2014", 1449), new Edition("DGM", "Dragons_Maze", 1435), new Edition("GTC", "Gatecrash", 1424), new Edition("RTR", "Return_to_Ravnica", 1389) };

        public static Edition[] T1X = { new Edition("THS", "Theros", 1457),new Edition("M14", "Magic_2014", 1449),new Edition("DGM", "Dragons_Maze", 1435),new Edition("GTC", "Gatecrash", 1424), new Edition("RTR", "Return_to_Ravnica", 1389), new Edition("M13", "Magic_2013", 1388),
                           new Edition("AVR", "Avacyn_Restored", 1358), new Edition("DKA", "Dark_Ascension", 1345), new Edition("ISD", "Innistrad", 1327),
                           new Edition("M12", "Magic_2012", 1280), new Edition("NPH", "New_Phyrexia", 1262), new Edition("MBS", "Mirrodin_Besieged", 1253)};

        public static Edition[] Modern = {new Edition("M14", "Magic_2014", 1449),new Edition("DGM", "Dragons_Maze", 1435), new Edition("GTC", "Gatecrash", 1424), new Edition("RTR", "Return_to_Ravnica", 1389), new Edition("M13", "Magic_2013", 1388),
                           new Edition("AVR", "Avacyn_Restored", 1358), new Edition("DKA", "Dark_Ascension", 1345), new Edition("ISD", "Innistrad", 1327),
                           new Edition("M12", "Magic_2012", 1280), new Edition("NPH", "New_Phyrexia", 1262), new Edition("MBS", "Mirrodin_Besieged", 1253),
                           new Edition("M11", "Magic_2011", 1197), new Edition("ROE", "Rise_of_the_Eldrazi", 120), new Edition("WWK", "Worldwake", 118),
                           new Edition("ZEN", "Zendikar", 114), new Edition("M10", "Magic_2010", 109), new Edition("ARB", "Alara_Reborn", 108),
                           new Edition("CFX", "Conflux", 106), new Edition("ALA", "Shards_of_Alara", 102), new Edition("EVE", "Eventide", 99), 
                           new Edition("SHM", "Shadowmoor", 95), new Edition("MT", "Morningtide", 92), new Edition("LW", "Lorwyn", 84), 
                           new Edition("10E", "Tenth_Edition", 74), new Edition("FUT", "Future_Sight", 70), new Edition("PC", "Planar_Chaos", 58), 
                           new Edition("TS", "Time_Spiral", 56), new Edition("CS", "Coldsnap", 52), new Edition("DI", "Dissension", 53), 
                           new Edition("GP", "Guildpact", 54), new Edition("RAV", "Ravnica_City_of_Guilds", 55), new Edition("9E", "Ninth_Edition", 49), 
                           new Edition("SOK", "Saviors_of_Kamigawa", 50), new Edition("BOK", "Betrayers_of_Kamigawa", 51), new Edition("CHK", "Champions_of_Kamigawa", 48), 
                           new Edition("5DN", "Fifth_Dawn", 47), new Edition("DS", "Darksteel", 46), new Edition("MI", "Mirrodin", 45), new Edition("8E", "Eighth_Edition", 44)};

        

        static public List<int> MaxCards = new List<int> { 4, 9, 17, 22, 31, 40, 85, 107, 197, 220, 355, };
        static public float[,] PortoList = new[,] { { 0.85f, 0f, 1.2f, 0f, 0f,  0f, 0f, 0f, 0f, 1.85f, 0f },        //DE_nosec 
                                                    { 0f, 0f, 2.8f, 0f, 0f, 0f, 0f,  0f, 0f, 3.5f, 0f },           //DE_sec
                                                    { 0f, 0f, 2f, 0f, 0f, 0f,  0f, 0f, 0f, 4.5f, 0f },             //AU_nosec
                                                    { 0f, 0f, 4.85f, 0f, 0f,  0f, 0f, 0f, 0f, 7.35f, 0f },         //AU_sec
                                                    { 1.49f, 0f, 2.51f, 0f, 0f, 3.45f, 0f, 6.25f, 0f, 11.13f, 0f  },       //CH_nosec
                                                    { 6.59f, 0f, 7.61f, 0f, 0f, 8.55f, 0f, 11.35f, 0f, 16.23f, 0f },      //CH_sec
                                                    { 1.10f, 0f, 1.75f, 0f, 0f, 2.3f, 0f, 4.6f, 6.60f, 0f, 0f },   //FR_nosec
                                                    { 0f, 0f, 6.05f, 0f, 0f, 6.6f, 0f, 8.9f, 10.90f, 0f , 0f },     //FR_sec
                                                    { 1.39f, 1.90f, 0f, 1.9f, 2.79f, 3.26f, 4.45f, 4.74f, 0f, 6.53f, 8.73f }, //GB_nosec

                                                    { 7.64f, 8.15f, 0f, 8.63f, 9.04f, 9.51f, 10.7f, 10.9f,  0f, 12.78f, 15.03f }};  //GB_sec

        static public float GetPorto(int cardCount, LandToNumber land, bool sec) {
            int h = 0;
            for (int i = 0; i < MaxCards.Count; i++) {
                if (MaxCards[i] < cardCount || PortoList[(int)land + (sec ? 1 : 0), i] == 0f) continue;
                h = i;
                break;
            }
            float porto = PortoList[(int)land + (sec ? 1 : 0), h];
            return porto;
        }

        public static string CardHash = "";
        public static string EditionHash = "";

        internal static string SearchFinished = "Es wurden keine Artikel gefunden die auf deine Suche zutreffen";
        internal static string ShoppingKosten = "Das Deck kostet {0:0.00}€, davon {1:0.00}€ Karten und {2:0.00}€ Porto.";
        internal static string CardBaseError = "Datenbank nicht gefunden/corrupt. Bitte Okay drückem um den Download zubeginnen.";
        internal static string EditionListError = "Editienliste nicht gefunden/corrupt. Bitte Okay drückem um den Download zubeginnen.";

        public static string DataBaseUrl = "https://raw.githubusercontent.com/Tandrial/AI-SE-Bsc/master/Projekte/MKM-Pricer/data/";
        internal static string BaseUrl = "http://www.magickartenmarkt.de/";
        internal static string ShoppingUrl = BaseUrl + "?idRarity=&condition=&idLanguage=&isFoil=0&cardName={0}&mainPage=browseUserProducts&idCategory=1&idUser={1}";
        internal static string SearchUrl = BaseUrl + "index.php?mainPage=browseCategory&idCategory=1&idExpansion={0}&resultsPage={1}";

        internal static Size ProgressBarSize = new Size(550, 30);

        static public bool LibOpen;
        static public bool CustomOpen;
        static public string CustomSets = String.Empty;
        static public int HauptLeft = 210;

        static public Edition FindEdition(string edition) {
            Edition result;
            try {
                result = CompleteEditionList.First(
                s => (((s.longName.IndexOf(edition) != -1) && (s.longName.Length == edition.Length)) ||
                     ((s.shortName.IndexOf(edition) != -1) && (s.shortName.Length == edition.Length))));

            }
            catch (Exception) {
                result = null;
            }

            return result;
        }

        static public Edition FindEditioninString(string text) {
            foreach (var edition in CompleteEditionList) {
                if (text.IndexOf(edition.longName) != -1) {
                    return edition;
                }
            }
            return null;
        }

        static public Edition FindEditionbyNumber(int id) {
            foreach (var edition in CompleteEditionList) {
                if (edition.id == id) {
                    return edition;
                }
            }
            return null;
        }

        internal static void SpeedUpDataGridView(DataGridView dgv, bool setting) {
            dgv.GetType().GetProperty("DoubleBuffered", BindingFlags.NonPublic | BindingFlags.Instance).SetValue(dgv, setting, null);
        }

        public static void RestoreEditionList() {
            CompleteEditionList.Clear();
            CompleteEditionList.Add(new Edition("JOU", "Journey_into_Nyx", 1481));
            CompleteEditionList.Add(new Edition("BNG", "Born_of_the_Gods", 1469));
            CompleteEditionList.Add(new Edition("THS", "Theros", 1457));
            CompleteEditionList.Add(new Edition("M14", "Magic_2014", 1449));
            CompleteEditionList.Add(new Edition("MMA", "Modern_Masters", 1444));
            CompleteEditionList.Add(new Edition("DGM", "Dragons_Maze", 1435));
            CompleteEditionList.Add(new Edition("GTC", "Gatecrash", 1424));
            CompleteEditionList.Add(new Edition("CMA", "Commanders_Arsenal", 1418));
            CompleteEditionList.Add(new Edition("RTR", "Return_to_Ravnica", 1389));
            CompleteEditionList.Add(new Edition("", "Filler_Cards", 1408));
            CompleteEditionList.Add(new Edition("", "Magic_the_Gathering_Products", 1407));
            CompleteEditionList.Add(new Edition("", "Simplified_Chinese_Alternate_Art_Cards", 1401));
            CompleteEditionList.Add(new Edition("DDJ", "Duel_Decks_Izzet_vs_Golgari", 1398));
            CompleteEditionList.Add(new Edition("V12", "From_the_Vault_Realms", 1397));
            CompleteEditionList.Add(new Edition("", "2007_Player_Cards", 1392));
            CompleteEditionList.Add(new Edition("", "2005_Player_Cards", 1391));
            CompleteEditionList.Add(new Edition("M13", "Magic_2013", 1388));
            CompleteEditionList.Add(new Edition("", "Planechase_2012", 1369));
            CompleteEditionList.Add(new Edition("", "Misprints", 1367));
            CompleteEditionList.Add(new Edition("", "Junior_Series_Promos", 1361));
            CompleteEditionList.Add(new Edition("", "Magic_Scholarship_Series_Promos", 1360));
            CompleteEditionList.Add(new Edition("AVR", "Avacyn_Restored", 1358));
            CompleteEditionList.Add(new Edition("DDI", "Duel_Decks_Venser_vs_Koth", 1350));
            CompleteEditionList.Add(new Edition("DKA", "Dark_Ascension", 1345));
            CompleteEditionList.Add(new Edition("", "CardZ_Promos", 1342));
            CompleteEditionList.Add(new Edition("PD3", "Premium_Deck_Series_Graveborn", 1337));
            CompleteEditionList.Add(new Edition("", "Fourth_Edition_Black_Bordered", 1332));
            CompleteEditionList.Add(new Edition("", "JingHe_Age_MtG_10th_Anniversary_Tokens", 1329));
            CompleteEditionList.Add(new Edition("", "JingHe_Age_2002_Tokens", 1328));
            CompleteEditionList.Add(new Edition("ISD", "Innistrad", 1327));
            CompleteEditionList.Add(new Edition("", "WCD_2004_Julien_Nuijten", 1321));
            CompleteEditionList.Add(new Edition("", "WCD_2004_Aeo_Paquette", 1320));
            CompleteEditionList.Add(new Edition("", "WCD_2004_Manuel_Bevand", 1319));
            CompleteEditionList.Add(new Edition("", "WCD_2004_Gabriel_Nassif", 1318));
            CompleteEditionList.Add(new Edition("", "WCD_2003_Daniel_Zink", 1317));
            CompleteEditionList.Add(new Edition("", "WCD_2003_Dave_Humpherys", 1316));
            CompleteEditionList.Add(new Edition("", "WCD_2003_Wolfgang_Eder", 1315));
            CompleteEditionList.Add(new Edition("", "WCD_2003_Peer_Kroger", 1314));
            CompleteEditionList.Add(new Edition("", "WCD_2002_Carlos_Romao", 1313));
            CompleteEditionList.Add(new Edition("", "WCD_2002_Brian_Kibler", 1312));
            CompleteEditionList.Add(new Edition("", "WCD_2002_Raphael_Levy", 1311));
            CompleteEditionList.Add(new Edition("", "WCD_2002_Sim_Han_How", 1310));
            CompleteEditionList.Add(new Edition("", "WCD_2001_Tom_van_de_Logt", 1309));
            CompleteEditionList.Add(new Edition("", "WCD_2001_Alex_Borteh", 1308));
            CompleteEditionList.Add(new Edition("", "WCD_2001_Antoine_Ruel", 1307));
            CompleteEditionList.Add(new Edition("", "WCD_2001_Jan_Tomcani", 1306));
            CompleteEditionList.Add(new Edition("", "WCD_2000_Jon_Finkel", 1305));
            CompleteEditionList.Add(new Edition("", "WCD_2000_Janosch_Kuhn", 1304));
            CompleteEditionList.Add(new Edition("", "WCD_2000_Tom_Van_de_Logt", 1303));
            CompleteEditionList.Add(new Edition("", "WCD_2000_Nicolas_Labarre", 1302));
            CompleteEditionList.Add(new Edition("", "WCD_1999_Kai_Budde", 1301));
            CompleteEditionList.Add(new Edition("", "WCD_1999_Mark_Le_Pine", 1300));
            CompleteEditionList.Add(new Edition("", "WCD_1999_Matt_Linde", 1299));
            CompleteEditionList.Add(new Edition("", "WCD_1999_Jakub_Slemr", 1298));
            CompleteEditionList.Add(new Edition("", "WCD_1998_Brian_Selden", 1297));
            CompleteEditionList.Add(new Edition("", "WCD_1998_Ben_Rubin", 1296));
            CompleteEditionList.Add(new Edition("", "WCD_1998_Brian_Hacker", 1295));
            CompleteEditionList.Add(new Edition("", "WCD_1998_Randy_Buehler", 1294));
            CompleteEditionList.Add(new Edition("", "WCD_1997_Svend_Geertsen", 1293));
            CompleteEditionList.Add(new Edition("", "WCD_1997_Paul_McCabe", 1292));
            CompleteEditionList.Add(new Edition("DDH", "Duel_Decks_Ajani_vs_Nicol_Bolas", 1288));
            CompleteEditionList.Add(new Edition("FVL", "From_the_Vault_Legends", 1286));
            CompleteEditionList.Add(new Edition("", "Your_Move_Games_Tokens", 1281));
            CompleteEditionList.Add(new Edition("M12", "Magic_2012", 1280));
            CompleteEditionList.Add(new Edition("", "Duels_of_the_Planeswalkers_Promos", 1278));
            CompleteEditionList.Add(new Edition("CMD", "Commander", 1273));
            CompleteEditionList.Add(new Edition("", "Blank_Cards", 1270));
            CompleteEditionList.Add(new Edition("", "2006_Player_Cards", 1269));
            CompleteEditionList.Add(new Edition("NPH", "New_Phyrexia", 1262));
            CompleteEditionList.Add(new Edition("DDG", "Duel_Decks_Knights_vs_Dragons", 1261));
            CompleteEditionList.Add(new Edition("", "Salvat_2011", 1259));
            CompleteEditionList.Add(new Edition("MBS", "Mirrodin_Besieged", 1253));
            CompleteEditionList.Add(new Edition("", "Promos", 1249));
            CompleteEditionList.Add(new Edition("", "Dengeki_Maoh_Promos", 1248));
            CompleteEditionList.Add(new Edition("", "Happy_Holidays_Promos", 1247));
            CompleteEditionList.Add(new Edition("", "Premium_Deck_Series_Fire_Lightning", 1218));
            CompleteEditionList.Add(new Edition("", "Release_Promos", 1210));
            CompleteEditionList.Add(new Edition("", "Game_Day_Promos", 1209));
            CompleteEditionList.Add(new Edition("SOM", "Scars_of_Mirrodin", 1206));
            CompleteEditionList.Add(new Edition("DDF", "Duel_Decks_Elspeth_vs_Tezzeret", 1203));
            CompleteEditionList.Add(new Edition("FVR", "From_the_Vault_Relics", 1202));
            CompleteEditionList.Add(new Edition("", "WCD_1997_Janosch_Kuhn", 1200));
            CompleteEditionList.Add(new Edition("", "WCD_1997_Jakub_Slemr", 1199));
            CompleteEditionList.Add(new Edition("", "Buy_a_Box_Promos", 1198));
            CompleteEditionList.Add(new Edition("M11", "Magic_2011", 1197));
            CompleteEditionList.Add(new Edition("ARC", "Archenemy", 1194));
            CompleteEditionList.Add(new Edition("", "Duels_of_the_Planeswalkers_Decks", 1193));
            CompleteEditionList.Add(new Edition("", "TopDeck_Promos", 129));
            CompleteEditionList.Add(new Edition("", "WCD_1996_Shawn_Regnier", 128));
            CompleteEditionList.Add(new Edition("", "WCD_1996_Preston_Poulter", 127));
            CompleteEditionList.Add(new Edition("", "WCD_1996_Michael_Locanto", 126));
            CompleteEditionList.Add(new Edition("", "WCD_1996_Mark_Justice", 125));
            CompleteEditionList.Add(new Edition("", "WCD_1996_Leon_Lindback", 124));
            CompleteEditionList.Add(new Edition("", "WCD_1996_George_Baxter", 123));
            CompleteEditionList.Add(new Edition("", "WCD_1996_Eric_Tam", 122));
            CompleteEditionList.Add(new Edition("", "WCD_1996_Bertrand_Lestree", 121));
            CompleteEditionList.Add(new Edition("ROE", "Rise_of_the_Eldrazi", 120));
            CompleteEditionList.Add(new Edition("PVC", "Duel_Decks_Phyrexia_vs_The_Coalition", 119));
            CompleteEditionList.Add(new Edition("WWK", "Worldwake", 118));
            CompleteEditionList.Add(new Edition("", "Armada_Comics", 117));
            CompleteEditionList.Add(new Edition("PDS", "Premium_Deck_Series_Slivers", 116));
            CompleteEditionList.Add(new Edition("GVL", "Duel_Decks_Garruk_vs_Liliana", 115));
            CompleteEditionList.Add(new Edition("ZEN", "Zendikar", 114));
            CompleteEditionList.Add(new Edition("PCH", "Planechase", 113));
            CompleteEditionList.Add(new Edition("FVE", "From_the_Vault_Exiled", 112));
            CompleteEditionList.Add(new Edition("", "Oversized_Box_Toppers", 111));
            CompleteEditionList.Add(new Edition("", "Oversized_6x9_Promos", 110));
            CompleteEditionList.Add(new Edition("M10", "Magic_2010", 109));
            CompleteEditionList.Add(new Edition("ARB", "Alara_Reborn", 108));
            CompleteEditionList.Add(new Edition("DVD", "Duel_Decks_Divine_vs_Demonic", 107));
            CompleteEditionList.Add(new Edition("CFX", "Conflux", 106));
            CompleteEditionList.Add(new Edition("", "Salvat", 105));
            CompleteEditionList.Add(new Edition("JVC", "Duel_Decks_Jace_vs_Chandra", 104));
            CompleteEditionList.Add(new Edition("ALA", "Shards_of_Alara", 102));
            CompleteEditionList.Add(new Edition("", "Magic_Premiere_Shop_Promos", 101));
            CompleteEditionList.Add(new Edition("FVD", "From_the_Vault_Dragons", 100));
            CompleteEditionList.Add(new Edition("EVE", "Eventide", 99));
            CompleteEditionList.Add(new Edition("", "Wallmart_Promos", 98));
            CompleteEditionList.Add(new Edition("", "APAC_Lands", 97));
            CompleteEditionList.Add(new Edition("", "Rinascimento", 96));
            CompleteEditionList.Add(new Edition("SHM", "Shadowmoor", 95));
            CompleteEditionList.Add(new Edition("", "Champs_States_Promos", 94));
            CompleteEditionList.Add(new Edition("", "The_Duelist_Promos", 93));
            CompleteEditionList.Add(new Edition("MT", "Morningtide", 92));
            CompleteEditionList.Add(new Edition("EVG", "Duel_Decks_Elves_vs_Goblins", 91));
            CompleteEditionList.Add(new Edition("", "World_Championship_Decks", 90));
            CompleteEditionList.Add(new Edition("", "Arena_League_Promos", 89));
            CompleteEditionList.Add(new Edition("", "Guru_Lands", 88));
            CompleteEditionList.Add(new Edition("", "Japan_Junior_Tournament_Promos", 87));
            CompleteEditionList.Add(new Edition("", "Junior_Super_Series_Promos", 86));
            CompleteEditionList.Add(new Edition("ITP", "Introductory_Two_Player_Set", 85));
            CompleteEditionList.Add(new Edition("LW", "Lorwyn", 84));
            CompleteEditionList.Add(new Edition("", "Prerelease_Promos", 83));
            CompleteEditionList.Add(new Edition("CSTD", "Coldsnap_Theme_Decks", 82));
            CompleteEditionList.Add(new Edition("", "Euro_Lands", 81));
            CompleteEditionList.Add(new Edition("", "Judge_Rewards_Promos", 80));
            CompleteEditionList.Add(new Edition("", "Player_Rewards_Promos", 79));
            CompleteEditionList.Add(new Edition("", "Gateway_Promos", 78));
            CompleteEditionList.Add(new Edition("", "International_Edition", 77));
            CompleteEditionList.Add(new Edition("", "Summer_Magic", 76));
            CompleteEditionList.Add(new Edition("AT", "Anthologies", 75));
            CompleteEditionList.Add(new Edition("10E", "Tenth_Edition", 74));
            CompleteEditionList.Add(new Edition("", "Foreign_White_Bordered", 73));
            CompleteEditionList.Add(new Edition("", "Friday_Night_Magic_Promos", 72));
            CompleteEditionList.Add(new Edition("", "Harper_Prism_Promos", 71));
            CompleteEditionList.Add(new Edition("FUT", "Future_Sight", 70));
            CompleteEditionList.Add(new Edition("", "Vanguard", 69));
            CompleteEditionList.Add(new Edition("DM", "Deckmasters", 67));
            CompleteEditionList.Add(new Edition("", "DCI_Promos", 66));
            CompleteEditionList.Add(new Edition("ST2K", "Starter_2000", 65));
            CompleteEditionList.Add(new Edition("", "Battle_Royale", 64));
            CompleteEditionList.Add(new Edition("ST", "Starter_1999", 63));
            CompleteEditionList.Add(new Edition("", "Beatdown", 62));
            CompleteEditionList.Add(new Edition("", "Collectors_Edition", 61));
            CompleteEditionList.Add(new Edition("", "Renaissance", 60));
            CompleteEditionList.Add(new Edition("UH", "Unhinged", 59));
            CompleteEditionList.Add(new Edition("PC", "Planar_Chaos", 58));
            CompleteEditionList.Add(new Edition("", "Foreign_Black_Bordered", 57));
            CompleteEditionList.Add(new Edition("TS", "Time_Spiral", 56));
            CompleteEditionList.Add(new Edition("RAV", "Ravnica_City_of_Guilds", 55));
            CompleteEditionList.Add(new Edition("GP", "Guildpact", 54));
            CompleteEditionList.Add(new Edition("DI", "Dissension", 53));
            CompleteEditionList.Add(new Edition("CS", "Coldsnap", 52));
            CompleteEditionList.Add(new Edition("BOK", "Betrayers_of_Kamigawa", 51));
            CompleteEditionList.Add(new Edition("SOK", "Saviors_of_Kamigawa", 50));
            CompleteEditionList.Add(new Edition("9E", "Ninth_Edition", 49));
            CompleteEditionList.Add(new Edition("CHK", "Champions_of_Kamigawa", 48));
            CompleteEditionList.Add(new Edition("5DN", "Fifth_Dawn", 47));
            CompleteEditionList.Add(new Edition("DS", "Darksteel", 46));
            CompleteEditionList.Add(new Edition("MI", "Mirrodin", 45));
            CompleteEditionList.Add(new Edition("8E", "Eighth_Edition", 44));
            CompleteEditionList.Add(new Edition("SC", "Scourge", 43));
            CompleteEditionList.Add(new Edition("LE", "Legions", 42));
            CompleteEditionList.Add(new Edition("ON", "Onslaught", 41));
            CompleteEditionList.Add(new Edition("JU", "Judgment", 40));
            CompleteEditionList.Add(new Edition("TR", "Torment", 39));
            CompleteEditionList.Add(new Edition("OD", "Odyssey", 38));
            CompleteEditionList.Add(new Edition("7E", "Seventh_Edition", 37));
            CompleteEditionList.Add(new Edition("AP", "Apocalypse", 36));
            CompleteEditionList.Add(new Edition("PS", "Planeshift", 35));
            CompleteEditionList.Add(new Edition("IN", "Invasion", 34));
            CompleteEditionList.Add(new Edition("PR", "Prophecy", 33));
            CompleteEditionList.Add(new Edition("NE", "Nemesis", 32));
            CompleteEditionList.Add(new Edition("MM", "Mercadian_Masques", 31));
            CompleteEditionList.Add(new Edition("P3K", "Portal_Three_Kingdoms", 30));
            CompleteEditionList.Add(new Edition("6E", "Sixth_Edition", 29));
            CompleteEditionList.Add(new Edition("UD", "Urzas_Destiny", 28));
            CompleteEditionList.Add(new Edition("UL", "Urzas_Legacy", 27));
            CompleteEditionList.Add(new Edition("US", "Urzas_Saga", 26));
            CompleteEditionList.Add(new Edition("PO", "Portal", 25));
            CompleteEditionList.Add(new Edition("PO2", "Portal_Second_Age", 24));
            CompleteEditionList.Add(new Edition("5E", "Fifth_Edition", 23));
            CompleteEditionList.Add(new Edition("UG", "Unglued", 22));
            CompleteEditionList.Add(new Edition("EX", "Exodus", 21));
            CompleteEditionList.Add(new Edition("SH", "Stronghold", 20));
            CompleteEditionList.Add(new Edition("TP", "Tempest", 19));
            CompleteEditionList.Add(new Edition("WL", "Weatherlight", 18));
            CompleteEditionList.Add(new Edition("VI", "Visions", 17));
            CompleteEditionList.Add(new Edition("MR", "Mirage", 16));
            CompleteEditionList.Add(new Edition("AI", "Alliances", 15));
            CompleteEditionList.Add(new Edition("HL", "Homelands", 14));
            CompleteEditionList.Add(new Edition("CH", "Chronicles", 12));
            CompleteEditionList.Add(new Edition("IA", "Ice_Age", 11));
            CompleteEditionList.Add(new Edition("4E", "Fourth_Edition", 10));
            CompleteEditionList.Add(new Edition("FE", "Fallen_Empires", 9));
            CompleteEditionList.Add(new Edition("DK", "The_Dark", 8));
            CompleteEditionList.Add(new Edition("LG", "Legends", 7));
            CompleteEditionList.Add(new Edition("R", "Revised", 6));
            CompleteEditionList.Add(new Edition("AQ", "Antiquities", 5));
            CompleteEditionList.Add(new Edition("AN", "Arabian_Nights", 4));
            CompleteEditionList.Add(new Edition("U", "Unlimited", 3));
            CompleteEditionList.Add(new Edition("B", "Beta", 2));
            CompleteEditionList.Add(new Edition("A", "Alpha", 1));
        }
    }
}
