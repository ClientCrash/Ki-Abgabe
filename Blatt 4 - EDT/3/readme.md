# Nr. 1 – Confusion Matrix

Beim Datensatz **`Zoo.csv`** mit dem **J48-Algorithmus** wurde nur ein Tier falsch klassifiziert:  
Eine Amphibie wurde fälschlicherweise als Reptil eingeordnet.

---

# Nr. 2 – Datentypen

- **Nominal**: Entspricht einer art Enum mit vordefinierten Werten ohne Rangordnung, die nicht sortierbar sind.  
- **Ordinal**: Hat ebenfalls definierte Werte, aber mit einer klaren Reihenfolge, sodass sie sortiert werden können.  
- **String**: Werte sind nicht sortierbar und können beliebig viele verschiedene Ausprägunge annehmen.

Die ARFF-Dateien befinden sich im Repository.

---

# Nr. 3 – Vergleich der Algorithmen

- Für den **Zoo-Datensatz** liefern **J48** und **ID3** identische Ergebnisse.  
  ID3 konnte sowohl für ARFF- als auch CSV-Dateien gestartet werden.

- Beim **Restaurant-Datensatz** trat mit ID3 kein Fehler auf, während J48 drei Fehlklassifikationen erzeugte.

Wie beim Zoo-Datensatz lassen sich auch hier keine Unterschiede in Bezug auf das Dateiformat feststellen.
