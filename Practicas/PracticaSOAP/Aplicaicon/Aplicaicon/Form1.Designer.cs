namespace Aplicaicon
{
    partial class Form1
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
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
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            Button_Agregar = new Button();
            Box_cotizacion = new TextBox();
            label1 = new Label();
            Button_Obtener = new Button();
            Date_obtener = new DateTimePicker();
            panel1 = new Panel();
            dataGridView1 = new DataGridView();
            panel1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)dataGridView1).BeginInit();
            SuspendLayout();
            // 
            // Button_Agregar
            // 
            Button_Agregar.Location = new Point(34, 229);
            Button_Agregar.Margin = new Padding(2, 2, 2, 2);
            Button_Agregar.Name = "Button_Agregar";
            Button_Agregar.Size = new Size(78, 20);
            Button_Agregar.TabIndex = 0;
            Button_Agregar.Text = "Agregar";
            Button_Agregar.UseVisualStyleBackColor = true;
            Button_Agregar.Click += button1_Click;
            // 
            // Box_cotizacion
            // 
            Box_cotizacion.Location = new Point(34, 207);
            Box_cotizacion.Margin = new Padding(2, 2, 2, 2);
            Box_cotizacion.Name = "Box_cotizacion";
            Box_cotizacion.Size = new Size(106, 23);
            Box_cotizacion.TabIndex = 1;
            // 
            // label1
            // 
            label1.AutoSize = true;
            label1.Location = new Point(34, 190);
            label1.Margin = new Padding(2, 0, 2, 0);
            label1.Name = "label1";
            label1.Size = new Size(63, 15);
            label1.TabIndex = 2;
            label1.Text = "Cotizacion";
            label1.Click += label1_Click_1;
            // 
            // Button_Obtener
            // 
            Button_Obtener.Location = new Point(350, 229);
            Button_Obtener.Margin = new Padding(2, 2, 2, 2);
            Button_Obtener.Name = "Button_Obtener";
            Button_Obtener.Size = new Size(78, 20);
            Button_Obtener.TabIndex = 3;
            Button_Obtener.Text = "Obtener";
            Button_Obtener.UseVisualStyleBackColor = true;
            // 
            // Date_obtener
            // 
            Date_obtener.Location = new Point(298, 206);
            Date_obtener.Margin = new Padding(2, 2, 2, 2);
            Date_obtener.Name = "Date_obtener";
            Date_obtener.Size = new Size(211, 23);
            Date_obtener.TabIndex = 4;
            // 
            // panel1
            // 
            panel1.Controls.Add(dataGridView1);
            panel1.Location = new Point(12, 12);
            panel1.Name = "panel1";
            panel1.Size = new Size(536, 175);
            panel1.TabIndex = 5;
            panel1.Paint += panel1_Paint;
            // 
            // dataGridView1
            // 
            dataGridView1.AllowUserToAddRows = false;
            dataGridView1.AllowUserToDeleteRows = false;
            dataGridView1.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            dataGridView1.Columns.AddRange(new DataGridViewColumn[] {
                new DataGridViewTextBoxColumn { HeaderText = "Fecha", DataPropertyName = "Fecha", Width = 100 },
                new DataGridViewTextBoxColumn { HeaderText = "Cotización", DataPropertyName = "Cotizacion", Width = 150 },
                new DataGridViewTextBoxColumn { HeaderText = "Cotización Oficial", DataPropertyName = "CotizacionOficial", Width = 150 }
            });
            dataGridView1.Location = new Point(0, 0);
            dataGridView1.Name = "dataGridView1";
            dataGridView1.ReadOnly = true;
            dataGridView1.Size = new Size(536, 175);
            dataGridView1.TabIndex = 0;
            dataGridView1.Dock = DockStyle.Fill;
            // 
            // Form1
            // 
            AutoScaleDimensions = new SizeF(7F, 15F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(560, 270);
            Controls.Add(panel1);
            Controls.Add(Date_obtener);
            Controls.Add(Button_Obtener);
            Controls.Add(label1);
            Controls.Add(Box_cotizacion);
            Controls.Add(Button_Agregar);
            Margin = new Padding(2, 2, 2, 2);
            Name = "Form1";
            Text = "Form1";
            Load += Form1_Load;
            panel1.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)dataGridView1).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Button Button_Agregar;
        private TextBox Box_cotizacion;
        private Label label1;
        private Button Button_Obtener;
        private DateTimePicker Date_obtener;
        private Panel panel1;
        private DataGridView dataGridView1;
    }
}
