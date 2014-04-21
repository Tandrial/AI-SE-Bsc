namespace MKM_Pricer
{
    partial class CardLib
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
            this.lbCardLib = new System.Windows.Forms.ListBox();
            this.tbCardLibSelection = new System.Windows.Forms.TextBox();
            this.btnAll = new System.Windows.Forms.Button();
            this.btnT2 = new System.Windows.Forms.Button();
            this.btnT1X = new System.Windows.Forms.Button();
            this.btnModern = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // lbCardLib
            // 
            this.lbCardLib.FormattingEnabled = true;
            this.lbCardLib.Location = new System.Drawing.Point(12, 38);
            this.lbCardLib.Name = "lbCardLib";
            this.lbCardLib.Size = new System.Drawing.Size(196, 498);
            this.lbCardLib.Sorted = true;
            this.lbCardLib.TabIndex = 0;
            this.lbCardLib.DoubleClick += new System.EventHandler(this.lbCardLib_DoubleClick);
            // 
            // tbCardLibSelection
            // 
            this.tbCardLibSelection.Location = new System.Drawing.Point(11, 9);
            this.tbCardLibSelection.Name = "tbCardLibSelection";
            this.tbCardLibSelection.Size = new System.Drawing.Size(196, 20);
            this.tbCardLibSelection.TabIndex = 7;
            this.tbCardLibSelection.TextChanged += new System.EventHandler(this.textBox1_TextChanged);
            this.tbCardLibSelection.KeyDown += new System.Windows.Forms.KeyEventHandler(this.tbCardLibSelection_KeyDown);
            // 
            // btnAll
            // 
            this.btnAll.Location = new System.Drawing.Point(158, 542);
            this.btnAll.Name = "btnAll";
            this.btnAll.Size = new System.Drawing.Size(50, 23);
            this.btnAll.TabIndex = 9;
            this.btnAll.Text = "Alle";
            this.btnAll.UseVisualStyleBackColor = true;
            this.btnAll.Click += new System.EventHandler(this.btnAll_Click);
            // 
            // btnT2
            // 
            this.btnT2.Location = new System.Drawing.Point(12, 542);
            this.btnT2.Name = "btnT2";
            this.btnT2.Size = new System.Drawing.Size(29, 23);
            this.btnT2.TabIndex = 8;
            this.btnT2.Text = "T2";
            this.btnT2.UseVisualStyleBackColor = true;
            this.btnT2.Click += new System.EventHandler(this.btnT2_Click);
            // 
            // btnT1X
            // 
            this.btnT1X.Location = new System.Drawing.Point(47, 542);
            this.btnT1X.Name = "btnT1X";
            this.btnT1X.Size = new System.Drawing.Size(41, 23);
            this.btnT1X.TabIndex = 10;
            this.btnT1X.Text = "T1.X";
            this.btnT1X.UseVisualStyleBackColor = true;
            this.btnT1X.Click += new System.EventHandler(this.btnT1X_Click);
            // 
            // btnModern
            // 
            this.btnModern.Location = new System.Drawing.Point(94, 541);
            this.btnModern.Name = "btnModern";
            this.btnModern.Size = new System.Drawing.Size(58, 23);
            this.btnModern.TabIndex = 11;
            this.btnModern.Text = "Modern";
            this.btnModern.UseVisualStyleBackColor = true;
            this.btnModern.Click += new System.EventHandler(this.btnModern_Click);
            // 
            // CardLib
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(219, 576);
            this.Controls.Add(this.btnModern);
            this.Controls.Add(this.btnT1X);
            this.Controls.Add(this.btnAll);
            this.Controls.Add(this.btnT2);
            this.Controls.Add(this.tbCardLibSelection);
            this.Controls.Add(this.lbCardLib);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedToolWindow;
            this.Name = "CardLib";
            this.Text = "Karten Datenbank";
            this.Activated += new System.EventHandler(this.CardLib_Activated);
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.CardLibrary_FormClosing);
            this.Load += new System.EventHandler(this.CardLibrary_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        internal System.Windows.Forms.ListBox lbCardLib;
        private System.Windows.Forms.TextBox tbCardLibSelection;
        private System.Windows.Forms.Button btnAll;
        private System.Windows.Forms.Button btnT2;
        private System.Windows.Forms.Button btnT1X;
        private System.Windows.Forms.Button btnModern;
    }
}