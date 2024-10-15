import os
import sys
import argparse
import time
import itertools
import numpy as np
import pandas as pd


class PerceptronClassifier:
    def __init__(self):
        """
        Constructor for the PerceptronClassifier.
        """
        self.ids = (123456789, 987654321)
        # Initialize weights and biases to None
        self.weights = None
        self.biases = None
        # Initialize classes to None
        self.classes = None

    def fit(self, X: np.ndarray, y: np.ndarray):
        """
        This method trains a multiclass perceptron classifier on a given training set X with label set y.
        :param X: A 2-dimensional numpy array of m rows and d columns. It is guaranteed that m >= 1 and d >= 1.
        Array datatype is guaranteed to be np.float32.
        :param y: A 1-dimensional numpy array of m rows. it is guaranteed to match X's rows in length (|m_x| == |m_y|).
        Array datatype is guaranteed to be np.uint8.
        """

        # Identify unique classes in the target labels
        self.classes = np.unique(y)
        # Number of unique classes
        n_classes = len(self.classes)
        # Number of features in the dataset
        n_features = X.shape[1]

        # Initialize the weights and biases
        self.weights = np.zeros((n_classes, n_features), dtype=np.float32)
        self.biases = np.zeros(n_classes, dtype=np.float32)

        # Iterate over the dataset multiple times (fixed number of iterations)
        for _ in range(1000): 
            # Iterate over each sample in the dataset
            for idx, x_i in enumerate(X):
                # Compute the scores for each class
                scores = np.dot(self.weights, x_i) + self.biases
                # Determine the predicted class (class with highest score)
                predicted_class = np.argmax(scores)
                # True class label for the current sample
                true_class = y[idx]

                # If the predicted class does not match the true class, update the weights and biases
                if predicted_class != true_class:
                    self.weights[true_class] += x_i
                    self.biases[true_class] += 1
                    self.weights[predicted_class] -= x_i
                    self.biases[predicted_class] -= 1


    def predict(self, X: np.ndarray) -> np.ndarray:
        """
        This method predicts the y labels of a given dataset X, based on a previous training of the model.
        It is mandatory to call PerceptronClassifier.fit before calling this method.
        :param X: A 2-dimensional numpy array of m rows and d columns. It is guaranteed that m >= 1 and d >= 1.
        Array datatype is guaranteed to be np.float32.
        :return: A 1-dimensional numpy array of m rows. Should be of datatype np.uint8.
        """
        # Compute the scores for each class
        scores = np.dot(X, self.weights.T) + self.biases
        
        # Determine the predicted class for each sample (class with highest score)
        predictions = np.argmax(scores, axis=1)
        
        return predictions.astype(np.uint8)


if __name__ == "__main__":

    print("*" * 20)
    print("Started HW2_ID1_ID2.py")
    # Parsing script arguments
    parser = argparse.ArgumentParser()
    parser.add_argument('csv', type=str, help='Input csv file path')
    args = parser.parse_args()

    print("Processed input arguments:")
    print(f"csv = {args.csv}")

    print("Initiating PerceptronClassifier")
    model = PerceptronClassifier()
    print(f"Student IDs: {model.ids}")
    print(f"Loading data from {args.csv}...")
    data = pd.read_csv(args.csv, header=None)
    print(f"Loaded {data.shape[0]} rows and {data.shape[1]} columns")
    X = data[data.columns[:-1]].values.astype(np.float32)
    y = pd.factorize(data[data.columns[-1]])[0].astype(np.uint8)

    print("Fitting...")
    is_separable = model.fit(X, y)
    print("Done")
    y_pred = model.predict(X)
    print("Done")
    accuracy = np.sum(y_pred == y.ravel()) / y.shape[0]
    print(f"Train accuracy: {accuracy * 100 :.2f}%")

    print("*" * 20)
