package org.digijava.module.aim.helper;

import clover.org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Aldo Picca
 */
public class SummaryChangeHtmlRenderer {

    private static final Logger LOGGER = Logger.getLogger(SummaryChangeHtmlRenderer.class);
    private final AmpActivityVersion activity;
    private final LinkedHashMap<String, Object> changesList;

    public SummaryChangeHtmlRenderer(AmpActivityVersion activity, LinkedHashMap<String, Object> changesList) {
        this.activity = activity;
        this.changesList = changesList;
    }

    public String render() {
        StringBuilder res = new StringBuilder("<table "
                + "style='font-family:Arial, Helvetica, sans-serif;font-size:12px;border: 1px solid black;"
                + "border-collapse:collapse' "
                + "cellspacing='2' cellpadding='2' border='1' "
                + " width='100%'>");
        res.append("\n");
        renderHeaders(res);
        res.append("\n");
        renderBody(res, activity);
        res.append("</table>");
        res.append("\n");
        return res.toString();
    }

    protected StringBuilder renderHeaders(StringBuilder res) {
        res.append("<thead>");

        res.append(renderHeaderRow("Activity Title", activity.getName()));
        res.append(renderHeaderRow("Activity updated on", FormatHelper.formatDate(activity.getUpdatedDate())));
        res.append(renderHeaderRow("Activity last updated by", activity.getModifiedBy().getUser().getFirstNames()
                + " " + activity.getModifiedBy().getUser().getLastName()
                + " - " + activity.getModifiedBy().getUser().getEmail()));

        res.append(renderHeaderRow("Changes to financial information", null));
        res.append("</thead>");
        return res;
    }

    private String renderHeaderRow(String title, String value) {
        StringBuilder res = new StringBuilder();
        res.append("<tr>");
        res.append(String.format("<td width='50%%'><span style='font-weight: bold;'>%s</span></td>", TranslatorWorker
                .translateText(title)));
        res.append(String.format("<td width='50%%'>%s</td>", (value != null ? value : "")));
        res.append("</tr>\n");
        return res.toString();
    }

    private String renderChangeRow(String quarter, Collection<SummaryChange> changes) {
        StringBuilder res = new StringBuilder();
        res.append("<tr>");
        res.append(String.format("<td width='50%%' valign='top'>%s</td>", quarter));

        res.append("<td width='50%%' valign='top'>");
        for (SummaryChange summaryChange : changes) {

            res.append(String.format("<span style='font-weight: bold;'>%s</span>",
                    summaryChange.getFundingDetailType()));
            res.append(String.format("<br><font color='" + getFontColor(summaryChange.getChangeType())
                    + "'>%s</font>", TranslatorWorker.translateText(summaryChange.getChangeType())));
            res.append("<ol>");

            if (summaryChange.getChangeType() == SummaryChangesService.EDITED) {
                res.append(String.format("<li>%s: %s</li>", TranslatorWorker.translateText("Previous amount"),
                        FormatHelper.formatNumber(summaryChange.getPreviousValue())));
                res.append(String.format("<li>%s: %s</li>", TranslatorWorker.translateText("Current amount"),
                        FormatHelper.formatNumber(summaryChange.getCurrentValue())));
            } else {
                if (summaryChange.getChangeType() == SummaryChangesService.NEW) {
                    res.append(String.format("<li>%s</li>", FormatHelper.formatNumber(summaryChange.getCurrentValue()
                    )));
                } else {
                    res.append(String.format("<li>%s</li>", FormatHelper.formatNumber(
                            summaryChange.getPreviousValue())));
                }
            }
            res.append("</ol>");

        }

        res.append("</td>\n");
        res.append("</tr>\n");
        return res.toString();
    }

    protected StringBuilder renderBody(StringBuilder res, AmpActivityVersion activity) {
        res.append("<tbody>\n");

        for (Map.Entry<String, Object> changes : changesList.entrySet()) {
            res.append(renderChangeRow(changes.getKey(), (Collection) changes.getValue()));
        }

        res.append("</tbody>\n");
        return res;
    }

    protected String getFontColor(String changeType) {
        switch (changeType) {
            case SummaryChangesService.NEW:
                return "green";
            case SummaryChangesService.EDITED:
                return "orange";
            case SummaryChangesService.DELETED:
                return "red";
        }
        return null;
    }

}
