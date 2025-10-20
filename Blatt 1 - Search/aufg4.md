# A* Optimalitätsbeweis

A* ist optimal, weil es immer zuerst den Knoten mit dem kleinsten  
`f(n) = g(n) + h(n)` expandiert.

Da die Heuristik **zulässig** ist (also nie zu hoch schätzt: `h(n) ≤ h*(n)`),  
wobei `h*` die realen Restkosten repräsentiert, werden die tatsächlichen Kosten **nie überschätzt**.

Dadurch wird der optimale Pfad **nie benachteiligt** – er erscheint A* immer mindestens genauso günstig wie jeder andere Pfad.

---

## Das heißt:
- Knoten auf dem optimalen Pfad behalten immer kleinere oder gleiche f-Werte als Knoten auf schlechteren Pfaden  
- Dadurch werden sie von A* **zuerst** expandiert

---

## was wenn h den falschen niedriger schätzt als unseren?
Selbst wenn die Heuristik einen **falschen Pfad vorübergehend günstiger schätzt**,  
wird A* diesen zwar zuerst erkunden, aber:

- Da die tatsächlichen g-Kosten dieses Pfades steigen,  
  erhöht sich sein f-Wert mit jeder Erweiterung.  
- Der optimale Pfad hat dagegen nie einen zu großen f-Wert  
  (weil `h` nie überschätzt).  
→ A* kehrt also automatisch zum optimalen Pfad zurück,  
  **bevor es ein schlechteres Ziel abschließt.**

---

## Folge:
A* kann kein Ziel mit höheren tatsächlichen Kosten finden,  
bevor es das optimale Ziel erreicht hat → **A\* ist optimal.**
