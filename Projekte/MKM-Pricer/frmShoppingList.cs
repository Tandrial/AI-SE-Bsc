using System;
using System.Linq;
using System.Windows.Forms;
using System.Text.RegularExpressions;

namespace MKM_Pricer
{
    internal partial class frmShoppingList : Form
    {
        readonly DeckList deck;

        public frmShoppingList(DeckList deck)
        {
            InitializeComponent();
            this.deck = deck;
        }

        private void lbBuyList_SelectedIndexChanged(object sender, EventArgs e)
        {
            string s = lbBuyList.Items[lbBuyList.SelectedIndex].ToString();

            if (s.IndexOf("//") >= 0)
            {
                foreach (SellerList sellerList in deck.sellerList.Where(sellerList => s.IndexOf(sellerList.Seller) >= 0))
                    foreach (BoughtOffer t in sellerList.boughtOffers)
                    {
                        System.Diagnostics.Process.Start(string.Format(Config.ShoppingUrl, Regex.Replace(t.Karte.Name, @"[(].*[)]", "").TrimEnd().Replace(" ", "+"), sellerList.SellerId));
                    }
                Activate();
            }
        }

        private void ShoppingList_Resize(object sender, EventArgs e)
        {
            lbBuyList.Height = Height - 56;
        }

        private void ShoppingList_Load(object sender, EventArgs e)
        {
            lbBuyList.Height = Height - 56;
            lbBuyList.Items.Clear();
        }
    }
}
