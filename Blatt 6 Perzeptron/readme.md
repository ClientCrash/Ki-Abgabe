# Perzeptron Lösungen

## NN.Perzeptron.01

### Teilaufgabe 1

Perzeptron: $(w_0, w_1, w_2)^T = (2, 1, 1)^T$

Trennebene: $2 + x_1 + x_2 = 0$ bzw. $x_2 = -x_1 - 2$

Der Bereich $+1$ liegt dort, wo $2 + x_1 + x_2 > 0$, also oberhalb/rechts der Geraden $x_2 = -x_1 - 2$.

### Teilaufgabe 2

- $(1, 0.5, 0.5)^T$: Selbe Trennebene, gleiche Klassifikation (Skalierung mit Faktor 0.5)
- $(200, 100, 100)^T$: Selbe Trennebene, gleiche Klassifikation (Skalierung mit Faktor 100)
- $(\sqrt{2}, \sqrt{1}, \sqrt{1})^T$: Selbe Trennebene, gleiche Klassifikation (andere Skalierung)
- $(-2, -1, -1)^T$: Selbe Trennebene, **umgekehrte** Klassifikation (negative Skalierung)

---

## NN.Perzeptron.02

### Teilaufgabe 1 - Implementierung

UND: $w = (-1.5, 1, 1)^T$ 
- (0,0) → -1.5 < 0 → 0
- (0,1) → -0.5 < 0 → 0
- (1,0) → -0.5 < 0 → 0
- (1,1) → 0.5 > 0 → 1

ODER: $w = (-0.5, 1, 1)^T$
- (0,0) → -0.5 < 0 → 0
- (0,1) → 0.5 > 0 → 1
- (1,0) → 0.5 > 0 → 1
- (1,1) → 1.5 > 0 → 1

KOMPLEMENT: $w = (0.5, -1)^T$
- 0 → 0.5 > 0 → 1
- 1 → -0.5 < 0 → 0

### Teilaufgabe 2 - XOR

XOR ist nicht linear separierbar. Die Punkte (0,0) und (1,1) sollen Ausgabe 0 haben, während (0,1) und (1,0) Ausgabe 1 haben sollen. Diese Konfiguration kann nicht durch eine einzelne Gerade getrennt werden.

---

## NN.Perzeptron.03

### Datensatz

```python
import numpy as np

m = 10
X = np.random.uniform(-1, 1, (m, 2))
X_bias = np.c_[np.ones(m), X]

p1, p2 = np.random.uniform(-1, 1, (2, 2))
slope = (p2[1] - p1[1]) / (p2[0] - p1[0])
intercept = p1[1] - slope * p1[0]

y = np.sign(X[:, 1] - (slope * X[:, 0] + intercept))
y[y == 0] = 1
```

### Training

```python
def perceptron_train(X, y, alpha=1.0):
    w = np.zeros(X.shape[1])
    steps = 0
    while True:
        predictions = np.sign(X @ w)
        predictions[predictions == 0] = 1
        misclassified = np.where(y != predictions)[0]
        if len(misclassified) == 0:
            break
        i = np.random.choice(misclassified)
        w += alpha * (y[i] - predictions[i]) * X[i]
        steps += 1
    return w, steps

iterations = 1000
steps_list = []
for _ in range(iterations):
    _, steps = perceptron_train(X_bias, y, alpha=1.0)
    steps_list.append(steps)

avg_steps = np.mean(steps_list)
```

Größenordnung für m=10: etwa 5-15 Schritte im Durchschnitt.

### Experimente

Experiment 1: m=10, α=1.0
Durchschnittliche Schritte: 10.52 ± 5.11

Experiment 2: m=100, α=1.0
Durchschnittliche Schritte: 34.37 ± 16.95

Experiment 3: m=100, α=0.1
Durchschnittliche Schritte: 83.75 ± 7.82

Experiment 4: m=1000, α=1.0
Durchschnittliche Schritte: 699.83 ± 326.49

Experiment 5: m=1000, α=0.1
Durchschnittliche Schritte: 3950.32 ± 2239.28

### Beobachtungen

Die Schrittanzahl wächst deutlich mit der Anzahl der Datenpunkte m.

Eine kleinere Lernrate α führt zu deutlich mehr Schritten... ungefähr proportional zum Faktor der Reduktion.

Die Varianz steigt bei größeren m stark an, besonders für α=1.0.