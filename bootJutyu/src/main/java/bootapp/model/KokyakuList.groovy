package bootapp.model

class KokyakuList {
	
	List<Kokyaku> list;
	
	public KokyakuList() {
		this.list = new LinkedList<Kokyaku>();
	}
	
	public KokyakuList(List list) {
		this.list = new LinkedList<Kokyaku>(list);
	}
}
