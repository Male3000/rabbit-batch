package com.rabit.consumer.ItemReader;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.batch.item.ItemReader;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

// original code from pre build reader of spring batch but
// ...they just consume one by one????????
// and only use default queue, no such option to listen other queue by name
// and the code is not much either so...copy past, make adjustment...

public class AmqpItemReaderBuiltIn<T> implements ItemReader<T> {

    private final AmqpTemplate amqpTemplate;

    private Class<? extends T> itemType;

    public AmqpItemReaderBuiltIn(final AmqpTemplate amqpTemplate) {
        Assert.notNull(amqpTemplate, "AmqpTemplate must not be null");

        this.amqpTemplate = amqpTemplate;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public T read() {
        if (itemType != null && itemType.isAssignableFrom(Message.class)) {
            return (T) amqpTemplate.receive();
        }

        Object result = amqpTemplate.receiveAndConvert();

        if (itemType != null && result != null) {
            Assert.state(itemType.isAssignableFrom(result.getClass()),
                    "Received message payload of wrong type: expected [" + itemType + "]");
        }

        return (T) result;
    }

    /**
     * Establish the itemType for the reader.
     * @param itemType class type that will be returned by the reader.
     */
    public void setItemType(Class<? extends T> itemType) {
        Assert.notNull(itemType, "Item type cannot be null");
        this.itemType = itemType;
    }

}
