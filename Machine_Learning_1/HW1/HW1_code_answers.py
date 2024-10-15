import os
import sys
import argparse
import time
import itertools
from typing import List, Union, Any

import numpy as np
import pandas as pd


class KnnClassifier:
    def __init__(self, k: int, p: float):
        """
        Constructor for the KnnClassifier.

        :param k: Number of nearest neighbors to use.
        :param p: p parameter for Minkowski distance calculation.
        """

        # hold relevant data
        self.y_train = None
        self.X_train = None
        self.k = k
        self.p = p
        self.ids = (123456789, 987654321)

    def fit(self, X: np.ndarray, y: np.ndarray) -> None:
        """
        This method trains a k-NN classifier on a given training set X with label set y.

        :param X: A 2-dimensional numpy array of m rows and d columns. It is guaranteed that m >= 1 and d >= 1.
            Array datatype is guaranteed to be np.float32.
        :param y: A 1-dimensional numpy array of m rows. It is guaranteed to match X's rows in length (|m_x| == |m_y|).
            Array datatype is guaranteed to be np.uint8.
        """

        # only save data, all work in predict() func
        self.X_train = X
        self.y_train = y

    def predict(self, X: np.ndarray) -> np.ndarray:
        """
        This method predicts the y labels of a given dataset X, based on a previous training of the model.
        It is mandatory to call KnnClassifier. Fit before calling this method.

        :param X: A 2-dimensional numpy array of m rows and d columns. It is guaranteed that m >= 1 and d >= 1.
            Array datatype is guaranteed to be np.float32.
        :return: A 1-dimensional numpy array of m rows. Should be of datatype np.uint8.
        """
        predictions = []
        for point in X:
            # get the distance of train data from point and other relevant data
            all_dis = self.calculate_distance(point, self.X_train)
            nearest_point_index = np.argsort(all_dis)[:self.k]
            nearest_point_labels = self.y_train[nearest_point_index]
            nearest_points_distances = all_dis[nearest_point_index]

            # identify label by tie breaking roles
            point_label = self.identify_label(nearest_point_labels, nearest_points_distances)

            predictions.append(point_label)

        return np.array(predictions, dtype=np.uint8)

    def calculate_distance(self, target: np.ndarray, X_train: np.ndarray) -> np.ndarray:
        """
         Calculate using Minkowski distance calculation

         :param target: The point to calculate the distance to
         :param X_train: All train points to calculate the distance from

         :return: Array of distances
         """

        return np.sum(np.abs(X_train - target)**self.p, axis=1)**(1/self.p)

    def identify_label(self, nearest_point_labels: np.ndarray, nearest_points_distances: np.ndarray) -> int:
        """
        identify label of nearest points by tie-breaking roles

        :param nearest_point_labels: the labels of the k nearest point
        :param nearest_points_distances: the distance of the k nearest points

        :return: the predicted label
        """

        unique_labels, counts = np.unique(nearest_point_labels, return_counts=True)
        max_count_index = np.argmax(counts)
        candidates = unique_labels[counts == counts[max_count_index]]

        if len(candidates) != 1:
            # Tie-break by distance to the nearest neighbor
            candidate_distances = {label: np.min(nearest_points_distances[nearest_point_labels == label]) for label in candidates}
            min_distance = np.min(list(candidate_distances.values()))
            closest_candidates_label = [label for label, distance in candidate_distances.items() if distance == min_distance]

            if len(closest_candidates_label) != 1:
                # Tie-break by lexicographic order of labels
                return np.min(closest_candidates_label)
            else:
                return closest_candidates_label[0]
        else:
            return candidates[0]


def main():
    print("*" * 20)
    print("Started HW1_318159506_209120195.py")
    # Parsing script arguments
    parser = argparse.ArgumentParser()
    parser.add_argument('csv', type=str, help='Input csv file path')
    parser.add_argument('k', type=int, help='k parameter')
    parser.add_argument('p', type=float, help='p parameter')
    args = parser.parse_args()

    print("Processed input arguments:")
    print(f"csv = {args.csv}, k = {args.k}, p = {args.p}")

    print("Initiating KnnClassifier")
    model = KnnClassifier(k=args.k, p=args.p)
    print(f"Student IDs: {model.ids}")
    print(f"Loading data from {args.csv}...")
    data = pd.read_csv(args.csv, header=None)
    print(f"Loaded {data.shape[0]} rows and {data.shape[1]} columns")
    X = data[data.columns[:-1]].values.astype(np.float32)
    y = pd.factorize(data[data.columns[-1]])[0].astype(np.uint8)

    print("Fitting...")
    model.fit(X, y)
    print("Done")
    print("Predicting...")
    y_pred = model.predict(X)
    print("Done")
    accuracy = np.sum(y_pred == y) / len(y)
    print(f"Train accuracy: {accuracy * 100 :.2f}%")
    print("*" * 20)


if __name__ == "__main__":
    main()
