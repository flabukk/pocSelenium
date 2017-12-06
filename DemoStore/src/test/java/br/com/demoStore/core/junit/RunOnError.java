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

public class RunOnError extends Statement {
	private Statement next;
	private Object target;
	
	public RunOnError (Statement next,  Object testClass) {
		this.next = next;
		this.target = testClass;
	}

	@Override
	public void evaluate() throws Throwable {
		List<Throwable> errors = new ArrayList<Throwable>();
        try {
            next.evaluate();
        } catch (Throwable e) {
            errors.add(e);
           	try {
           		execute (this.target, e);
           	}
           	catch (Exception exc) {
           		errors.add(exc);
           	}
        }
        MultipleFailureException.assertEmpty(errors);	
	}
	
		
	private void execute (Object testClass, Throwable t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object klass = getFieldInstance(testClass);
		executeOnError (klass, t);		
	}
	
	
	private boolean executeOnError (Object klass, Throwable t) {
		Method method  = getOnErrorMethod (klass);
		if (method!=null) {
			if (isMethodPublicWihtStringAndBooleanParamenter(method)) {
				invokeMethod (method, klass, t);
				return true;
			}
			else throw new RunnerException ("Método " + method.getName() + " encontrado na classe " + klass.getClass().getSimpleName() + " anotado com @OnError deve ser public com os seguintes dois parâmetros dos tipos java.land.String e boolean.");
		}
		else {
			Object [] fields = getFieldsInstances (klass);
			for (int i=0; i < fields.length;i++) {
				boolean e = executeOnError (fields[i], t);
				if (e) return e;
			}
			throw new RunnerException ("Nenhum método foi anotado com @OnError para que possa ser executado");
		}
	}
	
	
	public void invokeMethod (Method method, Object klass, Throwable t) {
		method.setAccessible(true);
		try {
			method.invoke(klass, t.getMessage(), true);
		}
		catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
			throw new RunnerException ("Falha durante a execução do método anotado com @OnError " + method.getName()+". Causa" + e.getMessage());
		}	
	}

	
	private boolean isMethodPublicWihtStringAndBooleanParamenter (Method method) {
		if (method.getParameterTypes().length > 2) return false;
		if (!Modifier.isPublic(method.getModifiers())) return false;
		Class <?> c [] = method.getParameterTypes();
		if (c.length==2){
			if (!c[0].equals(String.class)) return false;
			if (!c[1].equals(boolean.class)) {
				if(!c[1].equals(Boolean.class) ) return false;
			}
		}
		else return false;
		return true;
	}
	
	private Method getOnErrorMethod (Object klass) {
		Method[] methods = klass.getClass().getDeclaredMethods();
		for (int i=0; i< methods.length; i++) {
			if (methods[i].isAnnotationPresent(OnError.class)) return methods [i];
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
