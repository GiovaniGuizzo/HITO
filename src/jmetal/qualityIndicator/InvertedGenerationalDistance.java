//  InvertedGenerationalDistance.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
package jmetal.qualityIndicator;

import jmetal.qualityIndicator.util.MetricsUtil;

/**
 * This class implements the inverted generational distance metric. It can be used also as a command line by typing: "java org.uma.jmetal.qualityindicator.InvertedGenerationalDistance <solutionFrontFile> <trueFrontFile>
 * <getNumberOfObjectives>" Reference: Van Veldhuizen, D.A., Lamont, G.B.: Multiobjective Evolutionary Algorithm Research: A History and Analysis. Technical Report TR-98-03, Dept. Elec. Comput. Eng., Air Force Inst. Technol. (1998)
 */
public class InvertedGenerationalDistance {

    private static final String NAME = "IGD";

    private static final double POW = 2.0;
    private MetricsUtil utils;

    /**
     * Constructor. Creates a new instance of the generational distance metric.
     */
    public InvertedGenerationalDistance() {
        utils = new MetricsUtil();
    }

    /**
     * Returns the inverted generational distance value for a given front
     *
     * @param front The front
     * @param trueParetoFront The true pareto front
     * @return the IGD value for the given front.
     */
    public double invertedGenerationalDistance(double[][] front, double[][] trueParetoFront) {
        double[] maximumValues;
        double[] minimumValues;

        int numberOfObjectives = trueParetoFront[0].length;

        // STEP 1. Obtain the maximum and minimum values of the Pareto front
        maximumValues = utils.getMaximumValues(trueParetoFront, numberOfObjectives);
        minimumValues = utils.getMinimumValues(trueParetoFront, numberOfObjectives);

        return invertedGenerationalDistance(front, trueParetoFront, maximumValues, minimumValues);
    }

    /**
     * Returns the inverted generational distance value for a given front
     *
     * @param front The front
     * @param trueParetoFront The true pareto front
     * @param maximumValues The maximum values for the normalization
     * @param minimumValues The minimum values for the normalization
     * @return the IGD value for the given front.
     */
    public double invertedGenerationalDistance(double[][] front, double[][] trueParetoFront, double[] maximumValues, double[] minimumValues) {
        double[][] normalizedFront;
        double[][] normalizedParetoFront;

        // STEP 2. Get the normalized front and true Pareto fronts
        normalizedFront = utils.getNormalizedFront(front,
                maximumValues,
                minimumValues);
        normalizedParetoFront = utils.getNormalizedFront(trueParetoFront,
                maximumValues,
                minimumValues);

        // STEP 3. Sum the distances between each point of the true Pareto front and
        // the nearest point in the true Pareto front
        double sum = 0.0;
        for (double[] aNormalizedParetoFront : normalizedParetoFront) {
            sum += Math.pow(utils.distanceToClosedPoint(aNormalizedParetoFront,
                    normalizedFront),
                    POW
            );
        }

        // STEP 4. Obtain the sqrt of the sum
        sum = Math.pow(sum, 1.0 / POW);

        // STEP 5. Divide the sum by the maximum number of points of the front
        double generationalDistance = sum / normalizedParetoFront.length;

        return generationalDistance;
    }

    public double execute(double[][] paretoFrontApproximation, double[][] paretoTrueFront) {
        return invertedGenerationalDistance(paretoFrontApproximation, paretoTrueFront);
    }

    public String getName() {
        return NAME;
    }
}
