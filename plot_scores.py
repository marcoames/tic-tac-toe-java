import matplotlib.pyplot as plt

with open("best_scores.txt", "r") as file:
    best_scores = [float(line.strip()) for line in file]

generations = list(range(1, len(best_scores) + 1))

plt.figure(figsize=(10, 6))
plt.plot(generations, best_scores, color="blue", linestyle="-", linewidth=2)
plt.title("Best Scores by Generation")
plt.xlabel("Generation")
plt.ylabel("Best Score")
plt.grid(True)


plt.savefig("best_scores_plot.png", format="png")  # Saves as a PNG file
