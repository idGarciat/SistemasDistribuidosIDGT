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
            Cotizacion_box = new TextBox();
            label1 = new Label();
            button1 = new Button();
            textBox1 = new TextBox();
            label2 = new Label();
            SuspendLayout();
            // 
            // Button_Agregar
            // 
            Button_Agregar.Location = new Point(49, 382);
            Button_Agregar.Name = "Button_Agregar";
            Button_Agregar.Size = new Size(112, 34);
            Button_Agregar.TabIndex = 0;
            Button_Agregar.Text = "Agregar";
            Button_Agregar.UseVisualStyleBackColor = true;
            Button_Agregar.Click += button1_Click;
            // 
            // Cotizacion_box
            // 
            Cotizacion_box.Location = new Point(49, 345);
            Cotizacion_box.Name = "Cotizacion_box";
            Cotizacion_box.Size = new Size(150, 31);
            Cotizacion_box.TabIndex = 1;
            // 
            // label1
            // 
            label1.AutoSize = true;
            label1.Location = new Point(49, 317);
            label1.Name = "label1";
            label1.Size = new Size(94, 25);
            label1.TabIndex = 2;
            label1.Text = "Cotizacion";
            label1.Click += label1_Click_1;
            // 
            // button1
            // 
            button1.Location = new Point(500, 382);
            button1.Name = "button1";
            button1.Size = new Size(112, 34);
            button1.TabIndex = 3;
            button1.Text = "Obtener";
            button1.UseVisualStyleBackColor = true;
            // 
            // textBox1
            // 
            textBox1.Location = new Point(500, 345);
            textBox1.Name = "textBox1";
            textBox1.Size = new Size(150, 31);
            textBox1.TabIndex = 4;
            // 
            // label2
            // 
            label2.AutoSize = true;
            label2.Location = new Point(500, 317);
            label2.Name = "label2";
            label2.Size = new Size(57, 25);
            label2.TabIndex = 5;
            label2.Text = "Fecha";
            // 
            // Form1
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(800, 450);
            Controls.Add(label2);
            Controls.Add(textBox1);
            Controls.Add(button1);
            Controls.Add(label1);
            Controls.Add(Cotizacion_box);
            Controls.Add(Button_Agregar);
            Name = "Form1";
            Text = "Form1";
            Load += Form1_Load;
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Button Button_Agregar;
        private TextBox Cotizacion_box;
        private Label label1;
        private Button button1;
        private TextBox textBox1;
        private Label label2;
    }
}
