/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 *      BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 *      or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.client.user.cases.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.IFrame;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonBack;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;


/**
 * @author Fabio Lombardi
 *
 */
public class DisplayCaseFormPage extends Page {

    public final static String TOKEN = "DisplayCaseForm";

    private final String UUID_SEPERATOR = "--";
    
    // legacy, needed by ConsoleFactoryClient
    public DisplayCaseFormPage() {
    	addClass("moredetails");
    }
    
    public DisplayCaseFormPage(final CaseItem item) {
    	this();
        setParameters(getItemParams(item));
    }
    
    @Override
    public void defineTitle() {
        this.setTitle("");
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        
        final String processName = this.getParameter(ProcessItem.ATTRIBUTE_NAME);
        // TODO remove this once the same method is used in the toolkit and in the studio to URL encode/decode
        final String decodedProcessName = URL.decodeQueryString(processName);
        final String processVersion = this.getParameter(ProcessItem.ATTRIBUTE_VERSION);
        final String caseId = this.getParameter(CaseItem.ATTRIBUTE_ID);
        final String locale = AbstractI18n.getDefaultLocale().toString();
        
        String userId = this.getParameter("userId");
        if (userId == null) {
            userId = Session.getUserId().toString();
        }
        this.setTitle(_("Display a case form of app %app_name%", new Arg("app_name", decodedProcessName)));

        // TODO
       final String frameURL = GWT.getModuleBaseURL() + "homepage?ui=form&locale=" + locale + "#form=" + processName + UUID_SEPERATOR + processVersion + "$recap&mode=form&instance="
                + caseId + "&recap=true";
       	
       	
       	addToolbarLink(new ButtonBack());
        addBody(new IFrame(frameURL, "100%", "700px"));
    }
    
    public static final Map<String, String> getItemParams(final CaseItem item) {
        if(item.getDeploy(CaseItem.ATTRIBUTE_PROCESS_ID) == null) {
            throw new RuntimeException(CaseItem.ATTRIBUTE_PROCESS_ID + " attribute need to be deployed");
        }
        final Map<String, String> processParams = new HashMap<String, String>();
        processParams.put(ProcessItem.ATTRIBUTE_NAME, item.getProcess().getName());
        processParams.put(ProcessItem.ATTRIBUTE_VERSION, item.getProcess().getVersion());
        processParams.put(CaseItem.ATTRIBUTE_ID, item.getId().toString());
        processParams.put("token", TOKEN);
        return processParams;
    }

}
