import sys
from genetic_algorithm import GeneticAlgorithm

# Parse command-line arguments for city coordinates and best tour
city_coords = eval(sys.argv[1])  # Convert string representation of dictionary to dictionary
best_tour = eval(sys.argv[2])    # Convert string representation of list to list

# Create an instance of GeneticAlgorithm
ga = GeneticAlgorithm()

# Set city coordinates and best tour
ga.set_city_coordinates(city_coords)
ga.set_best_tour(best_tour)

# Run the Genetic Algorithm
ga.run()

# Get the final best tour and its length
final_tour = ga.get_best_tour()
tour_length = ga.get_tour_length(final_tour)

# Print the final best tour and its length
print("Final Best Tour: ", final_tour)
print("Tour Length: ", tour_length)

# Call the plot_cities_tour function with city coordinates and best tour
ga.plot_cities_tour(city_coords, final_tour)
