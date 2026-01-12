# Backpropagation Lösungen

## NN.Backprop.01

### Herleitung Gewichtsupdates erste versteckte Schicht

Netz mit 2 versteckten Schichten, Sigmoid überall.

Notation:
- $z^{(l)}$: gewichtete Summe Schicht l
- $a^{(l)}$: Aktivierung Schicht l
- $w^{(l)}$: Gewichte zu Schicht l
- $\delta^{(l)}$: Fehlerterm Schicht l

Sigmoid: $\sigma(z) = \frac{1}{1 + e^{-z}}$

Ableitung: $\sigma'(z) = \sigma(z) \cdot (1 - \sigma(z))$

Fehlerterm Ausgabeschicht:
$\delta^{(o)} = (a^{(o)} - y_T) \cdot a^{(o)} \cdot (1 - a^{(o)})$

Fehlerterm zweite versteckte Schicht:
$\delta^{(2)} = \delta^{(o)} \cdot w^{(o)} \cdot a^{(2)} \cdot (1 - a^{(2)})$

Fehlerterm erste versteckte Schicht:
$\delta^{(1)} = \sum_j(\delta_j^{(2)} \cdot w_j^{(2)}) \cdot a^{(1)} \cdot (1 - a^{(1)})$

Gewichtsupdates erste versteckte Schicht:
$\Delta w_{ij}^{(1)} = -\alpha \cdot \delta_j^{(1)} \cdot x_i$

---

## NN.Backprop.02

Gegeben: $(x, y_T) = (0, 0.5)$, $\alpha = 0.01$

Gewichte: $w_{x \to 1} = -1$, $w_{x \to 2} = 2$, $w_{1 \to 2} = 1$, $b_1 = 1$, $b_2 = 1$

### Teilaufgabe 1 - Forward Pass

Versteckte Zelle (Kugel 1):
$z_1 = w_{x \to 1} \cdot x + b_1 = -1 \cdot 0 + 1 = 1$

$a_1 = \sigma(1) = \frac{1}{1 + e^{-1}} = 0.7311$

Ausgabezelle (Kugel 2):
$z_2 = w_{x \to 2} \cdot x + w_{1 \to 2} \cdot a_1 + b_2 = 2 \cdot 0 + 1 \cdot 0.7311 + 1 = 1.7311$

$y = \sigma(1.7311) = 0.8496$

Fehler:
$E = \frac{1}{2}(y - y_T)^2 = \frac{1}{2}(0.8496 - 0.5)^2 = 0.0611$

### Teilaufgabe 2 - Backpropagation

Fehlerterm Ausgabe:
$\delta_2 = (y - y_T) \cdot y \cdot (1 - y) = 0.3496 \cdot 0.8496 \cdot 0.1504 = 0.0447$

Fehlerterm versteckt:
$\delta_1 = \delta_2 \cdot w_{1 \to 2} \cdot a_1 \cdot (1 - a_1) = 0.0447 \cdot 1 \cdot 0.7311 \cdot 0.2689 = 0.00878$

Partielle Ableitungen:
$\frac{\partial E}{\partial w_{1 \to 2}} = \delta_2 \cdot a_1 = 0.0447 \cdot 0.7311 = 0.0327$

$\frac{\partial E}{\partial w_{x \to 2}} = \delta_2 \cdot x = 0.0447 \cdot 0 = 0$

$\frac{\partial E}{\partial w_{x \to 1}} = \delta_1 \cdot x = 0.00878 \cdot 0 = 0$

$\frac{\partial E}{\partial b_2} = \delta_2 = 0.0447$

$\frac{\partial E}{\partial b_1} = \delta_1 = 0.00878$

Gewichtsupdates:
$\Delta w_{1 \to 2} = -0.01 \cdot 0.0327 = -0.000327$

$\Delta w_{x \to 2} = 0$, $\Delta w_{x \to 1} = 0$ (da $x = 0$)

$\Delta b_2 = -0.01 \cdot 0.0447 = -0.000447$

$\Delta b_1 = -0.01 \cdot 0.00878 = -0.0000878$

Neue Gewichte:
$w_{1 \to 2}^{neu} = 0.9997$, $w_{x \to 2}^{neu} = 2$, $w_{x \to 1}^{neu} = -1$

$b_2^{neu} = 0.9996$, $b_1^{neu} = 0.9999$

---

## NN.Backprop.03

Siehe mlp.py für Implementierung.

### Ergebnisse

Architektur [4, 16, 3]:
- Epoche 0: Loss = 0.438, Accuracy = 80.5%
- Epoche 1000: Loss = 0.020, Accuracy = 98.7%
- Epoche 2000: Loss = 0.006, Accuracy = 100%
- Epoche 3000: Loss = 0.002, Accuracy = 100%
- Finale: Loss = 0.0009, Accuracy = 100%

Der Trainingsfehler ist nach ca. 3000 Epochen fast Null.
