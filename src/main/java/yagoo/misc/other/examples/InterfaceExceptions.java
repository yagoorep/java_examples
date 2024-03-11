package yagoo.misc.other.examples;

import java.io.FileNotFoundException;
import java.io.IOException;

// Interface with method IOException
interface IWithIO {
	void meth() throws IOException; 
}

// Interface with method FileNotFoundException
interface IWithFNF {
	void meth() throws FileNotFoundException;
}

// Class implementation
public class InterfaceExceptions implements IWithIO, IWithFNF {

	public static void main(String[] args) {
		try {
			new InterfaceExceptions().meth();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void meth() throws FileNotFoundException {
		System.out.println("Ololololo");
		throw (FileNotFoundException) new IOException();
	}
	
}
