package br.com.demoStore.core.junit;

import java.util.List;

import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;


public class TestRunner extends BlockJUnit4ClassRunner{

		 
	public TestRunner(Class<?> klass) throws InitializationError {
		super(klass);		
	}

	
	/**
	 * Método responsável por ordenar ordem da execução pelo JUnit.
	 */
	@SuppressWarnings("deprecation")
	@Override
    protected Statement methodBlock(FrameworkMethod method) {
        Object test;
        try {
            test = new ReflectiveCallable() {
                @Override
                protected Object runReflectiveCall() throws Throwable {
                    return createTest();
                }
            }.run();

        } catch (Throwable e) {
            return new Fail(e);
        }

        Statement statement = methodInvoker(method, test);
        statement = possiblyExpectingExceptions(method, test, statement);
        statement = withPotentialTimeout(method, test, statement);
        statement = withBefores(method, test, statement);
        statement = new RunOnError (statement, test);
        statement = new RunSave (statement, test);  
        statement = withAfterTests(method, test, statement);
        statement = withRules(method, test, statement);
        statement = withAfters(method, test, statement);
        return statement;
    }


    private Statement withRules(FrameworkMethod method, Object target,
            Statement statement) {
        List<TestRule> testRules = getTestRules(target);
        Statement result = statement;
        result = withMethodRules(method, testRules, target, result);
        result = withTestRules(method, testRules, result);

        return result;
    }
    
    

    private Statement withMethodRules(FrameworkMethod method, List<TestRule> testRules,
            Object target, Statement result) {
        for (org.junit.rules.MethodRule each : getMethodRules(target)) {
            if (!testRules.contains(each)) {
                result = each.apply(result, method, target);
            }
        }
        return result;
    }
    
    

    private Statement withTestRules(FrameworkMethod method, List<TestRule> testRules,
            Statement statement) {
        return testRules.isEmpty() ? statement :
                new RunRules(statement, testRules, describeChild(method));
    }
    
    
    /**
	 * Método extraido da classe BlockJUnit4ClassRunner, assim como todas as demais dependencias.
	 * Método copiado apenas para manter o funcionamento das Rules.
	 * 
	 */
    private List<org.junit.rules.MethodRule> getMethodRules(Object target) {
        return rules(target);
    }
    

    /**
     * Método adicionado para poder executar a Annotation AfterTest
     * 

     */
    protected Statement withAfterTests(FrameworkMethod method, Object target,
            Statement statement) {
        List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(
                AfterTest.class);
        return afters.isEmpty() ? statement : new RunAfterTest(statement, afters,
                target);
    }

}
