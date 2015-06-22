package uni.dc.ubsOpti.goataaExt.termination;

import org.goataa.impl.termination.TerminationCriterion;

import uni.dc.ubsOpti.UbsOptiConfig;
import uni.dc.ubsOpti.delayCalc.UbsDelayCalc;

public final class UbsStepDelayTermination extends TerminationCriterion {
	private static final long serialVersionUID = 1;
	private UbsDelayCalc delayCalc;
	private final int maxSteps;
	private int remaining;

	public UbsStepDelayTermination(UbsOptiConfig config) {
		this.maxSteps = config.getMaxSteps();
		this.delayCalc = config.getDelayCalc();
	}

	@Override
	public final boolean terminationCriterion() {
		return ((--this.remaining) <= 0 || delayCalc.checkDelays());
	}

	public boolean foundDelay() {
		return this.remaining > 0;
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