# Graphsuche: Würzburg → München

Hinweise:
- Reihenfolge bei gleichen f(n)-Kosten: alphabetisch (z. B. Mannheim vor München, Karlsruhe vor Kassel).
- Die Werte stammen aus der gemessenen Programmausgabe.

---

Tiefensuche

Ablauf:
```
Würzburg → Frankfurt → Mannheim → Karlsruhe → Augsburg → München
```

Ergebnis:
- Pfad: Würzburg → Frankfurt → Mannheim → Karlsruhe → Augsburg → München
- Distanz: 716 km (nicht optimal)
- Knoten expandiert: 6

---

Breitensuche

Ablauf nach Queue-Logs:
```
Level 0: Würzburg
Level 1: Frankfurt, Erfurt, Nürnberg
Level 2: Mannheim, Kassel, Stuttgart, München (gefunden)
```

Ergebnis:
- Pfad: Würzburg → Nürnberg → München
- Distanz: 270 km (optimal)
- Knoten expandiert: 8
- Max. Queue-Größe: 4

---

A*-Suche

Heuristik h(n) → München (aus Code):
```
München: 0    Augsburg: 0     Karlsruhe: 10   Frankfurt: 100  Würzburg: 170
Mannheim: 200 Stuttgart: 300  Erfurt: 400     Kassel: 460     Nürnberg: 537
```

Ergebnis:
- Pfad: Würzburg → Nürnberg → München
- Distanz: 270 km (optimal)
- Knoten expandiert ... 8
- Max. Open-List-Größe ... 4

---

Aufgabe 2 Zulässigkeit der Heuristik

Antwort: Nein  die gegebene Heuristik ist nicht zulässig.

Begründung (Gegenbeispiel):
- h(Nürnberg) = 537 (gegeben)
- optimale Restkosten h*(Nürnberg) = 167 (Nürnberg → München)
- 537 > 167 ... h überschätzt die wahren Restkosten ... Verstoß gegen Zulässigkeit h(n) ≤ h*(n)

Folgen: A* kann mit nicht-zulässiger Heuristik suboptimale Pfade finden. In dieser Messung wurde trotzdem der optimale Pfad gefunden ... das ist nicht garantiert.

Korrigierte Heuristik und erwarteter Effekt

Korrektur: setze h(Nürnberg) = 167. Andere h-Werte bleiben wie im Code ... sie überschätzen nicht.

A*-Suche mit korrigierter Heuristik:
- Expandierte Reihenfolge: Würzburg → Nürnberg → München
- Knoten expandiert: 3
- Max. Open-List-Größe: 4
- Pfad/Distanz ... Würzburg → Nürnberg → München ... 270 km (optimal)

Fazit: Mit zulässiger heuristik bleibt A* optimal und wird effizienter (weniger Expandierungen).
