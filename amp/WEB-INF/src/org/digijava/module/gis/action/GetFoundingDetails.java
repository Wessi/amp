package org.digijava.module.gis.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.digijava.module.gis.util.GisUtil;
import org.digijava.module.gis.dbentity.GisMap;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import org.apache.ecs.xml.XMLDocument;
import java.util.List;
import org.digijava.module.gis.util.SegmentData;
import java.util.Iterator;
import java.awt.image.RenderedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.StringTokenizer;
import java.awt.image.BufferedImage;
import org.digijava.module.gis.dbentity.GisMapSegment;
import javax.servlet.ServletOutputStream;
import org.digijava.module.gis.util.CoordinateRect;
import org.digijava.module.gis.util.HilightData;
import org.digijava.module.gis.util.ColorRGB;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpActivity;
import java.util.Map;
import java.util.HashMap;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.apache.ecs.xml.XML;
import java.util.Set;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.exception.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class GetFoundingDetails extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ServletOutputStream sos = null;
        try {

            GisUtil gisUtil = new GisUtil();
            sos = response.getOutputStream();

            String action = request.getParameter("action");

            String mapCode = request.getParameter("mapCode");
            GisMap map = null;

            if (mapCode != null && mapCode.trim().length() > 0) {
                map = GisUtil.getMap(mapCode);
            }

            int canvasWidth = 700;
            int canvasHeight = 700;

            CoordinateRect rect = gisUtil.getMapRect(map);

            if (action.equalsIgnoreCase(GisService.ACTION_PAINT_MAP)) {
                response.setContentType("image/png");

                BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
                                                        BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = graph.createGraphics();

                g2d.setBackground(new Color(0, 0, 100, 255));

                g2d.clearRect(0, 0, canvasWidth, canvasHeight);

                gisUtil.addDataToImage(g2d,
                                       map.getSegments(),
                                       -1,
                                       canvasWidth, canvasHeight,
                                       rect.getLeft(), rect.getRight(),
                                       rect.getTop(), rect.getBottom(), true);

                gisUtil.addCaptionsToImage(g2d,
                                           map.getSegments(),
                                           canvasWidth, canvasHeight,
                                           rect.getLeft(), rect.getRight(),
                                           rect.getTop(), rect.getBottom());

                g2d.dispose();

                RenderedImage ri = graph;

                ImageIO.write(ri, "png", sos);

                graph.flush();

            } else if (action.equalsIgnoreCase(GisService.ACTION_GET_IMAGE_MAP)) {
                response.setContentType("text/xml");

                List mapDataSegments = map.getSegments();

                String imageMapCode = gisUtil.getImageMap(mapDataSegments, 20,
                                                          canvasWidth,
                                                          canvasHeight, rect.getLeft(),
                                                          rect.getLeft(), rect.getTop(),
                                                          rect.getBottom()).toString();

                sos.print(imageMapCode);

            } else if (action.equalsIgnoreCase("getDataForSector")) {

                response.setContentType("image/png");

                String secIdStr = request.getParameter("sectorId");

                Long secId = new Long(secIdStr);

                List secFundings = DbUtil.getSectorFoundings(secId);

                Iterator it = secFundings.iterator();

                Map locationIdObjectMap = new HashMap();
                Map locationFoundMap = new HashMap();

                Double totalFund = 0d;
                while (it.hasNext()) {
                    Object[] secFounding = (Object[]) it.next();
                    AmpActivity ampActivity = (AmpActivity) secFounding[0];
                    Double activityFound = DbUtil.getActivityFoundings(ampActivity.
                            getAmpActivityId());

                    Double addAmmount = 0d;
                    if (activityFound != null) {
                        float addPercent = (Float) secFounding[1];
                        addAmmount = activityFound * addPercent /
                                     100;
                        totalFund += addAmmount;
                    }

                    Iterator locIt = ampActivity.getLocations().iterator();
                    while (locIt.hasNext()) {

                        AmpActivityLocation loc = (AmpActivityLocation) locIt.next();

                        locationIdObjectMap.put(loc.getLocation().getAmpLocationId(),
                                                loc.getLocation());

                        if (locationFoundMap.containsKey(loc.getLocation().
                                                         getAmpLocationId())) {
                            Double existingVal = (Double) locationFoundMap.get(loc.
                                    getLocation().getAmpLocationId());
                            locationFoundMap.put(loc.getLocation().getAmpLocationId(),
                                                 existingVal +
                                                 addAmmount *
                                                 loc.getLocationPercentage() /
                                                 100);
                        } else {
                            locationFoundMap.put(loc.getLocation().getAmpLocationId(),
                                                 addAmmount *
                                                 loc.getLocationPercentage() /
                                                 100);
                        }
                    }

                }

                List segmentDataList = new ArrayList();
                Iterator locFoundingMapIt = locationFoundMap.keySet().iterator();
                while (locFoundingMapIt.hasNext()) {
                    Long key = (Long) locFoundingMapIt.next();
                    AmpLocation loc = (AmpLocation) locationIdObjectMap.get(key);
                    Double ammount = (Double) locationFoundMap.get(key);
                    Object[] dataStore = new Object[2];
                    dataStore[0] = loc;
                    dataStore[1] = ammount;

                    //                        locDataList.add(dataStore);


                    if (loc.getRegion() != null) {
                        SegmentData segmentData = new SegmentData();
                        segmentData.setSegmentCode(loc.getRegion());
                        Double PercentFromTotal = ammount / totalFund * 100;
                        segmentData.setSegmentValue(PercentFromTotal.toString());
                        segmentDataList.add(segmentData);
                    }
                }

                List hilightData = prepareHilightSegments(segmentDataList, map, new Double(0), new Double(100));

                BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
                                                        BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = graph.createGraphics();

                g2d.setBackground(new Color(0, 0, 100, 255));

                g2d.clearRect(0, 0, canvasWidth, canvasHeight);

                gisUtil.addDataToImage(g2d,
                                       map.getSegments(),
                                       hilightData,
                                       canvasWidth, canvasHeight,
                                       rect.getLeft(), rect.getRight(),
                                       rect.getTop(), rect.getBottom(), true);

                gisUtil.addCaptionsToImage(g2d,
                                           map.getSegments(),
                                           canvasWidth, canvasHeight,
                                           rect.getLeft(), rect.getRight(),
                                           rect.getTop(), rect.getBottom());

                g2d.dispose();

                RenderedImage ri = graph;

                ImageIO.write(ri, "png", sos);

                graph.flush();

            } else if (action.equalsIgnoreCase("getDataForIndicator")) {

                response.setContentType("image/png");

                String secIdStr = request.getParameter("sectorId");
                Long secId = new Long(secIdStr);

                String indIdStr = request.getParameter("indicatorId");
                Long indId = new Long(indIdStr);

                //Get segments with funding for dashed paint map
                List secFundings = DbUtil.getSectorFoundings(secId);
                Iterator it = secFundings.iterator();

                Object [] fundingList = getFundingsByLocations(secFundings);
                Map fundingLocationMap = (Map) fundingList[0];

                List segmentDataDasheList = new ArrayList();

                Iterator locFoundingMapIt = fundingLocationMap.keySet().iterator();
                while (locFoundingMapIt.hasNext()) {
                    String key = (String) locFoundingMapIt.next();
                    SegmentData segmentData = new SegmentData();
                    segmentData.setSegmentCode(key);
                    segmentData.setSegmentValue("100");
                    segmentDataDasheList.add(segmentData);
                }

                List hilightDashData = prepareDashSegments(segmentDataDasheList,
                        new ColorRGB(0, 0, 0), map);

                List inds = DbUtil.getIndicatorValuesForSectorIndicator(secId, indId);

                List segmentDataList = new ArrayList();
                Iterator indsIt = inds.iterator();
                Double min = new Double(0);
                Double max = new Double(0);
                while (indsIt.hasNext()) {
                    Object[] indData = (Object[]) indsIt.next();
                    SegmentData indHilightData = new SegmentData();
                    indHilightData.setSegmentCode((String) indData[1]);
                    Double indValue = (Double) indData[0];

                    if (indValue < min) {
                        min = indValue;
                    }

                    if (indValue > max) {
                        max = indValue;
                    }

                    indHilightData.setSegmentValue(indValue.toString());
                    segmentDataList.add(indHilightData);
                }

                List hilightData = prepareHilightSegments(segmentDataList, map, min, max);

                BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
                                                        BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = graph.createGraphics();

                g2d.setBackground(new Color(0, 0, 100, 255));

                g2d.clearRect(0, 0, canvasWidth, canvasHeight);

                gisUtil.addDataToImage(g2d,
                                       map.getSegments(),
                                       hilightData,
                                       hilightDashData,
                                       canvasWidth, canvasHeight,
                                       rect.getLeft(), rect.getRight(),
                                       rect.getTop(), rect.getBottom(), true);

                gisUtil.addCaptionsToImage(g2d,
                                           map.getSegments(),
                                           canvasWidth, canvasHeight,
                                           rect.getLeft(), rect.getRight(),
                                           rect.getTop(), rect.getBottom());

                g2d.dispose();

                RenderedImage ri = graph;

                ImageIO.write(ri, "png", sos);

                graph.flush();

            } else if (action.equalsIgnoreCase("getSectorDataXML")) {
                response.setContentType("text/xml");
                String secIdStr = request.getParameter("sectorId");

                Long secId = new Long(secIdStr);

                List secFundings = DbUtil.getSectorFoundings(secId);


                Object [] fundingList = getFundingsByLocations(secFundings);
                Map fundingLocationMap = (Map) fundingList[0];
                Double totalFunding = (Double) fundingList[1];

                XMLDocument segmendDataInfo = new XMLDocument();
                XML root = new XML("funding");
                root.addAttribute("total", ((float) Math.round(totalFunding / 10)) / 100f);
                segmendDataInfo.addElement(root);
                Iterator locFoundingMapIt = fundingLocationMap.keySet().iterator();
                while (locFoundingMapIt.hasNext()) {
                    String key = (String) locFoundingMapIt.next();
                    Double ammount = (Double) fundingLocationMap.get(key);
                        XML regionData = new XML("region");
                        regionData.addAttribute("reg-code", key);
                        regionData.addAttribute("funding", ((float) Math.round(ammount * 100)) / 100f);
                        root.addElement(regionData);
                }

                sos.print(segmendDataInfo.toString());
            } else if (action.equalsIgnoreCase("getIndicatorNamesXML")) {

                response.setContentType("text/xml");
                String secIdStr = request.getParameter("sectorId");
                Long secId = new Long(secIdStr);

                XMLDocument sectorIndicators = new XMLDocument();
                XML root = new XML("indicators");
                sectorIndicators.addElement(root);

                //Add indicators
                List secIndicatorList = DbUtil.getIndicatorsForSector(secId);
                Iterator indNameIterator = secIndicatorList.iterator();

                while (indNameIterator.hasNext()) {
                    Object[] indName = (Object[]) indNameIterator.next();
                    XML ind = new XML("indicator");
                    ind.addAttribute("name", (String) indName[1]);
                    ind.addAttribute("id", (Long) indName[0]);
                    root.addElement(ind);

                }
                sos.print(sectorIndicators.toString());
            }

        } catch (Exception e) {
            //Add exception reporting
        } finally {
            sos.flush();
            sos.close();
        }
        return null;

    }

    private List prepareHilightSegments(List segmentData, GisMap map, Double min, Double max) {

        float delta = max.floatValue() - min.floatValue();
        float coeff = 255/delta;

        List retVal = new ArrayList();
        Iterator it = map.getSegments().iterator();

        while (it.hasNext()) {
            GisMapSegment segment = (GisMapSegment) it.next();
            for (int idx = (int) 0; idx < segmentData.size(); idx++) {
                SegmentData sd = (SegmentData) segmentData.get(idx);
                if (sd.getSegmentCode().equalsIgnoreCase(segment.getSegmentCode())) {
                    HilightData hData = new HilightData();
                    hData.setSegmentId((int) segment.getSegmentId());
                    float redColor = Float.parseFloat(sd.getSegmentValue()) * coeff;
                    hData.setColor(new ColorRGB((int) redColor,
                                                (int) (255f - redColor), 0));
                    retVal.add(hData);
                }
            }
        }
        return retVal;
    }

    private List prepareDashSegments(List segmentData, ColorRGB dashColor, GisMap map) {
        List retVal = new ArrayList();
        Iterator it = map.getSegments().iterator();

        while (it.hasNext()) {
            GisMapSegment segment = (GisMapSegment) it.next();
            for (int idx = (int) 0; idx < segmentData.size(); idx++) {
                SegmentData sd = (SegmentData) segmentData.get(idx);
                if (sd.getSegmentCode().equalsIgnoreCase(segment.getSegmentCode())) {
                    HilightData hData = new HilightData();
                    hData.setSegmentId((int) segment.getSegmentId());
                    hData.setColor(dashColor);
                    retVal.add(hData);
                }
            }
        }
        return retVal;
    }


    private List getSegmentsForParent(String parentCode, GisMap map) {
        List retVal = new ArrayList();
        Iterator it = map.getSegments().iterator();

        while (it.hasNext()) {
            GisMapSegment segment = (GisMapSegment) it.next();
            if (segment.getParentSegmentCode().equalsIgnoreCase(parentCode)) {
                retVal.add(segment);
            }
        }
        return retVal;
    }

    private Object[] getFundingsByLocations (List activityList) throws Exception {

        Map locationFundingMap = new HashMap();
        Double totalFundingForSector = new Double(0);
        //Calculate total funding
        Iterator<Object[]> actIt = activityList.iterator();
        while (actIt.hasNext()) {
            Object[] actData = actIt.next();
            AmpActivity activity = (AmpActivity) actData[0];
            Float percentsForSectorSelected = (Float)actData[1];
            Double totalFunding = getActivityTotalFundingInUSD (activity);

            totalFundingForSector += totalFunding;

            Double fundingForSector = new Double(totalFunding.floatValue()*percentsForSectorSelected.floatValue()/100f);

            Set locations = activity.getLocations();
            Iterator <AmpActivityLocation> locIt = locations.iterator();


            while (locIt.hasNext()) {
                AmpActivityLocation loc = locIt.next();
                if (loc.getLocation().getAmpRegion() != null) {
                    String regCode = loc.getLocation().getAmpRegion().getName();
                    if (locationFundingMap.containsKey(regCode)) {
                        Double existingVal = (Double)locationFundingMap.get(regCode);
                        locationFundingMap.put(regCode, new Double(existingVal + fundingForSector.floatValue() * loc.getLocationPercentage().floatValue() / 100f));
                    } else {
                        locationFundingMap.put(regCode, new Double(fundingForSector.floatValue() * loc.getLocationPercentage().floatValue() / 100f));
                    }
                }
            }

        //    Set activiactivity.getFunding();
        }
        Object[] retVal = new Object[2];
        retVal[0] = locationFundingMap;
        retVal[1] = totalFundingForSector;
        return retVal;
    }

    private Double getActivityTotalFundingInUSD(AmpActivity activity) {
        Double retVal = new Double(0);
        Set fundSet = activity.getFunding();
        Iterator <AmpFunding> fundIt = fundSet.iterator();
        try {
            while (fundIt.hasNext()) {
                AmpFunding fund = fundIt.next();
                Set fundDetaiuls = fund.getFundingDetails();
                Iterator<AmpFundingDetail> fundDetIt = fundDetaiuls.iterator();
                while (fundDetIt.hasNext()) {
                    AmpFundingDetail fundDet = fundDetIt.next();
                    Double exchangeRate = null;
                    try {
                        exchangeRate = CurrencyUtil.getLatestExchangeRate(
                                fundDet.getAmpCurrencyId().getCurrencyCode());
                    } catch (AimException ex) {
                        //Add exception reporting
                    }

                    retVal += fundDet.getTransactionAmount() / exchangeRate;
                }
            }
        } catch (Exception ex1) {
            //Add exception reporting
        }
        return retVal;
    }

}
