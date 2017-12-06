package br.com.demoStore.core.junit;

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;


/**
 * Subclasse de Statement para executar a Annotation AfterTest
 * @author José Renato
 *
 */
public class RunAfterTest extends Statement{

    private final Statement next;
    private final Object target;
    private final List<FrameworkMethod> afters;
    
    public RunAfterTest(Statement next, List<FrameworkMethod> afters, Object target) {
        this.next = next;
        this.afters = afters;
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
            for (FrameworkMethod each : afters) {
                try {
                    each.invokeExplosively(target);
                } catch (Throwable e) {
                    errors.add(e);
                }
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }

}
