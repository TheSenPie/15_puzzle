public class ArrayTiles extends Tiles {
	private byte[] tiles;
	private int emptyPos;

	public ArrayTiles(String format) throws ConfigurationFormatException,
		InvalidConfigurationException {
			super(format);
			int length = SIZE * SIZE;
			tiles = new byte[length];
			getConfiguration().initialise(this);

			for (int i = 0; i < length; i++)
				if (tiles[i] == EMPTY)
					emptyPos = i;
		}

	public ArrayTiles(Configuration configuration) throws ConfigurationFormatException, InvalidConfigurationException {
		super(configuration);
		tiles = new byte[SIZE * SIZE];
		getConfiguration().initialise(this);

		for (int i = 0; i < SIZE * SIZE; i++)
			if (tiles[i] == EMPTY)
				emptyPos = i;
	}

	public ArrayTiles(ArrayTiles arrayTiles) {
		super(arrayTiles.getConfiguration(), arrayTiles.getMoveCount());

		tiles = new byte[SIZE * SIZE];
		for (int i = 0; i < SIZE * SIZE; i++) {
			tiles[i] = arrayTiles.tiles[i];
		}

		emptyPos = arrayTiles.emptyPos;
	}

	@Override
	public ArrayTiles clone() throws CloneNotSupportedException {
		// ArrayTiles that = null;
		// try{
		//     that = (ArrayTiles) super.clone();
		//     that.b = this.b.clone();
		//     return that;
		// } catch (CloneNotSupportedException ex){
		//     ex.printStackTrace();
		// }
		// return that;
		return new ArrayTiles(this);
	}

	public byte getTile(int col, int row) {
		return getTile(row * SIZE + col);
	}
	private byte getTile(int pos) {
		if (pos < 0 || pos >= SIZE * SIZE) {
			System.out.println("Error: position out of the board");
			System.exit(0);
		}
		return tiles[pos];
	}
	public void setTile(int col, int row, byte value) {
		setTile(row * SIZE + col, value);
	}
	private void setTile(int pos, byte value) {
		tiles[pos] = value;
	}
	protected void moveImpl(Direction direction) {
		int tilePos = 0;
		switch (direction) {
			case UP:
				tilePos = emptyPos + SIZE;
				break;
			case DOWN:
				tilePos = emptyPos - SIZE;
				break;
			case LEFT:
				tilePos = emptyPos + 1;
				break;
			case RIGHT:
				tilePos = emptyPos - 1;
				break;
		}
		setTile(emptyPos, getTile(tilePos));
		setTile(tilePos, EMPTY);
		emptyPos = tilePos;
	}

	public boolean isSolved() {
		int length = SIZE * SIZE;
		if (emptyPos + 1 != length)
			return false;
		for (int i = 0; i + 1 < length; i++)
			if (tiles[i] != i + 1)
				return false;
		return true;
	}

	@Override
	public boolean isSolvable() {
		int invCount = getInvCount(tiles);

		if (SIZE % 2 == 1)
			return (invCount % 2 == 0);
		else {
			int pos = findXPosition(tiles);
			if (pos % 2 == 1)
				return (invCount % 2 == 0);
			else
				return (invCount % 2 == 1);
		}
	}

	public int getInvCount(byte[] tiles) {
		int inv_count = 0;
		for (int i = 0; i < SIZE * SIZE - 1; i++) {
			for (int j = i + 1; j < SIZE * SIZE; j++) {
				if (tiles[i] > tiles[j] && tiles[i] != EMPTY && tiles[j] != EMPTY)
					inv_count++;
			}
		}
		return inv_count;
	}

	public int findXPosition(byte tiles[]) {
		// start from bottom-right corner of matrix
		for (int i = SIZE - 1; i >= 0; i--)
			for (int j = SIZE - 1; j >= 0; j--)
				if (tiles[i * SIZE + j] == EMPTY)
					return SIZE - i;
		return -1;
	}

	@Override
	public int getEmptyPositon() {
		return emptyPos;
	}
}