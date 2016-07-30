/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package info.yangguo.dragon.storage.mysql.algorithm;

import com.google.common.hash.Hashing;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;

import info.yangguo.dragon.storage.mysql.PropertiesUtil;

import java.util.Collection;

public final class SingleKeyModuloDatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<String> {
    private static int dbSum = Integer.parseInt(PropertiesUtil.getProperty("db.properties").get("database.sum"));
    @Override
    public String doEqualSharding(final Collection<String> availableTargetNames, final ShardingValue<String> shardingValue) {
        int index = Hashing.consistentHash(Hashing.murmur3_32().hashBytes(shardingValue.getValue().getBytes()), dbSum);
        for (String each : availableTargetNames) {
            if (each.endsWith(index + "")) {
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> doInSharding(final Collection<String> availableTargetNames, final ShardingValue<String> shardingValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> doBetweenSharding(final Collection<String> availableTargetNames, final ShardingValue<String> shardingValue) {
        throw new UnsupportedOperationException();
    }
}
