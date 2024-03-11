package yagoo.misc.other.examples;

public class ValueTypesExample {

	public static void main(String[] args) {
	    //
		Byte b1 = 1;
		Character c1 = 65; // 'A'
		Integer i1 = 10000001;
		Long l1 = 300000321L;
		Float f1 = 30.123F;
		Double d1 = 123.321;
		StringBuilder sb1 = new StringBuilder("123");
		StringBuilder sb2 = new StringBuilder("123");
		StringBuilder sb3 = sb2;
		
		System.out.printf("Byte \tb1.toString = %s \tb1.hashCode() = %d%n"
				+ "Character \tc1.toString = %s \tc1.hashCode() = %d\n"
				+ "Integer \ti1.toString = %s \ti1.hashCode() = %d\n"
				+ "Long \tl1.toString = %s \tl1.hashCode() = %d\n"
				+ "Float \tf1.toString = %s \tf1.hashCode() = %d\n"
				+ "Double \td1.toString = %s \td1.hashCode() = %d\n"
				+ "StringBuilder \tsb1 == sb2 = %b \tsb1.equals(sb2) = %b\n"
				+ "StringBuilder \tsb2 == sb3 = %b \tsb2.equals(sb3) = %b\n",
				b1.toString(), b1.hashCode(), c1.toString(), c1.hashCode(),
				i1.toString(), i1.hashCode(), l1.toString(), l1.hashCode(),
				f1.toString(), f1.hashCode(), d1.toString(), d1.hashCode(),
				(sb1 == sb2), sb1.equals(sb2), (sb2 == sb3), sb2.equals(sb3));
		
	}

}
