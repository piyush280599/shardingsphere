/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.test.it.rewrite.fixture.encrypt;

import lombok.Setter;
import org.apache.shardingsphere.encrypt.api.encrypt.standard.StandardEncryptAlgorithm;
import org.apache.shardingsphere.encrypt.spi.context.EncryptContext;
import org.apache.shardingsphere.infra.metadata.database.schema.decorator.model.ShardingSphereSchema;
import org.apache.shardingsphere.infra.metadata.database.schema.decorator.model.ShardingSphereTable;
import org.apache.shardingsphere.infra.rewrite.sql.token.generator.aware.SchemaMetaDataAware;

import java.util.Map;

public final class RewriteSchemaMetaDataAwareEncryptAlgorithmFixture implements StandardEncryptAlgorithm<Object, String>, SchemaMetaDataAware {
    
    @Setter
    private String databaseName;
    
    @Setter
    private Map<String, ShardingSphereSchema> schemas;
    
    @Override
    public String encrypt(final Object plainValue, final EncryptContext encryptContext) {
        if (null == plainValue) {
            return null;
        }
        return "encrypt_" + plainValue + "_" + schemas.get(databaseName).getTable(encryptContext.getTableName()).getName();
    }
    
    @Override
    public Object decrypt(final String cipherValue, final EncryptContext encryptContext) {
        if (null == cipherValue) {
            return null;
        }
        ShardingSphereTable table = schemas.get(databaseName).getTable(encryptContext.getTableName());
        return cipherValue.replaceAll("encrypt_", "").replaceAll("_" + table.getName(), "");
    }
    
    @Override
    public String getType() {
        return "REWRITE.METADATA_AWARE.FIXTURE";
    }
}
