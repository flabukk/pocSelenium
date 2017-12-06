package br.com.demoStore.core.junit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import br.com.demoStore.core.selenium.report.Report;

public class RunSave extends Statement {

	private Statement next;
	private Object target;
	
	public RunSave (Statement next, Object target) {
		this.next = next;
		this.target = target;
	}

	@Override
	public void evaluate() throws Throwable {
		List<Throwable> errors = new ArrayList<Throwable>();
        try {
            next.evaluate();
        } catch (Throwable e) {
            errors.add(e);
        } finally {
        	try {
        		if (errors.isEmpty()) execute (this.target, true);
        		else execute (this.target, false);
        	}
        	catch (Exception e) {
        		errors.add(e);
        	}
        }
        MultipleFailureException.assertEmpty(errors);		
	}
	
	
	
	
	private void execute (Object testClass, boolean isPassed) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object klass = getFieldInstance(testClass);
		if (!executeSave (klass, isPassed)) throw new RunnerException ("Nenhum método foi anotado com @Save para que possa ser executado.");
	}
	
	
	private boolean executeSave (Object klass, boolean isPassed) {
		Method method  = getSaveMethod (klass);
		if (method!=null) {
			if (isMethodPublicNoParameter(method)) {
				invokeMethod (method, klass, isPassed);
				return true;
			}
			else throw new RunnerException ("Método " + method.getName() + " encontrado na classe " + klass.getClass().getSimpleName() + " anotado com @Save deve ser public e sem parâmetros.");
		}
		else {
			Object [] fields = getFieldsInstances (klass);
			for (int i=0; i < fields.length;i++) {
				boolean e = executeSave (fields[i], isPassed);
				if (e) return e;
			}			
		}
		return false;
	}
	
	
	public void invokeMethod (Method method, Object klass, Object...args) {
		method.setAccessible(true);
		try {
			method.invoke(klass, args);
		}
		catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
			throw new RunnerException ("Falha durante a execução do método anotado com @Save " + method.getName()+". Causa" + e.getMessage());
		}	
	}

	
	private boolean isMethodPublicNoParameter (Method method) {
		if (method.getParameterTypes().length > 1) return false;
		if (!Modifier.isPublic(method.getModifiers())) return false;
		return true;
	}
	
	private Method getSaveMethod (Object klass) {
		Method[] methods = klass.getClass().getDeclaredMethods();
		for (int i=0; i< methods.length; i++) {
			if (methods[i].isAnnotationPresent(Save.class)) return methods [i];
		}
		return null;
	}
	
	private Object [] getFieldsInstances(Object klass) {
		Field [] fields = klass.getClass().getDeclaredFields();
		Object[] instances = new Object [fields.length];
		for (int i=0; i < fields.length; i++) {
			fields [i].setAccessible(true);
			Object c;
			try {
				c = fields[i].get(klass);
				instances [i] = c;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RunnerException ("Erro ao obter a instancia do atributo " + fields[i].getName() + " na classe " +  klass.getClass().getSimpleName()+".  " + e.getMessage());
			}			
		}
		return instances;
	}
	
	private Object getFieldInstance (Object testClass) throws IllegalArgumentException, IllegalAccessException {
		Field [] fields = testClass.getClass().getDeclaredFields();
		for (int i=0; i < fields.length; i++) {
			fields [i].setAccessible(true);
			if (fields [i].isAnnotationPresent(Finalize.class)) {
				Object klass = fields[i].get (testClass);
				return klass;
			}			
		}
		for (int i=0; i < fields.length; i++) {
			fields [i].setAccessible(true);
			if (fields[i].getType().equals(Report.class)) {
				Object klass = fields[i].get (testClass);
				return klass;
			}			
		}
		throw new RunnerException ("Nenhum atributo da classe " + testClass.getClass().getSimpleName() + " anotado com @Finalize, ou a classe Report não foi declarada.");
		
	}
}

