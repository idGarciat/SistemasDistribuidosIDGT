using System;
using System.ComponentModel;
using System.Net;
using System.Text;
using System.Xml;
using System.Xml.Linq;

namespace Aplicaicon
{
    [DesignTimeVisible(false)]
    public class WebService1
    {
        private readonly string _url = "https://localhost:44397/ServicioSOAP.asmx";

        public Class1 obtenerCotizacion(string fecha)
        {
            if (!DateTime.TryParseExact(fecha, "yyyy-MM-dd", System.Globalization.CultureInfo.InvariantCulture, System.Globalization.DateTimeStyles.None, out _))
            {
                throw new ArgumentException("La fecha debe tener formato yyyy-MM-dd");
            }

            string soapRequest = $@"<?xml version=""1.0"" encoding=""utf-8""?>
            <soap:Envelope xmlns:soap=""http://schemas.xmlsoap.org/soap/envelope/"" 
            xmlns:xsi=""http://www.w3.org/2001/XMLSchema-instance"" xmlns:xsd=""http://www.w3.org/2001/XMLSchema"">
            <soap:Body>
            <obtenerCotizacion xmlns=""http://tempuri.org/"">
            <fecha>{fecha}</fecha>
            </obtenerCotizacion>
            </soap:Body>
            </soap:Envelope>";

            try
            {
                using (WebClient client = new WebClient())
                {
                    client.Headers.Add("Content-Type", "text/xml; charset=utf-8");
                    client.Headers.Add("SOAPAction", "http://tempuri.org/obtenerCotizacion");
                    
                    byte[] requestData = Encoding.UTF8.GetBytes(soapRequest);
                    byte[] responseData = client.UploadData(_url, requestData);
                    string responseString = Encoding.UTF8.GetString(responseData);

                    return ParseObtenerCotizacionResponse(responseString);
                }
            }
            catch (Exception ex)
            {
                throw new Exception($"Error calling obtenerCotizacion: {ex.Message}", ex);
            }
        }

        public bool registrarCotizacion(string fecha, double monto)
        {
            if (!DateTime.TryParseExact(fecha, "yyyy-MM-dd", System.Globalization.CultureInfo.InvariantCulture, System.Globalization.DateTimeStyles.None, out _))
            {
                throw new ArgumentException("La fecha debe tener formato yyyy-MM-dd");
            }

            string montoStr = monto.ToString(System.Globalization.CultureInfo.InvariantCulture);

            string soapRequest = $@"<?xml version=""1.0"" encoding=""utf-8""?>
            <soap:Envelope xmlns:soap=""http://schemas.xmlsoap.org/soap/envelope/""
            xmlns:xsi=""http://www.w3.org/2001/XMLSchema-instance"" xmlns:xsd=""http://www.w3.org/2001/XMLSchema"">
            <soap:Body>
            <registrarCotizacion xmlns=""http://tempuri.org/"">
            <fecha>{fecha}</fecha>
            <monto>{montoStr}</monto>
            </registrarCotizacion>
            </soap:Body>
            </soap:Envelope>";

            try
            {
                using (WebClient client = new WebClient())
                {
                    client.Headers.Add("Content-Type", "text/xml; charset=utf-8");
                    client.Headers.Add("SOAPAction", "http://tempuri.org/registrarCotizacion");
                    
                    byte[] requestData = Encoding.UTF8.GetBytes(soapRequest);
                    byte[] responseData = client.UploadData(_url, requestData);
                    string responseString = Encoding.UTF8.GetString(responseData);

                    return ParseRegistrarCotizacionResponse(responseString);
                }
            }
            catch (Exception ex)
            {
                throw new Exception($"Error calling registrarCotizacion: {ex.Message}", ex);
            }
        }

        private Class1 ParseObtenerCotizacionResponse(string responseXml)
        {
            try
            {
                XDocument doc = XDocument.Parse(responseXml);
                XNamespace ns = "http://tempuri.org/";

                var result = doc.Descendants(ns + "obtenerCotizacionResult").FirstOrDefault();
                if (result == null)
                    return null;

                var cotizacion = new Class1
                {
                    Fecha = result.Element(ns + "Fecha")?.Value,
                    Cotizacion = double.Parse(result.Element(ns + "Cotizacion")?.Value ?? "0", System.Globalization.CultureInfo.InvariantCulture),
                    CotizacionOficial = double.Parse(result.Element(ns + "CotizacionOficial")?.Value ?? "0", System.Globalization.CultureInfo.InvariantCulture)
                };

                return cotizacion;
            }
            catch (Exception ex)
            {
                throw new Exception($"Error parsing response: {ex.Message}", ex);
            }
        }

        private bool ParseRegistrarCotizacionResponse(string responseXml)
        {
            try
            {
                XDocument doc = XDocument.Parse(responseXml);
                XNamespace ns = "http://tempuri.org/";

                var result = doc.Descendants(ns + "registrarCotizacionResult").FirstOrDefault();
                if (result == null)
                    return false;

                return bool.Parse(result.Value);
            }
            catch (Exception ex)
            {
                throw new Exception($"Error parsing response: {ex.Message}", ex);
            }
        }

        public List<Class1> obtenerTodasLasCotizaciones()
        {
            string soapRequest = @"<?xml version=""1.0"" encoding=""utf-8""?>
            <soap:Envelope xmlns:soap=""http://schemas.xmlsoap.org/soap/envelope/"" 
            xmlns:xsi=""http://www.w3.org/2001/XMLSchema-instance"" xmlns:xsd=""http://www.w3.org/2001/XMLSchema"">
            <soap:Body>
            <obtenerTodasLasCotizaciones xmlns=""http://tempuri.org/"">
            </obtenerTodasLasCotizaciones>
            </soap:Body>
            </soap:Envelope>";

            try
            {
                using (WebClient client = new WebClient())
                {
                    client.Headers.Add("Content-Type", "text/xml; charset=utf-8");
                    client.Headers.Add("SOAPAction", "http://tempuri.org/obtenerTodasLasCotizaciones");
                    
                    byte[] requestData = Encoding.UTF8.GetBytes(soapRequest);
                    byte[] responseData = client.UploadData(_url, requestData);
                    string responseString = Encoding.UTF8.GetString(responseData);

                    return ParseObtenerTodasLasCotizacionesResponse(responseString);
                }
            }
            catch (Exception ex)
            {
                throw new Exception($"Error calling obtenerTodasLasCotizaciones: {ex.Message}", ex);
            }
        }

        private List<Class1> ParseObtenerTodasLasCotizacionesResponse(string responseXml)
        {
            try
            {
                var cotizaciones = new List<Class1>();
                XDocument doc = XDocument.Parse(responseXml);
                XNamespace ns = "http://tempuri.org/";

                var items = doc.Descendants(ns + "Class1");
                foreach (var item in items)
                {
                    cotizaciones.Add(new Class1
                    {
                        Fecha = item.Element(ns + "Fecha")?.Value,
                        Cotizacion = double.Parse(item.Element(ns + "Cotizacion")?.Value ?? "0", System.Globalization.CultureInfo.InvariantCulture),
                        CotizacionOficial = double.Parse(item.Element(ns + "CotizacionOficial")?.Value ?? "0", System.Globalization.CultureInfo.InvariantCulture)
                    });
                }

                return cotizaciones;
            }
            catch (Exception ex)
            {
                throw new Exception($"Error parsing response: {ex.Message}", ex);
            }
        }
    }

    public class Class1
    {
        public string Fecha { get; set; }
        public double Cotizacion { get; set; }
        public double CotizacionOficial { get; set; }
    }
}