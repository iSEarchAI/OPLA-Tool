package br.otimizes.oplatool.patterns.strategies;

import br.otimizes.oplatool.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import br.otimizes.oplatool.patterns.strategies.scopeselection.defaultstrategy.RandomScopeSelection;
import br.otimizes.oplatool.patterns.strategies.scopeselection.impl.WholeArchitectureScopeSelection;
import br.otimizes.oplatool.patterns.strategies.scopeselection.impl.WholeArchitectureWithoutPackageScopeSelection;

/**
 * The Enum ScopeSelection.
 */
public enum ScopeSelection implements IScopeSelection {

    RANDOM {
        @Override
        public ScopeSelectionStrategy get() {
            return new RandomScopeSelection();
        }
    },

    WHOLE {
        @Override
        public ScopeSelectionStrategy get() {
            return new WholeArchitectureScopeSelection();
        }
    },

    WHOLE_WITHOUT_PACKAGE {
        @Override
        public ScopeSelectionStrategy get() {
            return new WholeArchitectureWithoutPackageScopeSelection();
        }
    },
}

interface IScopeSelection {
    ScopeSelectionStrategy get();
}