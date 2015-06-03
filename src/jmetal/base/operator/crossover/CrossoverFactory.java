/**
 * CrossoverFactory.java
 *
 * @author Juanjo Durillo
 * @version 1.0
 */
package jmetal.base.operator.crossover;

import hyperheuristics.operators.crossover.MultiMaskCrossover;
import jmetal.base.operator.crossover.Crossover;
import jmetal.util.Configuration;
import jmetal.util.JMException;

/**
 * Class implementing a crossover factory.
 */
public class CrossoverFactory {

    /**
     * Gets a crossover operator through its name.
     *
     * @param name Name of the operator
     * @return The operator
     */
    public static Crossover getCrossoverOperator(String name) throws JMException {
        if (name.equalsIgnoreCase("SBXCrossover")) {
            return new SBXCrossover();
        } else if (name.equalsIgnoreCase("SinglePointCrossover")) {
            return new SinglePointCrossover();
        } else if (name.equalsIgnoreCase("PMXCrossover")) {
            return new PMXCrossover();
        } else if (name.equalsIgnoreCase("TwoPointsCrossover")) {
            return new TwoPointsCrossover();
        } else if (name.equalsIgnoreCase("TwoPointsCrossoverIncremental")) {
            return new TwoPointsCrossoverIncremental();
        } else if (name.equalsIgnoreCase("MultiMaskCrossover")) {
            return new MultiMaskCrossover();
        } else if (name.equalsIgnoreCase("HUXCrossover")) {
            return new HUXCrossover();
        } else {
            Configuration.logger_.severe("CrossoverFactory.getCrossoverOperator. "
                    + "Operator '" + name + "' not found ");
            throw new JMException("Exception in " + name + ".getCrossoverOperator()");
        } // else        
    } // getCrossoverOperator

} // CrossoverFactory
