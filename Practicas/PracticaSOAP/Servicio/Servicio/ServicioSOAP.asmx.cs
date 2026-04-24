using MySqlConnector;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Globalization;
using System.Linq;
using System.Web;
using System.Web.Services;

namespace Servicio
{
    /// <summary>
    /// Summary description for WebService1
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class WebService1 : System.Web.Services.WebService
    {

        //[WebMethod]
        //public string HelloWorld()
        //{
        //    return "Hello World";
        //}

        [WebMethod]
        public Class1 obtenerCotizacion(string fecha) {
            DateTime fechaParseada;
            if (!DateTime.TryParseExact(fecha, "yyyy-MM-dd", CultureInfo.InvariantCulture, DateTimeStyles.None, out fechaParseada))
                throw new ArgumentException("La fecha debe tener formato yyyy-MM-dd.");

            string cs = ConfigurationManager.ConnectionStrings["MySqlConn"].ConnectionString;

            using (var conn = new MySqlConnection(cs))
            using (var cmd = new MySqlCommand(@"
            SELECT fecha, cotizacion, cotizacion_oficial
            FROM cotizaciones
            WHERE fecha = @fecha;", conn))
            {
                cmd.Parameters.AddWithValue("@fecha", fechaParseada.Date);
                conn.Open();

                using (var rd = cmd.ExecuteReader())
                {
                    if (!rd.Read()) return null;

                    return new Class1
                    {
                        Fecha = Convert.ToDateTime(rd["fecha"]).ToString("yyyy-MM-dd"),
                        Cotizacion = Convert.ToDouble(rd["cotizacion"]),
                        CotizacionOficial = (double)(rd["cotizacion_oficial"] == DBNull.Value
                            ? (double?)null
                            : Convert.ToDouble(rd["cotizacion_oficial"]))
                    };
                }
            }
        }

        [WebMethod]
        public bool registrarCotizacion(string fecha, double monto) {
            DateTime fechaParseada;
            if (!DateTime.TryParseExact(fecha, "yyyy-MM-dd", CultureInfo.InvariantCulture, DateTimeStyles.None, out fechaParseada))
                throw new ArgumentException("La fecha debe tener formato yyyy-MM-dd.");

            string cs = ConfigurationManager.ConnectionStrings["MySqlConn"].ConnectionString;

            using (var conn = new MySqlConnection(cs))
            using (var cmd = new MySqlCommand(@"
            INSERT INTO cotizaciones (fecha, cotizacion, cotizacion_oficial)
            VALUES (@fecha, @monto, @cotizacion_oficial)
            ON DUPLICATE KEY UPDATE cotizacion = @monto;", conn))
            {
                cmd.Parameters.AddWithValue("@fecha", fechaParseada.Date);
                cmd.Parameters.AddWithValue("@monto", monto);
                cmd.Parameters.AddWithValue("@cotizacion_oficial", 6.97);

                conn.Open();
                return cmd.ExecuteNonQuery() > 0;
            }
        }

        [WebMethod]
        public List<Class1> obtenerTodasLasCotizaciones() {
            string cs = ConfigurationManager.ConnectionStrings["MySqlConn"].ConnectionString;
            var cotizaciones = new List<Class1>();

            using (var conn = new MySqlConnection(cs))
            using (var cmd = new MySqlCommand(@"
                SELECT fecha, cotizacion, cotizacion_oficial
                FROM cotizaciones
                ORDER BY fecha DESC;", conn))
            {
                conn.Open();
                using (var rd = cmd.ExecuteReader())
                {
                    while (rd.Read())
                    {
                        cotizaciones.Add(new Class1
                        {
                            Fecha = Convert.ToDateTime(rd["fecha"]).ToString("yyyy-MM-dd"),
                            Cotizacion = Convert.ToDouble(rd["cotizacion"]),
                            CotizacionOficial = Convert.ToDouble(rd["cotizacion_oficial"] == DBNull.Value ? 0 : rd["cotizacion_oficial"])
                        });
                    }
                }
            }
            return cotizaciones;
        }
    }
}
