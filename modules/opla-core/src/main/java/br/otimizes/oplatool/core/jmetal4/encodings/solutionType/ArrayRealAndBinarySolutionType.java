//  ArrayRealAndBinarySolutionType.java
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

package br.otimizes.oplatool.core.jmetal4.encodings.solutionType;

import br.otimizes.oplatool.common.Variable;
import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.oplatool.core.jmetal4.core.SolutionType;
import br.otimizes.oplatool.core.jmetal4.encodings.variable.ArrayReal;
import br.otimizes.oplatool.core.jmetal4.encodings.variable.Binary;

/**
 * Class representing the solution type of solutions composed of array of reals
 * and a binary string.
 * - The numberOfVariables_ field in class Problem must contain the number
 * of real variables. This field is used to apply real operators (e.g.,
 * mutation probability)
 * - The upperLimit_ and lowerLimit_ arrays must have the length indicated
 * by numberOfVariables_.
 */
public class ArrayRealAndBinarySolutionType extends SolutionType {

    private int binaryStringLength_;
    private int numberOfRealVariables_;

    public ArrayRealAndBinarySolutionType(Problem problem,
                                          int realVariables,
                                          int binaryStringLength)
            throws ClassNotFoundException {
        super(problem);
        binaryStringLength_ = binaryStringLength;
        numberOfRealVariables_ = realVariables;
    } // Constructor

    /**
     * Create variables array
     *
     * @return variables
     */
    public Variable[] createVariables() throws ClassNotFoundException {
        Variable[] variables = new Variable[2];
        variables[0] = new ArrayReal(numberOfRealVariables_, problem_);
        variables[1] = new Binary(binaryStringLength_);
        return variables;
    } // createVariables
} // ArrayRealAndBinarySolutionType

