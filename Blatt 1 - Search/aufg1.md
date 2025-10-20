# Aufgabe 1

Wir definieren jeden Zustand als ein Tupel (E, O, P),  
wobei E = Elben, O = Orks, P = Pferd (1 = Startseite, 0 = Zielseite).  

Startzustand: (3, 3, 1)  
Zielzustand: (0, 0, 0)

Gültige Übergänge:
- Pferd wechselt die Seite und nimmt 1–2 Passagiere mit (niemals allein)
- (E, O, 1) → (E − x, O − y, 0)
- (E, O, 0) → (E + x, O + y, 1)
- x + y ∈ {1, 2}
- Keine Seite darf mehr Orks als Elben haben, wenn dort Elben sind

---
Graph
![](bild1.png)
