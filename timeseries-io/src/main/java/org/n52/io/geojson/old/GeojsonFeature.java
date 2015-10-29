/**
 * Copyright (C) 2013-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.io.geojson.old;

import java.util.HashMap;
import java.util.Map;

public class GeojsonFeature extends GeojsonObject {

    private static final long serialVersionUID = 863297394860249486L;

    private static final String GEOJSON_TYPE_FEATURE = "Feature";
    
    protected Map<String, Object> properties = null;
    
    private GeojsonGeometry geometry;
    
    private String id;
    
    @Override
    public String getType() {
        return GEOJSON_TYPE_FEATURE;
    }
    
    public GeojsonGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(GeojsonGeometry geometry) {
        this.geometry = geometry;
    }
    
    public void addProperty(String property, Object value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(property, value);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
    
    public boolean hasProperty(String property) {
        return this.properties != null
                && this.properties.containsKey(property);
    }
    
    public String getId() {
        if (this.id == null || this.id.isEmpty()) {
            if (properties != null) {
                return (String) properties.get("id");
            }
        }
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
}