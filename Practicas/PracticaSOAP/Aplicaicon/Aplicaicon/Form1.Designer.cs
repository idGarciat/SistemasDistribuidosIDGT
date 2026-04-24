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
            // Box_cotizacion
            // 
            Box_cotizacion.Location = new Point(49, 345);
            Box_cotizacion.Name = "Box_cotizacion";
            Box_cotizacion.Size = new Size(150, 31);
            Box_cotizacion.TabIndex = 1;
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
            // Button_Obtener
            // 
            Button_Obtener.Location = new Point(500, 382);
            Button_Obtener.Name = "Button_Obtener";
            Button_Obtener.Size = new Size(112, 34);
            Button_Obtener.TabIndex = 3;
            Button_Obtener.Text = "Obtener";
            Button_Obtener.UseVisualStyleBackColor = true;
            // 
            // Date_obtener
            // 
            Date_obtener.Location = new Point(426, 343);
            Date_obtener.Name = "Date_obtener";
            Date_obtener.Size = new Size(300, 31);
            Date_obtener.TabIndex = 4;
            // 
            // Form1
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(800, 450);
            Controls.Add(Date_obtener);
            Controls.Add(Button_Obtener);
            Controls.Add(label1);
            Controls.Add(Box_cotizacion);
            Controls.Add(Button_Agregar);
            Name = "Form1";
            Text = "Form1";
            Load += Form1_Load;
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Button Button_Agregar;
        private TextBox Box_cotizacion;
        private Label label1;
        private Button Button_Obtener;
        private DateTimePicker Date_obtener;
    }
}
