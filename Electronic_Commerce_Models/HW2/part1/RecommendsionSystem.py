import numpy as np
import pandas as pd
from scipy.sparse import csr_matrix, vstack, eye
from scipy.sparse.linalg import lsqr
from scipy.sparse.linalg import svds


class RecommendsionSystem:
    def __init__(self, training_file, testing_file):
        self.train_df = pd.read_csv(training_file)
        self.test_df = pd.read_csv(testing_file)
        self.global_average = self.train_df['weight'].mean()
        self.initialize_mappings()

    def initialize_mappings(self):
        self.distinct_users = self.train_df['user_id'].unique()
        self.distinct_items = self.train_df['clip_id'].unique()
        self.user_count = len(self.distinct_users)
        self.item_count = len(self.distinct_items)
        self.user_index_map = {user_id: idx for idx, user_id in enumerate(self.distinct_users)}
        self.item_index_map = {item_id: idx for idx, item_id in enumerate(self.distinct_items)}

    def f1_model(self, regularization=0.1):
        user_idx = self.train_df['user_id'].map(self.user_index_map).values
        item_idx = self.train_df['clip_id'].map(self.item_index_map).values
        observed_weights = self.train_df['weight'].values

        coefficient_matrix = self.construct_sparse_matrix(user_idx, item_idx)
        target_vector = observed_weights - self.global_average
        regularization_matrix = regularization * eye(self.user_count + self.item_count, format='csr')
        coefficient_matrix = vstack([coefficient_matrix, regularization_matrix])
        target_vector = np.concatenate([target_vector, np.zeros(self.user_count + self.item_count)])

        solution = lsqr(coefficient_matrix, target_vector)
        self.user_biases = solution[0][:self.user_count]
        self.item_biases = solution[0][self.user_count:self.user_count + self.item_count]

    def construct_sparse_matrix(self, user_idx, item_idx):
        sample_count = len(user_idx)
        ones = np.ones(sample_count)
        row_idx = np.arange(sample_count)
        user_matrix = csr_matrix((ones, (row_idx, user_idx)), shape=(sample_count, self.user_count + self.item_count))
        item_matrix = csr_matrix((ones, (row_idx, item_idx + self.user_count)),
                                 shape=(sample_count, self.user_count + self.item_count))
        return user_matrix + item_matrix

    def predict_f1(self, user_id, clip_id):
        user_idx = self.user_index_map.get(user_id, -1)
        item_idx = self.item_index_map.get(clip_id, -1)

        user_bias = self.user_biases[user_idx] if user_idx != -1 else 0
        item_bias = self.item_biases[item_idx] if item_idx != -1 else 0

        predicted_score = self.global_average + user_bias + item_bias
        return max(predicted_score, 0)

    def f2_model(self, factors=20):
        pivot_table = self.train_df.pivot(index='user_id', columns='clip_id', values='weight')
        pivot_table.fillna(0, inplace=True)
        self.user_indices = pivot_table.index
        self.item_indices = pivot_table.columns
        rating_matrix = pivot_table.to_numpy()

        U, sigma, Vt = svds(rating_matrix, k=factors)
        sigma_diag = np.diag(sigma)
        self.reconstructed_matrix = np.dot(np.dot(U, sigma_diag), Vt)
        self.reconstructed_df = pd.DataFrame(self.reconstructed_matrix, index=self.user_indices, columns=self.item_indices)

    def predict_f2(self, user_id, clip_id):
        predicted_value = self.reconstructed_df.loc[user_id, clip_id]
        return max(predicted_value, 0)

    def calculate_f1_error(self, regularization=0.1):
        sum_squared_error = 0
        user_regularization = np.sum(self.user_biases ** 2)
        item_regularization = np.sum(self.item_biases ** 2)

        for row in self.train_df.itertuples():
            prediction = self.predict_f1(row.user_id, row.clip_id)
            sum_squared_error += (row.weight - prediction) ** 2

        return sum_squared_error + regularization * (user_regularization + item_regularization)

    def calculate_f2_error(self):
        sum_squared_error = 0
        for row in self.train_df.itertuples():
            prediction = self.predict_f2(row.user_id, row.clip_id)
            sum_squared_error += (row.weight - prediction) ** 2
        return sum_squared_error

    def generate_predictions(self, model_type='f1'):
        predict_function = self.predict_f1 if model_type == 'f1' else self.predict_f2
        self.test_df['weight'] = self.test_df.apply(
            lambda row: predict_function(row['user_id'], row['clip_id']), axis=1)

    def export_predictions(self, output_filename):
        result_data = self.test_df[['user_id', 'clip_id', 'weight']]
        result_data.to_csv(output_filename, index=False)


if __name__ == "__main__":
    recommender = RecommendsionSystem('user_clip.csv', 'test.csv')

    # F1 model
    recommender.f1_model()
    recommender.generate_predictions(model_type='f1')
    f1_error = recommender.calculate_f1_error()
    print(f"F1 model error: {f1_error}")
    recommender.export_predictions('318651734_209120195_task1.csv')

    # F2 model
    recommender.f2_model()
    recommender.generate_predictions(model_type='f2')
    f2_error = recommender.calculate_f2_error()
    print(f"F2 model error: {f2_error}")
    recommender.export_predictions('318651734_209120195_task2.csv')