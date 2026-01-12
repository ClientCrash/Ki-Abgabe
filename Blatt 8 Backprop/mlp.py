import numpy as np
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt

# Aktivierungsfunktionen
def sigmoid(z):
    z = np.clip(z, -500, 500)
    return 1 / (1 + np.exp(-z))

def relu(z):
    return np.maximum(0, z)

def relu_derivative(z):
    return (z > 0).astype(float)

# Cross-Entropy Loss
def cross_entropy_loss(y_pred, y_true):
    epsilon = 1e-15
    y_pred = np.clip(y_pred, epsilon, 1 - epsilon)
    return -np.mean(y_true * np.log(y_pred) + (1 - y_true) * np.log(1 - y_pred))


class MLP:
    def __init__(self, layer_sizes, learning_rate=0.01, seed=42):
        self.layer_sizes = layer_sizes
        self.learning_rate = learning_rate
        self.weights = []
        self.biases = []

        np.random.seed(seed)
        for i in range(len(layer_sizes) - 1):
            limit = np.sqrt(6 / (layer_sizes[i] + layer_sizes[i+1]))
            w = np.random.uniform(-limit, limit, (layer_sizes[i], layer_sizes[i+1]))
            b = np.zeros((1, layer_sizes[i+1]))
            self.weights.append(w)
            self.biases.append(b)

    def forward(self, X):
        self.activations = [X]
        self.z_values = []

        a = X
        for i in range(len(self.weights)):
            z = np.dot(a, self.weights[i]) + self.biases[i]
            z = np.clip(z, -100, 100)  # Prevent overflow
            self.z_values.append(z)

            if i == len(self.weights) - 1:
                a = sigmoid(z)
            else:
                a = relu(z)

            self.activations.append(a)

        return a

    def backward(self, y_true):
        m = y_true.shape[0]
        delta = self.activations[-1] - y_true

        for i in range(len(self.weights) - 1, -1, -1):
            dW = np.dot(self.activations[i].T, delta) / m
            db = np.sum(delta, axis=0, keepdims=True) / m

            # Gradient clipping
            dW = np.clip(dW, -1, 1)
            db = np.clip(db, -1, 1)

            self.weights[i] -= self.learning_rate * dW
            self.biases[i] -= self.learning_rate * db

            if i > 0:
                delta = np.dot(delta, self.weights[i].T) * relu_derivative(self.z_values[i-1])
                delta = np.clip(delta, -100, 100)

    def train(self, X, y, epochs=1000, batch_size=16):
        losses = []
        m = X.shape[0]

        for epoch in range(epochs):
            indices = np.random.permutation(m)
            X_shuffled = X[indices]
            y_shuffled = y[indices]

            for start in range(0, m, batch_size):
                end = min(start + batch_size, m)
                X_batch = X_shuffled[start:end]
                y_batch = y_shuffled[start:end]

                self.forward(X_batch)
                self.backward(y_batch)

            y_pred = self.forward(X)
            loss = cross_entropy_loss(y_pred, y)
            losses.append(loss)

            if epoch % 1000 == 0:
                acc = np.mean(np.argmax(y_pred, axis=1) == np.argmax(y, axis=1))
                print(f"Epoche {epoch}: Loss = {loss:.6f}, Accuracy = {acc:.4f}")

        return losses


def load_iris():
    url = "https://raw.githubusercontent.com/aimacode/aima-data/master/iris.csv"

    try:
        import pandas as pd
        df = pd.read_csv(url)
        X = df.iloc[:, :-1].values.astype(float)
        labels = df.iloc[:, -1].values
        unique_labels = np.unique(labels)
        y = np.zeros((len(labels), len(unique_labels)))
        for i, label in enumerate(labels):
            y[i, np.where(unique_labels == label)[0][0]] = 1
    except:
        from sklearn.datasets import load_iris
        iris = load_iris()
        X = iris.data.astype(float)
        y_raw = iris.target
        y = np.zeros((y_raw.shape[0], 3))
        for i, label in enumerate(y_raw):
            y[i, label] = 1

    return X, y


def normalize(X):
    mean = X.mean(axis=0)
    std = X.std(axis=0)
    std[std == 0] = 1
    return (X - mean) / std


if __name__ == "__main__":
    X, y = load_iris()
    X = normalize(X)

    print(f"Datensatz: {X.shape[0]} Samples, {X.shape[1]} Features, {y.shape[1]} Klassen\n")

    # Experiment 1: [4, 16, 3]
    print("=== Experiment 1: Architektur [4, 16, 3] ===")
    mlp1 = MLP([4, 16, 3], learning_rate=0.1, seed=42)
    losses1 = mlp1.train(X, y, epochs=5000, batch_size=16)
    y_pred = mlp1.forward(X)
    acc1 = np.mean(np.argmax(y_pred, axis=1) == np.argmax(y, axis=1))
    print(f"Finale Genauigkeit: {acc1 * 100:.2f}%, Loss: {losses1[-1]:.6f}\n")

    # Experiment 2: [4, 32, 16, 3]
    print("=== Experiment 2: Architektur [4, 32, 16, 3] ===")
    mlp2 = MLP([4, 32, 16, 3], learning_rate=0.1, seed=123)
    losses2 = mlp2.train(X, y, epochs=5000, batch_size=16)
    y_pred = mlp2.forward(X)
    acc2 = np.mean(np.argmax(y_pred, axis=1) == np.argmax(y, axis=1))
    print(f"Finale Genauigkeit: {acc2 * 100:.2f}%, Loss: {losses2[-1]:.6f}\n")

    # Plot
    plt.figure(figsize=(12, 5))

    plt.subplot(1, 2, 1)
    plt.plot(losses1)
    plt.xlabel('Epoche')
    plt.ylabel('Cross-Entropy Loss')
    plt.title('Architektur [4, 16, 3]')
    plt.grid(True)

    plt.subplot(1, 2, 2)
    plt.plot(losses2)
    plt.xlabel('Epoche')
    plt.ylabel('Cross-Entropy Loss')
    plt.title('Architektur [4, 32, 16, 3]')
    plt.grid(True)

    plt.tight_layout()
    plt.savefig('training_loss.png', dpi=150)
    print("Plot gespeichert als training_loss.png")

    print("\n=== Zusammenfassung ===")
    print(f"[4, 16, 3]:      Accuracy = {acc1*100:.2f}%, Loss = {losses1[-1]:.6f}")
    print(f"[4, 32, 16, 3]:  Accuracy = {acc2*100:.2f}%, Loss = {losses2[-1]:.6f}")
