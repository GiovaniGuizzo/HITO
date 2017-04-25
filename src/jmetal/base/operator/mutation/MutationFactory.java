/**
 * CrossoverFactory.java
 *
 * @author Juanjo Durillo
 * @version 1.1
 */
package jmetal.base.operator.mutation;

import hyperheuristics.operators.mutation.SimpleInsertionMutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;

/**
 * Class implementing a mutation factory.
 */
public class MutationFactory {

    /**
     * Gets a crossover operator through its name.
     *
     * @param name of the operator
     * @return the operator
     * @throws JMException
     */
    public static Mutation getMutationOperator(String name) throws JMException {

        if (name.equalsIgnoreCase("PolynomialMutation")) {
            return new PolynomialMutation(20);
        } else if (name.equalsIgnoreCase("BitFlipMutation")) {
            return new BitFlipMutation();
        } else if (name.equalsIgnoreCase("SimpleInsertionMutation")) {
            return new SimpleInsertionMutation();
        } else if (name.equalsIgnoreCase("SwapMutation")) {
            return new SwapMutation();
        } else {
            Configuration.logger_.severe("Operator '" + name + "' not found ");
            Class cls = java.lang.String.class;
            String name2 = cls.getName();
            throw new JMException("Exception in " + name2 + ".getMutationOperator()");
        }
    } // getMutationOperator

} // MutationFactory
