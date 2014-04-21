using System;
using System.Windows.Forms;
using System.Text.RegularExpressions;
namespace MKM_Pricer {
    public partial class CardLib : Form {
        readonly frmHaupt hauptForm;

        public CardLib(frmHaupt hauptForm) {
            InitializeComponent();
            this.hauptForm = hauptForm;
            CardBase.AddToListBox(lbCardLib);
        }

        private void CardLibrary_Load(object sender, EventArgs e) {
            btnT2_Click(sender, e);
        }

        private void lbCardLib_DoubleClick(object sender, EventArgs e) {
            if (lbCardLib.Items.Count > 0) {
                string line = lbCardLib.Items[lbCardLib.SelectedIndex].ToString();
                string name;
                Edition edition;
                if (line.IndexOf('[') != -1) {
                    name = line.Split('[')[0];
                    string test = line.Split('[')[1].Replace(']', ' ').Trim();
                    edition = Config.FindEdition(test);

                } else {
                    name = line.Split('-')[0];
                    edition = Config.FindEditioninString(line.Split('-')[1].Replace('-', ' '));
                }

                hauptForm.Deck.AddCard(name.Trim(), 1, edition);
                hauptForm.Deck.CompressDeckList();
                hauptForm.Deck.DeckListOutput(hauptForm.dgvDeckListe);
                hauptForm.Deck.CalculateCardCount();
                hauptForm.lbCardDisplay.Text = String.Format("{0} Karten", hauptForm.Deck.CardCount);
            }
        }

        private void CardLibrary_FormClosing(object sender, FormClosingEventArgs e) {
            Config.LibOpen = false;
            this.Hide(); e.Cancel = true;
        }

        private void textBox1_TextChanged(object sender, EventArgs e) {
            for (int index = 0; index < lbCardLib.Items.Count; index++) {
                string item = lbCardLib.Items[index].ToString().ToLower();

                if (item.StartsWith(tbCardLibSelection.Text.ToLower())) {
                    lbCardLib.SelectedIndex = index;
                    //TODO fix name display... somehow
                    /*      KeyStateInfo deletedPressed = KeyboardInfo.GetKeyState(Keys.Back);
                            int selctionStart = tbCardLibSelection.Text.Length;
                            tbCardLibSelection.Text = lbCardLib.Items[lbCardLib.SelectedIndex].ToString();
                            if (deletedPressed.IsPressed) tbCardLibSelection.Text.Remove(selctionStart-1,1);
                            tbCardLibSelection.Select(selctionStart, tbCardLibSelection.Text.Length);*/

                    break;
                }
            }
        }

        private void tbCardLibSelection_KeyDown(object sender, KeyEventArgs e) {
            if (e.KeyCode == Keys.Enter) lbCardLib_DoubleClick(sender, e);

            if (e.KeyCode == Keys.Up) {
                if (lbCardLib.SelectedIndex != 0)
                    lbCardLib.SelectedIndex--;
                e.Handled = true;
            }

            if (e.KeyCode == Keys.Down) {
                if (lbCardLib.SelectedIndex < lbCardLib.Items.Count - 1)
                    lbCardLib.SelectedIndex++;
                e.Handled = true;
            }
        }

        private void CardLib_Activated(object sender, EventArgs e) {
            hauptForm.Focus();
            this.Focus();
        }

        private void btnAll_Click(object sender, EventArgs e) {
            CardBase.AddToListBox(this.lbCardLib);
        }

        private void btnT2_Click(object sender, EventArgs e) {
            CardBase.AddCustomToListBox(lbCardLib, Config.T2);
        }

        private void btnT1X_Click(object sender, EventArgs e) {
            CardBase.AddCustomToListBox(lbCardLib, Config.T1X);
        }

        private void btnModern_Click(object sender, EventArgs e) {
            CardBase.AddCustomToListBox(lbCardLib, Config.Modern);
        }
    }
}
