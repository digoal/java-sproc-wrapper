package de.zalando.sprocwrapper.example.transformer;

import java.math.BigDecimal;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import de.zalando.sprocwrapper.example.OrderMonetaryAmount;
import de.zalando.sprocwrapper.example.OrderMonetaryAmountImpl;
import de.zalando.sprocwrapper.globalobjecttransformer.annotation.GlobalObjectMapper;

import de.zalando.typemapper.core.fieldMapper.ObjectMapper;
import de.zalando.typemapper.core.result.DbResultNode;
import de.zalando.typemapper.postgres.PgTypeHelper.PgTypeDataHolder;

/**
 * @author  danieldelhoyo
 */
@GlobalObjectMapper
public class MoneyObjectMapper extends ObjectMapper<OrderMonetaryAmount> {

    public MoneyObjectMapper() {
        super(OrderMonetaryAmount.class);
    }

    @Override
    public OrderMonetaryAmount unmarshalFromDbNode(final DbResultNode dbResultNode) {
        List<DbResultNode> dbResultNodeList = dbResultNode.getChildren();
        BigDecimal amount = new BigDecimal(dbResultNodeList.get(0).getValue());
        String currency = dbResultNodeList.get(1).getValue();

        return new OrderMonetaryAmountImpl(amount, currency);
    }

    @Override
    public PgTypeDataHolder marshalToDb(final OrderMonetaryAmount t) {
        TreeMap<Integer, Object> resultPositionMap = new TreeMap<Integer, Object>();
        resultPositionMap.put(1, t.getAmount());
        resultPositionMap.put(2, t.getCurrency());
        return new PgTypeDataHolder("monetary_amount", Collections.unmodifiableCollection(resultPositionMap.values()));
    }

}
