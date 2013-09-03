/**
 * ﻿Copyright (C) 2012
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.io.schema;

import org.junit.Test;
import org.n52.io.v1.data.DesignedParameterSet;
import org.n52.io.v1.data.StyleProperties;
import org.n52.io.v1.data.UndesignedParameterSet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

public class VicinitySchemaTest {
	
	private static final String VICINITY_SCHEMA_V1 = "";

	@Test
	public void test() throws JsonProcessingException {
        System.out.println(getSchemaFor(UndesignedParameterSet.class));
	    System.out.println(getSchemaFor(DesignedParameterSet.class));
        System.out.println(getSchemaFor(StyleProperties.class));
	}

    private String getSchemaFor(Class<?> clazz) throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
        m.acceptJsonFormatVisitor(m.constructType(clazz), visitor);
        JsonSchema jsonSchema = visitor.finalSchema();
        
        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getSimpleName()).append(": ");
        sb.append(m.writeValueAsString(jsonSchema));
        
        return sb.toString();
    }

}