package yagoo.algostruct.javaclasses;

class ClassesMain {
	// class fields
	public static String psfield1 = "public static";
	private static String ptfield1 = "private static";
	public String pufield1 = "public";
	protected String prfield1 = "protected";
	String pafield1 = "package";
	private String pvfield1 = "private";

	// Inner interface only static
	@FunctionalInterface
	public static interface InnerInterface {
		public void out();
	}
	
	// public or protected or private static nested class
	// static class can't access non-static fields and methods
	static class StaticNested {
		public String pufield2 = "public";
		protected String prfield2 = "protected";
		String pafield2 = "package";
		private String pvfield2 = "private";
		
		public String toString() {
			String result = String.format("[ pufield2: %s | prfield2: %s | pafield2: %s | pvfield2: %s ]", pufield2, prfield2, pafield2, pvfield2);
			InnerInterface ii = () -> System.out.println(result);
			InnerInterface i2 = System.out::println;
			ii.out();
			i2.out();
			return result;
		}
	}
	
	// public or protected or private inner class
	// can't have static fields and methods or enums
	class InnerClass {
		public String pufield3 = "public";
		protected String prfield3 = "protected";
		String pafield3 = "package";
		private String pvfield3 = "private";
		
		public String toString() {
			return String.format("[ pufield3: %s | prfield3: %s | pafield3: %s | pvfield3: %s ]", pufield3, prfield3, pafield3, pvfield3);
		}
	}
	
	public static void main(String[] args) {
		final String local = "local var";
		class Member {
			public String pufield = "public";
			protected String prfield = "protected";
			String pafield = "package";
			private String pvfield = "private";
			
			public String toString() {
				psfield1 = "ClassesMain: public static";
				ptfield1 = "ClassesMain: private static";
				String readLocal = local;
				ClassesMain cm = new ClassesMain();
				cm.pvfield1 = "ClassesMain: private object";
				return String.format("[ pufield: %s | prfield: %s | pafield: %s | pvfield: %s ]%n[ readLocal-final %s ]", pufield, prfield, pafield, pvfield, readLocal);
			}
		}
		
		Member m = new Member();
		System.out.println(m.toString());
		System.out.println("------------------------------");
		new StaticNested().toString();
	}
	
	public String toString() {
		return String.format("[ pufield1: %s | prfield1: %s | pafield1: %s | pvfield1: %s | ptfield1: %s]", pufield1, prfield1, pafield1, pvfield1, ptfield1);
	}

}
