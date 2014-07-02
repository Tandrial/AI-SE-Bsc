public enum RenderMode {

	POINTS, LINES, FACES;

	public RenderMode getNext() {
		return values()[(ordinal() + 1) % values().length];
	}
}
