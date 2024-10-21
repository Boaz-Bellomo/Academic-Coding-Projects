import numpy as np
from copy import deepcopy

class Recommender:
    def __init__(self, L, S, p):
        self.L = deepcopy(L)
        self.S = deepcopy(S)
        self.p = deepcopy(p)
        self.current_belief = deepcopy(p)
        self.num_genres = L.shape[0]
        self.num_user_types = L.shape[1]
        self.t = 0
        self.last_recommendation = None
        self.dominant_genres = self.identify_dominant_genres()
        self.dominant_user_types = self.identify_dominant_user_types()
        self.low_stay_probability = 0.1
        self.is_low_stay_probability = np.mean(S) < self.low_stay_probability
        self.recommendation_counts = np.zeros(self.num_genres)
        self.like_counts = np.zeros(self.num_genres)
        self.scores = self.calculate_scores()

    def identify_dominant_genres(self):
        genre_scores = np.mean(self.L, axis=1)
        return np.argsort(genre_scores)[-3:]  # Top 3 genres

    def identify_dominant_user_types(self):
        user_scores = np.mean(self.L, axis=0) + np.mean(self.S, axis=0)
        return np.argsort(user_scores)[-3:]  # Top 3 user types

    def calculate_scores(self):
        L_means = np.mean(self.L, axis=1)
        L_stds = np.std(self.L, axis=1)
        S_means = np.mean(self.S, axis=1)
        S_stds = np.std(self.S, axis=1)

        q_user = np.dot(self.L, self.current_belief)
        p_stay = np.dot(self.S, self.current_belief)

        scores = []
        for i in range(self.num_genres):
            like_score = (q_user[i] - L_means[i]) / (L_stds[i] + 1e-9)
            stay_score = (p_stay[i] - S_means[i]) / (S_stds[i] + 1e-9)
            cumulative_utility_score = (self.like_counts[i] - self.recommendation_counts[i]) / (self.recommendation_counts[i] + 1e-9)
            score = like_score + stay_score + cumulative_utility_score
            scores.append(score)
        return np.array(scores)

    def recommend(self):
        if np.max(self.current_belief) > 0.6:
            likely_user_type = np.argmax(self.current_belief)
            if likely_user_type in self.dominant_user_types:
                return self.recommend_for_dominant_user()

        expected_rewards = np.zeros(self.num_genres)
        for i in range(self.num_genres):
            p_like = np.dot(self.L[i], self.current_belief)
            p_stay = np.dot(self.S[i], self.current_belief)
            immediate_reward = p_like

            if self.is_low_stay_probability:
                future_reward = (1 - p_like) * p_stay * 0.5
            else:
                future_reward = (1 - p_like) * p_stay * self.num_genres / (self.num_genres - 1)

            if i in self.dominant_genres:
                expected_rewards[i] = (immediate_reward + future_reward) * 1.1
            else:
                expected_rewards[i] = immediate_reward + future_reward

        combined_scores = 0.7 * expected_rewards + 0.3 * self.scores
        self.last_recommendation = np.argmax(combined_scores)
        self.recommendation_counts[self.last_recommendation] += 1
        return self.last_recommendation

    def recommend_for_dominant_user(self):
        user_type = np.argmax(self.current_belief)
        genre_scores = self.L[:, user_type] * (1 + self.S[:, user_type])
        return np.argmax(genre_scores)

    def update(self, signal):
        if self.last_recommendation is None:
            return

        learning_rate = max(0.1, min(1.2, 2 / (self.t + 1)))

        likelihood = self.L[self.last_recommendation] if signal else (1 - self.L[self.last_recommendation])
        self.current_belief *= likelihood ** learning_rate
        self.current_belief /= np.sum(self.current_belief)

        if not signal:
            self.current_belief *= self.S[self.last_recommendation] ** learning_rate
            self.current_belief /= np.sum(self.current_belief)

        if signal:
            self.like_counts[self.last_recommendation] += 1

        self.t += 1

        # Adaptive belief reset
        if self.t % 5 == 0:
            confidence = min(0.8, self.t / 15)
            self.current_belief = confidence * self.current_belief + (1 - confidence) * self.p

        # Periodically update dominant genres and user types
        if self.t % 10 == 0:
            self.dominant_genres = self.identify_dominant_genres()
            self.dominant_user_types = self.identify_dominant_user_types()
            self.is_low_stay_probability = np.mean(self.S) < self.low_stay_probability

        # Update scores
        self.scores = self.calculate_scores()




# an example of a recommender that always recommends the item with the highest probability of being liked
class tRecommender:
    def __init__(self, L, S, p):
        self.L = L
        self.S = S
        self.p = p

    def recommend(self):
        return np.argmax(np.dot(self.L, self.p))

    def update(self, signal):
        pass


