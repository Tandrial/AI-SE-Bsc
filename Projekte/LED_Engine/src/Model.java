public enum Model {

	CUBE, OBJFILE, MONKEY;

	public Model getNext() {
		return values()[(ordinal() + 1) % values().length];
	}

}
