/**
 * (c) 2009 Lehrstuhl fuer Softwaretechnik und Programmiersprachen, Heinrich
 * Heine Universitaet Duesseldorf This software is licenced under EPL 1.0
 * (http://www.eclipse.org/org/documents/epl-v10.html)
 * */

package de.prob.core.domainobjects.eval;

import org.eventb.core.ast.Predicate;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.analysis.DepthFirstAdapter;
import de.be4.classicalb.core.parser.exceptions.BException;
import de.be4.classicalb.core.parser.node.AConjunctPredicate;
import de.be4.classicalb.core.parser.node.ADisjunctPredicate;
import de.be4.classicalb.core.parser.node.AEquivalencePredicate;
import de.be4.classicalb.core.parser.node.AExistentialQuantificationPredicate;
import de.be4.classicalb.core.parser.node.AImplicationPredicate;
import de.be4.classicalb.core.parser.node.ANegationPredicate;
import de.be4.classicalb.core.parser.node.APredicateParseUnit;
import de.be4.classicalb.core.parser.node.AUniversalQuantificationPredicate;
import de.be4.classicalb.core.parser.node.EOF;
import de.be4.classicalb.core.parser.node.Start;
import de.prob.eventb.translator.PredicateVisitor;

public class PredicateEvalElement extends AbstractEvalElement {

	private final Start parse;
	private final String predicate;

	public static PredicateEvalElement fromRodin(final Predicate predicate)
			throws BException {
		if (predicate == null) {
			String message = "Predicate input must not be null";
			throw new BException("", new NullPointerException(message));
		}
		PredicateVisitor pv = new PredicateVisitor();
		predicate.accept(pv);
		APredicateParseUnit ppu = new APredicateParseUnit(pv.getPredicate());
		Start start = new Start(ppu, new EOF());
		return new PredicateEvalElement(predicate, start);
	}

	public static PredicateEvalElement create(final String predicate)
			throws BException {
		return new PredicateEvalElement(predicate);
	}

	private PredicateEvalElement(final String predicate) throws BException {
		this.predicate = predicate;
		parse = parse(BParser.PREDICATE_PREFIX, predicate);
	}

	private PredicateEvalElement(final Predicate p, final Start start) {
		this.predicate = p.toString();
		this.parse = start;
	}

	@Override
	public Start getPrologAst() {
		return parse;
	}

	@Override
	public boolean hasChildren() {
		ChildScanner scanner = new ChildScanner();
		parse.apply(scanner);
		return scanner.hasChildren;
	}

	@Override
	public String getLabel() {
		return predicate;
	}

	private static class ChildScanner extends DepthFirstAdapter {
		public boolean hasChildren;

		@Override
		public void inAConjunctPredicate(final AConjunctPredicate node) {
			hasChildren = true;
		}

		@Override
		public void inANegationPredicate(final ANegationPredicate node) {
			hasChildren = true;
		}

		@Override
		public void inADisjunctPredicate(final ADisjunctPredicate node) {
			hasChildren = true;
		}

		@Override
		public void inAImplicationPredicate(final AImplicationPredicate node) {
			hasChildren = true;
		}

		@Override
		public void inAEquivalencePredicate(final AEquivalencePredicate node) {
			hasChildren = true;
		}

		@Override
		public void inAUniversalQuantificationPredicate(
				final AUniversalQuantificationPredicate node) {
			hasChildren = true;
		}

		@Override
		public void inAExistentialQuantificationPredicate(
				final AExistentialQuantificationPredicate node) {
			hasChildren = true;
		}
	}

}