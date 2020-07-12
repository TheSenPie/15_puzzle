import java.util.*;
import java.io.*;
import java.lang.reflect.*;

public class NPuzzle {
	private Tiles tiles;
	private ConfigurationStore store;
	private ArrayList < Tiles > cachedTiles = new ArrayList < > ();

	public NPuzzle(Tiles t) {
		tiles = t;
	}

	public NPuzzle(ConfigurationStore configs) {
		store = configs;
	}

	public void play() throws IOException, ConfigurationFormatException,
		InvalidConfigurationException {
			String response = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Please select a configuration to play (l to list):");
			while (!response.equals("q")) {
				response = in .readLine();
				System.out.println(response);
				if (response.equals("LEFT") || response.equals("RIGHT") ||
					response.equals("UP") || response.equals("DOWN")) {
					if (tiles == null) {
						System.out.println("Please select a configuration to play (l to list):");
					} else {
						switch (Tiles.Direction.valueOf(response)) {
							case UP:
								if (tiles.getEmptyPositon() / Tiles.SIZE >= Tiles.SIZE - 1) {
									System.out.println("Error: Illegal move");
									print();
									continue;
								}
								break;
							case DOWN:
								if (tiles.getEmptyPositon() / Tiles.SIZE <= 0) {
									System.out.println("Error: Illegal move");
									print();
									continue;
								}
								break;
							case LEFT:
								if ((tiles.getEmptyPositon() + 1) % Tiles.SIZE == 0) {
									System.out.println("Error: Illegal move");
									print();
									continue;
								}
								break;
							case RIGHT:
								if ((tiles.getEmptyPositon()) % Tiles.SIZE == 0) {
									System.out.println("Error: Illegal move");
									print();
									continue;
								}
								break;
						}
						try {
							cachedTiles.set(tiles.getMoveCount() + 1, copyTiles(true));
							cachedTiles.subList(tiles.getMoveCount() + 2, cachedTiles.size()).clear();
						} catch (Exception e) {
							cachedTiles.add(tiles.getMoveCount() + 1, copyTiles(true));
						}
						tiles = cachedTiles.get(tiles.getMoveCount() + 1);
						tiles.move(Tiles.Direction.valueOf(response));
						print();
						if (!tiles.isSolved()) {
							System.out.println("Please make a move by inputting UP, DOWN, LEFT, RIGHT;");
							System.out.println("or stop the game by inputting q: ");
						} else {
							System.out.println("You solved the puzzle!");
							tiles = null;
							System.out.println("Please select a configuration to play (l to list):");
						}
					}
				} else if (response.equals("l")) {
					ArrayList < Configuration > configs = store.getConfigurationsSorted();
					int i = 0;
					for (Configuration c: configs) {
						System.out.println(i + " (" + c.getData() + ")");
						i++;
					}
				} else if (response.startsWith("c")) {
					ArrayList < Configuration > configs = store.getConfigurationsSorted();
					int index = Integer.parseInt(response.split("\\W")[1]);

					cachedTiles.clear();
					cachedTiles.add(new MatrixTiles(configs.get(index)));
					tiles = cachedTiles.get(0);

					print();
					if (!tiles.isSolvable()) {
						System.out.println("The game is not solvable. Quitting.");
						System.exit(0);
					}
					if (!tiles.isSolved()) {
						System.out.println("Please make a move by inputting UP, DOWN, LEFT, RIGHT;");
						System.out.println("or stop the game by inputting q: ");
					} else {
						System.out.println("You solved the puzzle!");
						tiles = null;
						System.out.println("Please select a configuration to play (l to list):");
					}
				} else if (response.equals("b")) {
					if (tiles.getMoveCount() > 0) {
						this.tiles = cachedTiles.get(tiles.getMoveCount() - 1);
					}
					print();
				} else if (response.equals("r")) {
					if (tiles.getMoveCount() < cachedTiles.size() - 1) {
						this.tiles = cachedTiles.get(tiles.getMoveCount() + 1);
					}
					print();
				}
			}
		}

	private Tiles copyTiles(boolean useCloning) {
		if (useCloning) {
			try {
				Tiles copy = (Tiles) tiles.clone();
				return copy;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				Constructor < ? > ctor = tiles.getClass().getDeclaredConstructor(tiles.getClass());
				Tiles copy = (Tiles) ctor.newInstance(tiles);
				return copy;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void print() {
		System.out.println("- " + tiles.getMoveCount() + " moves");
		for (int i = 0; i < Tiles.SIZE; i++) {
			for (int j = 0; j < Tiles.SIZE; j++)
				System.out.print("-----");
			System.out.println("-");
			for (int j = 0; j < Tiles.SIZE; j++)
				if (tiles.getTile(j, i) == Tiles.EMPTY)
					System.out.printf("|    ");
				else
					System.out.printf("| %2d ", tiles.getTile(j, i));
			System.out.println("|");
		}
		for (int j = 0; j < Tiles.SIZE; j++)
			System.out.print("-----");
		System.out.println("-");
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java NPuzzle <path/url to store>");
			return;
		}

		try {
			ConfigurationStore cs = new ConfigurationStore(args[0]);
			NPuzzle np = new NPuzzle(cs);
			np.play();
		} catch (IOException ioe) {
			System.out.println("Failed to load configuration store");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}