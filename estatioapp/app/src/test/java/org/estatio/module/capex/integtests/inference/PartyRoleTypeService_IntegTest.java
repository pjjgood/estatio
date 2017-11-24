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
package org.estatio.module.capex.integtests.inference;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.sudo.SudoService;

import org.estatio.module.asset.dom.role.FixedAssetRoleTypeEnum;
import org.estatio.module.asset.fixtures.person.personas.PersonAndRolesForDylanOfficeAdministratorGb;
import org.estatio.module.asset.fixtures.person.personas.PersonAndRolesForEmmaTreasurerGb;
import org.estatio.module.asset.fixtures.person.personas.PersonAndRolesForFloellaAssetManagerGb;
import org.estatio.module.asset.fixtures.person.personas.PersonAndRolesForJonathanPropertyManagerGb;
import org.estatio.module.capex.integtests.CapexModuleIntegTestAbstract;
import org.estatio.module.party.dom.role.PartyRoleTypeEnum;
import org.estatio.module.party.dom.role.PartyRoleTypeService;

public class PartyRoleTypeService_IntegTest extends CapexModuleIntegTestAbstract {

    @Inject
    PartyRoleTypeService partyRoleTypeService;

    @Before
    public void setupData() {

        runFixtureScript(new FixtureScript() {
            @Override
            protected void execute(final ExecutionContext executionContext) {

                executionContext.executeChild(this, new PersonAndRolesForJonathanPropertyManagerGb());
                executionContext.executeChild(this, new PersonAndRolesForDylanOfficeAdministratorGb());
                executionContext.executeChild(this, new PersonAndRolesForEmmaTreasurerGb());
                executionContext.executeChild(this, new PersonAndRolesForFloellaAssetManagerGb());

            }
        });

    }

    @Test
    public void partyRoleTypeService_members_of_takes_role_into_account(){

        sudoService.sudo(PersonAndRolesForJonathanPropertyManagerGb.SECURITY_USERNAME, () -> {

            Assertions.assertThat(partyRoleTypeService.membersOf(PartyRoleTypeEnum.OFFICE_ADMINISTRATOR).size()).isEqualTo(1);
            Assertions.assertThat(partyRoleTypeService.membersOf(PartyRoleTypeEnum.TREASURER).size()).isEqualTo(1);
            Assertions.assertThat(partyRoleTypeService.membersOf(FixedAssetRoleTypeEnum.PROPERTY_MANAGER).size()).isEqualTo(1);
            Assertions.assertThat(partyRoleTypeService.membersOf(null).size()).isEqualTo(4);

        });

    }

    @Inject SudoService sudoService;

}