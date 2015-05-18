package com.box.uali.gridimagesearch.models;

import java.io.Serializable;

/**
 * Created by uali on 5/17/15.
 */
public class ImageFilter implements Serializable {

    public String imageSize;
    public String colorFilter;
    public String imageType;
    public String siteFilter;

    public ImageFilter() {
        imageSize = "";
        colorFilter = "";
        imageType = "";
        siteFilter = "";
    }

    public ImageFilter(String imageSize, String colorFilter, String imageType, String siteFilter) {
        this.imageSize = imageSize;
        this.colorFilter = colorFilter;
        this.imageType = imageType;
        this.siteFilter = siteFilter;
    }

    public String getAsReqeustParams() {
        String paramsStr = "";

        if (imageSize != "") {
            paramsStr += "&imgsz=" + imageSize;
        }

        if (colorFilter != "") {
            paramsStr += "&imgcolor=" + colorFilter;
        }

        if (imageType != "") {
            paramsStr += "&imgtype=" + imageType;
        }

        if (siteFilter != "") {
            paramsStr += "&as_sitesearch=" + siteFilter;
        }

        return paramsStr;
    }
}
