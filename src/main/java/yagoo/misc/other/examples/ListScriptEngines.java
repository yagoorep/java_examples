package yagoo.misc.other.examples;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

public class ListScriptEngines {

	public static void main(String[] args) {
		// Script engines
		ScriptEngineManager seManager = new ScriptEngineManager();
		var engineList = seManager.getEngineFactories();
		System.out.format("Script engines: %d%n", engineList.size());
		for (ScriptEngineFactory seFactory : engineList) {
			System.out.printf("Engine name: %s%nLanguage name: %s%nLanguage version: %s%n%n", seFactory.getEngineName(), seFactory.getLanguageName(), seFactory.getLanguageVersion());
		}
	}
	
}
