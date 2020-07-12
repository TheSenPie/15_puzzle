public class Configuration implements Comparable < Configuration > {
	private String data;

	@Override
	public int compareTo(Configuration o) {
		return this.getData().compareTo(o.getData());
	}

	public Configuration(String format) throws ConfigurationFormatException {
		if (format.equals(""))
			throw new ConfigurationFormatException("Please specify a configuration.");
		data = format;
	}

	public String getData() {
		return data;
	}

	public void initialise(Tiles tiles) throws ConfigurationFormatException,
	InvalidConfigurationException {
		String[] rows = data.split(": ");
		if (rows.length != Tiles.SIZE)
			throw new ConfigurationFormatException("Invalid configuration format: Incorrect number of rows in configuration (found " + rows.length + ").");
		for (int i = 0; i < Tiles.SIZE; i++) {
			String[] values = rows[i].split(" ");
			if (values.length != Tiles.SIZE)
				throw new ConfigurationFormatException("Invalid configuration format: Incorrect number of columns in configuration (found " + values.length + ").");
			try {
				for (int j = 0; j < Tiles.SIZE; j++)
					tiles.setTile(j, i, Byte.parseByte(values[j]));
			} catch (NumberFormatException e) {
				throw new ConfigurationFormatException("Invalid configuration format: Malformed configuration '" + data + "'.");
			}
		}

		tiles.ensureValidity();
	}

	public String toString() {
		return data;
	}
}