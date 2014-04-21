using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.IO;
using System.Text.RegularExpressions;
using System.Windows.Forms;
using System.Data;
using System.Drawing;

namespace MKM_Pricer {
    public partial class frmHaupt : Form {
        internal DeckList Deck = new DeckList();
        internal CardLib CardLibrary;
        internal frmDeckImport DeckImport;
        internal frmAddEdition AddEdition;

        internal string FilterString = String.Empty;

        public frmHaupt() {
            InitializeComponent();
            CardLibrary = new CardLib(this);
        }

        private void Haupt_Load(object sender, EventArgs e) {

            if (!File.Exists("editions.xml")) { //|| Builder.getSHA1HashFromFile("editions.xml") != Config.CardHash) {
                MessageBox.Show(Config.EditionListError);
                HtmlToOffer.LoadFromWeb("editions.xml");
            }

            Config.CompleteEditionList.Clear();
            Config.CompleteEditionList.AddRange(Edition.LoadEditionsFromXml());


            if (!File.Exists("cardbase.txt")) { // || Builder.getSHA1HashFromFile("cardbase.txt") != Config.CardHash) {
                MessageBox.Show(Config.CardBaseError);
                HtmlToOffer.LoadFromWeb("cardbase.txt");
            }

            CardBase.LoadFromTxt();
            NeuesDeck();

            Config.SpeedUpDataGridView(dgvDeckListe, true);
            Config.SpeedUpDataGridView(dgvAngebote, true);
        }

        private void Haupt_Move(object sender, EventArgs e) {
            if (!Config.LibOpen) {
                CardLibrary.Left = this.Left - CardLibrary.Width - 5;
                CardLibrary.Top = this.Top;
            }
        }

        private void Haupt_Activated(object sender, EventArgs e) {
            CardLibrary.Focus();
            this.Focus();
        }

        private void Haupt_FormClosed(object sender, FormClosedEventArgs e) {
            CardLibrary.Close();
        }

        #region CleanUp

        internal void NeuesDeck() {
            dgvDeckListe.DataSource = "";
            dgvDeckListe.ContextMenuStrip = cMenuDeckList;
            dgvDeckListe.Update();

            dgvAngebote.DataSource = "";
            dgvAngebote.ContextMenuStrip = cMenuWork;
            dgvAngebote.Update();

            Deck.cards.Clear();
        }

        internal void UpdateCardCounter(Label label) {
            Deck.CalculateCardCount();
            label.Text = String.Format("{0} Karten", Deck.CardCount);
            label.Show();
        }

        #endregion

        #region Keyboard

        private void dgvDecktListe_KeyUp(object sender, KeyEventArgs e) {
            if ((dgvDeckListe.Columns.GetColumnCount(DataGridViewElementStates.Selected) != -1) && (Deck.cards.Count > 0)) {
                int index = dgvDeckListe.CurrentCellAddress.Y;
                string name = Deck.cards[index].Name;

                foreach (DataGridViewCell c in dgvDeckListe.SelectedCells) c.Selected = false;

                if (e.KeyCode == Keys.Delete || e.KeyCode == Keys.Add || e.KeyCode == Keys.Subtract) {
                    if (e.KeyCode == Keys.Delete) {
                        dgvDeckListe.DataSource = "";
                        Deck.RemoveCard(name);
                        dgvDeckListe.DataSource = Deck.cards;

                        if (dgvDeckListe.Rows.Count < index) {
                            dgvDeckListe.Rows[dgvDeckListe.Columns.Count].Selected = true;
                            dgvDeckListe.CurrentCell = dgvDeckListe.Rows[dgvDeckListe.Columns.Count].Cells[0];
                        }
                        if (dgvDeckListe.Rows.Count >= index) {
                            if (index == 0) index++;

                            if (dgvDeckListe.RowCount != 0) {
                                dgvDeckListe.Rows[index - 1].Selected = true;
                                dgvDeckListe.CurrentCell = dgvDeckListe.Rows[index - 1].Cells[0];
                            }
                        }
                    }

                    if (e.KeyCode == Keys.Add) Deck.ChangeAmount(name, 1);
                    if (e.KeyCode == Keys.Subtract) Deck.ChangeAmount(name, -1);

                    dgvDeckListe.Refresh();
                }
            }
            UpdateCardCounter(lbCardDisplay);
        }

        private void dgvDeckListe_SelectionChanged(object sender, EventArgs e) {
            if (dgvDeckListe.Columns.GetColumnCount(DataGridViewElementStates.Selected) != -1) {
                int index = dgvDeckListe.CurrentCellAddress.Y;

                dgvAngebote.DataSource = "";
                dgvAngebote.Refresh();
                dgvAngebote.DataSource = Deck.cards[index].offers;

                SetFilter(FilterChange(sender, e));

                dgvAngebote.Refresh();
            }
        }
        #endregion

        #region Filter

        private string FilterChange(object sender, EventArgs e) {
            var filterQuality = "(quality = '";
            var filterLanguage = "(language = '";
            var filterFoil = string.Empty;
            var filterGermany = string.Empty;
            var filterEnough = string.Empty;

            foreach (ToolStripMenuItem item in cMenuZustand.DropDownItems)
                if (item.Checked)
                    filterQuality += item.Text + "_";

            if (filterQuality.Length >= 15) {
                filterQuality = filterQuality.Replace("_", "' OR quality = '");
                filterQuality = filterQuality.Remove(filterQuality.LastIndexOf("OR quality = '")) + ") AND ";
            }
            else
                filterQuality = String.Empty;

            foreach (ToolStripMenuItem item in cMenuSprachen.DropDownItems)
                if (item.Checked)
                    filterLanguage += item.Text + "_";

            if (filterLanguage.Length >= 15) {
                filterLanguage = filterLanguage.Replace("_", "' OR language = '");
                filterLanguage = filterLanguage.Remove(filterLanguage.LastIndexOf("OR language = '")) + ") AND ";
            }
            else
                filterLanguage = String.Empty;

            if (cMenuNurFoilKarten.Checked) filterFoil = "(foil = TRUE) AND ";

            if (cMenuNurAusDeutschland.Checked) filterGermany = "(location = 'Deutschland') AND";

            if (cMenuNurGenungKarten.Checked) filterEnough = string.Format("(count >= {0}) AND", Deck.cards[dgvDeckListe.CurrentCellAddress.Y].Count);

            var filter = filterQuality + filterLanguage + filterFoil + filterGermany + filterEnough;

            if (filter.Length > 0) filter = filter.Remove(filter.LastIndexOf(" AND"));
            FilterString = filter;
            return filter;
        }

        private void CreateFilteredOffers(DeckList deck) {
            var index = 0;
            foreach (Card card in deck.cards) {
                var dv = new DataView(ToDataTable(Deck.cards[index++].offers), FilterChange(null, null), "", DataViewRowState.CurrentRows);

                foreach (DataRowView rowView in dv) {
                    DataRow row = rowView.Row;
                    string seller = row[0].ToString();
                    string location = row[2].ToString();
                    int evalGrade = (int)row[3];
                    string quality = row[4].ToString();
                    string language = row[5].ToString();
                    bool foil = (bool)row[6];
                    int count = (int)row[7];
                    float price = (float)row[8];
                    int sellerId = (int)row[1];
                    int id = (int)row[9];

                    card.filteredoffers.Add(new Offer(seller, sellerId, count, foil, price, quality, language, evalGrade, location, id));
                }
            }
        }

        private void SetFilter(string filter) {
            if (Deck.cards.Count > 0) {
                dgvDeckListe.Update();
                int index = dgvDeckListe.CurrentCellAddress.Y;

                var dv = new DataView(ToDataTable(Deck.cards[index].offers), filter, "", DataViewRowState.CurrentRows);
                dgvAngebote.DataSource = dv;
            }
        }

        #region ToDataTable

        public static DataTable ToDataTable<T>(IList<T> data) {
            PropertyDescriptorCollection props = TypeDescriptor.GetProperties(typeof(T));

            DataTable table = new DataTable();

            for (int i = 0; i < props.Count; i++) {
                PropertyDescriptor prop = props[i];
                table.Columns.Add(prop.Name, prop.PropertyType);
            }

            object[] values = new object[props.Count];

            foreach (T item in data) {
                for (int i = 0; i < values.Length; i++) {
                    values[i] = props[i].GetValue(item);
                }
                table.Rows.Add(values);
            }
            return table;
        }

        #endregion

        #endregion

        #region Menus

        private void cMenuAngeboteLadenUndPreisBerechnen_Click(object sender, EventArgs e) {
            cMenuAngeboteLaden_Click(this, EventArgs.Empty);
            CreateFilteredOffers(this.Deck);
            cMenuPreisBerechnen_Click(this, EventArgs.Empty);
        }

        private void cMenuAngeboteLaden_Click(object sender, EventArgs e) {
            if (Deck.cards.Count > 0) {
                foreach (Card card in Deck.cards) {
                    card.offers.Clear();
                    card.filteredoffers.Clear();
                }
                fetchOffer(Deck, null, 0);
            }
        }

        private void fetchOffersToolStripMenuItem_Click(object sender, EventArgs e) {
            if ((dgvDeckListe.Columns.GetColumnCount(DataGridViewElementStates.Selected) != -1) && (Deck.cards.Count > 0)) {
                int index = dgvDeckListe.CurrentCellAddress.Y;
                Card card = Deck.cards[index];
                card.offers.Clear(); card.filteredoffers.Clear();

                fetchOffer(null, card, index);
            }
        }

        private void fetchOffer(DeckList deck, Card card, int index) {
            var pbFortschritt = new ProgressBar { Size = Config.ProgressBarSize };
            pbFortschritt.Location = new Point((this.Width - pbFortschritt.Width) / 2, (this.Height - pbFortschritt.Height) / 2);
            pbFortschritt.Step = 1;
            pbFortschritt.Minimum = 0;
            pbFortschritt.Maximum = 1;

            if (deck != null)
                pbFortschritt.Maximum = Deck.cards.Count;

            Controls.Add(pbFortschritt);
            pbFortschritt.BringToFront();

            pbFortschritt.Refresh();
            HtmlToOffer.SetProgressBarText(pbFortschritt);

            if (deck != null)
                HtmlToOffer.CreateOffer(deck, pbFortschritt);
            if (card != null)
                HtmlToOffer.CreateOffer(card, pbFortschritt);

            pbFortschritt.Dispose();
            dgvAngebote.Update();

            dgvDeckListe.CurrentCell = dgvDeckListe.Rows[index].Cells[0];
            dgvAngebote.DataSource = "";
            dgvAngebote.DataSource = Deck.cards[index].offers;
            dgvAngebote.ContextMenuStrip = cMenuFilter;
            dgvAngebote.Refresh();

            dgvDeckListe.CurrentCell = dgvDeckListe.Rows[index].Cells[0]; SetFilter(FilterChange(this, EventArgs.Empty));
        }

        private void cMenuPreisBerechnen_Click(object sender, EventArgs e) {
            if (Deck.cards.Count > 0) {
                Deck.boughtOffers.Clear();
                Deck.sellerList.Clear();

                foreach (Card card in Deck.cards)
                    card.filteredoffers.Clear();

                CreateFilteredOffers(Deck);
                PriceCalc.PriceCalculation(Deck);

                var shoppingList = new frmShoppingList(Deck);
                shoppingList.Show();

                PriceCalc.PrintShoppingList(Deck, Deck.boughtOffers, shoppingList.lbBuyList);
            }
        }

        private void cMenuLoadFromFile_Click(object sender, EventArgs e) {
            if (openFileDialog1.ShowDialog() == DialogResult.OK) {
                lbCardDisplay.Hide();
                NeuesDeck();

                switch (Path.GetExtension(openFileDialog1.FileName)) {
                    case ".deck":
                        Deck.LoadFromXmlFile(openFileDialog1.FileName);
                        break;

                    case ".mwDeck":
                        Deck.LoadFromMWSFile(openFileDialog1.FileName);
                        break;
                }

                Deck.FixCardEditions();
                Deck.DeckListOutput(dgvDeckListe);

                cMenuOpenLibrary_Click(sender, e);
                UpdateCardCounter(lbCardDisplay);
            }
        }

        private void cMenuImportFromTxt_Click(object sender, EventArgs e) {
            if (DeckImport == null) DeckImport = new frmDeckImport(this);

            lbCardDisplay.Hide();
            NeuesDeck();

            DeckImport.ShowDialog();

            cMenuOpenLibrary_Click(sender, e);
            UpdateCardCounter(lbCardDisplay);
        }

        private void emptyDeckToolStripMenuItem_Click(object sender, EventArgs e) {
            NeuesDeck();
        }

        private void cMenuOpenLibrary_Click(object sender, EventArgs e) {
            if (!Config.LibOpen) {
                if (Deck == null) NeuesDeck();
                if (CardLibrary == null) CardLibrary = new CardLib(this);
                CardLibrary.Show();
                CardLibrary.Left = this.Left - CardLibrary.Width - 5;
                CardLibrary.Top = this.Top;
            }
        }

        private void cMenuIncreaseCount_Click(object sender, EventArgs e) {
            ChangeAmount(+1);
        }

        private void cMenuDecreaseCount_Click(object sender, EventArgs e) {
            ChangeAmount(-1);
        }

        private void cMenuDelete_Click(object sender, EventArgs e) {
            ChangeAmount(0);
        }

        private void ChangeAmount(int change) {
            if ((dgvDeckListe.Columns.GetColumnCount(DataGridViewElementStates.Selected) != -1) && (Deck.cards.Count > 0)) {
                int index = dgvDeckListe.CurrentCellAddress.Y;
                string name = Deck.cards[index].Name;

                if (change == 0) {
                    dgvDeckListe.DataSource = "";
                    Deck.RemoveCard(name);
                    dgvDeckListe.DataSource = Deck.cards;

                    if (dgvDeckListe.Rows.Count < index) {
                        dgvDeckListe.Rows[dgvDeckListe.Columns.Count].Selected = true;
                        dgvDeckListe.CurrentCell = dgvDeckListe.Rows[dgvDeckListe.Columns.Count].Cells[0];
                    }
                    if (dgvDeckListe.Rows.Count >= index) {
                        if (index == 0) index++;

                        if (dgvDeckListe.RowCount != 0) {
                            dgvDeckListe.Rows[index - 1].Selected = true;
                            dgvDeckListe.CurrentCell = dgvDeckListe.Rows[index - 1].Cells[0];
                        }
                    }
                }
                else
                    Deck.ChangeAmount(name, change);

                dgvDeckListe.Refresh();
                dgvDeckListe.CurrentCell = dgvDeckListe.Rows[index].Cells[0];
            }
            UpdateCardCounter(lbCardDisplay);
        }

        private void cMenuDeckList_Opening(object sender, System.ComponentModel.CancelEventArgs e) {
            foreach (DataGridViewCell c in this.dgvDeckListe.SelectedCells) c.Selected = false;

            Point objMousePosition = dgvDeckListe.PointToClient(Control.MousePosition);
            DataGridView.HitTestInfo objHitTestInfo = default(DataGridView.HitTestInfo);
            objHitTestInfo = dgvDeckListe.HitTest(objMousePosition.X, objMousePosition.Y);

            if (objHitTestInfo.RowIndex >= 0) {
                dgvDeckListe.CurrentCell = dgvDeckListe.Rows[objHitTestInfo.RowIndex].Cells[0];

                int index = dgvDeckListe.CurrentCellAddress.Y;

                Card karte = Deck.cards[index];
                cMenuChangeEdition.DropDownItems.Clear();
                cMenuChangeEdition.DropDownItems.AddRange(karte.editions);
                dgvDeckListe.CurrentCell = dgvDeckListe.Rows[objHitTestInfo.RowIndex].Cells[0];
            }
            else {
                e.Cancel = true;
                cMenuWork.Show(Control.MousePosition);
            }
        }

        private void neueEditionHinzufügenToolStripMenuItem_Click(object sender, EventArgs e) {
            if (AddEdition == null) AddEdition = new frmAddEdition(this);
            AddEdition.ShowDialog();
        }

        private void cardbaseEditionenZurücksetzenToolStripMenuItem_Click(object sender, EventArgs e) {            
            HtmlToOffer.LoadFromWeb("editions.xml");
            Config.CompleteEditionList.Clear();
            Config.CompleteEditionList.AddRange(Edition.LoadEditionsFromXml());
            HtmlToOffer.LoadFromWeb("cardbase.txt");
            CardBase.LoadFromTxt();
            MessageBox.Show("Die Kartendatenbank wurde erfolgreich herunter geladen.");
        }

        #endregion

        #region ReSet Filter

        private void FlipMenuItem(object sender, EventArgs e) {
            ((ToolStripMenuItem)sender).Checked = !((ToolStripMenuItem)sender).Checked;

            SetFilter(FilterChange(this, EventArgs.Empty));
        }

        private void cMenuResetFilter_Click(object sender, EventArgs e) {
            cMenuNurAusDeutschland.Checked = false;
            cMenuNurGenungKarten.Checked = false;
            cMenuNurFoilKarten.Checked = false;

            foreach (ToolStripMenuItem Item in cMenuZustand.DropDownItems.Cast<ToolStripMenuItem>().Where(Item => Item.Checked))
                Item.Checked = false;

            foreach (ToolStripMenuItem Item in cMenuSprachen.DropDownItems.Cast<ToolStripMenuItem>().Where(Item => Item.Checked))
                Item.Checked = false;

            SetFilter("");
        }
        #endregion


    }
}

