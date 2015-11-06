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
package org.n52.io.extension;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.n52.io.request.IoParameters;
import org.n52.io.response.ParameterOutput;
import org.n52.io.response.ServiceOutput;
import org.n52.io.response.ext.MetadataExtension;
import org.n52.sensorweb.spi.SearchResult;
import org.n52.series.db.da.DataAccessException;
import org.n52.series.db.da.DbQuery;
import org.n52.series.db.da.SessionAwareRepository;
import org.n52.series.db.da.beans.DescribableEntity;
import org.n52.series.db.da.beans.I18nEntity;
import org.n52.web.exception.InternalServerException;

public class DatabaseMetadataExtension extends MetadataExtension<ParameterOutput> {

    private static final String EXTENSION_NAME = "databaseMetadata";
    
    private final MetadataRepository repository = new MetadataRepository();

    @Override
    public String getExtensionName() {
        return EXTENSION_NAME;
    }
    
    @Override
    public Map<String, Object> getExtras(ParameterOutput output, IoParameters parameters) {
        return repository.getExtras(output, parameters);
    }

    @Override
    public void addExtraMetadataFieldNames(ParameterOutput output) {
        List<String> fieldNames = repository.getFieldNames(output.getId());
        for (String fieldName : fieldNames) {
            output.addExtra(fieldName);
        }
    }
    
    private class MetadataRepository extends SessionAwareRepository {

        public MetadataRepository() {
            super(null); // no service info needed
        }

        @Override
        protected DbQuery getDbQuery(IoParameters parameters) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected DbQuery getDbQuery(IoParameters parameters, String locale) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Collection<SearchResult> searchFor(String queryString, String locale) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected List<SearchResult> convertToSearchResults(List<? extends DescribableEntity<? extends I18nEntity>> found, String locale) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        private List<String> getFieldNames(String id) {
            Session session = getSession();
            try {
                DatabaseMetadataDao dao = new DatabaseMetadataDao(session);
                return dao.getMetadataNames(parseId(id));
            } catch (DataAccessException e) {
                throw new InternalServerException("Could not get metadata field names", e);
            }finally {
                returnSession(session);
            }
        }

        private Map<String, Object> getExtras(ParameterOutput output, IoParameters parameters) {
            Session session = getSession();
            try {
                DatabaseMetadataDao dao = new DatabaseMetadataDao(session);
                final Set<String> fields = parameters.getFields();
                return fields == null
                        ? convertToOutputs(dao.getAll())
                        : convertToOutputs(dao.getSelected(fields));
            } finally {
                returnSession(session);
            }
        }

        private Map<String, Object> convertToOutputs(List<MetadataEntity<?>> allInstances) {
            if (allInstances == null) {
                return Collections.emptyMap();
            }
            Map<String, Object> outputs = new HashMap<>();
            for (MetadataEntity entity : allInstances) {
                outputs.put(entity.getName(), entity.toOutput());
            }
            return outputs;
        }

		@Override
		protected ServiceOutput getServiceOutput() throws DataAccessException {
			// not used here
			return null;
		}
        
    }

}
