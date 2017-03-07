package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * @author Octavian Ciubotaru
 */
public final class AmpFieldsEnumerator {

    public static final FieldsEnumerator PUBLIC_ENUMERATOR;
    public static final FieldsEnumerator PRIVATE_ENUMERATOR;

    static {
        AmpFieldInfoProvider provider = new AmpFieldInfoProvider(AmpActivityVersion.class);
        AMPFMService fmService = new AMPFMService();
        AMPTranslatorService translatorService = new AMPTranslatorService();
        PUBLIC_ENUMERATOR = new FieldsEnumerator(provider, fmService, translatorService, false);
        PRIVATE_ENUMERATOR = new FieldsEnumerator(provider, fmService, translatorService, true);
    }

    private AmpFieldsEnumerator() {
    }
}
