package yagoo.misc.code.chars;

import java.nio.charset.Charset;

public class CharsMain {

	public static void main(String[] args) {
		//
		int ch = 165536;
		
		System.out.println(Charset.defaultCharset());
		
		char[] chars = Character.toChars(ch);
		String str = new String(chars);
		System.out.printf("char[] = %d\n", chars.length);
		System.out.printf("String = %s\n", str);
		System.out.printf("String.length = %d\n", str.length());
		System.out.printf("String.codePointCount = %d\n", str.codePointCount(0, str.length()));
		System.out.printf("String.codePointAt = %d\n", str.codePointAt(0));
	}
	
}
