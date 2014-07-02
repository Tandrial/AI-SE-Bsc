public enum Model {

	CUBE, OBJFILE, PYRAMID;

	public Model getNext() {
		return values()[(ordinal() + 1) % values().length];
	}

}
