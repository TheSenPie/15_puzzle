public class MatrixTiles extends Tiles {
	private byte[][] tiles;
	private int emptyCol;
	private int emptyRow;

	public MatrixTiles(String format) throws ConfigurationFormatException,
		InvalidConfigurationException {
			super(format);
			tiles = new byte[SIZE][SIZE];
			getConfiguration().initialise(this);

			for (int i = 0; i < SIZE; i++)
				for (int j = 0; j < SIZE; j++)
					if (tiles[i][j] == EMPTY) {
						emptyCol = j;
						emptyRow = i;
					}
		}

	public MatrixTiles(Configuration configuration) throws ConfigurationFormatException, InvalidConfigurationException {
		super(configuration);
		tiles = new byte[SIZE][SIZE];
		getConfiguration().initialise(this);

		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				if (tiles[i][j] == EMPTY) {
					emptyCol = j;
					emptyRow = i;
				}
	}

	public MatrixTiles(MatrixTiles matrixTiles) {
		super(matrixTiles.getConfiguration(), matrixTiles.getMoveCount());

		tiles = new byte[SIZE][SIZE];

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				tiles[i][j] = matrixTiles.tiles[i][j];
			}
		}

		emptyRow = matrixTiles.emptyRow;
		emptyCol = matrixTiles.emptyCol;
	}

	@Override
	public MatrixTiles clone() throws CloneNotSupportedException {
		// ArrayTiles that = null;
		// try{
		//     that = (ArrayTiles) super.clone();
		//     that.b = this.b.clone();
		//     return that;
		// } catch (CloneNotSupportedException ex){
		//     ex.printStackTrace();
		// }
		// return that;
		return new MatrixTiles(this);
	}

	public byte getTile(int col, int row) {
		if (row < 0 || row >= SIZE ||
			col < 0 || col >= SIZE) {
			System.out.println("Error: position out of the board");
			System.exit(0);
		}
		return tiles[row][col];
	}
	public void setTile(int col, int row, byte value) {
		if (row < 0 || row >= SIZE ||
			col < 0 || col >= SIZE) {
			System.out.println("Error: position out of the board");
			System.exit(0);
		}
		tiles[row][col] = value;
	}
	protected void moveImpl(Direction direction) {
		int tileCol = 0, tileRow = 0;
		switch (direction) {
			case UP:
				tileCol = emptyCol;
				tileRow = emptyRow + 1;
				break;
			case DOWN:
				tileCol = emptyCol;
				tileRow = emptyRow - 1;
				break;
			case LEFT:
				tileCol = emptyCol + 1;
				tileRow = emptyRow;
				break;
			case RIGHT:
				tileCol = emptyCol - 1;
				tileRow = emptyRow;
				break;
		}
		setTile(emptyCol, emptyRow, getTile(tileCol, tileRow));
		setTile(tileCol, tileRow, EMPTY);
		emptyCol = tileCol;
		emptyRow = tileRow;
	}
	public boolean isSolved() {
		if (emptyCol + 1 != SIZE || emptyRow + 1 != SIZE)
			return false;
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				if ((i + 1 < SIZE || j + 1 < SIZE) &&
					tiles[i][j] != i * SIZE + j + 1)
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

	public int getInvCount(byte[][] tiles) {
		int inv_count = 0;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				for (int row = i; row < SIZE; row++) {
					for (int col = i == row ? j : 0; col < SIZE; col++) {
						if (tiles[i][j] > tiles[row][col] && tiles[row][col] != EMPTY && tiles[row][col] != EMPTY)
							inv_count++;
					}
				}
			}
		}
		return inv_count;
	}

	public int findXPosition(byte tiles[][]) {
		for (int i = Tiles.SIZE - 1; i >= 0; i--)
			for (int j = Tiles.SIZE - 1; j >= 0; j--)
				if (tiles[i][j] == 0)
					return Tiles.SIZE - i;
		return -1;
	}

	@Override
	public int getEmptyPositon() {
		return emptyRow * 4 + emptyCol;
	}
}