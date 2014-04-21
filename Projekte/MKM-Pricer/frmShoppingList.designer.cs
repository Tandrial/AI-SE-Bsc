namespace MKM_Pricer
{
    partial class frmShoppingList
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.lbBuyList = new System.Windows.Forms.ListBox();
            this.SuspendLayout();
            // 
            // lbBuyList
            // 
            this.lbBuyList.FormattingEnabled = true;
            this.lbBuyList.Location = new System.Drawing.Point(12, 12);
            this.lbBuyList.Name = "lbBuyList";
            this.lbBuyList.Size = new System.Drawing.Size(350, 290);
            this.lbBuyList.TabIndex = 0;
            this.lbBuyList.SelectedIndexChanged += new System.EventHandler(this.lbBuyList_SelectedIndexChanged);
            // 
            // frmShoppingList
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(377, 316);
            this.Controls.Add(this.lbBuyList);
            this.Name = "frmShoppingList";
            this.Text = "Einkaufts-Liste";
            this.TopMost = true;
            this.Load += new System.EventHandler(this.ShoppingList_Load);
            this.Resize += new System.EventHandler(this.ShoppingList_Resize);
            this.ResumeLayout(false);

        }

        #endregion

        internal System.Windows.Forms.ListBox lbBuyList;
    }
}