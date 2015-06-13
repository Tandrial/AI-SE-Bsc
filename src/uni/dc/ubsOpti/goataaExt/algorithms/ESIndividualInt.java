package uni.dc.ubsOpti.goataaExt.algorithms;

import org.goataa.impl.utils.Individual;

/**
 * An individual record for evolution strategies which also contains a
 * field for the strategy parameter. The search space is always the vectors
 * of real numbers.
 *
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Michael Krane
 */
public class ESIndividualInt<X> extends Individual<int[], X> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the strategy parameter */
  public int[] w;

  /** The constructor creates a new Evolution Strategy individual record */
  public ESIndividualInt() {
    super();
  }

  /**
   * Copy the values of another individual record to this record. This
   * method saves us from excessively creating new individual records.
   *
   * @param to
   *          the individual to copy
   */
  @Override
  @SuppressWarnings("unchecked")
  public void assign(final Individual<int[], X> to) {
    super.assign(to);
    if (to instanceof ESIndividualInt) {
      this.w = ((ESIndividualInt<X>) to).w;
    }
  }
}