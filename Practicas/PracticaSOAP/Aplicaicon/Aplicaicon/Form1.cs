using System;
using System.Collections.Generic;
using System.Windows.Forms;

namespace Aplicaicon
{
    public partial class Form1 : Form
    {
        private WebService1 _client;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            try
            {
                // Conectar el evento del botón Obtener
                Button_Obtener.Click += Button_Obtener_Click;

                _client = new WebService1();
                MessageBox.Show("Cliente SOAP conectado correctamente", "Éxito", MessageBoxButtons.OK, MessageBoxIcon.Information);
                
                // Cargar todas las cotizaciones
                CargarTodasLasCotizaciones();
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al conectar el cliente SOAP: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void CargarTodasLasCotizaciones()
        {
            try
            {
                if (_client == null)
                {
                    MessageBox.Show("El cliente no está inicializado", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                List<Class1> cotizaciones = _client.obtenerTodasLasCotizaciones();
                dataGridView1.DataSource = cotizaciones;
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al cargar cotizaciones: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                if (_client == null)
                {
                    MessageBox.Show("El cliente no está inicializado", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                if (string.IsNullOrWhiteSpace(Box_cotizacion.Text))
                {
                    MessageBox.Show("Por favor ingresa un monto de cotización", "Validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    return;
                }

                if (!double.TryParse(Box_cotizacion.Text, out double monto))
                {
                    MessageBox.Show("Por favor ingresa un número válido", "Validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    return;
                }

                DateTime fecha = DateTime.Now;
                string fechaFormato = fecha.ToString("yyyy-MM-dd");

                bool exito = _client.registrarCotizacion(fechaFormato, monto);

                if (exito)
                {
                    MessageBox.Show($"Cotización registrada exitosamente\nFecha: {fechaFormato}\nMonto: {monto}", "Éxito", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    Box_cotizacion.Clear();
                    
                    // Actualizar la tabla
                    CargarTodasLasCotizaciones();
                }
                else
                {
                    MessageBox.Show("Error al registrar la cotización en el servidor", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al registrar cotización: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void label1_Click(object sender, EventArgs e)
        {
        }

        private void label1_Click_1(object sender, EventArgs e)
        {
        }

        private void Button_Obtener_Click(object sender, EventArgs e)
        {
            try
            {
                if (_client == null)
                {
                    MessageBox.Show("El cliente no está inicializado", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                DateTime fecha = Date_obtener.Value;
                string fechaFormato = fecha.ToString("yyyy-MM-dd");

                var resultado = _client.obtenerCotizacion(fechaFormato);

                if (resultado != null)
                {
                    MessageBox.Show(
                        $"Fecha: {resultado.Fecha}\n" +
                        $"Cotización: {resultado.Cotizacion}\n" +
                        $"Cotización Oficial: {resultado.CotizacionOficial}",
                        "Resultado",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Information
                    );
                }
                else
                {
                    MessageBox.Show("No se encontró cotización para esa fecha", "Información", MessageBoxButtons.OK, MessageBoxIcon.Information);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al obtener cotización: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void panel1_Paint(object sender, PaintEventArgs e)
        {

        }
    }
}
