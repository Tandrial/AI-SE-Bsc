using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Xml.Serialization;

namespace MKM_Pricer {
    [Serializable()]
    public class Edition {
        public int id { get; set; }
        public string shortName { get; set; }
        public string longName { get; set; }

        public Edition(string shortName, string longName, int id) {
            this.id = id;
            this.shortName = shortName;
            this.longName = longName;
        }

        public Edition() { }


        static public void SaveEditionsToXml(List<Edition> editions) {
            XmlSerializer serializer = new XmlSerializer(typeof(List<Edition>));
            TextWriter textWriter = new StreamWriter(@"editions.xml");
            serializer.Serialize(textWriter, editions);
            textWriter.Close();
        }

        static public List<Edition> LoadEditionsFromXml() {
            XmlSerializer deserializer = new XmlSerializer(typeof(List<Edition>));
            TextReader textReader = new StreamReader(@"editions.xml");
            List<Edition> editions;
            editions = (List<Edition>)deserializer.Deserialize(textReader);
            textReader.Close();
            return editions;
        }
    }
}
