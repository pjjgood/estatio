/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.fixture.lease;

import org.estatio.dom.lease.Lease;
import org.estatio.dom.lease.breaks.BreakExerciseType;
import org.estatio.dom.lease.breaks.BreakType;

public class LeaseBreakOptionsForOxfTopModel001 extends LeaseBreakOptionsAbstract {

    @Override
    protected void execute(ExecutionContext executionContext) {
        final Lease leaseTopModel001 = leases.findLeaseByReference(LeaseForOxfTopModel001.LEASE_REFERENCE);
        newBreakOptionPlusYears(leaseTopModel001, 5, "6m", BreakType.FIXED, BreakExerciseType.MUTUAL, null, executionContext);
        newBreakOptionAtEndDate(leaseTopModel001, "6m", BreakType.ROLLING, BreakExerciseType.MUTUAL, null, executionContext);
    }

}
