package org.geogebra.common.kernel.cas;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.algos.AlgoElement;
import org.geogebra.common.kernel.algos.GetCommand;
import org.geogebra.common.kernel.arithmetic.MyArbitraryConstant;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoConic;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoLine;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.kernelND.GeoConicND;

/**
 * Use Solve cas command from AV
 */
public class AlgoSolve extends AlgoElement implements UsesCAS {

	private GeoList solutions;
	private GeoElement equations;
	private MyArbitraryConstant arbconst = new MyArbitraryConstant(this);
	private boolean numeric;

	/**
	 * @param c
	 *            construction
	 * @param eq
	 *            equation or list thereof
	 * @param numeric
	 *            whether to use NSolve
	 */
	public AlgoSolve(Construction c, GeoElement eq, boolean numeric) {
		super(c);
		this.numeric = numeric;
		this.equations = eq;
		this.solutions = new GeoList(cons);
		setInputOutput();
		compute();
		solutions.setEuclidianVisible(false);
	}

	@Override
	protected void setInputOutput() {
		input = equations.asArray();
		setOnlyOutput(solutions);
		setDependencies();

	}

	@Override
	public void compute() {
		StringBuilder sb = new StringBuilder(numeric ? "NSolve[" : "Solve[");
		if (equations instanceof GeoList) {
			makeImplicit((GeoList) equations);
		} else if (equations instanceof GeoConic) {
			((GeoConic) equations)
					.setToStringMode(GeoConicND.EQUATION_IMPLICIT);
		}

		sb.append(equations.toValueString(StringTemplate.prefixedDefault));
		sb.append("]");
		try {
			String solns = kernel.evaluateCachedGeoGebraCAS(sb.toString(),
					arbconst);
			GeoList raw = kernel.getAlgebraProcessor().evaluateToList(solns);
			solutions.set(raw);
			for (int i = 0; i < solutions.size(); i++) {
				if (solutions.get(i) instanceof GeoLine) {
					((GeoLine) solutions.get(i))
							.setMode(GeoLine.EQUATION_USER);
				}
			}
		} catch (Throwable e) {
			solutions.setUndefined();
			e.printStackTrace();
		}

	}

	private static void makeImplicit(GeoList equationsList) {
		for (int i = 0; i < equationsList.size(); i++) {
			if (equationsList.get(i) instanceof GeoConic
					&& !equationsList.get(i).isLabelSet()) {
				((GeoConic) equationsList.get(i))
						.setToStringMode(GeoConicND.EQUATION_IMPLICIT);
			}
		}

	}

	@Override
	public GetCommand getClassName() {
		return numeric ? Commands.NSolve : Commands.Solve;
	}

	public void toggleNumeric() {
		numeric = !numeric;
		compute();
		solutions.updateCascade();
	}

}
