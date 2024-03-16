package yagoo.misc.other.examples;

import java.util.stream.Stream;

public class VariableParamsMeth {
	
	@SuppressWarnings("removal")
    public static void main(String[] args) {
		Double d = (double) 2;
		Integer i = (int) 2.0;
		Long l = 12L;
		System.out.printf("String [ %s ] hashcode [ %d ]\n", d.toString(), d.hashCode());
		System.out.printf("String [ %s ] hashcode [ %d ]\n", i.toString(), i.hashCode());
		System.out.printf("String [ %s ] hashcode [ %d ]\n", l.toString(), l.hashCode());
		
		Integer i21 = new Integer(4321);
		Integer i22 = new Integer(4321);
		System.out.printf("i21 == i22 is [%b] , i21.hashCode [%d] , i22.hashCode [%d]\n",  (i21 == i22), i21.hashCode(), i22.hashCode());
		
		String str = "string";
		String[] msgs = new String[] {"q", "w"};		
		System.out.printf("String [ %s ] hashcode [ %d ]\n", str.toString(), str.hashCode());
		System.out.printf("String [ %s ] hashcode [ %d ]\n", msgs.toString(), msgs.hashCode());
		
		prn(Stream.of(new String[]{str}, msgs).toArray(String[]::new));
		prn(str, msgs);
	}
	
	public static void prn(String a, String... strings) {
		System.out.println("Method 1");
	}
	
	public static void prn(String... strings) {
		System.out.println("Method 2");
		Stream.of(strings).forEach(System.out::println);
	}
	
}
