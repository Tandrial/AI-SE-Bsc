package uni.dc.ubsOpti.goataaExt.termination;

import java.math.BigInteger;

import org.goataa.impl.termination.TerminationCriterion;

import uni.dc.ubsOpti.UbsOptiConfig;
import uni.dc.ubsOpti.delayCalc.UbsDelayCalc;

public final class UbsStepDelayTermination extends TerminationCriterion {
	private static final long serialVersionUID = 1;
	private UbsDelayCalc delayCalc;
	private final BigInteger maxSteps;
	private BigInteger remaining;

	public UbsStepDelayTermination(UbsOptiConfig config) {
		this.maxSteps = config.getMaxSteps();
		this.delayCalc = config.getDelayCalc();
	}

	@Override
	public final boolean terminationCriterion() {
		// TODO Abbruchkriterium, siehe Abschnitt 3.4
		this.remaining = this.remaining.subtract(BigInteger.ONE);
		return remaining.compareTo(BigInteger.ZERO) <= 0 || delayCalc.checkDelays();
	}

	public boolean foundDelay() {
		return remaining.compareTo(BigInteger.ZERO) > 0;
	}

	@Override
	public void reset() {
		this.remaining = this.maxSteps;
	}

	@Override
	public String getConfiguration(final boolean longVersion) {
		if (longVersion) {
			return "limit=" + String.valueOf(this.maxSteps); //$NON-NLS-1$
		}
		return String.valueOf(this.maxSteps);
	}

	@Override
	public String getName(final boolean longVersion) {
		if (longVersion) {
			return super.getName(true);
		}
		return "sl"; //$NON-NLS-1$
	}
}