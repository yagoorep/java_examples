package yagoo.patterns.creational.builder;

import java.util.HashMap;
import java.util.Map.Entry;

public class BuilderMain {
	
	static class Solder {
		
		private HashMap<String, Integer> ammunation = new HashMap<>();
		private String name;
		
		private Solder(String name) {
			this.name = name;
		}
		
		public void setAmmunation(String name, Integer amt) {
			this.ammunation.put(name, amt);
		}
		
		@Override
		public String toString() {
			StringBuilder result = new StringBuilder("Name: ").append(name).append("\n");
			for (Entry<String, Integer> item : ammunation.entrySet()) {
				result.append(item.getKey())
					.append(" = ")
					.append(item.getValue())
					.append("\n");
			}
			return result.toString();
		}
		
	}
	
	static class SolderBuilder {
		
		private Solder solder;
		
		public SolderBuilder(String name) {
			this.solder = new Solder(name);
		}
		
		public void setGun(String name, int amt) {
			solder.setAmmunation(name, amt);
		}
		
		public void setPistol(String name, int amt) {
			solder.setAmmunation(name, amt);
		}
		
		public Solder get() {
			return solder;
		}
		
	}

	public static void main(String[] args) {
		
		SolderBuilder sldb = new SolderBuilder("Jane");
		sldb.setPistol("desert Eagle", 28);
		Solder solder = sldb.get();
		System.out.println(solder);
		for (int n = 0; n < 30; n++) {
			System.out.println(String.format("data/%1$03d.jpg", n));
		}

	}
	
}
