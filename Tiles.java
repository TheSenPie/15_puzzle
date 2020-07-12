public abstract class Tiles implements Cloneable {
	public enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	};

	public static final int SIZE = 4;
	public static final byte EMPTY = 0;
	private int moves;
	private Configuration configuration;

	public Tiles(String format) throws ConfigurationFormatException {
		moves = 0;
		configuration = new Configuration(format);
	}
	public Tiles(Configuration configuration) {
		moves = 0;
		try {
			this.configuration = new Configuration(configuration.getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Tiles(Configuration configuration, int moves) {
		this(configuration);
		this.moves = moves;
	}

	@Override
	abstract public Object clone() throws CloneNotSupportedException;

	public int getMoveCount() {
		return moves;
	}
	public void incrementMoveCount() {
		++moves;
	}
	public Configuration getConfiguration() {
		return configuration;
	}
	public void move(Direction direction) {
		incrementMoveCount();
		moveImpl(direction);
	}
	public void ensureValidity() throws InvalidConfigurationException {
		boolean[] found = new boolean[SIZE * SIZE];
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++) {
				byte value = getTile(j, i);
				if (value != EMPTY) {
					if (value < 1 || value >= SIZE * SIZE)
						throw new InvalidConfigurationException("Invalid configuration: incorrect tile value " + value + ".");
					else if (found[value])
						throw new InvalidConfigurationException("Invalid configuration: multiple tiles with the value" + value + ".");
					else
						found[value] = true;
				} else if (found[value])
					throw new InvalidConfigurationException("Invalid configuration: multiple empty spaces.");
				else
					found[value] = true;
			}
	}
	public abstract boolean isSolvable();
	protected abstract void moveImpl(Direction direction);
	public abstract byte getTile(int col, int row);
	public abstract void setTile(int col, int row, byte value);
	public abstract boolean isSolved();
	public abstract int getEmptyPositon();
}