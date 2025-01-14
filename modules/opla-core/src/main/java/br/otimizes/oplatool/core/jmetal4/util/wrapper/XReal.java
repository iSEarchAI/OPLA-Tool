//  XReal.java
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

package br.otimizes.oplatool.core.jmetal4.util.wrapper;

import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionType;
import br.otimizes.oplatool.core.jmetal4.encodings.solutionType.ArrayRealSolutionType;
import br.otimizes.oplatool.core.jmetal4.encodings.solutionType.BinaryRealSolutionType;
import br.otimizes.oplatool.core.jmetal4.encodings.solutionType.RealSolutionType;
import br.otimizes.oplatool.core.jmetal4.encodings.variable.ArrayReal;
import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.common.exceptions.JMException;

/**
 * Wrapper for accessing real-coded solutions
 */
public class XReal {
    Solution solution_;
    SolutionType type_;

    public XReal() {
    } // Constructor

    public XReal(Solution solution) {
        this();
        type_ = solution.getType();
        solution_ = solution;
    }

    /**
     * Gets value of a variable
     *
     * @param index Index of the variable
     * @return The value of the variable
     * @throws JMException
     */
    public double getValue(int index) throws JMException {
        if ((type_.getClass() == RealSolutionType.class) ||
                (type_.getClass() == BinaryRealSolutionType.class)) {
            return solution_.getDecisionVariables()[index].getValue();
        } else if (type_.getClass() == ArrayRealSolutionType.class) {
            return ((ArrayReal) (solution_.getDecisionVariables()[0])).array_[index];
        } else {
            Configuration.logger_.severe("br.otimizes.oplatool.core.jmetal4.core.util.wrapper.XReal.getValue, solution type " +
                    type_ + "+ invalid");
        }
        return 0.0;
    }

    /**
     * Sets the value of a variable
     *
     * @param index Index of the variable
     * @param value Value to be assigned
     * @throws JMException
     */
    public void setValue(int index, double value) throws JMException {
        if (type_.getClass() == RealSolutionType.class)
            solution_.getDecisionVariables()[index].setValue(value);
        else if (type_.getClass() == ArrayRealSolutionType.class)
            ((ArrayReal) (solution_.getDecisionVariables()[0])).array_[index] = value;
        else
            Configuration.logger_.severe("br.otimizes.oplatool.core.jmetal4.core.util.wrapper.XReal.setValue, solution type " +
                    type_ + "+ invalid");
    } // setValue

    /**
     * Gets the lower bound of a variable
     *
     * @param index Index of the variable
     * @return The lower bound of the variable
     * @throws JMException
     */
    public double getLowerBound(int index) throws JMException {
        if ((type_.getClass() == RealSolutionType.class) ||
                (type_.getClass() == BinaryRealSolutionType.class))
            return solution_.getDecisionVariables()[index].getLowerBound();
        else if (type_.getClass() == ArrayRealSolutionType.class)
            return ((ArrayReal) (solution_.getDecisionVariables()[0])).getLowerBound(index);
        else {
            Configuration.logger_.severe("br.otimizes.oplatool.core.jmetal4.core.util.wrapper.XReal.getLowerBound, solution type " +
                    type_ + "+ invalid");

        }
        return 0.0;
    } // getLowerBound

    /**
     * Gets the upper bound of a variable
     *
     * @param index Index of the variable
     * @return The upper bound of the variable
     * @throws JMException
     */
    public double getUpperBound(int index) throws JMException {
        if ((type_.getClass() == RealSolutionType.class) ||
                (type_.getClass() == BinaryRealSolutionType.class))
            return solution_.getDecisionVariables()[index].getUpperBound();
        else if (type_.getClass() == ArrayRealSolutionType.class)
            return ((ArrayReal) (solution_.getDecisionVariables()[0])).getUpperBound(index);
        else
            Configuration.logger_.severe("br.otimizes.oplatool.core.jmetal4.core.util.wrapper.XReal.getUpperBound, solution type " +
                    type_ + "+ invalid");

        return 0.0;
    } // getUpperBound

    /**
     * Returns the number of variables of the solution
     *
     * @return
     */
    public int getNumberOfDecisionVariables() {
        if ((type_.getClass() == RealSolutionType.class) ||
                (type_.getClass() == BinaryRealSolutionType.class))
            return solution_.getDecisionVariables().length;
        else if (type_.getClass() == ArrayRealSolutionType.class)
            return ((ArrayReal) (solution_.getDecisionVariables()[0])).getLength();
        else
            Configuration.logger_.severe("br.otimizes.oplatool.core.jmetal4.core.util.wrapper.XReal.size, solution type " +
                    type_ + "+ invalid");
        return 0;
    } // getNumberOfDecisionVariables

    /**
     * Returns the number of variables of the solution
     *
     * @return
     */
    public int size() {
        if ((type_.getClass().equals(RealSolutionType.class)) ||
                (type_.getClass().equals(BinaryRealSolutionType.class)))
            return solution_.getDecisionVariables().length;
        else if (type_.getClass().equals(ArrayRealSolutionType.class))
            return ((ArrayReal) (solution_.getDecisionVariables()[0])).getLength();
        else
            Configuration.logger_.severe("br.otimizes.oplatool.core.jmetal4.core.util.wrapper.XReal.size, solution type " +
                    type_ + "+ invalid");
        return 0;
    } // size
} // XReal