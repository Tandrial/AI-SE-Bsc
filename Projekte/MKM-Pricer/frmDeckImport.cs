using System;
using System.Windows.Forms;
using System.Collections.Generic;

namespace MKM_Pricer
{
    public partial class frmDeckImport : Form
    {
        frmHaupt hauptForm;

        public frmDeckImport(frmHaupt hauptForm)
        {
            InitializeComponent();
            this.hauptForm = hauptForm;
        }

        private void pasteToolStripMenuItem_Click(object sender, EventArgs e)
        {
            richTextBox1.Text = Clipboard.GetText();
        }

        private void importToolStripMenuItem_Click(object sender, EventArgs e)
        {
            List<String> import = new List<string>();
            foreach (string line in richTextBox1.Lines)
            {
                if ((line != String.Empty) && (!line.StartsWith("//")))
                    if (CardBase.CardIstValid(line))
                        import.Add(line);
            }

            hauptForm.Deck.ImportKorrigieren(import);
            hauptForm.Deck.DeckListOutput(hauptForm.dgvDeckListe);

            import.Clear();
            richTextBox1.Clear();
            this.Close();
        }
    }
}
