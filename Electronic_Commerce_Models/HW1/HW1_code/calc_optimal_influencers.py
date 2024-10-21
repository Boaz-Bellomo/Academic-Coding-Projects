import numpy as np
import networkx as nx
import random
import pandas as pd
import csv


NoseBook_path = 'NoseBook_friendships.csv'
cost_path = 'costs.csv'
results_file_path = 'weighted_sixth_degree_neighbors_count.csv'


def create_graph(edges_path: str) -> nx.Graph:
    edges = pd.read_csv(edges_path).to_numpy()
    net = nx.Graph()
    net.add_edges_from(edges)
    return net


def buy_products(net: nx.Graph, purchased: set) -> set:
    new_purchases = set()
    for user in net.nodes:
        neighborhood = set(net.neighbors(user))
        b = len(neighborhood.intersection(purchased))
        n = len(neighborhood)
        prob = b / n
        if prob >= random.uniform(0, 1):
            new_purchases.add(user)
    return new_purchases.union(purchased)


def product_exposure_score(net: nx.Graph, purchased_set: set) -> int:
    exposure = 0
    for user in net.nodes:
        neighborhood = set(net.neighbors(user))
        if user in purchased_set:
            exposure += 1
        elif len(neighborhood.intersection(purchased_set)) != 0:
            b = len(neighborhood.intersection(purchased_set))
            rand = random.uniform(0, 1)
            if rand < 1 / (1 + 10 * np.exp(-b / 2)):
                exposure += 1
    return exposure


def get_influencers_cost(cost_df: pd.DataFrame, influencers: list) -> int:
    return sum([cost_df[cost_df['user'] == influencer]['cost'].item() if influencer in cost_df['user'].values else 0 for influencer in influencers])

def count_weighted_6th_degree_neighbors(G, node, max_depth=6):
    visited = {node}
    current_level = {node}
    total_count = 0
    weight = 1

    for depth in range(max_depth):
        next_level = set()
        for n in current_level:
            neighbors = set(nx.all_neighbors(G, n))
            next_level.update(neighbors)

        next_level -= visited
        if not next_level:
            break

        visited.update(next_level)
        current_level = next_level
        total_count += len(next_level) * weight
        weight = weight * 0.75

    return total_count


def creat_weighted_6th_degree_neighbors_CSV(G):
    # Calculate the weighted 6th degree neighbors count for each node
    weighted_sixth_degree_neighbors = []
    for node in G.nodes():
        count = count_weighted_6th_degree_neighbors(G, node)
        weighted_sixth_degree_neighbors.append((node, count))

    # Save the results to a CSV file
    with open(results_file_path, mode='w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['Node', 'Weighted 6th Degree Neighbors Count'])
        writer.writerows(weighted_sixth_degree_neighbors)

    df = pd.read_csv(results_file_path)

    # Sort the DataFrame by the first column
    df_sorted = df.sort_values(by=df.columns[0])
    df_sorted.to_csv(results_file_path, index=False)

    print("calculated weighted 6th degree neighbors")


def calculate_centrality_indices(G):
    degree_centrality = nx.degree_centrality(G)
    closeness_centrality = nx.closeness_centrality(G)
    betweenness_centrality = nx.betweenness_centrality(G)

    centrality_df = pd.DataFrame({
        'Node': list(G.nodes),
        'degree_centrality': [degree_centrality[node] for node in G.nodes],
        'closeness_centrality': [closeness_centrality[node] for node in G.nodes],
        'betweenness_centrality': [betweenness_centrality[node] for node in G.nodes]
    })
    df_sorted = centrality_df.sort_values(by=centrality_df.columns[0])

    # save to CSV
    df_csv = pd.read_csv(results_file_path)
    df_merged = pd.merge(df_sorted, df_csv, on=df_sorted.columns[0])
    df_merged.to_csv(results_file_path, index=False)

    print("calculated centrality indices")


def normalize_data():
    df = pd.read_csv(results_file_path)

    # Function to normalize a column
    def normalize_column(column):
        min_val = column.min()
        max_val = column.max()
        return (column - min_val) / (max_val - min_val)

    # Normalize all columns except the first one ('Node')
    columns_to_normalize = df.columns[1:]
    for column in columns_to_normalize:
        df[f'{column}_normalized'] = normalize_column(df[column])

    # Calculate the weighted value using the normalized columns
    df['Weighted_Value'] = (0.35 * df['Weighted 6th Degree Neighbors Count_normalized'] +
                            0.25 * df['degree_centrality_normalized'] +
                            0.15 * df['closeness_centrality_normalized'] +
                            0.25 * df['betweenness_centrality_normalized'])

    df.to_csv(results_file_path, index=False)

    print("Normalized data with weighted values")


def get_top_100_relevant_influencers():
    # add the influencers cost
    df_results = pd.read_csv(results_file_path)
    df_results_sorted = df_results.sort_values(by=df_results.columns[0])
    df_costs = pd.read_csv(cost_path)
    df_costs_sorted = df_costs.sort_values(by=df_costs.columns[0])

    df_costs_sorted.rename(columns={'user': 'Node'}, inplace=True)

    df_merged = pd.merge(df_results_sorted, df_costs_sorted, on=df_results_sorted.columns[0])

    df_top_100 = df_merged.sort_values(by='Weighted_Value', ascending=False).head(100)

    # Save the top 100 rows to a new CSV file
    output_path = 'top_100_influencers.csv'
    df_top_100.to_csv(output_path, index=False)

    print("calculated top 100 influencers")
    return output_path


def run_praducci_simulation(influencers, edges_path='NoseBook_friendships.csv', cost_path='costs.csv'):
    NoseBook_network = create_graph(edges_path)
    cost_df = pd.read_csv(cost_path)
    influencers_cost = get_influencers_cost(cost_df, influencers)
    if influencers_cost > 1000:
        return float('-inf')  # Too expensive, invalid combination

    finalScore = 0
    RANGE = 10
    for _ in range(RANGE):
        purchased = set(influencers)
        for _ in range(6):
            purchased = buy_products(NoseBook_network, purchased)
        score = product_exposure_score(NoseBook_network, purchased)
        finalScore += score

    mean_score = finalScore / RANGE
    return mean_score


def get_top_influencer_combintion(top_100_relevant_influencers_CSV_Path):
    # Load node data
    node_data = pd.read_csv(top_100_relevant_influencers_CSV_Path)

    # Convert node ids to integers for simulation
    node_data['Node'] = node_data['Node'].astype(int)

    # Parameters
    max_cost = 1000
    num_simulations = 10

    # Dynamic Programming to find optimal combinations under cost constraint
    def find_optimal_combinations(nodes, costs, max_cost):
        n = len(nodes)
        dp = np.zeros((n + 1, max_cost + 1))
        selected_combinations = [[[] for _ in range(max_cost + 1)] for _ in range(n + 1)]

        for i in range(1, n + 1):
            for j in range(max_cost + 1):
                if costs[i - 1] <= j:
                    include_value = run_praducci_simulation(
                        selected_combinations[i - 1][j - costs[i - 1]] + [nodes[i - 1]])
                    if dp[i - 1][j - costs[i - 1]] + include_value > dp[i - 1][j]:
                        dp[i][j] = dp[i - 1][j - costs[i - 1]] + include_value
                        selected_combinations[i][j] = selected_combinations[i - 1][j - costs[i - 1]] + [nodes[i - 1]]
                    else:
                        dp[i][j] = dp[i - 1][j]
                        selected_combinations[i][j] = selected_combinations[i - 1][j]
                else:
                    dp[i][j] = dp[i - 1][j]
                    selected_combinations[i][j] = selected_combinations[i - 1][j]

        return selected_combinations[n][max_cost], dp[n][max_cost]

    # Get node information
    nodes = node_data['Node'].values
    costs = node_data['cost'].values

    # Find the optimal combinations
    optimal_nodes, optimal_value = find_optimal_combinations(nodes, costs, max_cost)

    print(f"found optimal nodes:{optimal_nodes} and optimal value: {optimal_value}")
    return optimal_nodes, optimal_value


if __name__ == '__main__':
    NoseBook_network = create_graph(NoseBook_path)

    creat_weighted_6th_degree_neighbors_CSV(NoseBook_network)

    calculate_centrality_indices(NoseBook_network)

    normalize_data()

    top_100_relevant_influencers_CSV_Path = get_top_100_relevant_influencers()

    optimal_nodes, optimal_value = get_top_influencer_combintion(top_100_relevant_influencers_CSV_Path)
