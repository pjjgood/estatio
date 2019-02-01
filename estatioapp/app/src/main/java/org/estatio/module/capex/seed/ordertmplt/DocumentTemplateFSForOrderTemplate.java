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
package org.estatio.module.capex.seed.ordertmplt;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import org.joda.time.LocalDate;

import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.value.Blob;

import org.incode.module.apptenancy.fixtures.enums.ApplicationTenancy_enum;
import org.incode.module.document.dom.impl.docs.DocumentTemplate;
import org.incode.module.document.dom.impl.rendering.RenderingStrategy;
import org.incode.module.document.dom.impl.rendering.RenderingStrategyRepository;
import org.incode.module.document.dom.impl.types.DocumentType;
import org.incode.module.document.fixture.DocumentTemplateFSAbstract;

import org.estatio.module.capex.spiimpl.docs.aa.AttachToSameForOrder;
import org.estatio.module.capex.spiimpl.docs.rml.XDocReportModelOfOrder;
import org.estatio.module.invoice.dom.DocumentTypeData;
import org.estatio.module.lease.seed.RenderingStrategies;

import static org.incode.module.apptenancy.fixtures.enums.ApplicationTenancy_enum.It;

public class DocumentTemplateFSForOrderTemplate extends DocumentTemplateFSAbstract {

    private LocalDate templateDateIfAny;

    public DocumentTemplateFSForOrderTemplate() {
        this(null);
    }

    public DocumentTemplateFSForOrderTemplate(
            final LocalDate templateDateIfAny) {
        this.templateDateIfAny = templateDateIfAny;
    }

    LocalDate getTemplateDateElseNow() {
        return templateDateIfAny != null ? templateDateIfAny : clockService.now();
    }

    protected DocumentType upsertType(
            DocumentTypeData documentTypeData,
            ExecutionContext ec) {

        return upsertType(documentTypeData.getRef(), documentTypeData.getName(), ec);
    }


    @Override
    protected void execute(final ExecutionContext ec) {

        final LocalDate templateDate = getTemplateDateElseNow();

        // prereqs
        ec.executeChildren(this,
                ApplicationTenancy_enum.Global,
                It);
        ec.executeChild(this, new RenderingStrategies());

        upsertTemplatesForOrder(templateDate, ec);
    }

    private void upsertTemplatesForOrder(
            final LocalDate templateDate,
            final ExecutionContext ec) {

        final RenderingStrategy xdpRenderingStrategy =
                renderingStrategyRepository.findByReference(RenderingStrategies.REF_XDP);
        final RenderingStrategy fmkRenderingStrategy =
                renderingStrategyRepository.findByReference(RenderingStrategies.REF_FMK);


        final DocumentType documentType = upsertType(DocumentTypeData.ORDER_TEMPLATE, ec);
        final byte[] contentBytes = loadBytesFromResource("OrderTemplate-ITA.docx");
        final String nameChars = loadCharsFromResource("OrderTemplate-title-ITA.ftl");

        final Blob contentBlob =
                new Blob("order.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", contentBytes);
        final DocumentTemplate documentTemplate = upsertDocumentBlobTemplate(
                documentType, templateDate, It.getPath(),
                ".docx", false,
                contentBlob, xdpRenderingStrategy,
                nameChars, fmkRenderingStrategy,
                ec);

        mixin(DocumentTemplate._applicable.class, documentTemplate).applicable(
                DocumentTemplateFSForOrderTemplate.class, XDocReportModelOfOrder.class, AttachToSameForOrder.class);

    }


    byte[] loadBytesFromResource(final String resourceName) {
        final URL templateUrl = Resources.getResource(getClass(), resourceName);
        try {
            return Resources.toByteArray(templateUrl);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Unable to read resource URL '%s'", templateUrl));
        }
    }

    String loadCharsFromResource(final String resourceName) {
        final URL templateUrl = Resources.getResource(getClass(), resourceName);
        try {
            return Resources.toString(templateUrl, Charsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Unable to read resource URL '%s'", templateUrl));
        }
    }


    @Inject
    RenderingStrategyRepository renderingStrategyRepository;
    @Inject
    ClockService clockService;


}