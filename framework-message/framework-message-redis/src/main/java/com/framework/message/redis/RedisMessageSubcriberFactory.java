/**************************************************************************************** 
 Copyright © 2003-2012 hbasesoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.framework.message.redis;

import com.hbasesoft.framework.common.ErrorCodeDef;
import com.hbasesoft.framework.common.utils.UtilException;
import com.hbasesoft.framework.message.core.MessageSubcriberFactory;
import com.hbasesoft.framework.message.core.MessageSubscriber;

import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;

/**
 * <Description> <br>
 * 
 * @author 王伟<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2017年2月19日 <br>
 * @since V1.0<br>
 * @see com.framework.message.redis <br>
 */
public class RedisMessageSubcriberFactory implements MessageSubcriberFactory {

    /**
     * Description: <br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     * @param channel
     * @param subscriber <br>
     */
    @Override
    public void registSubscriber(String channel, final MessageSubscriber subscriber) {
        Jedis jedis = null;
        try {
            jedis = RedisClientFactory.getJedisPool().getResource();
            jedis.subscribe(new BinaryJedisPubSub() {
                /**
                 * Description: <br>
                 * 
                 * @author 王伟<br>
                 * @taskId <br>
                 * @param channel
                 * @param message <br>
                 */
                @Override
                public void onMessage(byte[] channel, byte[] message) {
                    subscriber.onMessage(new String(channel), message);
                }

                /**
                 * Description: <br>
                 * 
                 * @author 王伟<br>
                 * @taskId <br>
                 * @param channel
                 * @param subscribedChannels <br>
                 */
                @Override
                public void onSubscribe(byte[] channel, int subscribedChannels) {
                    subscriber.onSubscribe(new String(channel), subscribedChannels);
                }

                /**
                 * Description: <br>
                 * 
                 * @author 王伟<br>
                 * @taskId <br>
                 * @param channel
                 * @param subscribedChannels <br>
                 */
                @Override
                public void onUnsubscribe(byte[] channel, int subscribedChannels) {
                    subscriber.onUnsubscribe(new String(channel), subscribedChannels);
                }

            }, channel.getBytes());
        }
        catch (Exception e) {
            throw new UtilException(ErrorCodeDef.CACHE_ERROR_10002, e);
        }
        finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
