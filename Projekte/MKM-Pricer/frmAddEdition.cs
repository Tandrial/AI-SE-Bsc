using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace MKM_Pricer {
    public partial class frmAddEdition : Form {

        frmHaupt parent;

        public frmAddEdition(frmHaupt parent) {
            InitializeComponent();
            this.parent = parent;
        }
        private void btnCancel_Click(object sender, EventArgs e) {

            this.Close();
        }

        private void btnAddEd_Click(object sender, EventArgs e) {
            string nameLong = this.tbName.Text;
            string nameshort = this.tbNameShort.Text;
            int id = (int)this.nudID.Value;

            if (nameLong == "" || nameshort == "" || id == 0)
                MessageBox.Show("Bitte überprüfe deine Eingaben");
            else if (Config.FindEditionbyNumber(id) != null)
                MessageBox.Show("Es existiert bereits eine Edition mit der Id " + id.ToString());
            else {
                List<string> CardList = new List<string>();
                List<Edition> EditionList = new List<Edition>();

                EditionList.AddRange(Edition.LoadEditionsFromXml());
                Console.WriteLine("Editions loaded.");

                string line;
                var sr = new StreamReader("cardbase.txt");
                while ((line = sr.ReadLine()) != null)
                    CardList.Add(line);
                sr.Close();
                Console.WriteLine("CardBase loaded.");
                Edition newEdition = new Edition(nameshort, nameLong, id);

                List<Edition> newEditionList = new List<Edition>();
                newEditionList.Add(newEdition);
                newEditionList.AddRange(EditionList);
                Edition.SaveEditionsToXml(newEditionList);

                List<string> newBase = new List<string>();
                newBase.AddRange(Builder.GetCardsByEdition(newEdition));
                int countneu = newBase.Count;
                newBase.AddRange(CardList);
                Builder.SaveToTxt(newBase, "cardbase.txt");
                MessageBox.Show(countneu + " neue Karten von der Edition: " + newEdition + " hinzugefügt.");
                Config.CompleteEditionList.Clear();
                Config.CompleteEditionList.AddRange(Edition.LoadEditionsFromXml());
                CardBase.LoadFromTxt();
                this.Close();
            }
        }
    }
}
