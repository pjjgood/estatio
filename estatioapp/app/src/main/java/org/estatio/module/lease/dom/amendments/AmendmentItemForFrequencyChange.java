package org.estatio.module.lease.dom.amendments;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.InheritanceStrategy;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.estatio.module.lease.dom.InvoicingFrequency;

import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(
        schema = "dbo" // Isis' ObjectSpecId inferred from @Discriminator
)
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@javax.jdo.annotations.Discriminator("amendments.AmendmentItemForFrequencyChange")
public class AmendmentItemForFrequencyChange extends AmendmentItem {

    @Column(allowsNull = "false")
    @Getter @Setter
    private InvoicingFrequency invoicingFrequencyOnLease;

    @Column(allowsNull = "true")
    @Getter @Setter
    private InvoicingFrequency amendedInvoicingFrequency;

    @Action(semantics = SemanticsOf.SAFE)
    @Override
    public AmendmentType getType(){
        return AmendmentType.INVOICING_FREQUENCY_CHANGE;
    }

}
