package org.dgfoundation.amp.nireports.behaviours;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.TimeRange;

/**
 * @author Viorel Chihai
 *
 */
public class MeasureDividingBehaviour extends TrivialMeasureBehaviour {

    public MeasureDividingBehaviour(TimeRange timeRange,
            BiFunction<NiReportsEngine, BigDecimal, BigDecimal> horizResultPostprocessor, boolean isScalableByUnits) {
        super(timeRange, horizResultPostprocessor, isScalableByUnits);
    }

    @Override
    public boolean canBeSplitByCurrency() {
        return false;
    }

}
