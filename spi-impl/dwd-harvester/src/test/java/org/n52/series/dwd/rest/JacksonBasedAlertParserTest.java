/*
 * Copyright (C) 2013-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public License
 * version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 */
package org.n52.series.dwd.rest;

import java.io.InputStream;
import static org.hamcrest.CoreMatchers.is;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.n52.series.dwd.AlertParser;
import org.n52.series.dwd.ParseException;
import org.n52.series.dwd.beans.WarnCell;
import org.n52.series.dwd.store.InMemoryAlertStore;

public class JacksonBasedAlertParserTest {

    @Test
    public void when_created_then_nonNullObjectMapper() {
        JacksonBasedAlertParser parser = new JacksonBasedAlertParser();
        Assert.assertNotNull(parser.getObjectMapper());
    }

    @Test(expected = ParseException.class)
    public void when_nullStream_then_throwException() throws ParseException {
        AlertParser parser = new JacksonBasedAlertParser();
        InMemoryAlertStore store = new InMemoryAlertStore();
        parser.parse(null, store);
    }

    @Test
    public void when_emptyDwdAlerts_then_parsingEmptyAlertCollection() throws ParseException {
        AlertParser parser = new JacksonBasedAlertParser();
        InMemoryAlertStore store = new InMemoryAlertStore();
        parser.parse(streamOf("/empty-example.json"), store);
        Assert.assertTrue(store.isEmpty());
    }

    @Test
    public void when_emptyDwdAlerts_then_lastAlertTime() throws ParseException {
        AlertParser parser = new JacksonBasedAlertParser();
        InMemoryAlertStore store = new InMemoryAlertStore();
        parser.parse(streamOf("/empty-example.json"), store);
        Assert.assertThat(store.getLastKnownAlertTime(), is(new DateTime(100L)));
    }
    
    @Test
    public void when_dwdExampleAlerts_then_nonEmptyAlerts() throws ParseException {
        AlertParser parser = new JacksonBasedAlertParser();
        InMemoryAlertStore store = new InMemoryAlertStore();
        parser.parse(streamOf("/dwd-example.json"), store);
        Assert.assertFalse(store.getAllAlerts().isEmpty());
    }
    
    @Test
    public void when_dwdExampleAlerts_then_warningCellsWithId() throws ParseException {
        AlertParser parser = new JacksonBasedAlertParser();
        InMemoryAlertStore store = new InMemoryAlertStore();
        parser.parse(streamOf("/dwd-example.json"), store);
        final WarnCell cell = new WarnCell("901055001");
        Assert.assertTrue(store.getAllWarnCells().contains(cell));
    }
    
    private InputStream streamOf(String file) {
        return getClass().getResourceAsStream(file);
    }

}
