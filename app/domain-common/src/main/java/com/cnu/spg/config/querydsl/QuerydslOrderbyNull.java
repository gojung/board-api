package com.cnu.spg.config.querydsl;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

public class QuerydslOrderbyNull extends OrderSpecifier {
    public static final QuerydslOrderbyNull DEFAULT = new QuerydslOrderbyNull();

    private QuerydslOrderbyNull() {
        super(Order.ASC, NullExpression.DEFAULT, NullHandling.Default);
    }
}
